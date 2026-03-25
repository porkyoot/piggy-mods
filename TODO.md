[x] Placing adjacent blocks gets interupted if an existing block of the same kind is in the way
[x] Placing adjacent block doesn't continue placing when in diagonal/stair mode
[x] Tool switching is not working as intended and needs to be rewritten
[x] New Ui for tool switching
[x] XRay detection should not trigger mining blocks above y=64
[x] FEATURE: Add logging (like with the sign logging) of when someone is placing TNT, tnt minecarts, or tnt minecart tracks, in a dispenser. Also when they cause an explosion.
[x] Stack restocking isn't fast enough when fast placing blocks
[x] Sorting doesn't merge stacks together to gain inventory space
[x] Sorting in grid isn't consistent depending on the existing arangement (should be independant of existing arangement)
[x] Sorting should ensure that same items are adjacent to each other
[x] sorting algorithm gets stuck in player inventory with locked slots and two type of stackable items
[x] Add support for deposit/withdraw all items to/from a modded chest or inventory (sophisticated storage)
[x] Add message when break protection saved the player
[x] Protect non silktouchable blocks in silktouch mode from being mined at all (budding amethyst, suspicious blocks)
[x] Visuals got broken with shaders
[x] Adding continous crafting
[x] Adding continious operations like stone cutter, etc
[x] XRAY add iron, gold and emeralds to the list of precious ores. Add longer detection window too
[x] TNT Tp the author not @s (admin) like intended
[x] Log ender crystal and beds in other dimmensions too
[x] AI anti swear, insults, threats, dox, politics/religion/sensitive topics
[x] Make ALL operations obey the click per second setting
[x] Remove debug before release
[x] FEATURE: Adding light overlay
[x] Sorting bug in big inventory (debug)
[x] Make modes more understandable and explicit in inventory mod
[x] Light level message less intrusive. Less message in general
[x] Inverted loot and depo icons
[x] Build : parkour placing blocks under you auto
[-] Investigate villager fast trading
[-] Inventory tabs !
[ ] Piggy build other shapes ?
[x] Fast loot and depo in other containers (furnaces, modded anvils, etc) (only matching items)
[x] CPS is set to 0 in the config (unlimited) but enforced to 1 instead
[x] CPS is inconsistent in bloc placing mode
[x] MLG is not working
[x] Can't disable tool switching using the middle option, (might be the same for weapon switching)
[x] Add Snow bucket MLG
[x] Add twisting vines MLG
[x] Add bed MLG (DO NOT SLEEP, it kills you even in the overworld (Minecraft bug))
[x] Add boat MLG
[-] TRY for vines and ladder MLG
[x] Add existing rideable entities MLG (minecart, etc) + Saddle animals if not already saddled
[x] Weapon switch should be high priority
[x] Telemetry and verbose logs should be level debug and disabled in production. But for user facing errors (sort or mlg fails) we should display a chat message with an explanation and a link to the file for issue reporting.
[x] Take into account MLG cost (item consumption) when selecting the best strategy
[x] MLG should be able to use items from the offhand
[x] When falling on water loggable bloc un prioritise the water bucket. If selected anyway, use the flexible bloc placement to place the water ABOVE the water loggable block unless the water loggable block is the safe once water logged
[x] Do not flash MLG icon if no viable method have been found
[x] Stop flashing MLG icon when safe or dead
[x] Make a generic place bloc action that will try to place a bloc no mater what (flexible placement) (like the flexible block placement in build mode)
[x] Move all generic action to Piggy-lib (scoop liquid, consume food, mount entity, throw eggs/snowballs)