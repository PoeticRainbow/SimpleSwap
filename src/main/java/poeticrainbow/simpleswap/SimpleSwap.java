package poeticrainbow.simpleswap;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.event.client.player.ClientPlayerBlockBreakEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.item.BlockItem;
import net.minecraft.item.Item;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvents;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.Vec3d;
import org.lwjgl.glfw.GLFW;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class SimpleSwap implements ModInitializer, ClientModInitializer {
    public static KeyBinding SIMPLE_SWAP_KEY = new KeyBinding("key.simpleswap.simpleSwap", GLFW.GLFW_KEY_LEFT_ALT, KeyBinding.GAMEPLAY_CATEGORY);
    public static List<BlockHitResult> BLOCKS_TO_BE_SWAPPED = new ArrayList<>();

    @Override
    public void onInitialize() {
    }

    @Override
    public void onInitializeClient() {
        SIMPLE_SWAP_KEY = KeyBindingHelper.registerKeyBinding(SIMPLE_SWAP_KEY);


        ClientPlayerBlockBreakEvents.AFTER.register((world, player, pos, state) -> {
            if (SIMPLE_SWAP_KEY.isPressed() && world.isClient()) {
                MinecraftClient client = MinecraftClient.getInstance();
                Item mainhandItem = player.getMainHandStack().getItem();
                Item offhandItem = player.getOffHandStack().getItem();

                if (offhandItem instanceof BlockItem || mainhandItem instanceof BlockItem &&
                        client.crosshairTarget != null && client.interactionManager != null) {
                    BlockHitResult target = (BlockHitResult) client.crosshairTarget;
                    if (target == null) return;
                    Vec3d hitPos = target.getPos().offset(target.getSide().getOpposite(), 1);
                    target = new BlockHitResult(hitPos, target.getSide(), pos, false);
                    BLOCKS_TO_BE_SWAPPED.add(target);
                }
            }
        });

        ClientTickEvents.END_WORLD_TICK.register(world -> {
            MinecraftClient client = MinecraftClient.getInstance();
            if (client.player != null && client.interactionManager != null) {
                Iterator<BlockHitResult> iterator = BLOCKS_TO_BE_SWAPPED.iterator();
                iterator.forEachRemaining(target -> {
                    Item mainhandItem = client.player.getMainHandStack().getItem();
                    client.player.playSoundToPlayer(SoundEvents.ENTITY_ITEM_PICKUP, SoundCategory.BLOCKS, 0.4f, 0.6f);

                    Hand hand = mainhandItem instanceof BlockItem ? Hand.MAIN_HAND : Hand.OFF_HAND;

                    client.interactionManager.interactBlock(client.player, hand, target);
                    client.gameRenderer.firstPersonRenderer.resetEquipProgress(hand);
                });
                BLOCKS_TO_BE_SWAPPED = new ArrayList<>();
            }
        });
    }
}
