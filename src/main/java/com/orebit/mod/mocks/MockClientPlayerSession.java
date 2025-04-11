/**
 * MockClientPlayerSession.java
 *
 * Main Component: mocks/
 * Environment: MAIN (server-side only)
 *
 * This class provides a mock implementation of Minecraft’s internal
 * `ClientPlayerSession`, which normally handles network-backed interaction
 * between the server and a connected client. This mock is used to simulate
 * the presence of a player-like entity (the virtual bot) that behaves like a
 * networked player in terms of chunk loading, rendering, and entity logic—
 * but exists entirely on the server and does not involve a real client.
 *
 * ---------------------------
 * What This File Does:
 * ---------------------------
 * - Mocks the `ClientPlayerSession` class used for real players
 * - Allows the virtual bot to receive server-driven updates without requiring a socket connection
 * - Intercepts or suppresses packet handling and client-bound sync that would be wasted on the bot
 * - Ensures the server accepts the bot as a valid player entity
 *
 * ---------------------------
 * Why This Is Necessary:
 * ---------------------------
 * - Minecraft's server code assumes all `ServerPlayerEntity` instances have an associated session
 * - Certain systems (e.g., chunk loading, advancement tracking, packet broadcasting) break without one
 * - A virtual player must "fit in" with these systems while handling everything internally
 *
 * ---------------------------
 * Expectations of the Mock:
 * ---------------------------
 * - Must behave like a valid session from the server's perspective
 * - Must **not** attempt to transmit or decode network traffic
 * - Should route certain internal "packets" directly to logic modules (e.g., inventory, animation, stats)
 * - May stub or override behaviors that would otherwise error out without a socket
 *
 * ---------------------------
 * How This Differs from Vanilla:
 * ---------------------------
 * - Does not create or hold a network channel
 * - Overrides all methods that expect a client to respond or acknowledge
 * - Fakes client acknowledgment of animations, ticks, syncs, etc.
 *
 * ---------------------------
 * Dependencies:
 * ---------------------------
 * - Minecraft’s `ClientPlayerSession` (via extension or reflection)
 * - `VirtualPlayer`: The bot that this mock session is attached to
 * - `VirtualPlayerController`, `BotLifecycleHooks`: Use this to insert the bot into the world cleanly
 *
 * ---------------------------
 * Dependents:
 * ---------------------------
 * - The server's networking layer (indirectly): will interact with this mock as if it were a real session
 * - `InventoryAdapter`, `AnimationHandler`, `ReplayRecorder`: May intercept events here
 * - `RegionLoader`, `ChunkManager`, `EntityTracker`: Will detect this session as valid
 *
 * ---------------------------
 * Implementation Details:
 * ---------------------------
 * - All packet-sending methods should be overridden to perform **no-op or local processing**
 * - Should respond "successfully" to any pings, handshakes, or queries the server performs
 * - May route animation or command feedback directly into bot-side logic
 * - Ensure this class survives version changes in Minecraft mappings or protocol
 */
package com.orebit.mod.mocks;
