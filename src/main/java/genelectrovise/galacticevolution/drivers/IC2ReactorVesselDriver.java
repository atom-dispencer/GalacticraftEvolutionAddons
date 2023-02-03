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

import ic2.core.block.reactor.tileentity.TileEntityReactorChamberElectric;
import ic2.core.block.reactor.tileentity.TileEntityReactorVessel;
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

public class IC2ReactorVesselDriver extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityReactorVessel.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntityReactorVessel) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntityReactorVessel> implements NamedBlock {

        public static final String NAME = "ge_ic2_reactor_vessel";
        private final TileEntityReactorVessel vessel;

        public Environment(final TileEntityReactorVessel tileEntity) {
            super(tileEntity, NAME);
            this.vessel = tileEntity;
        }

        // OpenComputers boilerplate

        @Override
        public String preferredName() { return NAME; }

        @Override
        public int priority() {
            return 0;
        }

        // Lua API

        @Callback(doc = "function():boolean -- (GE) Get whether this reactor vessel is a wall (always true)")
        public Object[] ge_isWall(final Context context, final Arguments args) {
            return new Object[] { vessel.isWall() };
        }

        @Callback(doc = "function():boolean -- (GE) Get whether this reactor vessel is part of a functional reactor.")
        public Object[] ge_getReactor(final Context context, final Arguments args) {
            return new Object[] { vessel.getReactorInstance() };
        }
    }
}
