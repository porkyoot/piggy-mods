# Piggy Mods Regression Test Suite

This document serves as a comprehensive manual test suite to ensure no regressions occur before a new release. Tests are categorized by mod.

## 1. piggy-build
### Block Placement
- [ ] **Adjacent Block Placement:** Verify that placing adjacent blocks is correctly interrupted if an existing block of the same kind is already in the way.
- [ ] **Diagonal/Stair Mode:** Verify that adjacent block placement does *not* incorrectly continue to place blocks when in diagonal or stair mode.
- [ ] **Continuous/Fast Placing:** Verify that building quickly smoothly restocks the current stack from the inventory without delays.
- [ ] **Auto-Parkour:** Verify that when parkour mode is active, jumping off an edge automatically and reliably places a block under the player.
- [x] **CPS Limiter Integration:** Fast placement should adhere to the global Clicks Per Second (CPS) limit defined in the config. (Set CPS to 0 and verify it allows unlimited, not forced to 1).

### Auto-MLG
- [ ] **Fall Prediction:** Jump off a varying heights and ensure the fall predictor correctly calculates the `impactPos`.
- [ ] **Ledge Clipping Mitigation:** Verify that the 4-corner downward raycast correctly identifies the highest block when falling near ledges/corners.
- [ ] **MLG State Machine:** Ensure the state machine cycles cleanly through `IDLE -> FALLING -> PREPARATION -> EXECUTION -> RECOVERY -> CLEANUP`.
- [ ] **Method: Water Bucket:** Verify successful MLG and that the water is picked back up during cleanup.
- [ ] **Method: Slime Block:** Verify successful MLG.
- [ ] **Method: Boat:** Verify successful MLG and that the player enters the boat safely.
- [ ] **Method: Chorus Fruit:** Verify successful MLG and ensure the `keyUse` input (right-click) is correctly released afterward so the player doesn't continuously eat.
- [ ] **Method: Cobweb:** Verify successful MLG.
- [ ] **Bounce Handling (Loop Bug Fix):** Perform a bounce (e.g., off a slime block) and ensure the MLG system triggers a *second* MLG attempt successfully if falling again, rather than getting suppressed/stuck.

### Visuals
- [ ] **Light Overlay:** Toggle the light overlay feature and verify it highlights correct light levels.
- [ ] **Shader Compatibility:** Verify that the light overlay and other custom rendering do not break when shaders (e.g., Iris) are enabled.

---

## 2. piggy-inventory
### Sorting
- [ ] **Basic Sorting:** Verify grid sorting handles normal items correctly.
- [ ] **Stack Merging:** Verify sorting merges incomplete stacks together to maximize inventory space.
- [ ] **Adjacency:** Verify that items of the same type are placed adjacent to each other.
- [ ] **Consistency:** Verify that sorting the same items multiple times results in the same layout, completely independent of the existing disorganized arrangement.
- [ ] **Locked Slots:** Verify that sorting completely ignores and preserves the contents of locked inventory slots.
- [ ] **Large Inventories:** Test sorting in a large chest (e.g., double chest or modded expansion) to ensure no bugs or missing items occur.

### Quick Loot & Deposit
- [ ] **Vanilla Containers:** Test fast loot/deposit in Chests, Trapped Chests, Barrels.
- [ ] **Specialized Containers:** Test fast loot/deposit in Furnaces, Smokers, Blast Furnaces, Crafters, Droppers, Dispensers, Hoppers. Ensure items only go to valid slots (e.g., fuel in furnaces).
- [ ] **Modded Containers:** Test with Sophisticated Storage or similar modded inventories.
- [ ] **Icon Rendering:** Verify the `quick_sort.png` icon (and inverted variants) render correctly in the container GUI.

### Tool & Weapon Swapping
- [ ] **Auto-Swap Logic:** Verify the mod selects the best tool for the block currently being mined.
- [ ] **Silk Touch Protection:** Try mining Budding Amethyst or Suspicious Sand/Gravel without a Silk Touch tool. The mod should prevent mining it entirely.
- [ ] **Durability Warning:** Degrade a tool to near-broken and verify the `tool_break.png` flashing icon appears.
- [ ] **UI/Feedback:** Verify the new Tool Switching UI functions as intended and cleanly displays fast placement speed feedback.
- [ ] **Break Protection Saving:** Verify the "break protection saved the player" message is triggered properly when a tool is about to break.

### Continuous Operations
- [ ] **Continuous Crafting:** Test holding down the craft button to continuously craft items.
- [ ] **Continuous Stonecutter:** Test holding down the output slot in a stonecutter to continuously cut items.

---

## 3. piggy-admin
### Moderation & Logging
- [ ] **AI Chat Moderation:** Attempt to send a message containing a swear, insult, threat, dox, or sensitive topic and verify the AI flags/blocks it.
- [ ] **Explosive Logging (TNT):** Place TNT, TNT minecarts, and TNT minecart tracks in a dispenser. Verify logs are generated. Trigger an explosion and verify the log correctly identifies the author (and not `@s`).
- [ ] **Dimension Logging (Beds/Crystals):** Place and detonate an Ender Crystal or Bed in another dimension. Verify the logging works.
- [ ] **Admin Formatting:** Verify admin messages are standardized, properly formatted, and have working links.

### Anti-Cheat
- [ ] **X-Ray Detection:** Mine Iron, Gold, and Emeralds. Ensure it flags suspicious long-window mining. 
- [ ] **Y-Level Constraints:** Verify X-Ray detection does *not* trigger when mining blocks above y=64.

---

## 4. piggy-lib (Core)
### Action Queue & Standardization
- [ ] **Action Polling:** Verify interactions require verification (e.g., a block is actually placed or slot is actually changed) before popping from the queue.
- [x] **CPS Enforcer:** Verify that generic actions (sorting, cleanup) abide by normal CPS limits, but `HIGHEST` priority frame-perfect actions (like MLG) bypass the human-like CPS limits.
- [ ] **Messaging Standardization:** Ensure all toast/on-screen notifications have been successfully converted to chat messages via the `PiggyMessenger`.
- [ ] **Config/Keybinds:** Open the ModMenu config for Piggy Mods. Verify that keybinds are editable interactive elements (opening the vanilla KeyBinds screen) rather than non-editable text.
