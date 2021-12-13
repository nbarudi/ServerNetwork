# Server Network
(Disclaimer: Since classes started Development will be delayed until my schedule opens up)

## Types of Servers:
    1. RDvZ >>
       1. Retro Dwarves Vs Zombies
       2. Consisting of a fully functional **EVENT** server
          1. Could evolve into an automated server if population continues
    2. Mafia >>
       1. Town of Salem style Mafia gamemode
       2. Will Consist of 4 types of roles
          1. Innocents -> Must find the mafia before losing majority
          2. Mafia -> Must kill enough innocents to gain majority
          3. Sheriff -> Can *Detect* one player each night unless dead
          4. Doctor -> Can heal one player each night unless dead
    3. Basic Survival >>
       1. Normal survival with block based land claiming
          1. AKA I don't like factions
    4. Hardcore Survival >>
       1. Hardcore style server running custom Hardcore plugin
          1. (Re-Creating a previous hardcore servers plugin I made)


# Server 'Gimmick'
## Leveling System
    - Each server will have 1 combined leveling service
    - Levels will allow you to unlock restricted features depending on which server you play on
    - Levels are gained in multiple different ways
      - Each method if gaining a level requires you to gain an ever increasing amount of experience based on which level you are trying to obtain
        - (1000 -> LvL 2 | 1250 -> LvL 3 | 1500 -> LvL 4 | etc..)
    - Levels are planned to be gained using one of these methods:
      - Killing mobs >>
        - Killing any Friendly, Neutral or, Hostile mob in Survival / Hardcore survival will gain you a pre set amount of experience.
        - Killing monsters in RDvZ will also gain experience
          - (As well as monsters killing dwarves will gain extra)
      - Completing a gamemode >>
        - Finishing 1 round of RDvZ will gain you a set amount of experience
        - Finishing 1 round of Mafia will gain you experience
        - Winning 1 round of Mafia will gain you extra experience
### Leveling in Survival / Hardcore
    - Each level will gain the user 1 SP (Skill Point)
      - Skill points can be used to unlock extra abilities / perks that are usable in survival
      - Perks may consist of >>
        - Keep Inventory
        - Keep Experience
        - Auto Smelt
        - Extra Fortune
        - Permanent Potion Effects
        - Flight
        - Extra Block Claims
        - *More to come*
    - Leveling up will gain the player extra claim blocks for survival

    **Hardcore Specific**
    - Skill points are gained at the same rate
    - Perks cost more (how much unknown) to purchase with SP
    - Players can buy more lives with their SP 
       (Cost to be set based on balance as time goes on)

### What is Lootable Corpses?
If you cannot follow along with what the name explains here is the general idea:

Lootable Corpses is a small side project I was working on to allow for a different death system to exist on the server.
The idea for this death system is instead of a player being killed and dropping all their items on the ground,
their items are stored in a 'corpse' object that lays on the ground. Players can return to their death point and recollect their items once they respawn and allows for their items to not be despawned if they take too long.

Key features: Corpses persist between server restarts / reloads allowing for players to not worry about losing items if the server was to crash or reboot.

This plugin is public use but currently (At the time of writing this) only functions on the most recent version of Spigot 1.16.5.
The reasoning behind this limitation is that this feature requires a lot of NMS Packet logic which is version specific.
Future plans and updates for this plugin can be found on its Github Repo [Here](https://github.com/nbarudi/LootableCorpses)

## Plans will change as time goes on depending on new ideas, server plans, and balance changes.
*
*
*
*
# Programming Plans
