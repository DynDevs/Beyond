package mine.block.beyond;

import mine.block.beyond.data.BeyondAdvancements;
import net.fabricmc.api.ModInitializer;
import net.kyrptonaught.customportalapi.api.CustomPortalBuilder;
import net.minecraft.block.Blocks;
import net.minecraft.item.Items;
import net.minecraft.util.Identifier;

public class Beyond implements ModInitializer {
    @Override
    public void onInitialize() {
        CustomPortalBuilder.beginPortal()
                .frameBlock(Blocks.REINFORCED_DEEPSLATE)
                .destDimID(new Identifier("the_end"))
                .tintColor(5, 98, 93)
                .forcedSize(20, 6)
                .registerPortal();

        BeyondAdvancements.init();
    }

    public static Identifier id(String id) {
        return new Identifier("beyond", id);
    }
}
