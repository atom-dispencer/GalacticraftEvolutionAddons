/**
 * Galactic Evolution Addons - Tweaks and compatibility improvements for the Galactic Evolution mod-pack for Minecraft.
 * Copyright (C) 2023  GenElectrovise
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Affero General Public License as published
 * by the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU Affero General Public License for more details.
 *
 * You should have received a copy of the GNU Affero General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package genelectrovise.galacticevolutionaddons;

import genelectrovise.galacticevolutionaddons.drivers.*;
import li.cil.oc.api.Driver;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import org.apache.logging.log4j.Logger;

@Mod(modid = GalacticEvolutionAddons.MODID, name = GalacticEvolutionAddons.NAME, version = GalacticEvolutionAddons.VERSION)
public class GalacticEvolutionAddons
{
    public static final String MODID = "galacticevolutionaddons";
    public static final String NAME = "Galactic Evolution Addons";
    public static final String VERSION = "1.0";

    private static Logger LOGGER;

    @EventHandler
    public void preInit(FMLPreInitializationEvent event)
    {
        LOGGER = event.getModLog();
        LOGGER.info(NAME + " - Pre-initialisation");
    }

    @EventHandler
    public void init(FMLInitializationEvent event)
    {
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
}
