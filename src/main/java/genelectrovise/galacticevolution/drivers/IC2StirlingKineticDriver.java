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

import com.google.common.collect.Maps;
import ic2.core.block.TileEntityBlock;
import ic2.core.block.comp.Fluids;
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
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidTank;

import javax.annotation.Nullable;
import java.util.Map;

public class IC2StirlingKineticDriver extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityStirlingKineticGenerator.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntityStirlingKineticGenerator) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntityStirlingKineticGenerator> implements NamedBlock {

        public static final String NAME = "ge_stirling_kinetic";
        @Nullable
        private final TileEntityStirlingKineticGenerator generator;

        public Environment(final TileEntityStirlingKineticGenerator entity) {
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

        @Callback(doc = "function():number -- (GE) Get the contents of the reactor input tank as a table: " +
                "{ String name, number amount, number capacity, boolean canFill, boolean canDrain }")
        public Object[] ge_getInputTank(final Context context, final Arguments args) {
            FluidTank inputTank = generator.inputTank;
            if (inputTank == null) return new Object[0];

            boolean canFill = inputTank.canFill();
            boolean canDrain = inputTank.canDrain();
            int capacity = inputTank.getCapacity();
            FluidStack fluid = inputTank.getFluid();
            int amount = fluid != null ? fluid.amount : 0;
            String name = fluid != null ? fluid.getUnlocalizedName() : "<no_name>";

            return new Object[] { canFill, canDrain, capacity, amount, name};
        }

        @Callback(doc = "function():number -- (GE) Get the contents of the reactor input tank as an array: " +
                "{ String name, number amount, number capacity, boolean canFill, boolean canDrain }")
        public Object[] ge_getOutputTank(final Context context, final Arguments args) {
            FluidTank inputTank = generator.outputTank;
            if (inputTank == null) return new Object[0];

            boolean canFill = inputTank.canFill();
            boolean canDrain = inputTank.canDrain();
            int capacity = inputTank.getCapacity();
            FluidStack fluid = inputTank.getFluid();
            int amount = fluid != null ? fluid.amount : 0;
            String name = fluid != null ? fluid.getUnlocalizedName() : "<no_name>";

            return new Object[] { canFill, canDrain, capacity, amount, name};
        }

        @Callback(doc = "function():number -- (GE) Get the KU stored by this device.")
        public Object[] ge_getKUStored(final Context context, final Arguments args) {
            return new Object[] { generator.drawKineticEnergy(generator.getFacing(), Integer.MAX_VALUE, true) };
        }

        @Callback(doc = "function():number -- (GE) Get the max KU output of this device")
        public Object[] ge_getMaxKU(final Context context, final Arguments args) {
            return new Object[] { generator.getConnectionBandwidth(generator.getFacing()) };
        }
    }
}
