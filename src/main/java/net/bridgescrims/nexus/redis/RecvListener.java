package net.bridgescrims.nexus.redis;

import net.bridgescrims.nexus.Nexus;
import redis.clients.jedis.JedisPubSub;

public class RecvListener extends JedisPubSub {
    private Nexus plugin;

    public RecvListener(Nexus plugin) {
        this.plugin = plugin;
    }

    @Override
    public void onMessage(String channel, String message) {
        plugin.getStandManager().sendSerialisedPacket(message);
    }
}

