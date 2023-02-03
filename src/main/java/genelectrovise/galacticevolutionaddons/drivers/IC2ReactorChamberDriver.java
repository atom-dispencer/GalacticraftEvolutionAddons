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

import ic2.core.block.reactor.tileentity.TileEntityReactorChamberElectric;
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

public class IC2ReactorChamberDriver extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityReactorChamberElectric.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntityReactorChamberElectric) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntityReactorChamberElectric> implements NamedBlock {

        public static final String NAME = "ge_reactor_chamber";
        private final TileEntityReactorChamberElectric chamber;

        public Environment(final TileEntityReactorChamberElectric tileEntity) {
            super(tileEntity, NAME);
            this.chamber = tileEntity;
        }

        // OpenComputers boilerplate

        @Override
        public String preferredName() { return NAME; }

        @Override
        public int priority() {
            return 0;
        }

        // Lua API

        @Callback(doc = "function():number -- (GE) Get whether this chamber is a wall (always true)")
        public Object[] ge_isWall(final Context context, final Arguments args) {
            return new Object[] { chamber.isWall() };
        }

        @Callback(doc = "function():number -- (GE) Get the reactor which this chamber is part of, if any.")
        public Object[] ge_getReactor(final Context context, final Arguments args) {
            return new Object[] { chamber.getReactorInstance() };
        }
    }
}
