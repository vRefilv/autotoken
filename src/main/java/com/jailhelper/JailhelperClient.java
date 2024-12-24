package com.jailhelper;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.text.Text;

public class JailhelperClient implements ClientModInitializer {

	private long lastTradeTime = 0; // Track the last time the command was sent

	@Override
	public void onInitializeClient() {
		System.out.println("AutoToken Client Mod initialized!");

		// Register a tick event to check player level and send the command
		ClientTickEvents.END_CLIENT_TICK.register(client -> {
			if (client.world != null && client.player != null) {
				ClientPlayerEntity player = client.player;

				// If player experience level is 20 or greater, and it's been at least 1 second since the last command
				long currentTime = System.currentTimeMillis();
				if (player.experienceLevel >= 20 && currentTime - lastTradeTime >= 1000) {
					// Send /tradeexp as a chat message
                    assert MinecraftClient.getInstance().player != null;
                    MinecraftClient.getInstance().player.networkHandler.sendCommand("tradeexp");

					// Send confirmation message to the player
					player.sendMessage(Text.literal("Auto-Token!"), false);

					// Update the lastTradeTime to the current time
					lastTradeTime = currentTime;
				}
			}
		});
	}
}
