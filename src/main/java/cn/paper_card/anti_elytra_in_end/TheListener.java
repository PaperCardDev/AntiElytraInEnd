package cn.paper_card.anti_elytra_in_end;

import com.destroystokyo.paper.event.player.PlayerElytraBoostEvent;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityToggleGlideEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

class TheListener implements Listener {
    private final @NotNull AntiElytraInEnd plugin;

    TheListener(@NotNull AntiElytraInEnd plugin) {
        this.plugin = plugin;
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
        if (d >= 256) {
            event.setCancelled(true);
            player.sendMessage(Component.text("在当前区域不可使用鞘翅飞行，请等待禁飞期结束！").color(NamedTextColor.RED));
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
        if (d >= 256) {
            event.setCancelled(true);
            p.sendMessage(Component.text("在当前区域不可使用鞘翅飞行，请等待禁飞期结束！").color(NamedTextColor.RED));
        }
    }
}
