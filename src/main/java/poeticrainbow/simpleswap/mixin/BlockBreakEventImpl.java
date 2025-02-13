package poeticrainbow.simpleswap.mixin;

import com.llamalad7.mixinextras.sugar.Local;
import net.minecraft.block.BlockState;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerInteractionManager;
import net.minecraft.util.math.BlockPos;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import poeticrainbow.simpleswap.SimpleSwap;

@Mixin(ClientPlayerInteractionManager.class)
public class BlockBreakEventImpl {
    @Shadow @Final private MinecraftClient client;

    @Inject(method = "breakBlock", at = @At(value = "INVOKE", target = "Lnet/minecraft/block/Block;onBroken(Lnet/minecraft/world/WorldAccess;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/block/BlockState;)V"))
    private void simpleSwap$onBlockBroken(BlockPos pos, CallbackInfoReturnable<Boolean> cir, @Local BlockState blockState) {
        SimpleSwap.onClientBlockBroken(client.world, client.player, pos, blockState);
    }
}
