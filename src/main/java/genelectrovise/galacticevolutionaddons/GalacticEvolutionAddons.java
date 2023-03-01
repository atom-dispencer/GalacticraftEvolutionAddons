/**
 * Galactic Evolution Addons - Tweaks and compatibility improvements for the Galactic Evolution mod-pack for Minecraft.
 * Copyright (C) 2023  GenElectrovise
 * <p>
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 * <p>
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 * <p>
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package genelectrovise.galacticevolutionaddons;

import genelectrovise.galacticevolutionaddons.drivers.*;
import ic2.api.recipe.Recipes;
import ic2.core.IC2;
import ic2.core.item.ItemIC2;
import ic2.core.recipe.RecipeInputItemStack;
import ic2.core.ref.ItemName;
import li.cil.oc.api.Driver;
import li.cil.oc.integration.minecraft.RecipeHandler;
import net.minecraft.client.renderer.block.model.ModelResourceLocation;
import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.init.Items;
import net.minecraft.item.EnumRarity;
import net.minecraft.item.Item;
import net.minecraft.item.ItemNameTag;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.client.event.ModelRegistryEvent;
import net.minecraftforge.client.model.ModelLoader;
import net.minecraftforge.common.IRarity;
import net.minecraftforge.event.RegistryEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.registry.GameRegistry;
import net.minecraftforge.oredict.OreDictionary;
import org.apache.logging.log4j.Logger;

import java.util.Arrays;

@Mod(
        modid = GalacticEvolutionAddons.MODID,
        name = GalacticEvolutionAddons.NAME,
        version = GalacticEvolutionAddons.VERSION,
        dependencies = "required-before:ic2"
)
@Mod.EventBusSubscriber()
public class GalacticEvolutionAddons {
    public static final String MODID = "galacticevolutionaddons";
    public static final String NAME = "Galactic Evolution Addons";
    public static final String VERSION = "1.0";

    private static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event) {
        LOGGER = event.getModLog();
        LOGGER.info(NAME + " - Pre-initialisation");
    }

    @EventHandler
    public void init(FMLInitializationEvent event) {
        LOGGER.info(NAME + " - Initialisation");

        LOGGER.info("Adding Galactic Evolution Addons OpenComputers drivers...");
        Driver.add(new IC2ElectricBlockDriver());
        Driver.add(new IC2ElectricKineticDriver());
        Driver.add(new IC2KineticGeneratorDriver());
        Driver.add(new IC2ReactorChamberDriver());
        Driver.add(new IC2ReactorDriver());
        Driver.add(new IC2ReactorVesselDriver());
        Driver.add(new IC2SteamGeneratorDriver());
        Driver.add(new IC2SteamKineticDriver());
        Driver.add(new IC2StirlingKineticDriver());
        Driver.add(new IC2TransformerDriver());
        Driver.add(new IC2WaterKineticDriver());
        Driver.add(new IC2WindKineticDriver());
    }

    public static final CreativeTabs GEA_TAB = (new CreativeTabs("galacticevolutionaddonsTab") {
        @Override
        public ItemStack getTabIconItem() {
            return new ItemStack(GEAItems.IRIDIUM_DUST);
        }
    });

    @SubscribeEvent
    public static void registerItems(RegistryEvent.Register<Item> event) {
        LOGGER.info(NAME + " - Registering items...");
        event.getRegistry().register(GEAItems.NICKEL_DUST);
        event.getRegistry().register(GEAItems.CRUSHED_NICKEL);
        event.getRegistry().register(GEAItems.PURIFIED_NICKEL);
        event.getRegistry().register(GEAItems.IRIDIUM_DUST);
        event.getRegistry().register(GEAItems.TINY_IRIDIUM_DUST);

        LOGGER.info(NAME + " - Registering ore dictionary entries...");
        OreDictionary.registerOre("dustNickel", GEAItems.NICKEL_DUST);
        OreDictionary.registerOre("crushedNickel", GEAItems.CRUSHED_NICKEL);
        OreDictionary.registerOre("crushedPurifiedNickel", GEAItems.PURIFIED_NICKEL);
        OreDictionary.registerOre("dustIridium", GEAItems.IRIDIUM_DUST);
        OreDictionary.registerOre("dustTinyIridium", GEAItems.TINY_IRIDIUM_DUST);
    }

    @SubscribeEvent
    public static void registerModels(ModelRegistryEvent event) {
        ModelLoader.setCustomModelResourceLocation(GEAItems.NICKEL_DUST, 0, new ModelResourceLocation(GEAItems.NICKEL_DUST.getRegistryName(), ""));
        ModelLoader.setCustomModelResourceLocation(GEAItems.CRUSHED_NICKEL, 0, new ModelResourceLocation(GEAItems.CRUSHED_NICKEL.getRegistryName(), ""));
        ModelLoader.setCustomModelResourceLocation(GEAItems.PURIFIED_NICKEL, 0, new ModelResourceLocation(GEAItems.PURIFIED_NICKEL.getRegistryName(), ""));
        ModelLoader.setCustomModelResourceLocation(GEAItems.IRIDIUM_DUST, 0, new ModelResourceLocation(GEAItems.IRIDIUM_DUST.getRegistryName(), ""));
        ModelLoader.setCustomModelResourceLocation(GEAItems.TINY_IRIDIUM_DUST, 0, new ModelResourceLocation(GEAItems.TINY_IRIDIUM_DUST.getRegistryName(), ""));
    }

    public static class GEAItems {
        static {
            LOGGER.info(NAME + " - Statically loading GEAItems");
        }

        public static final Item NICKEL_DUST = new GEAItem(new ResourceLocation(MODID, "nickel_dust"), GEA_TAB, EnumRarity.COMMON);
        public static final Item CRUSHED_NICKEL = new GEAItem(new ResourceLocation(MODID, "crushed_nickel"), GEA_TAB, EnumRarity.COMMON);
        public static final Item PURIFIED_NICKEL = new GEAItem(new ResourceLocation(MODID, "purified_nickel"), GEA_TAB, EnumRarity.UNCOMMON);
        public static final Item IRIDIUM_DUST = new GEAItem(new ResourceLocation(MODID, "iridium_dust"), GEA_TAB, EnumRarity.RARE);
        public static final Item TINY_IRIDIUM_DUST = new GEAItem(new ResourceLocation(MODID, "tiny_iridium_dust"), GEA_TAB, EnumRarity.UNCOMMON);
    }
}
