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
package genelectrovise.galacticevolution.drivers;

import ic2.core.block.TileEntityBlock;
import ic2.core.block.kineticgenerator.tileentity.*;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class IC2SteamKineticDriver extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntitySteamKineticGenerator.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntitySteamKineticGenerator) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntitySteamKineticGenerator> implements NamedBlock {

        public static final String NAME = "ge_steam_kinetic";
        @Nullable
        private final TileEntitySteamKineticGenerator generator;

        public Environment(final TileEntitySteamKineticGenerator entity) {
            super(entity, NAME);
            this.generator = entity;
        }

        // OpenComputers boilerplate

        @Override
        public String preferredName() { return NAME; }

        @Override
        public int priority() {
            return 0;
        }

        // Lua API

        @Callback(doc = "function():boolean -- (GE) Get whether this generator contains a turbine in its turbine slot.")
        public Object[] ge_hasTurbine(final Context context, final Arguments args) {
            return new Object[] { generator.hasTurbine() };
        }

        @Callback(doc = "function():boolean -- (GE) Get whether this generator's turbine, if it exists, is blocked.")
        public Object[] ge_isTurbineBlocked(final Context context, final Arguments args) {
            return new Object[] { generator.isTurbineBlockedByWater() };
        }

        @Callback(doc = "function():boolean -- (GE) Get whether this generator is venting steam.")
        public Object[] ge_isVenting(final Context context, final Arguments args) {
            return new Object[] { generator.isVentingSteam() };
        }

        @Callback(doc = "function():boolean -- (GE) Get whether this generator is throttled.")
        public Object[] ge_isThrottled(final Context context, final Arguments args) {
            return new Object[] { generator.isThrottled() };
        }

        @Callback(doc = "function():int -- (GE) Get the fill of the distilled water tank.")
        public Object[] ge_getDistilledWaterFill(final Context context, final Arguments args) {
            return new Object[] { generator.getDistilledWaterTankFill() };
        }

        @Callback(doc = "function():int -- (GE) Get max KU bandwidth of this generator.")
        public Object[] ge_getConnectionBandwidth(final Context context, final Arguments args) {
            return new Object[] { generator.getConnectionBandwidth(generator.getFacing()) };
        }

        @Callback(doc = "function():int -- (GE) Get the current KU output of this generator.")
        public Object[] ge_getKUOutput(final Context context, final Arguments args) {
            return new Object[] { generator.getKUoutput() };
        }

        @Callback(doc = "function():int -- (GE) Get the tick rate of this generator. No idea why this is needed as it's always 20.")
        public Object[] ge_getTickRate(final Context context, final Arguments args) {
            return new Object[] { generator.getTickRate() };
        }

        @Callback(doc = "function():int -- (GE) Get the tick rate of this generator. No idea why this is needed as it's always 20.")
        public Object[] ge_getTickRate(final Context context, final Arguments args) {
            return new Object[] { generator. };
        }
    }
}
