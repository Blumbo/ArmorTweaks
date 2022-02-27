# Combat Tweaks server mod
Combat Tweaks is a combat test 8c mod and a fork of Armor Tweaks. The mod changes some combat mechanics and was created for a server I'm working on.
The server's discord: https://discord.gg/Mj2ehN9XVN
### List of everything different from combat snapshot 8c:  
- Swords do the same amount of damage as in 1.16
- Axes do 1 more damage than swords
- Pickaxes do 1 less damage than swords
- Shovels do 1 less damage than pickaxes
- Tridents do 8 damage instead of 7
- Impaling has been nerfed: First enchantment level gives +2 damage in water, each next enchantment gives +1
- Enchantment calculations use the (15/(15+x)) formula instead of (1-x/25) where x is enchantment protection
- Enchantment protection is no longer capped at 20
- Explosions do 0.5625 times the damage as in 1.16 (as a balance for nerfed protection)
<a/>

Note: Due to the way I've made the mod a lot of weapons do not show their accurate attack damage in-game. The only source with accurate information about weapon damage is this github page.
### Armor tweaks settings
Armor tweaks settings (scoreboard values) used in Combat Tweaks server:  
- armor.divisor armor.tweaks 30
- enchantment.nerf armor.tweaks 15
- vanilla.armor armor.tweaks 1
- vanilla.enchantment armor.tweaks 0
- send.damage armor.tweaks -1
