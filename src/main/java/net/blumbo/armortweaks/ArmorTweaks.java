package net.blumbo.armortweaks;

import net.blumbo.armortweaks.commands.ArmorTweaksCmd;
import net.fabricmc.api.ModInitializer;
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback;
import net.minecraft.scoreboard.Scoreboard;
import net.minecraft.scoreboard.ScoreboardCriterion;
import net.minecraft.scoreboard.ScoreboardObjective;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.text.Text;

public class ArmorTweaks implements ModInitializer {

    public static ServerWorld overWorld;
    public static Scoreboard scoreboard;
    public static ScoreboardObjective objective;

    public static final int armorMultiplier = 60;

    public static boolean vanillaArmor = false;
    public static boolean vanillaEnchantment = false;
    public static boolean vanillaDamage = false;

    // Higher numbers mean more resistance for the player
    public static Integer nakedBuff = 10;
    // Higher numbers mean lower armor protection efficiency
    public static Integer armorNerf = 144;

    // Higher numbers mean lower enchantment protection efficiency
    public static Integer eProtNerf = 11000;
    // Higher numbers mean higher enchantment protection efficiency, mainly affects low protection levels
    public static Integer eProtLowLevelBuff = 10;

    public static boolean damageFeedback = false;

    public void onInitialize() {
        CommandRegistrationCallback.EVENT.register(ArmorTweaksCmd::register);
    }

    private static void scoreboardSetup(ServerWorld world) {
        overWorld = world;
        scoreboard = overWorld.getScoreboard();

        String objectiveName = "armor.tweaks";
        if (scoreboard.getObjective(objectiveName) == null) {
            scoreboard.addObjective(objectiveName, ScoreboardCriterion.DUMMY,
                    Text.of(objectiveName), ScoreboardCriterion.RenderType.INTEGER);
        }
        objective = scoreboard.getObjective(objectiveName);
    }

    public static void reloadValues(ServerWorld world) {
        scoreboardSetup(world);
        Integer score;

        score = getTweakScore("low.armor.buff", nakedBuff);
        if (score != null) nakedBuff = score;
        score = getTweakScore("armor.nerf.per" + armorMultiplier, armorNerf);
        if (score != null) armorNerf = score;
        score = getTweakScore("low.prot.buff", eProtLowLevelBuff);
        if (score != null) eProtLowLevelBuff = score;
        score = getTweakScore("prot.nerf", eProtNerf);
        if (score != null) eProtNerf = score;

        score = getTweakScore("vanilla.armor", vanillaArmor);
        if (score != null) vanillaArmor = score == 1;
        score = getTweakScore("vanilla.ench", vanillaEnchantment);
        if (score != null) vanillaEnchantment = score == 1;
        score = getTweakScore("vanilla.damage", vanillaDamage);
        if (score != null) vanillaDamage = score == 1;

        score = getTweakScore("damage.feedback", damageFeedback);
        if (score != null) damageFeedback = score == 1;
    }

    private static Integer getTweakScore(String scoreName, int score) {
        if (scoreboard.playerHasObjective(scoreName, objective)) {
            return scoreboard.getPlayerScore(scoreName, objective).getScore();
        }
        scoreboard.getPlayerScore(scoreName, objective).setScore(score);
        return null;
    }

    private static Integer getTweakScore(String scoreName, boolean score) {
        if (scoreboard.playerHasObjective(scoreName, objective)) {
            return scoreboard.getPlayerScore(scoreName, objective).getScore();
        }
        scoreboard.getPlayerScore(scoreName, objective).setScore(score ? 1 : 0);
        return null;
    }

}
