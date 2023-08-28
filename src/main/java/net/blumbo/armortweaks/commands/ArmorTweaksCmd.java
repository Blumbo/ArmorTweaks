package net.blumbo.armortweaks.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import net.blumbo.armortweaks.ArmorTweaks;
import net.minecraft.command.CommandRegistryAccess;
import net.minecraft.server.command.CommandManager;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.Text;

import java.util.HashMap;
import java.util.Map;

public class ArmorTweaksCmd {

    public static void register(CommandDispatcher<ServerCommandSource> dispatcher,
                                CommandRegistryAccess registryAccess,
                                CommandManager.RegistrationEnvironment environment) {

        LiteralArgumentBuilder<ServerCommandSource> argumentBuilder = CommandManager.literal("armortweaks")
                .then(CommandManager.literal("showall").executes(ArmorTweaksCmd::showAll));

        LiteralArgumentBuilder<ServerCommandSource> set = CommandManager.literal("set");
        LiteralArgumentBuilder<ServerCommandSource> reset = CommandManager.literal("reset");

        for (String key : ArmorTweaks.defaultInts.keySet()) {
            set.then(CommandManager.literal(key)
                    .then(CommandManager.argument("value", IntegerArgumentType.integer(0))
                            .executes(context -> setInt(context, key))));
            reset.then(CommandManager.literal(key).executes(context -> resetInt(context, key)));
        }
        for (String key : ArmorTweaks.defaultBools.keySet()) {
            set.then(CommandManager.literal(key)
                    .then(CommandManager.argument("value", BoolArgumentType.bool())
                            .executes(context -> setBool(context, key))));
            reset.then(CommandManager.literal(key).executes(context -> resetBool(context, key)));
        }

        argumentBuilder.then(set).then(reset);
        dispatcher.register(argumentBuilder);
    }

    private static int showAll(CommandContext<ServerCommandSource> context) {
        StringBuilder stringBuilder = new StringBuilder();
        addMap(stringBuilder, ArmorTweaks.ints);
        addMap(stringBuilder, ArmorTweaks.bools);
        stringBuilder.append("\n");
        context.getSource().sendMessage(Text.of(stringBuilder.toString()));
        return 0;
    }

    private static <S, T> void addMap(StringBuilder stringBuilder, HashMap<S, T> hashMap) {
        for (Map.Entry<S, T> entry : hashMap.entrySet()) {
            stringBuilder.append("\n§7").append(entry.getKey()).append(" ").append(formatVal(entry.getValue()));
        }
    }

    private static int resetInt(CommandContext<ServerCommandSource> context, String key) {
        reset(context.getSource(), key, ArmorTweaks.defaultInts.get(key), ArmorTweaks.ints);
        return 0;
    }

    private static int resetBool(CommandContext<ServerCommandSource> context, String key) {
        reset(context.getSource(), key, ArmorTweaks.defaultBools.get(key), ArmorTweaks.bools);
        return 0;
    }

    private static <T> void reset(ServerCommandSource source, String key, T defaultVal, HashMap<String, T> hashmap) {
        T prev = hashmap.get(key);
        if (prev.equals(defaultVal)) {
            source.sendMessage(Text.of("§7" + key + " §7is already the default value §c" + defaultVal));
            return;
        }
        hashmap.put(key, defaultVal);
        source.sendMessage(Text.of("§7" + key + " §7reset from " + formatVal(prev) + " §7to " + formatVal(defaultVal)));
    }


    private static int setInt(CommandContext<ServerCommandSource> context, String key) {
        int value = context.getArgument("value", Integer.class);
        set(context.getSource(), key, value, ArmorTweaks.ints);
        return 0;
    }

    private static int setBool(CommandContext<ServerCommandSource> context, String key) {
        boolean value = context.getArgument("value", Boolean.class);
        set(context.getSource(), key, value, ArmorTweaks.bools);
        return 0;
    }

    private static <T> void set(ServerCommandSource source, String key, T value, HashMap<String, T> hashmap) {
        T prev = hashmap.get(key);
        if (prev.equals(value)) {
            source.sendMessage(Text.of("§7" + key + " §7has already been set to §c" + value));
            return;
        }
        hashmap.put(key, value);
        source.sendMessage(Text.of("§7" + key + " §7set from " + formatVal(prev) + "§7 to " + formatVal(value)));
    }

    private static <T> String formatVal(T value) {
        if (value instanceof Boolean booleanValue) {
            if (booleanValue) return "§e" + true;
            return "§6" + false;
        }
        return "§b" + value;
    }

}
