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
import ic2.core.block.machine.tileentity.TileEntitySteamGenerator;
import ic2.core.block.machine.tileentity.TileEntitySteamRepressurizer;
import li.cil.oc.api.driver.NamedBlock;
import li.cil.oc.api.machine.Arguments;
import li.cil.oc.api.machine.Callback;
import li.cil.oc.api.machine.Context;
import li.cil.oc.api.network.ManagedEnvironment;
import li.cil.oc.api.prefab.DriverSidedTileEntity;
import li.cil.oc.integration.ManagedTileEntityEnvironment;
import li.cil.oc.server.driver.Registry$;
import net.minecraft.util.EnumFacing;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import javax.annotation.Nullable;

public class IC2ElectricKineticDriver extends DriverSidedTileEntity {

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityElectricKineticGenerator.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntityElectricKineticGenerator) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntityElectricKineticGenerator> implements NamedBlock {

        public static final String NAME = "ge_electric_kinetic";
        @Nullable
        private final TileEntityElectricKineticGenerator generator;

        public Environment(final TileEntityElectricKineticGenerator entity) {
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

        @Callback(doc = "function():boolean -- (GE) Get whether this generator contains a motor in its motor slot.")
        public Object[] ge_hasMotorInSlot(final Context context, final Arguments args) {
            return new Object[] { generator.slotMotor.isEmpty(args.checkInteger(0)) };
        }

        @Callback(doc = "function():boolean -- (GE) Get whether this generator contains an internal power source.")
        public Object[] ge_hasInternalPowerSource(final Context context, final Arguments args) {
            return new Object[] { generator.dischargeSlot.isEmpty(args.checkInteger(0)) };
        }

        @Callback(doc = "function():number -- (GE) Get the KU stored by this generator.")
        public Object[] ge_getKU(final Context context, final Arguments args) {
            return new Object[] { generator.ku };
        }

        @Callback(doc = "function():number -- (GE) Get the max KU output of this generator.")
        public Object[] ge_getMaxKU(final Context context, final Arguments args) {
            return new Object[] { generator.getMaxKU() };
        }
    }
}
