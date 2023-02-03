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
package genelectrovise.galacticevolutionaddons.drivers;

import ic2.core.block.machine.tileentity.TileEntitySteamGenerator;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class IC2SteamGeneratorDriver extends DriverSidedTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Class<?> getTileEntityClass() {return TileEntitySteamGenerator.class;}

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntitySteamGenerator) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntitySteamGenerator> implements NamedBlock {

        public static final String NAME = "ge_steam_generator";
        private final TileEntitySteamGenerator steam;

        public Environment(final TileEntitySteamGenerator entity) {
            super(entity, NAME);
            this.steam = entity;
        }

        // Supporting functions

        /**
         * Uses onNetworkEvent(Player,event).
         * <br>
         * If the network event is within [-2000,2000] the input rate is changed according to:
         * <br>
         * <code>this.inputMB = Math.max(Math.min(this.inputMB + event, 1000), 0);</code>
         * <br>
         * @param targetRate
         */
        public void setInputRate(int targetRate) {
            assert 0 <= targetRate && targetRate <= 1000;

            int change = targetRate - steam.getInputMB();
            LOGGER.info(String.format("Setting pressure of %1$s to %2$s (change by %3$s from %4$s)", steam.getPos(), targetRate, change, steam.getInputMB()));

            changeInputRate(change);
        }

        public void changeInputRate(int change) {
            assert change > -2000 && change < 2000;
            LOGGER.info("Changing input rate by: " + change);
            steam.onNetworkEvent(null, change);
        }

        /**
         * Uses onNetworkEvent(Player,event).
         * <br>
         * If the network event is >2000 the pressure is changed according to:
         * <br>
         * <code>this.pressure = Math.max(this.pressure + event + 2000, 0);</code>
         * <br>
         * If the network event is <-2000 the input rate is changed according to:
         * <br>
         * <code>this.pressure = Math.min(this.pressure + (event - 2000), 300);</code>
         * <br>
         * @param targetPressure
         */
        public void setPressure(int targetPressure) {
            assert 0 <= targetPressure && targetPressure <= 300;

            int change = targetPressure - steam.getInputMB();
            LOGGER.info(String.format("Setting pressure of %1$s to %2$s (change by %3$s from %4$s)", steam.getPos(), targetPressure, change, steam.getPressure()));

            changePressure(change);
        }

        public void changePressure(int change) {
            // +500 -> +2500
            // -650 -> -2650
            int adjustedChange = change + ( change > 0 ? 2000 : -2000);
            LOGGER.info("Changing pressure by: " + change + "(adjusted for network: " + adjustedChange + ")");
            steam.onNetworkEvent(null, adjustedChange);
        }

        // OpenComputers boilerplate

        @Override
        public String preferredName() {return NAME;}

        @Override
        public int priority() {
            return 0;
        }

        // Lua API

        @Callback(doc = "function():number -- (GE) Get the calcification level of this generator.")
        public Object[] ge_getCalcification(final Context context, final Arguments args) {
            return new Object[]{steam.getCalcification()};
        }

        @Callback(doc = "function():number -- (GE) Get the rate of heat input into this generator.")
        public Object[] ge_getHeatInput(final Context context, final Arguments args) {
            return new Object[]{steam.getHeatInput()};
        }

        @Callback(doc = "function():number -- (GE) Get the rate of flow of fluid into this generator in mB.")
        public Object[] ge_getInputMB(final Context context, final Arguments args) {
            return new Object[]{steam.getInputMB()};
        }

        @Callback(doc = "function():number -- (GE) Get the name of the fluid this generator is outputting.")
        public Object[] ge_getOutputFluidName(final Context context, final Arguments args) {
            return new Object[]{steam.getOutputFluidName()};
        }

        @Callback(doc = "function():number -- (GE) Get the rate of flow of fluid out of this generator in mB.")
        public Object[] ge_getOutputMB(final Context context, final Arguments args) {
            return new Object[]{steam.getOutputMB()};
        }

        @Callback(doc = "function():number -- (GE) Get whether this generator is fully calcified.")
        public Object[] ge_isCalcified(final Context context, final Arguments args) {
            return new Object[]{steam.isCalcified()};
        }

        @Callback(doc = "function():number -- (GE) Get the pressure of this generator.")
        public Object[] ge_getPressure(final Context context, final Arguments args) {
            return new Object[]{steam.getPressure()};
        }

        @Callback(doc = "function():number -- (GE) Get the heat level of this generator.")
        public Object[] ge_getSystemHeat(final Context context, final Arguments args) {
            return new Object[]{steam.getSystemHeat()};
        }


        @Callback(doc = "function(int:barPressure):[number pressureBefore, number pressureChange, number pressureAfter] " +
                "-- (GE) Set the output pressure of this boiler.")
        public Object[] ge_setPressure(final Context context, final Arguments args) {
            int before = steam.getPressure();
            int newPressure = args.checkInteger(0);
            setPressure(newPressure);
            int after = steam.getPressure();
            return new Object[] { before, after - before, after };
        }

        @Callback(doc = "function(int:mBRate):[number rateBefore, number rateChange, number rateAfter] " +
                "-- (GE) Set the rate of fluid intake for this boiler.")
        public Object[] ge_setInputRate(final Context context, final Arguments args) {
            int before = steam.getInputMB();
            int newRate = args.checkInteger(0);
            setInputRate(newRate);
            int after = steam.getInputMB();
            return new Object[] { before, after - before, after };
        }
    }
}



















