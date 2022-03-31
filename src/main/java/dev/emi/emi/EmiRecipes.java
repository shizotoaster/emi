package dev.emi.emi;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

import org.apache.commons.compress.utils.Lists;

import dev.emi.emi.api.recipe.EmiRecipe;
import dev.emi.emi.api.recipe.EmiRecipeCategory;
import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.util.Identifier;

public class EmiRecipes {
	public static List<EmiRecipeCategory> categories = Lists.newArrayList();
	public static Map<EmiRecipeCategory, List<EmiIngredient>> workstations = Maps.newHashMap();
	public static List<EmiRecipe> recipes = Lists.newArrayList();

	public static Map<Object, Map<EmiRecipeCategory, List<EmiRecipe>>> byInput = Maps.newHashMap();
	public static Map<Object, Map<EmiRecipeCategory, List<EmiRecipe>>> byOutput = Maps.newHashMap();
	public static Map<EmiRecipeCategory, List<EmiRecipe>> byCategory = Maps.newHashMap();
	public static Map<Identifier, EmiRecipe> byId = Maps.newHashMap();
	
	public static void reload() {
		categories.clear();
		workstations.clear();
		recipes.clear();
		byInput.clear();
		byOutput.clear();
		byCategory.clear();
		byId.clear();
	}

	public static void bake() {
		Map<Object, Map<EmiRecipeCategory, Set<EmiRecipe>>> byInput = Maps.newHashMap();
		Map<Object, Map<EmiRecipeCategory, Set<EmiRecipe>>> byOutput = Maps.newHashMap();
		for (EmiRecipe recipe : recipes) {
			Identifier id = recipe.getId();
			EmiRecipeCategory category = recipe.getCategory();
			if (!categories.contains(category)) {
				System.err.println("[emi] Recipe " + id + " loaded with unregistered category: " + category.getId());
			}
			byCategory.computeIfAbsent(category, a -> Lists.newArrayList()).add(recipe);
			if (id != null) {
				if (byId.containsKey(id)) {
					System.err.println("[emi] Recipe loaded with duplicate id: " + id);
				}
				byId.put(id, recipe);
			}
			getKeys(recipe.getInputs()).stream().forEach(i -> byInput.computeIfAbsent(i, a -> Maps.newHashMap())
				.computeIfAbsent(category, b -> Sets.newLinkedHashSet()).add(recipe));
			getKeys(recipe.getOutputs()).stream().forEach(i -> byOutput.computeIfAbsent(i, a -> Maps.newHashMap())
				.computeIfAbsent(category, b -> Sets.newLinkedHashSet()).add(recipe));
		}
		EmiRecipes.byInput = byInput.entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), m -> {
			return m.getValue().entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().stream().toList()));
		}));
		EmiRecipes.byOutput = byOutput.entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), m -> {
			return m.getValue().entrySet().stream().collect(Collectors.toMap(k -> k.getKey(), v -> v.getValue().stream().toList()));
		}));
	}

	public static void addCategory(EmiRecipeCategory category) {
		categories.add(category);
	}

	public static void addWorkstation(EmiRecipeCategory category, EmiIngredient workstation) {
		workstations.computeIfAbsent(category, k -> Lists.newArrayList()).add(workstation);
	}

	public static void addRecipe(EmiRecipe recipe) {
		recipes.add(recipe);
	}

	private static Set<Object> getKeys(List<? extends EmiIngredient> list) {
		Set<Object> set = Sets.newHashSet();
		for (EmiIngredient stackSet : list) {
			for (EmiStack stack : stackSet.getEmiStacks()) {
				if (!stack.isEmpty()) {
					set.add(stack.getKey());
				}
			}
		}
		return set;
	}
}