/**
 * BotManager.java
 *
 * Main Component: manager/
 * Environment: MAIN
 *
 * This class is the central registry and coordinator for all active bots
 * in the world. It tracks bot instances, allows global lookup by ID or
 * association (e.g., player ownership), and provides APIs for querying,
 * iterating, or interacting with bots at a population-wide scale.
 * It does not manage behavior or lifecycle directly, but acts as the
 * authoritative source of truth for “which bots exist right now.”
 *
 * ---------------------------
 * What This File Does:
 * ---------------------------
 * - Tracks all currently active `VirtualPlayerContext` instances
 * - Provides lookup methods (e.g., by UUID, by associated player, by proximity)
 * - Registers bots during spawn and deregisters during despawn
 * - May assign metadata, names, tags, or ownership info
 * - Exposes APIs for external systems to interact with bots in aggregate
 *
 * ---------------------------
 * How This File Differs:
 * ---------------------------
 * - Unlike `BotLifecycleHooks`, this class does not create or destroy bots—it tracks them
 * - Unlike `VirtualPlayerController`, this class does not control bots—it catalogs them
 * - Unlike `SaveManager`, this class is not persistent—it operates at runtime only
 *
 * ---------------------------
 * Assumptions:
 * ---------------------------
 * - All bots are registered immediately after creation and deregistered cleanly
 * - Bots can be safely identified via a UUID or other unique key
 * - This system may be queried frequently by task planners, schedulers, or command systems
 *
 * ---------------------------
 * Dependencies:
 * ---------------------------
 * - `VirtualPlayerContext`: Represents each registered bot
 * - `BotLifecycleHooks`: Registers and deregisters bots here during spawn/despawn
 * - Optional: `GlobalSettings`, `RelationshipManager`, `AIStateMachine` (indirectly)
 *
 * ---------------------------
 * Dependents:
 * ---------------------------
 * - `CommandHandler`, `ReplayRecorder`, `RegionGraph`, and others: May query or iterate bots
 * - `PlayerMonitor`: May look up a player's associated bot
 * - `TaskAssignmentManager`: May route tasks to bots via lookup
 *
 * ---------------------------
 * Implementation Details:
 * ---------------------------
 * - Likely maintains a `Map<UUID, VirtualPlayerContext>` for bot lookup
 * - May maintain additional indexes (e.g., `Map<PlayerUUID, BotUUID>`)
 * - Should provide hooks for safe registration and deregistration
 * - May emit `BotSpawnedEvent` or `BotDespawnedEvent` on state change
 */
package com.orebit.mod.manager;
