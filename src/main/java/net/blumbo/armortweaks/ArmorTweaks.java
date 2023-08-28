package net.blumbo.armortweaks;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import net.blumbo.armortweaks.commands.ArmorTweaksCmd;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.fabricmc.loader.api.FabricLoader;
import net.minecraft.entity.damage.DamageType;
import net.minecraft.registry.*;
import net.minecraft.server.MinecraftServer;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

public class ArmorTweaks implements ModInitializer {

    public static MinecraftServer server = null;
    public static Registry<DamageType> registry = null;

    private static final String dir = FabricLoader.getInstance().getConfigDir().toString() + "/armortweaks";
    private static final String file = "config.json";

    public static final int armorMultiplier = 60;

    private static final String intsKey = "intValues";
    public static HashMap<String, Integer> defaultInts = defaultInts();
    public static HashMap<String, Integer> ints;
    public static final String nakedBuffKey = "noArmorBuff";
    public static final String armorNerfKey = "armorNerf" + armorMultiplier;
    public static final String eProtLowBuffKey = "lowProtBuff";
    public static final String eProtNerfKey = "protNerf";

    private static final String boolsKey = "boolValues";
    public static HashMap<String, Boolean> defaultBools = defaultBools();
    public static HashMap<String, Boolean> bools;
    public static final String vanillaArmorKey = "vanillaArmor";
    public static final String vanillaEnchKey = "vanillaEnch";
    public static final String vanillaDamageKey = "vanillaDamage";
    public static final String sendFeedbackKey = "damageFeedback";

    // Shortcut as I use this in many parts of the code
    public static boolean vanillaDamage() {
        return bools.get(vanillaDamageKey);
    }

    public static void firstTick(MinecraftServer mcServer) {
        server = mcServer;
        registry = ArmorTweaks.server.getRegistryManager().get(RegistryKeys.DAMAGE_TYPE);
    }

    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(ArmorTweaksCmd::register);
        loadValues();
    }

    public static void saveValues() {
        Gson gson = new Gson();

        JsonObject object = new JsonObject();
        object.add(intsKey, gson.toJsonTree(ints));
        object.add(boolsKey, gson.toJsonTree(bools));

        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(dir + "/" + file));
            writer.write(object.toString());
            writer.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private static void loadValues() {
        loadHashMaps();
        addDefaults(defaultInts, ints);
        addDefaults(defaultBools, bools);
    }

    private static <S> void addDefaults(HashMap<String, S> from, HashMap<String, S> to) {
        for (Map.Entry<String, S> entry : from.entrySet()) {
            if (!to.containsKey(entry.getKey())) to.put(entry.getKey(), entry.getValue());
        }
    }

    private static void loadHashMaps() {
        String file = getFile();
        if (file == null) {
            ints = new HashMap<>();
            bools = new HashMap<>();
            return;
        }
        JsonElement element = JsonParser.parseString(file);

        bools = loadHashMap(element, boolsKey);

        ints = new HashMap<>();
        HashMap<String, Double> doubleVals = loadHashMap(element, intsKey);
        for (Map.Entry<String, Double> entry : doubleVals.entrySet()) {
            ints.put(entry.getKey(), entry.getValue().intValue());
        }
    }

    private static <S> HashMap<String, S> loadHashMap(JsonElement element, String key) {
        HashMap<String, S> hashMap;
        try {
            Gson gson = new Gson();
            hashMap = gson.fromJson(((JsonObject)element).get(key), HashMap.class);
            return hashMap;
        } catch (Exception e) {
            return new HashMap<>();
        }
    }

    private static String getFile() {
        try {
            Files.createDirectories(Paths.get(dir));

            Path path = Paths.get(dir + "/" + file);
            if (!Files.exists(path)) return null;
            return Files.readString(path);

        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    public static HashMap<String, Integer> defaultInts() {
        HashMap<String, Integer> map = new HashMap<>();
        map.put(nakedBuffKey, 8);
        map.put(armorNerfKey, 2 * armorMultiplier);
        map.put(eProtLowBuffKey, 10);
        map.put(eProtNerfKey, 11000);
        return map;
    }

    public static HashMap<String, Boolean> defaultBools() {
        HashMap<String, Boolean> map = new HashMap<>();
        map.put(vanillaArmorKey, false);
        map.put(vanillaEnchKey, false);
        map.put(vanillaDamageKey, false);
        map.put(sendFeedbackKey, false);
        return map;
    }

}
