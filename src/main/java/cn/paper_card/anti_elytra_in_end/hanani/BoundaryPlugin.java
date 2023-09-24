package cn.paper_card.anti_elytra_in_end.hanani;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.advancement.Advancement;
import org.bukkit.advancement.AdvancementProgress;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.Iterator;

public class BoundaryPlugin implements Listener {
    private final Location center;
    private final double maxDistance;

    private static Advancement advancementFly;

    public BoundaryPlugin(Location center, double maxDistance) {
        this.center = center;
        this.maxDistance = maxDistance;
    }
    //鞘翅成就
    static {
        Iterator<Advancement> iterator = Bukkit.advancementIterator();
        while (iterator.hasNext()) {
            Advancement advancement = iterator.next();
            if (advancement.getKey().getKey().equals("end/elytra")) {
                advancementFly = advancement;
                break;
            }
        }
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent event) {
        Player player = event.getPlayer();
        Location playerLocation = player.getLocation();
        //是否有鞘翅成就
        boolean isFly = hasElytraAdvancement(player);
        //是否在末地
        World world = playerLocation.getWorld();
        boolean isInEnd = World.Environment.THE_END.equals(world.getEnvironment());
        if (isFly && isInEnd && playerLocation.distance(center) > maxDistance) {
            System.out.println("远离中心距离为" + playerLocation.distance(center));
            player.teleport(center);
            player.sendMessage("你已超出指定范围，已被传送回原地。");
        }
    }

    public boolean hasElytraAdvancement(Player player) {
        AdvancementProgress progress = player.getAdvancementProgress(advancementFly);
        return progress.isDone();
    }
}
