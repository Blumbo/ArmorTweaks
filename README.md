# ArmorTweaks

ArmorTweaks is a fabric mod that changes the way Minecraft armor calculations work, made for exmperimental purposes and ranting ().

# Installation

Yes

# What does it exactly do

Minecraft currently uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/Nfm47Uj.png" width="650" title="hover text">
</p>
Here's a quick explanation of what different things mean:
- damage stands for how much damage was originally done (for example the attack damage of a weapon which was used)
- final damage is how much damage the player will end up taking
- armor is the amount of armor points player's armor has
- toughness is the amount of armor toughness points player's armor has
- enchantment prot is the amount of protection points player has. For example, Protection IV on each piece of armor gives 16 protection points. Protection IV on 3 pieces of armor and Blast Protection IV on one piece of armor would give the player 20 protection points for explosion damage and 12 protection points for other sorts of damage.
- max(a, b) means that the highest value of a and b will be used. For example if a > b, then max(a, b) will be a.
- min(a, b) means that the lowest value of a and b will be used.

In my opinion vanilla armor calculations have some flaws, so I created the mod to experiment with different armor calculations. Here's the formula the mod uses:
<p align="left">
  <img src="https://i.imgur.com/Nfm47Uj.png" width="https://i.imgur.com/4ZPyfDn.png" title="hover text">
</p>
