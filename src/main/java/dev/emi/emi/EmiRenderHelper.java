package dev.emi.emi;

import com.mojang.blaze3d.systems.RenderSystem;

import dev.emi.emi.api.stack.EmiIngredient;
import dev.emi.emi.api.stack.EmiStack;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;

public class EmiRenderHelper {
	public static final Identifier WIDGETS = new Identifier("emi", "textures/gui/widgets.png");

	public static void drawNinePatch(MatrixStack matrices, int x, int y, int w, int h, int u, int v, int cornerLength, int centerLength) {
		int cor = cornerLength;
		int cen = centerLength;
		int corcen = cor + cen;
		int innerWidth = w - cornerLength * 2;
		int innerHeight = h - cornerLength * 2;
		int coriw = cor + innerWidth;
		int corih = cor + innerHeight;
		// TL
		DrawableHelper.drawTexture(matrices, x,         y,         cor,        cor,         u,          v,          cor, cor, 256, 256);
		// T
		DrawableHelper.drawTexture(matrices, x + cor,   y,         innerWidth, cor,         u + cor,    v,          cen, cor, 256, 256);
		// TR
		DrawableHelper.drawTexture(matrices, x + coriw, y,         cor,        cor,         u + corcen, v,          cor, cor, 256, 256);
		// L
		DrawableHelper.drawTexture(matrices, x,         y + cor,   cor,        innerHeight, u,          v + cor,    cor, cen, 256, 256);
		// C
		DrawableHelper.drawTexture(matrices, x + cor,   y + cor,   innerWidth, innerHeight, u + cor,    v + cor,    cen, cen, 256, 256);
		// R
		DrawableHelper.drawTexture(matrices, x + coriw, y + cor,   cor,        innerHeight, u + corcen, v + cor,    cor, cen, 256, 256);
		// BL
		DrawableHelper.drawTexture(matrices, x,         y + corih, cor,        cor,         u,          v + corcen, cor, cor, 256, 256);
		// B
		DrawableHelper.drawTexture(matrices, x + cor,   y + corih, innerWidth, cor,         u + cor,    v + corcen, cen, cor, 256, 256);
		// BR
		DrawableHelper.drawTexture(matrices, x + coriw, y + corih, cor,        cor,         u + corcen, v + corcen, cor, cor, 256, 256);
	}

	public static void renderIngredient(EmiIngredient ingredient, MatrixStack matrices, int x, int y) {
		matrices.push();
		matrices.translate(0, 0, 200);
		RenderSystem.setShaderTexture(0, EmiRenderHelper.WIDGETS);
		DrawableHelper.drawTexture(matrices, x, y, 8, 252, 4, 4, 256, 256);
		matrices.pop();
	}

	public static void renderTag(EmiIngredient ingredient, MatrixStack matrices, int x, int y) {
		if (ingredient.getEmiStacks().size() > 1) {
			matrices.push();
			matrices.translate(0, 0, 200);
			RenderSystem.setShaderTexture(0, EmiRenderHelper.WIDGETS);
			DrawableHelper.drawTexture(matrices, x, y + 12, 0, 252, 4, 4, 256, 256);
			matrices.pop();
		}
	}

	public static void renderRemainder(EmiIngredient ingredient, MatrixStack matrices, int x, int y) {
		for (EmiStack stack : ingredient.getEmiStacks()) {
			EmiStack remainder = stack.getRemainder();
			if (!remainder.isEmpty()) {
				matrices.push();
				matrices.translate(0, 0, 200);
				RenderSystem.setShaderTexture(0, WIDGETS);
				DrawableHelper.drawTexture(matrices, x + 12, y, 4, 252, 4, 4, 256, 256);
				matrices.pop();
				return;
			}
		}
	}
}