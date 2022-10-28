package net.blumbo.armortweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import net.blumbo.armortweaks.ArmorTweaks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ArmorTweaksCmd {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("armortweaks")
                .then(CommandManager.literal("reload").executes(ArmorTweaksCmd::reload))
                .then(CommandManager.literal("show").executes(ArmorTweaksCmd::show))
                .then(CommandManager.literal("hide").executes(ArmorTweaksCmd::hide))
                .then(CommandManager.literal("setvanilla").executes(ArmorTweaksCmd::toVanilla))
                .then(CommandManager.literal("setmodded").executes(ArmorTweaksCmd::unVanilla))
        );
    }

    public static int reload(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        ArmorTweaks.reloadValues(source.getWorld());
        source.sendMessage(Text.of("Reloaded armor values from scoreboard."));
        return 0;
    }

    private static int show(CommandContext<ServerCommandSource> context) {
        ArmorTweaks.scoreboard.setObjectiveSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID, ArmorTweaks.objective);
        return 0;
    }

    private static int hide(CommandContext<ServerCommandSource> context) {
        if (ArmorTweaks.scoreboard.getObjectiveForSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID) == ArmorTweaks.objective) {
            ArmorTweaks.scoreboard.setObjectiveSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID, null);
        }
        return 0;
    }

    public static int toVanilla(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        toggleVanilla(source.getWorld(), 1);
        source.sendMessage(Text.of("Toggled all values to vanilla."));
        return 0;
    }

    public static int unVanilla(CommandContext<ServerCommandSource> context) {
        ServerCommandSource source = context.getSource();
        toggleVanilla(source.getWorld(), 0);
        source.sendMessage(Text.of("Toggled all values to modded."));
        return 0;
    }

    private static void toggleVanilla(ServerWorld world, int score) {
        for (String str : amazingCodeBlumbo) {
            ArmorTweaks.scoreboard.getPlayerScore(str, ArmorTweaks.objective).setScore(score);
        }
        ArmorTweaks.reloadValues(world);
    }

    private static String[] amazingCodeBlumbo = {"vanilla.armor", "vanilla.ench", "vanilla.damage"};
}
