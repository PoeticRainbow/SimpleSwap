package poeticrainbow.simpleswap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keymapping.v1.KeyMappingHelper;
import net.fabricmc.fabric.api.event.client.player.ClientPlayerBlockBreakEvents;
import net.minecraft.client.KeyMapping;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.Identifier;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleSwap implements ClientModInitializer {
    public static final Identifier SWAP_CROSSHAIR = Identifier.fromNamespaceAndPath("simpleswap", "blockswap");
    public static KeyMapping SIMPLE_SWAP_KEY = new KeyMapping("key.simpleswap.simpleSwap", GLFW.GLFW_KEY_LEFT_ALT, KeyMapping.Category.GAMEPLAY);
    public static List<BlockHitResult> BLOCKS_TO_BE_SWAPPED = new ArrayList<>();

    @Override
    public void onInitializeClient() {
        SIMPLE_SWAP_KEY = KeyMappingHelper.registerKeyMapping(SIMPLE_SWAP_KEY);

        ClientPlayerBlockBreakEvents.AFTER.register((world, player, pos, state) -> {
            if (SIMPLE_SWAP_KEY.isDown() && world.isClientSide()) {
                Minecraft client = Minecraft.getInstance();
                Item mainhandItem = player.getMainHandItem().getItem();
                Item offhandItem = player.getOffhandItem().getItem();

                if (offhandItem instanceof BlockItem || mainhandItem instanceof BlockItem &&
                        client.hitResult != null && client.gameMode != null) {
                    BlockHitResult target = (BlockHitResult) client.hitResult;
                    if (target == null) return;
                    Vec3 hitPos = target.getLocation().relative(target.getDirection().getOpposite(), 1);
                    target = new BlockHitResult(hitPos, target.getDirection(), pos, false);
                    BLOCKS_TO_BE_SWAPPED.add(target);
                }
            }
        });

        ClientTickEvents.END_LEVEL_TICK.register(world -> {
            Minecraft client = Minecraft.getInstance();
            if (client.player != null && client.gameMode != null) {
                Iterator<BlockHitResult> iterator = BLOCKS_TO_BE_SWAPPED.iterator();
                iterator.forEachRemaining(target -> {
                    Item mainhandItem = client.player.getMainHandItem().getItem();

                    if (client.level != null)
                        client.level.playPlayerSound(SoundEvents.ITEM_PICKUP, SoundSource.BLOCKS, 0.4f, 0.6f);

                    InteractionHand hand = mainhandItem instanceof BlockItem ? InteractionHand.MAIN_HAND : InteractionHand.OFF_HAND;

                    client.gameMode.useItemOn(client.player, hand, target);
                    client.gameRenderer.itemInHandRenderer.itemUsed(hand);
                });
                BLOCKS_TO_BE_SWAPPED = new ArrayList<>();
            }
        });
    }
}
