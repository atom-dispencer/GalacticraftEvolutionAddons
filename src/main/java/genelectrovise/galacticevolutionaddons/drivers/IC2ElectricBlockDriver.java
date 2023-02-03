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
package genelectrovise.galacticevolutionaddons.drivers;

import ic2.core.block.wiring.TileEntityElectricBlock;
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

public class IC2ElectricBlockDriver extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityElectricBlock.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntityElectricBlock) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntityElectricBlock> implements NamedBlock {

        public static final String NAME = "ge_electric_block";
        private final TileEntityElectricBlock block;

        public Environment(final TileEntityElectricBlock entity) {
            super(entity, NAME);
            this.block = entity;
        }

        // OpenComputers boilerplate

        @Override
        public String preferredName() { return NAME; }

        @Override
        public int priority() {
            return 0;
        }

        // Lua API

        @Callback(doc = "function():number -- (GE) Get the EU/tick output of this device.")
        public Object[] ge_getOutputPerTick(final Context context, final Arguments args) {
            return new Object[] { block.getOutputEnergyUnitsPerTick() };
        }

        @Callback(doc = "function():number -- (GE) Get the maximum EU/tick output of this device.")
        public Object[] ge_getMaxOutput(final Context context, final Arguments args) {
            return new Object[] { block.getOutput() };
        }

        @Callback(doc = "function():number -- (GE) Get the energy capacity of this device.")
        public Object[] ge_getCapacity(final Context context, final Arguments args) {
            return new Object[] { block.getCapacity() };
        }

        @Callback(doc = "function():number -- (GE) Get the energy stored by this device.")
        public Object[] ge_getStored(final Context context, final Arguments args) {
            return new Object[] { block.getStored() };
        }

        @Callback(doc = "function():number -- (GE) Get the size of this device's inventory.")
        public Object[] ge_getInventorySize(final Context context, final Arguments args) {
            return new Object[] { block.getSizeInventory() };
        }

        @Callback(doc = "function():number -- (GE) Get this device's redstone mode.")
        public Object[] ge_getRedstoneMode(final Context context, final Arguments args) {
            return new Object[] { block.redstoneMode };
        }
    }
}
