package dev.kofeychi.earthquake.impl.Debug;

import net.fabricmc.fabric.api.itemgroup.v1.ItemGroupEvents;
import net.minecraft.item.Item;
import net.minecraft.item.ItemGroups;
import net.minecraft.registry.Registries;
import net.minecraft.registry.Registry;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.util.Identifier;

public class RegDebug {
    public static void init(){
        ItemGroupEvents.modifyEntriesEvent(ItemGroups.INGREDIENTS)
            .register((itemGroup) -> itemGroup.add(DEBUG_ITEM));}
    public static final RegistryKey<Item> DEBUG_ITEM_KEY = RegistryKey.of(RegistryKeys.ITEM, Identifier.of("earthquake", "debugitem"));
    public static Item register(Item item, RegistryKey<Item> registryKey) {
        // Register the item.
        Item registeredItem = Registry.register(Registries.ITEM, registryKey.getValue(), item);

        // Return the registered item!
        return registeredItem;
    }
    public static final Item DEBUG_ITEM = register(new DebugItem(new Item.Settings().modelId(Identifier.ofVanilla("stick")).registryKey(DEBUG_ITEM_KEY)),DEBUG_ITEM_KEY);
}
