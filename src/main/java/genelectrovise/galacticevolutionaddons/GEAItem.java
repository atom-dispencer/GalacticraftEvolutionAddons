package genelectrovise.galacticevolutionaddons;

import net.minecraft.creativetab.CreativeTabs;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ResourceLocation;
import net.minecraftforge.common.IRarity;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.annotation.Resource;

public class GEAItem extends Item {

    private static final Logger LOGGER = LogManager.getLogger();
    private IRarity rarity;

    public GEAItem(ResourceLocation loc, CreativeTabs tab, IRarity rarity) {
        LOGGER.info("Creating GEAItem: " + loc);
        setUnlocalizedName(loc.toString());
        setRegistryName(loc);
        setCreativeTab(tab);
        this.rarity = rarity;
    }

    @Override
    public IRarity getForgeRarity(ItemStack stack) {
        return rarity;
    }
}
