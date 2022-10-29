# ArmorTweaks v2

ArmorTweaks (v2) is a fabric mod that changes different armor and damage mechanics to make survival more suitable for PvP.

# Main features
- Players take less damage by default, but armor matters less and full netherite is weaker than in vanilla
- Enchantment protection has been nerfed and is no longer infinitely strong at a certain protection level
- Enchantment protection is no longer capped at 20
- All explosions deal ~70% of vanilla damage to balance the damage calculation changes
- Anchor explosion power reduced from 5 to 4.5
- Pearl damage decreased from 2.5 to 2 hearts
- Pearl damage untied from feather falling enchantment

# Nerdy stuff

Minecraft currently uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/IN3oP7W.png" width="700" title="hover text">
</p>

The mod (with default settings) instead uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/W2gDFHY.png" width="808" title="hover text">
</p>

*`max(a, b)` means that the highest value of `a` and `b` will be used. For example `max(20, 1)` would be 20, `max(5, 30)` would be 30.  
`min(a, b)` function does the opposite.

### Configuring variables in the formula
You can change some variables in the damage calculation formula via commands.

Here's what you can change related to armor (marked in blue):
<p align="left">
  <img src="https://i.imgur.com/gwf85KH.png" width="796" title="hover text">
</p>

Here's what you can change related to enchantments (also marked in blue (wow)):
<p align="left">
  <img src="https://i.imgur.com/G8qBXab.png" width="705" title="hover text">
</p>

(I split those up so they would fit on the screen)

For a start you can run `/armortweaks showall` to see all the current values to base your changes off of.  
To change a value you'll have to run `/armortweaks set {variable} {value}`.  
To see the impact of what you've changed you can run `/armortweaks set damageFeedback true`. This will send you a message every time you take damage or damage another entity, displaying damage before and after different damage calculations.

You can also disable different parts of the mod by changing some boolean values via `/armortweaks set...`.  
`vanillaArmor` disables modded armor (not enchantment) calculations if set to true.  
`vanillaEnch` disables modded enchantment calculations if set to true.  
`vanillaDamage` disables all modded damage changes (eg explosives, pearls) if set to true.  