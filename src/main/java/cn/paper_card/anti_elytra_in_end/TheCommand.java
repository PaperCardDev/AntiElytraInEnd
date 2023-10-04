package cn.paper_card.anti_elytra_in_end;

import cn.paper_card.mc_command.TheMcCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.permissions.Permission;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

class TheCommand extends TheMcCommand.HasSub {

    private final @NotNull AntiElytraInEnd plugin;

    private final @NotNull Permission permission;


    TheCommand(@NotNull AntiElytraInEnd plugin) {
        super("anti-elytra-in-end");
        this.plugin = plugin;
        this.permission = Objects.requireNonNull(plugin.getServer().getPluginManager().getPermission("anti-elytra-in-end.command"));

        this.addSubCommand(new Enable());
        this.addSubCommand(new Disable());
        this.addSubCommand(new AntiElytraGoOut());
    }


    @Override
    protected boolean canNotExecute(@NotNull CommandSender commandSender) {
        return !commandSender.hasPermission(this.permission);
    }

    class Enable extends TheMcCommand {

        private final @NotNull Permission permission;


        protected Enable() {
            super("enable");
            this.permission = plugin.addPermission(TheCommand.this.permission.getName() + ".enable");
        }

        @Override
        protected boolean canNotExecute(@NotNull CommandSender commandSender) {
            return !commandSender.hasPermission(this.permission);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

            final String argRadius = strings.length > 0 ? strings[0] : null;

            if (argRadius != null) {
                final int radius;

                try {
                    radius = Integer.parseInt(argRadius);
                } catch (NumberFormatException e) {
                    plugin.sendError(commandSender, "%s 不是一个正确的数值！".formatted(argRadius));
                    return true;
                }

                plugin.setAllowFlyRadius(radius);
                plugin.sendInfo(commandSender, "已将末地允许鞘翅飞行的区域半径设置为：%d".formatted(plugin.getAllowFlyRadius()));
            }

            plugin.setAntiElytraEnable(true);
            plugin.sendInfo(commandSender, "已设置禁止末地鞘翅飞行为：%s".formatted(plugin.isAntiElytraEnable() ? "开启" : "关闭"));

            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            if (strings.length == 1) {
                final String argRadius = strings[0];
                final LinkedList<String> list = new LinkedList<>();
                if (argRadius.isEmpty()) list.add("[允许末地鞘翅飞行的半径]");
                return list;
            }
            return null;
        }
    }

    class Disable extends TheMcCommand {

        private final @NotNull Permission permission;

        protected Disable() {
            super("disable");
            this.permission = plugin.addPermission(TheCommand.this.permission.getName() + ".disable");
        }

        @Override
        protected boolean canNotExecute(@NotNull CommandSender commandSender) {
            return !commandSender.hasPermission(this.permission);
        }

        @Override
        public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

            plugin.setAntiElytraEnable(false);
            plugin.sendInfo(commandSender, "已设置禁止末地鞘翅飞行为：%s".formatted(plugin.isAntiElytraEnable() ? "开启" : "关闭"));

            return true;
        }

        @Override
        public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
            return null;
        }
    }

    class AntiElytraGoOut extends TheMcCommand.HasSub {

        private final @NotNull Permission permission;

        AntiElytraGoOut() {
            super("anti-elytra-go-out");
            this.permission = plugin.addPermission(TheCommand.this.permission.getName() + "." + this.getLabel());
            this.addSubCommand(new OnOff(true));
            this.addSubCommand(new OnOff(false));
        }

        @Override
        protected boolean canNotExecute(@NotNull CommandSender commandSender) {
            return !commandSender.hasPermission(this.permission);
        }

        class OnOff extends TheMcCommand {

            private final @NotNull Permission permission;
            private final boolean isOn;

            protected OnOff(boolean isOn) {
                super(isOn ? "on" : "off");
                this.isOn = isOn;
                this.permission = plugin.addPermission(AntiElytraGoOut.this.permission.getName() + "." + this.getLabel());
            }

            @Override
            protected boolean canNotExecute(@NotNull CommandSender commandSender) {
                return !commandSender.hasPermission(this.permission);
            }

            @Override
            public boolean onCommand(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {

                plugin.setAntiElytraPlayerGoOut(this.isOn);
                plugin.sendInfo(commandSender, "禁止完成鞘翅成就的玩家前往末地外岛设置为：%s".formatted(
                        plugin.isAntiElytraPlayerGoOut() ? "开启" : "关闭"
                ));

                return true;
            }

            @Override
            public @Nullable List<String> onTabComplete(@NotNull CommandSender commandSender, @NotNull Command command, @NotNull String s, @NotNull String[] strings) {
                return null;
            }
        }

    }

}
