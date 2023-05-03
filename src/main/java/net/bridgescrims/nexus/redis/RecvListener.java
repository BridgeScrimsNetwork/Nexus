package net.bridgescrims.nexus.redis;

import io.lettuce.core.pubsub.RedisPubSubAdapter;
import net.bridgescrims.nexus.Nexus;

public class RecvListener extends RedisPubSubAdapter<String, String> {
    private Nexus plugin;

    public RecvListener(Nexus plugin) {
        this.plugin = plugin;
    }

    @Override
    public void message(String channel, String message) {
        plugin.getStandManager().sendSerialisedPacket(message);
    }
}

