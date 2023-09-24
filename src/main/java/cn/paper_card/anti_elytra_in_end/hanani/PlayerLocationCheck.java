package cn.paper_card.anti_elytra_in_end.hanani;

import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.List;

public final class PlayerLocationCheck extends JavaPlugin {

    @Override
    public void onEnable() {
        System.out.println("启动末地距离检测");
//         Plugin startup logic
        Location center = new Location(getServer().getWorld("world_the_end"), 10, 66, 10);
        double maxDistance = 256.0;
        getServer().getPluginManager().registerEvents(new BoundaryPlugin(center, maxDistance), this);

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }
}
