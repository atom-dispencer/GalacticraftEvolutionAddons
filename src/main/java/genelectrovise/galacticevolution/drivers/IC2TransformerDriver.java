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
package genelectrovise.galacticevolution.drivers;

import com.google.common.collect.Maps;
import ic2.core.block.comp.Fluids;
import ic2.core.block.reactor.tileentity.TileEntityNuclearReactorElectric;
import ic2.core.block.wiring.TileEntityTransformer;
import ic2.core.block.wiring.TileEntityTransformerHV;
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
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IC2TransformerDriver extends DriverSidedTileEntity {

    private static final Logger LOGGER = LogManager.getLogger();

    @Override
    public Class<?> getTileEntityClass() {
        return TileEntityTransformer.class;
    }

    @Override
    public ManagedEnvironment createEnvironment(World world, BlockPos blockPos, EnumFacing enumFacing) {
        return new Environment((TileEntityTransformer) world.getTileEntity(blockPos));
    }

    public static final class Environment extends ManagedTileEntityEnvironment<TileEntityTransformer> implements NamedBlock {

        public static final String NAME = "ge_transformer";
        private final TileEntityTransformer transformer;

        public Environment(final TileEntityTransformer transformer) {
            super(transformer, NAME);
            this.transformer = transformer;
        }

        // OpenComputers boilerplate

        @Override
        public String preferredName() {return NAME;}

        @Override
        public int priority() {
            return 0;
        }

        // Lua API

        @Callback(doc = "function():number -- (GE) Get the size of the energy packets entering this transformer.")
        public Object[] ge_getInputPacketSize(final Context context, final Arguments args) {
            return new Object[] { transformer.getinputflow() };
        }

        @Callback(doc = "function():number -- (GE) Get the size of the energy packets leaving this transformer.")
        public Object[] ge_getOutputPacketSize(final Context context, final Arguments args) {
            return new Object[] { transformer.getoutputflow() };
        }

        @Callback(doc = "function():number -- (GE) Get the type of this transformer. " +
                "Normally one of 'LV', 'MV', 'HV' or 'EV'.")
        public Object[] ge_getType(final Context context, final Arguments args) {
            return new Object[] { transformer.getType() };
        }

        @Callback(doc = "function():number -- (GE) Get the mode of this transformer. " +
                "Normally one of 'REDSTONE', 'STEPDOWN' or 'STEPUP'")
        public Object[] ge_getMode(final Context context, final Arguments args) {
            return new Object[] { transformer.getMode().toString().toUpperCase() };
        }

        @Callback(doc = "function(mode:string):boolean -- (GE) Set the mode of this transformer. " +
                "Normally one of 'REDSTONE', 'STEPDOWN' or 'STEPUP'. " +
                "Returns the table { boolean success, String error } ")
        public Object[] ge_setMode(final Context context, final Arguments args) {

            boolean success = false;
            String errorMessage = "";
            String modeString = "";

            String modeFieldName = "configuredMode";

            try {
                if (!args.isString(0))
                    throw new IllegalStateException("Argument must be a string to set transformer mode.");

                modeString = args.checkString(0).toLowerCase().trim();
                TileEntityTransformer.Mode mode = TileEntityTransformer.Mode.valueOf(modeString);

                Field field = transformer.getClass().getField(modeFieldName);
                field.setAccessible(true);
                field.set(transformer, mode);

                success = true;

            } catch (IllegalArgumentException iae) {
                errorMessage = "No mode " + modeString;
                LOGGER.error(errorMessage);
                iae.printStackTrace();
            } catch (NoSuchFieldException nsf) {
                String error = "No field " + modeFieldName;
                LOGGER.error(error);

                Field[] fields = transformer.getClass().getFields();
                Stream<String> strings = Arrays.stream(fields).map(f -> f.getType().getName() + ":" + f.getName());
                LOGGER.error(transformer.getClass().getName() + " has available fields [" + strings.collect(Collectors.joining(",")) + "]");

                nsf.printStackTrace();
            } catch (IllegalAccessException iae) {
                errorMessage = "Can't access " + modeFieldName;
                LOGGER.error(errorMessage);
                iae.printStackTrace();
            } catch (SecurityException se) {
                errorMessage = "Forbidden " + modeFieldName;
                LOGGER.error(errorMessage);
                se.printStackTrace();
            } catch (Exception e) {
                errorMessage = "Strange error while setting transformer mode";
                LOGGER.error(errorMessage);
                e.printStackTrace();
            }

            return new Object[] { success, errorMessage };
        }
    }
}
