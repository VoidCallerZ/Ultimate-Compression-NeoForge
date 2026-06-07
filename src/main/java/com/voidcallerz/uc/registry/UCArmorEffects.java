package com.voidcallerz.uc.registry;

import com.voidcallerz.uc.ModConstants;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.tick.PlayerTickEvent;

import java.util.List;

/**
 * Grants potion effects when the player wears a full compressed armor set.
 * NeoForge version — uses PlayerTickEvent.Post instead of TickEvent.PlayerTickEvent.
 */
@EventBusSubscriber(modid = ModConstants.MOD_ID)
public class UCArmorEffects {

    private static final int CHECK_INTERVAL = 20;
    private static final int DURATION       = 40;

    private record ArmorSet(String prefix, List<MobEffectInstance> effects) {}

    private static final List<ArmorSet> ARMOR_SETS = List.of(

        new ArmorSet("compressed_copper", List.of(
            effect(MobEffects.RESISTANCE, 0)     // Resistance I
        )),

        new ArmorSet("compressed_iron", List.of(
            effect(MobEffects.STRENGTH, 0)          // Strength I
        )),

        new ArmorSet("compressed_gold", List.of(
            effect(MobEffects.SPEED, 1)        // Speed II
        )),

        new ArmorSet("compressed_diamond", List.of(
            effect(MobEffects.STRENGTH,    1),      // Strength II
            effect(MobEffects.RESISTANCE,  0)    // Resistance I
        )),

        new ArmorSet("compressed_netherite", List.of(
            effect(MobEffects.STRENGTH,   1),       // Strength II
            effect(MobEffects.REGENERATION, 0),         // Regeneration I
            effect(MobEffects.RESISTANCE, 1)     // Resistance II
        ))
    );

    private static MobEffectInstance effect(
            net.minecraft.core.Holder<net.minecraft.world.effect.MobEffect> effect,
            int amplifier) {
        return new MobEffectInstance(effect, DURATION, amplifier, false, false, true);
    }

    // In NeoForge, use PlayerTickEvent.Post (replaces TickEvent.PlayerTickEvent END phase)
    @SubscribeEvent
    public static void onPlayerTick(PlayerTickEvent.Post event) {
        Player player = event.getEntity();
        if (player.level().isClientSide()) return;
        if (player.tickCount % CHECK_INTERVAL != 0) return;

        for (ArmorSet set : ARMOR_SETS) {
            if (isWearingFullSet(player, set.prefix())) {
                for (MobEffectInstance template : set.effects()) {
                    MobEffectInstance current = player.getEffect(template.getEffect());
                    if (current == null
                            || current.getAmplifier() < template.getAmplifier()
                            || current.getDuration() < CHECK_INTERVAL) {
                        player.addEffect(new MobEffectInstance(
                            template.getEffect(),
                            DURATION,
                            template.getAmplifier(),
                            false, false, true
                        ));
                    }
                }
            }
        }
    }

    private static boolean isWearingFullSet(Player player, String prefix) {
        return isPiece(player.getItemBySlot(EquipmentSlot.HEAD), prefix + "_helmet")
            && isPiece(player.getItemBySlot(EquipmentSlot.CHEST), prefix + "_chestplate")
            && isPiece(player.getItemBySlot(EquipmentSlot.LEGS), prefix + "_leggings")
            && isPiece(player.getItemBySlot(EquipmentSlot.FEET), prefix + "_boots");
    }

    private static boolean isPiece(ItemStack stack, String registryName) {
        if (stack.isEmpty()) return false;
        var ro = UCEquipment.ALL_EQUIPMENT.get(registryName);
        if (ro == null || !ro.isBound()) return false;
        return stack.getItem() == ro.get();
    }
}