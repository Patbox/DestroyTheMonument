package eu.pb4.destroythemonument.game;

import it.unimi.dsi.fastutil.objects.Object2ObjectMap;
import net.minecraft.util.math.Vec3d;
import xyz.nucleoid.plasmid.game.GameSpace;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.GameMode;
import eu.pb4.destroythemonument.map.Map;
import xyz.nucleoid.plasmid.util.BlockBounds;
import xyz.nucleoid.plasmid.util.PlayerRef;

public class SpawnLogic {
    private final GameSpace gameSpace;
    private final Map map;
    private final Object2ObjectMap<PlayerRef, PlayerData> participants;

    public SpawnLogic(GameSpace gameSpace, Map map, Object2ObjectMap<PlayerRef, PlayerData> participants) {
        this.gameSpace = gameSpace;
        this.map = map;
        this.participants = participants;
    }

    public void resetPlayer(ServerPlayerEntity player, GameMode gameMode) {
        player.setGameMode(gameMode);
        player.inventory.clear();
        player.setVelocity(Vec3d.ZERO);
        player.fallDistance = 0.0f;
        player.setHealth(player.getMaxHealth());
        player.getHungerManager().setFoodLevel(20);
        player.clearStatusEffects();
    }

    public void spawnPlayer(ServerPlayerEntity entity) {
        ServerWorld world = this.gameSpace.getWorld();
        if (this.participants != null) {
            PlayerData player = participants.get(PlayerRef.of(entity));

            if (player != null && player.team != null) {
                BlockBounds spawn = this.map.teamRegions.get(player.team).getSpawn();

                double x = MathHelper.nextDouble(entity.getRandom(), spawn.getMin().getX(), spawn.getMax().getX());
                double z = MathHelper.nextDouble(entity.getRandom(), spawn.getMin().getZ(), spawn.getMax().getZ());

                entity.teleport(world, x, spawn.getMin().getY(), z, entity.yaw, entity.pitch);
                return;
            }
        }

        double x = MathHelper.nextDouble(entity.getRandom(), this.map.spawn.getMin().getX(), this.map.spawn.getMax().getX());
        double z = MathHelper.nextDouble(entity.getRandom(), this.map.spawn.getMin().getZ(), this.map.spawn.getMax().getZ());

        entity.teleport(world, x, this.map.spawn.getMin().getY(), z, entity.yaw, entity.pitch);
    }
}