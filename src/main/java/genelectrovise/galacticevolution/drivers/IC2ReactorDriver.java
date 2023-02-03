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

import ic2.api.reactor.IReactor;
import ic2.api.reactor.IReactorChamber;
import ic2.core.block.comp.Fluids;
import ic2.core.block.generator.tileentity.TileEntityKineticGenerator;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.network.Message;
import li.cil.oc.api.network.Node;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraftforge.fluids.FluidStack;

public class IC2ReactorDriver extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityNuclearReactorElectric.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntityNuclearReactorElectric) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntityNuclearReactorElectric> implements NamedBlock {

        public static final String NAME = "ge_ic2_reactor";
        private final TileEntityNuclearReactorElectric reactor;

        public Environment(final TileEntityNuclearReactorElectric reactor) {
            super(reactor, NAME);
            this.reactor = reactor;
        }

        // OpenComputers boilerplate

        @Override
        public String preferredName() { return NAME; }

        @Override
        public int priority() {
            return 0;
        }

        // Lua API

        @Callback(doc = "function():number -- (GE) Get whether this reactor is fluid-cooled.")
        public Object[] ge_isFluidCooled(final Context context, final Arguments args) {
            return new Object[] { reactor.isFluidCooled() };
        }

        @Callback(doc = "function():number -- (GE) Get the contents of the reactor input tank as a table: { name, amount, capacity, canFill, canDrain }")
        public Object[] ge_getInputTank(final Context context, final Arguments args) {
            Fluids.InternalFluidTank inputTank = reactor.inputTank;
            if (inputTank == null) return new Object[] { };

            boolean canFill = inputTank.canFill();
            boolean canDrain = inputTank.canDrain();
            int capacity = inputTank.getCapacity();
            FluidStack fluid = inputTank.getFluid();
            int amount = fluid != null ? fluid.amount : 0;
            String name = fluid != null ? fluid.getUnlocalizedName() : "<no_name>";

            return new Object[] { name, amount, capacity, canFill, canDrain };
        }

        @Callback(doc = "function():number -- (GE) Get the contents of the reactor output tank." +
                "Returns { String name, int amount, int capacity, boolean canFill, boolean canDrain }")
        public Object[] ge_getOutputTank(final Context context, final Arguments args) {
            Fluids.InternalFluidTank inputTank = reactor.outputTank;
            if (inputTank == null) return new Object[] { };

            boolean canFill = inputTank.canFill();
            boolean canDrain = inputTank.canDrain();
            int capacity = inputTank.getCapacity();
            FluidStack fluid = inputTank.getFluid();
            int amount = fluid != null ? fluid.amount : 0;
            String name = fluid != null ? fluid.getUnlocalizedName() : "<no_name>";

            return new Object[] { name, amount, capacity, canFill, canDrain };
        }
    }
}
