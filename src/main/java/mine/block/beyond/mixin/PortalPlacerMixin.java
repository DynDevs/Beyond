package mine.block.beyond.mixin;

import mine.block.beyond.data.BeyondAdvancements;
import net.kyrptonaught.customportalapi.portal.PortalIgnitionSource;
import net.kyrptonaught.customportalapi.portal.PortalPlacer;
import net.minecraft.block.Blocks;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LargeEntitySpawnHelper;
import net.minecraft.entity.SpawnReason;
import net.minecraft.entity.mob.WardenEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.Vec3i;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeKeys;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(PortalPlacer.class)
public class PortalPlacerMixin {



    private static void trySpawnWarden(ServerWorld world, BlockPos pos) {
        WardenEntity entity = LargeEntitySpawnHelper.trySpawnAt(EntityType.WARDEN, SpawnReason.TRIGGERED, world, pos, 20, 15, 10, LargeEntitySpawnHelper.Requirements.WARDEN).get();
    }

    @Inject(method = "attemptPortalLight(Lnet/minecraft/world/World;Lnet/minecraft/util/math/BlockPos;Lnet/minecraft/util/math/BlockPos;Lnet/kyrptonaught/customportalapi/portal/PortalIgnitionSource;)Z", at = @At("RETURN"), cancellable = false)
    private static void onPortalLight(World world, BlockPos portalPos, BlockPos framePos, PortalIgnitionSource ignitionSource, CallbackInfoReturnable<Boolean> cir) {
        if(!cir.getReturnValue() || !(world.getBlockState(framePos) == Blocks.REINFORCED_DEEPSLATE.getDefaultState()) || world.getBiome(framePos).getKey().get() != BiomeKeys.DEEP_DARK) {
            return;
        }

        for (int i = 0; i < 9; i++) {
            trySpawnWarden((ServerWorld) world, framePos);
        }

        WardenEntity.addDarknessToClosePlayers((ServerWorld) world, Vec3d.ofCenter(portalPos.add(new Vec3i(5, -3, 5))), null, 40);


        var entitys = world.getPlayers();

        for (PlayerEntity entity : entitys) {
            if(entity.getBlockPos().isWithinDistance(portalPos, 20)) {
                BeyondAdvancements.SPAWNED_PORTAL_CRITEREON.trigger((ServerPlayerEntity) entity, framePos);
            }
        }
    }
}
