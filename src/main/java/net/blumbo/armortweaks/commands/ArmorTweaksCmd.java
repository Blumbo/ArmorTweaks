package net.blumbo.armortweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.blumbo.armortweaks.ArmorTweaks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

public class ArmorTweaksCmd {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {
        dispatcher.register(CommandManager.literal("armortweaks")
                .then(CommandManager.literal("reload").executes(ArmorTweaksCmd::reload))
                .then(CommandManager.literal("show").executes(ArmorTweaksCmd::show))
                .then(CommandManager.literal("hide").executes(ArmorTweaksCmd::hide))
        );
    }

    public static int reload(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ArmorTweaks.reloadValues();
        context.getSource().sendMessage(Text.of("Reloaded armor values from scoreboard."));
        return 0;
    }

    public static int show(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        ArmorTweaks.scoreboard.setObjectiveSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID, ArmorTweaks.objective);
        return 0;
    }

    public static int hide(CommandContext<ServerCommandSource> context) throws CommandSyntaxException {
        if (ArmorTweaks.scoreboard.getObjectiveForSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID) == ArmorTweaks.objective) {
            ArmorTweaks.scoreboard.setObjectiveSlot(Scoreboard.SIDEBAR_DISPLAY_SLOT_ID, null);
        }
        return 0;
    }

}
