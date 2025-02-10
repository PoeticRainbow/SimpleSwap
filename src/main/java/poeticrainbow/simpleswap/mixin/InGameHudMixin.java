package poeticrainbow.simpleswap.mixin;

import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import poeticrainbow.simpleswap.SimpleSwap;

@Mixin(InGameHud.class)
public class InGameHudMixin {
    @Shadow private static final Identifier CROSSHAIR_TEXTURE = Identifier.ofVanilla("hud/crosshair");

    @ModifyArg(method = "renderCrosshair(Lnet/minecraft/client/gui/DrawContext;Lnet/minecraft/client/render/RenderTickCounter;)V",
            at = @At(value = "INVOKE", target = "Lnet/minecraft/client/gui/DrawContext;drawGuiTexture(Ljava/util/function/Function;Lnet/minecraft/util/Identifier;IIII)V"),
            index = 1)
    private Identifier inject(Identifier sprite) {
        return SimpleSwap.SIMPLE_SWAP_KEY.isPressed() && sprite.equals(CROSSHAIR_TEXTURE) ?
                Identifier.of("simpleswap", "blockswap") : sprite;
    }
}
