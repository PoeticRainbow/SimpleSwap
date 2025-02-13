package poeticrainbow.simpleswap.mixin;

import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;
import poeticrainbow.simpleswap.SimpleSwap;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Redirect(method = "renderCrosshair(Lnet/minecraft/client/gui/DrawContext;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawTexture(Lnet/minecraft/util/Identifier;IIIIII)V"))
    private void inject(DrawContext context, Identifier texture, int x, int y, int u, int v, int width, int height) {
        if (SimpleSwap.SIMPLE_SWAP_KEY.isPressed() && u == 0 && v == 0) {
            context.drawTexture(SimpleSwap.SIMPLE_SWAP_ICON, x, y, u, v, width, height, 15, 15);
        } else {
            context.drawTexture(texture, x, y, u, v, width, height);
        }
    }
}
