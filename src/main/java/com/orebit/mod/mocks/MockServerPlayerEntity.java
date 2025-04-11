/**
 * MockServerPlayerEntity.java
 *
 * Main Component: mocks/
 * Environment: MAIN (server-side only)
 *
 * This class mocks Minecraft’s `ServerPlayerEntity`, which represents a
 * fully logged-in player from the perspective of the server. It is the
 * primary entry point for most player-driven logic: movement, inventory,
 * health, hunger, visibility, chunk loading, and more. This mock is
 * required so that bots are accepted by vanilla systems as true players
 * while still being entirely locally controlled.
 *
 * ---------------------------
 * What This File Does:
 * ---------------------------
 * - Substitutes for a real `ServerPlayerEntity` instance
 * - Allows bots to participate in systems that only accept real players:
 *     - Chunk loading
 *     - Sleep mechanics
 *     - Scoreboards
 *     - Entity tracking
 *     - Permissions
 * - Provides safe overrides for behaviors that expect a real user
 *
 * ---------------------------
 * Why This Is Necessary:
 * ---------------------------
 * - Many Minecraft systems will not function unless the entity is of type `ServerPlayerEntity`
 * - Minecraft uses `instanceof ServerPlayerEntity` to check for "real" players
 * - A bot must pass those checks and behave similarly, while being internally controlled
 *
 * ---------------------------
 * Expectations of the Mock:
 * ---------------------------
 * - Must be fully compatible with systems that expect a real player
 * - Should override movement, input, and interaction to be fully local
 * - Must not trigger networking-related side effects
 *
 * ---------------------------
 * How This Differs from Vanilla:
 * ---------------------------
 * - Input and output are handled by local AI, not a client
 * - Network handlers are mocked (`MockServerPlayNetworkHandler`)
 * - Events like death, interaction, or sleep are handled internally
 *
 * ---------------------------
 * Dependencies:
 * ---------------------------
 * - Minecraft’s `ServerPlayerEntity`: Extended or mimicked
 * - `VirtualPlayerController`: Feeds behavior into this mock
 * - `VirtualPlayerContext`: May reference this entity
 *
 * ---------------------------
 * Dependents:
 * ---------------------------
 * - `BotManager`: Tracks active player-like bots
 * - `RegionLoader`: Relies on bots to keep chunks loaded
 * - Any vanilla logic that checks `ServerPlayerEntity` type
 *
 * ---------------------------
 * Implementation Details:
 * ---------------------------
 * - Constructor should call the parent with mocked dependencies
 * - Override AI-sensitive methods like:
 *     - `tick()`
 *     - `onDeath()`
 *     - `attack()`
 *     - `interact()`
 * - Support serialization or replacement as needed by world loading
 */
package com.orebit.mod.mocks;
