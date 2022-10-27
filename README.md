# ArmorTweaks v2

ArmorTweaks (v2) is a fabric mod that changes different armor and damage mechanics to make survival more suitable for PvP.

# Main features
- Players take less damage by default, but armor matters less and full netherite is weaker than in vanilla
- Enchantment protection, especially in higher levels, has been nerfed
- Enchantment protection is no longer capped at 20
- All explosions deal ~70% of vanilla damage (before armor calculations)
- Anchor explosion power reduced from 5 to 4.5
- Pearl damage decreased from 2.5 to 2 hearts
- Pearl damage untied from feather falling enchantment

# Nerdy stuff
### Formula used for calculating damage

Minecraft currently uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/IN3oP7W.png" width="700" title="hover text">
</p>

The mod (with default settings) uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/W2gDFHY.png" width="808" title="hover text">
</p>

*max(a, b) means that the highest value of a and b will be used. For example max(20, 1) is 20, max(5, 30) is 30.  
Min(a, b) function does the opposite.  

### Configuring variables in the mod
The mod uses scoreboard values to allow configuring different variables.  
You should start off by executing `/armortweaks show` in-game to see which variables you're able to change. A scoreboard should show up in the sidebar.  
  
To change a value you must run  
`/scoreboard players set *your variable* armor.tweaks *your value*`.  
Eg if you wanted to set `low.armor.buff` to 7, you would have to run  
`/scoreboard players set low.armor.buff armor.tweaks 7`  
If you're done modifying values you must run `/armortweaks reload` to actually apply the changes.  

Here's what you can change related to armor (marked in blue):
<p align="left">
  <img src="https://i.imgur.com/Z0xiXga.png" width="687" title="hover text">
</p>

Here's what you can change related to enchantments (also marked in blue 😱):
<p align="left">
  <img src="https://i.imgur.com/tXwygb0.png" width="612" title="hover text">
</p>

You can also disable all the mod features if you want to:  
`vanilla.armor` disables modded armor (not enchantment) calculations if set to 1.  
`vanilla.ench` disables modded enchantment calculations if set to 1.  
`vanilla.damage` disables all modded damage changes (eg explosives, pearls) if set to 1.  
Every scoreboard value change will only be updated after running `/armortweaks reload`.
