# ArmorTweaks v2

ArmorTweaks (v2) is a fabric mod that changes different armor and damage mechanics to make survival more suitable for PvP.

# Features

Minecraft currently uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/IN3oP7W.png" width="700" title="hover text">
</p>

The mod (with default settings) uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/W2gDFHY.png" width="808" title="hover text">
</p>
*max(a, b) means that the highest value of a and b will be used. For example max(20, 1) is 20, max(5, 30) is 30. Min(a, b) function does the opposite.  
<a/>

# Modifying variables in armor calculation formula
The mod uses scoreboard values to allow configuring some variables of the formula in-game.  
You should start off by executing `/armortweaks show` in-game to see which variables you're able to change. A scoreboard should show up in the sidebar.  
  
To change a value you must run  
´/scoreboard players set *your variable* armor.tweaks *your value*`.  
Eg if you wanted to set low.armor.buff to 7, you would have to run  
´/scoreboard players set low.armor.buff armor.tweaks 7`  
If you're done modifying values you must ´/armortweaks reload` to actually apply the changes.  

Here's what you can change related to armor (marked in blue):
<p align="left">
  <img src="https://i.imgur.com/Z0xiXga.png" width="687" title="hover text">
</p>

Here's what you can change related to enchantments (marked in blue):
<p align="left">
  <img src="https://i.imgur.com/tXwygb0.png" width="612" title="hover text">
</p>

You can also technically disable all the mod features (replace them with vanilla).  
`vanilla.armor` disables modded armor (not enchantment) calculations if set to 1.  
`vanilla.ench` disables modded enchantment calculations if set to 1.  
`vanilla.damage` disables all modded damage values if set to 1.  
