package mine.block.beyond.data;

import mine.block.beyond.Beyond;
import mine.block.beyond.data.critereon.SpawnedPortalCritereon;
import net.minecraft.advancement.criterion.Criteria;

public class BeyondAdvancements {

    public static final SpawnedPortalCritereon SPAWNED_PORTAL_CRITEREON = new SpawnedPortalCritereon(Beyond.id("spawned_portal"));

    public static void init() {
        Criteria.register(SPAWNED_PORTAL_CRITEREON);
    }
}
