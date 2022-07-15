package mine.block.beyond.data.critereon;

import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import mine.block.beyond.Beyond;
import net.kyrptonaught.customportalapi.CustomPortalApiRegistry;
import net.minecraft.advancement.criterion.AbstractCriterion;
import net.minecraft.advancement.criterion.AbstractCriterionConditions;
import net.minecraft.block.Block;
import net.minecraft.block.BlockState;
import net.minecraft.item.ItemStack;
import net.minecraft.predicate.entity.AdvancementEntityPredicateDeserializer;
import net.minecraft.predicate.entity.AdvancementEntityPredicateSerializer;
import net.minecraft.predicate.entity.EntityPredicate;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.JsonHelper;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;

import java.util.function.Predicate;

public class SpawnedPortalCritereon extends AbstractCriterion<SpawnedPortalCritereon.Conditions> {

    private final Identifier id;

    public SpawnedPortalCritereon(Identifier id) {
        this.id = id;
    }

    @Override
    protected Conditions conditionsFromJson(JsonObject obj, EntityPredicate.Extended playerPredicate, AdvancementEntityPredicateDeserializer predicateDeserializer) {
        return new Conditions(this.id, playerPredicate, getBlockPos(obj));
    }

    @Override
    public Identifier getId() {
        return id;
    }

    @Nullable
    private static BlockPos getBlockPos(JsonObject root) {
        if (root.has("blockPos")) {
            return BlockPos.fromLong(root.get("blockPos").getAsLong());
        } else {
            return null;
        }
    }

    public void trigger(ServerPlayerEntity player, BlockPos framePos) {
        super.trigger(player, conditions -> conditions.test(player.world, framePos));
    }

    public static class Conditions extends AbstractCriterionConditions {

        private final BlockPos framePos;

        public Conditions(Identifier id, EntityPredicate.Extended playerPredicate, BlockPos framePos) {
            super(id, playerPredicate);

            this.framePos = framePos;
        }

        public boolean test(World world, BlockPos portalFrame) {
            return CustomPortalApiRegistry.isRegisteredFrameBlock(world.getBlockState(portalFrame));
        }

        public JsonObject toJson(AdvancementEntityPredicateSerializer predicateSerializer) {
            JsonObject jsonObject = super.toJson(predicateSerializer);
            if (this.framePos != null) {
                jsonObject.addProperty("blockPos", this.framePos.asLong());
            }
            return jsonObject;
        }

        public static Conditions create(BlockPos framePos) {
            return new Conditions(Beyond.id("spawned_portal"), EntityPredicate.Extended.EMPTY, framePos);
        }
    }
}
