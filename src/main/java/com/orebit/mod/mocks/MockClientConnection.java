/**
 * MockClientConnection.java
 *
 * Main Component: mocks/
 * Environment: MAIN (server-side only)
 *
 * This class provides a mock implementation of Minecraft's `ClientConnection`,
 * which normally represents the low-level network connection between a client
 * and the server. For virtual players (bots), no actual socket exists—yet
 * many core server systems rely on the presence of a `ClientConnection`
 * for sending packets, maintaining state, and completing the login process.
 * This mock replaces the real connection to satisfy those dependencies
 * while routing communication internally or discarding it entirely.
 *
 * ---------------------------
 * What This File Does:
 * ---------------------------
 * - Stands in for a real `ClientConnection` for bots that do not have a physical client
 * - Allows the bot to be logged in and treated as a full player entity
 * - Suppresses actual networking while remaining structurally compatible with Minecraft’s systems
 * - Provides safe no-ops for all send, close, and error-handling methods
 *
 * ---------------------------
 * Why This Is Necessary:
 * ---------------------------
 * - Minecraft’s server stack expects every `ServerPlayerEntity` to have a `ClientConnection`
 * - Systems like entity tracking, chunk syncing, keep-alives, and chat rely on this
 * - Without it, the server throws exceptions or skips critical logic
 *
 * ---------------------------
 * Expectations of the Mock:
 * ---------------------------
 * - Must appear structurally valid to the server and associated systems
 * - Must override all outgoing packet logic to avoid I/O
 * - Must never timeout, disconnect, or throw I/O exceptions
 * - Should optionally allow internal message routing for debug or replay purposes
 *
 * ---------------------------
 * How This Differs from Vanilla:
 * ---------------------------
 * - Does not open a socket or bind to a real network stream
 * - Does not queue or transmit packets
 * - Overrides lifecycle and error methods to be inert
 *
 * ---------------------------
 * Dependencies:
 * ---------------------------
 * - Minecraft’s `ClientConnection`: this class mimics or extends its API
 * - `MockClientPlayerSession`: May reference this connection as part of the login process
 * - `VirtualPlayerController`: Injects this into the server when spawning the bot
 *
 * ---------------------------
 * Dependents:
 * ---------------------------
 * - `ServerPlayNetworkHandler`: Relies on this connection for player management
 * - Core server systems: Packet broadcasting, player sync, dimension transitions, etc.
 * - `ReplayRecorder`, `Debug tools`: May optionally intercept or inspect packets via hooks
 *
 * ---------------------------
 * Implementation Details:
 * ---------------------------
 * - All `send(Packet<?> packet)` methods should be overridden as no-ops or local dispatches
 * - Any `disconnect()` or `close()` methods should do nothing
 * - Should claim to be “connected” and pass all liveness checks
 * - May track or replay packets internally without external transmission
 */
package com.orebit.mod.mocks;
