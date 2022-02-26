# ArmorTweaks

ArmorTweaks is a fabric mod that changes the way Minecraft armor calculations work, made for exmperimental purposes and ranting ().

# Installation

Yes

# What does it exactly do

Minecraft currently uses this formula to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/o1xFYJJ.png" width="700" title="hover text">
</p>
Here's a quick explanation of what different things mean:  

- damage stands for how much damage was originally done (for example the attack damage of a weapon which was used).  
- final damage is how much damage the player will end up taking.  
- armor is the amount of armor points the player's armor has.  
- toughness is the amount of armor toughness points the player's armor has.  
- enchantment prot is how much enchantment protection the player has. For example, Protection IV on each piece of armor gives 16 protection points.
- max(a, b) means that the highest value of a and b will be used. For example if a > b, then max(a, b) is equal to a.  
- min(a, b) means that the lowest value of a and b will be used.  
<a/>
In my opinion vanilla armor calculations have some flaws, so I created the mod to experiment with different armor calculations. Here's what the mod uses to calculate damage:
<p align="left">
  <img src="https://i.imgur.com/uwlAqKh.png" width="500" title="hover text">
</p>

# Modifying values in the mod
#### Setup
The mod uses scoreboard values to allow changing some variables of the formula in-game.  
To do that you first need create a dummy scoreboard objective named "armor.tweaks" using the following command:  
/scoreboard objectives add armor.tweaks dummy  
I also recommend adding the scoreboard to the sidebar for a better overview of what you will have changed later on:  
/scoreboard objectives setdisplay sidebar armor.tweaks  
You can't see a sidebar yet, but as soon as you add a value to the scoreboard a sidebar will appear.

#### Modifying values in the formula
Here's what you're able to change in the formula:
<p align="left">
  <img src="https://i.imgur.com/tMUlHSu.png" width="650" title="hover text">
</p>

To change armor.divisor use the following command:  
/scoreboard players set armor.divisor armor.tweaks (your value)  
Reasonable numbers for armor.divisor are something between 25 and 125. Setting this below 1 sets it back to it's default value 30.

To change enchantment.nerf use the following command:  
/scoreboard players set enchantment.nerf armor.tweaks (your value)  
Reasonable numbers for enchantment.nerf are something between 2 and 30.  Setting this below 1 sets it back to it's default value 15.

#### Disabling parts of the formula
You can also replace parts of the mod formula with parts from the vanilla formula and basically make the mod change nothing.
<p align="left">
  <img src="https://i.imgur.com/7sTWZ9C.png" width="650" title="hover text">
</p>
To stop the mod from changing non-enchantment related calculations (marked in blue) use the following command:  
/scoreboard players set vanilla.armor armor.tweaks 1  
To change it back set the score back to 0.

To stop the mod from changing enchantment related calculations (marked in purple) use the following command:  
/scoreboard players set vanilla.enchantment armor.tweaks 1  
To change it back set the score back to 0.

#### Enabling/disabling damage feedback messages
By default, the mod sends a message into the console every time an entity gets hit. The message displays 3 values and looks something like this:  
Base: 10.0 | Armor: 5.0 | Enchantments: 2.5  
"Base" shows the damage originally done, "Armor" shows damage after armor calculations and "Enchantments" shows damage after enchantment (or all) calculations.

To disable this feature use the following command:  
/scoreboard players set send.damage armor.tweaks -1

You can also make the message get sent to the attacker and the player taking damage with the following command:  
/scoreboard players set send.damage armor.tweaks 1  
In the chat this will look something like:
<p align="left">
  <img src="https://i.imgur.com/sdgXjB3.png" width="500" title="hover text">
</p>
To reset this to console-only, set the value back to 0.
