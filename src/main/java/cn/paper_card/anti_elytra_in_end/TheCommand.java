package cn.paper_card.anti_elytra_in_end;

import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;

class TheCommand implements CommandExecutor, TabCompleter {

    private final @NotNull AntiElytraInEnd plugin;

    private final @NotNull Permission permView;
    private final @NotNull Permission permToggle;

    TheCommand(@NotNull AntiElytraInEnd plugin) {
        this.plugin = plugin;
        this.permView = plugin.addPermission("anti-elytra-in-end.view");
        this.permToggle = plugin.addPermission("anti-elytra-in-end.toggle");
    }

    private void sendError(@NotNull CommandSender sender, @NotNull String error) {
        sender.sendMessage(Component.text(error).color(NamedTextColor.DARK_RED));
    }

    @Override
    public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        final String argEnable = strings.length > 0 ? strings[0] : null;

        if (argEnable == null) {
            // 权限判断
            if (!commandSender.hasPermission(this.permView)) {
                sendError(commandSender, "你没有权限查看当前的鞘翅禁飞启用状态！");
                return true;
            }

            commandSender.sendMessage(Component.text("当前的鞘翅禁飞启用状态为：%s。".formatted(
                    this.plugin.isAntiElytraEnable() ? "启用" : "禁用"
            )));

            return true;
        }


        // 权限判断
        if (!commandSender.hasPermission(this.permToggle)) {
            sendError(commandSender, "你没有权限切换鞘翅禁飞启用状态！");
            return true;
        }

        final boolean enable;
        if ("on".equals(argEnable)) enable = true;
        else if ("off".equals(argEnable)) enable = false;
        else {
            sendError(commandSender, "不正确的参数：%s，只能为on或off！");
            return true;
        }

        this.plugin.setAntiElytraEnable(enable);

        commandSender.sendMessage(Component.text("已设置当前的鞘翅禁飞启用状态为：%s。".formatted(
                this.plugin.isAntiElytraEnable() ? "启用" : "禁用"
        )));


        return true;
    }

    @Override
    public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, org.bukkit.command.@NotNull Command command, @NotNull String s, @NotNull String[] strings) {
        if (strings.length == 1) {
            if (!commandSender.hasPermission(this.permToggle)) return null;

            final String arg = strings[0];
            final LinkedList<String> list = new LinkedList<>();
            if (arg.isEmpty()) list.add("[on|off]");
            if ("on".startsWith(arg)) list.add("on");
            if ("off".startsWith(arg)) list.add("off");
            return list;
        }

        return null;
    }
}
