/**
 * MockServerPlayNetworkHandler.java
 *
 * Main Component: mocks/
 * Environment: MAIN (server-side only)
 *
 * This class provides a mock implementation of Minecraft’s `ServerPlayNetworkHandler`,
 * which normally handles incoming packets and outbound communications between the
 * server and a real player after login is complete. For a bot, no such client exists,
 * but Minecraft’s server expects this handler to be present and fully functional.
 * This mock allows the virtual player to complete login, stay connected, and
 * respond to server-driven events without generating errors or attempting to send
 * real packets.
 *
 * ---------------------------
 * What This File Does:
 * ---------------------------
 * - Stands in for a fully initialized server-side network handler
 * - Allows `ServerPlayerEntity` (the bot) to complete its login lifecycle
 * - Safely no-ops or redirects all incoming and outgoing packet handlers
 * - Ensures compatibility with player-bound systems like stats, chat, and advancements
 *
 * ---------------------------
 * Why This Is Necessary:
 * ---------------------------
 * - `ServerPlayerEntity` requires a `ServerPlayNetworkHandler` to function
 * - Without one, systems like `ServerPlayNetworkHandler#tick()` and `PlayerList` will crash
 * - Minecraft routes nearly all player-specific state updates through this class
 *
 * ---------------------------
 * Expectations of the Mock:
 * ---------------------------
 * - Must not rely on a socket or real client connection
 * - Must handle all packet processing internally or suppress it
 * - Should fake success for login, teleport, ping, keep-alive, etc.
 *
 * ---------------------------
 * How This Differs from Vanilla:
 * ---------------------------
 * - Does not read from or write to a `ClientConnection` over a socket
 * - Ignores or fakes inbound packets
 * - Optionally redirects outbound messages to bot-local systems (e.g., `AnimationHandler`)
 *
 * ---------------------------
 * Dependencies:
 * ---------------------------
 * - Minecraft’s `ServerPlayNetworkHandler`
 * - `MockClientConnection`: Replaces the real network connection
 * - `MockServerPlayerEntity`: The bot this handler manages
 *
 * ---------------------------
 * Dependents:
 * ---------------------------
 * - `ServerPlayerEntity` → `networkHandler` field
 * - Packet broadcasting, teleport logic, statistics, etc.
 * - `BotLifecycleHooks`: Installs this during virtual player initialization
 *
 * ---------------------------
 * Implementation Details:
 * ---------------------------
 * - Override `send(Packet<?>)` to suppress or log instead of transmit
 * - Implement `tick()` to do nothing or route to bot-local systems
 * - Override `onX()` packet handlers (e.g., `onChatMessage`) to skip processing
 */
package com.orebit.mod.mocks;
