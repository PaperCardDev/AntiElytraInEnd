package cn.paper_card.anti_elytra_in_end;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import org.bukkit.Location;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Objects;

class TheListener implements Listener {
    private final @NotNull AntiElytraInEnd plugin;

    private final @NotNull Advancement advancementElytra;

    TheListener(@NotNull AntiElytraInEnd plugin) {
        this.plugin = plugin;
        this.advancementElytra = Objects.requireNonNull(plugin.getServer().getAdvancement(
                Objects.requireNonNull(NamespacedKey.fromString("end/elytra"))));

    }

    private boolean isNotTheEnd(@Nullable World world) {
        if (world == null) return true;
        return !world.getKey().asString().equals("minecraft:the_end");
    }

    @EventHandler
    public void on(@NotNull PlayerElytraBoostEvent event) { // 烟花加速鞘翅飞行

        if (!this.plugin.isAntiElytraEnable()) return; // 没有禁止鞘翅飞行

        final Player player = event.getPlayer(); // 玩家

        final Location location = player.getLocation(); // 位置
        final World world = location.getWorld(); // 世界

        if (this.isNotTheEnd(world)) return;

        // 在末地
        final double d = Math.max(Math.abs(location.getX()), Math.abs(location.getZ()));
        if (d >= plugin.getAllowFlyRadius()) {
            event.setCancelled(true);
            plugin.sendError(player, "在当前区域不可使用鞘翅飞行，请等待禁飞期结束！");
        }
    }

    @EventHandler
    public void on2(@NotNull EntityToggleGlideEvent event) { // 滑行状态

        if (!this.plugin.isAntiElytraEnable()) return; // 没有禁止鞘翅飞行

        if (event.getEntityType() != EntityType.PLAYER) return;  // 不是玩家

        if (!event.isGliding()) return; // 不是准备开始滑行

        if (!(event.getEntity() instanceof Player p)) return;

        final Location location = p.getLocation(); // 玩家位置

        final World world = location.getWorld();
        if (this.isNotTheEnd(world)) return;

        final double d = Math.max(Math.abs(location.getX()), Math.abs(location.getZ()));
        if (d >= plugin.getAllowFlyRadius()) {
            event.setCancelled(true);
            plugin.sendInfo(p, "在当前区域不可使用鞘翅飞行，请等待禁飞期结束！");
        }
    }

    @EventHandler
    public void on3(@NotNull PlayerMoveEvent event) {

        // 没有开启
        if (!plugin.isAntiElytraPlayerGoOut()) return;

        final Player player = event.getPlayer();

        final Location location = player.getLocation();
        final World world = location.getWorld();
        if (world == null) return;

        // 不是末地
        boolean isInEnd = World.Environment.THE_END.equals(world.getEnvironment());
        if (!isInEnd) return;

        final int dx = Math.abs(location.getBlockX());
        final int dz = Math.abs(location.getBlockZ());
        final int allowFlyRadius = plugin.getAllowFlyRadius();

        // 在主岛范围
        if (dx < allowFlyRadius && dz < allowFlyRadius) return;

        final AdvancementProgress progress = player.getAdvancementProgress(this.advancementElytra);

        if (progress.isDone()) {
//            event.setCancelled(true);
            plugin.sendError(player, "你已完成鞘翅成就，当前不可前往末地外岛");
            int x = location.getBlockX();
            if (x >= allowFlyRadius) x = allowFlyRadius - 4;
            if (x <= -allowFlyRadius) x = -allowFlyRadius + 4;
            int z = location.getBlockZ();
            if (z >= allowFlyRadius) z = allowFlyRadius - 4;
            if (z <= -allowFlyRadius) z = -allowFlyRadius + 4;

            final Location newLoc = new Location(location.getWorld(), x, location.getBlockY(), z);
            player.teleportAsync(newLoc);
        }
    }
}
