package cn.paper_card.anti_elytra_in_end;

import org.bukkit.command.PluginCommand;
import org.bukkit.permissions.Permission;
import org.bukkit.plugin.java.JavaPlugin;
import org.jetbrains.annotations.NotNull;

public final class AntiElytraInEnd extends JavaPlugin {

    private final static String KEY = "anti-elytra-in-end";
    private final static String KEY_ENABLE = KEY + ".enable";

    private final static String KEY_ALLOW_FLY_RADIUS = KEY + ".allow-fly-radius";

    private int allowFlyRadius = 1024;

    void setAntiElytraEnable(boolean enable) {
        this.getConfig().set(KEY_ENABLE, enable);
    }


    boolean isAntiElytraEnable() {
        return this.getConfig().getBoolean(KEY_ENABLE, false);
    }

    void setAllowFlyRadius(int radius) {
        this.allowFlyRadius = radius;
    }

    int getAllowFlyRadius() {
        return this.allowFlyRadius;
    }

    @NotNull Permission addPermission(@NotNull String name) {
        final Permission permission = new Permission(name);
        this.getServer().getPluginManager().addPermission(permission);
        return permission;
    }

    @Override
    public void onEnable() {

        this.getServer().getPluginManager().registerEvents(new TheListener(this), this);

        // 配置命令
        final PluginCommand command = this.getCommand("anti-elytra-in-end");
        assert command != null;
        final TheCommand theCommand = new TheCommand(this);
        command.setTabCompleter(theCommand);
        command.setExecutor(theCommand);


        // 保存默认值
        this.setAntiElytraEnable(this.isAntiElytraEnable());
        this.allowFlyRadius = this.getConfig().getInt(KEY_ALLOW_FLY_RADIUS, 1024);
        this.getConfig().set(KEY_ALLOW_FLY_RADIUS, this.allowFlyRadius);

        this.saveConfig();
    }

    @Override
    public void onDisable() {

        this.getConfig().set(KEY_ALLOW_FLY_RADIUS, this.allowFlyRadius);
        this.saveConfig();
    }
}
