package poeticrainbow.simpleswap.mixin;

import com.llamalad7.mixinextras.injector.wrapoperation.Operation;
import com.llamalad7.mixinextras.injector.wrapoperation.WrapOperation;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.Hud;
import net.minecraft.resources.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import poeticrainbow.simpleswap.SimpleSwap;

@Mixin(Hud.class)
public class GuiMixin {
    @Shadow @Final private static Identifier CROSSHAIR_SPRITE;

    @WrapOperation(
        method = "extractCrosshair",
        at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/GuiGraphicsExtractor;blitSprite(Lcom/mojang/renderpearl/api/pipeline/RenderPipeline;Lnet/minecraft/resources/Identifier;IIII)V")
    )
    private void simpleswap$swap_crosshair(GuiGraphicsExtractor instance, com.mojang.renderpearl.api.pipeline.RenderPipeline renderPipeline, Identifier location, int x, int y, int width, int height, Operation<Void> original) {
        if (SimpleSwap.SIMPLE_SWAP_KEY.isDown() && location.equals(CROSSHAIR_SPRITE)) {
            original.call(instance, renderPipeline, SimpleSwap.SWAP_CROSSHAIR, x, y, width, height);
        } else {
            original.call(instance, renderPipeline, location, x, y, width, height);
        }
    }
}
