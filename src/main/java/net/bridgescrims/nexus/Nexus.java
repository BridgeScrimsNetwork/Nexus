package net.bridgescrims.nexus;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;
import io.lettuce.core.api.sync.RedisCommands;
import io.lettuce.core.pubsub.StatefulRedisPubSubConnection;
import io.lettuce.core.pubsub.api.async.RedisPubSubAsyncCommands;
import net.bridgescrims.nexus.packet.PacketSerialiser;
import net.bridgescrims.nexus.redis.RecvListener;
import net.bridgescrims.nexus.stand.Stand;
import net.bridgescrims.nexus.stand.StandManager;
import net.bridgescrims.nexus.packet.wrapper.*;
import net.bridgescrims.nexus.utils.PacketUtils;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.time.Duration;
import java.util.HashMap;
import java.util.UUID;

public class Nexus extends JavaPlugin implements Listener {
    public static Nexus INSTANCE;
    private StandManager standManager;
    private HashMap<Integer, UUID> ids;

    public RedisClient redisClient;
    public StatefulRedisConnection<String, String> redisConnection;
    public RedisCommands<String, String> commands;

    @Override
    public void onEnable() {
        saveDefaultConfig();
        INSTANCE = this;
        standManager = new StandManager();
        ids = new HashMap<>();

        getServer().getPluginManager().registerEvents(this, this);
        createRedis();
        createPacketListeners();
    }

    public void createRedis() {
        redisClient = RedisClient.create("redis://password@localhost:6379/0");

        redisConnection = redisClient.connect();
        commands = redisConnection.sync();

        StatefulRedisPubSubConnection<String, String> subscribeConnection = redisClient.connectPubSub();
        subscribeConnection.addListener(new RecvListener(this));

        RedisPubSubAsyncCommands<String, String> async = subscribeConnection.async();
        async.subscribe(getConfig().getString("recv-name"));
    }

    public void createPacketListeners() {
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.REL_ENTITY_MOVE) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerRelEntityMove packet = new WrapperPlayServerRelEntityMove(event.getPacket());
                UUID uuid = ids.get(packet.getEntityID());
                if (uuid != null) {
                    //standManager.getStand(uuid).relMove(packet.getDx(), packet.getDy(), packet.getDz(), packet.getOnGround());
                    String serialised = PacketSerialiser.REL_ENTITY_MOVE(uuid, packet.getDx(), packet.getDy(), packet.getDz(), packet.getOnGround());
                    sendSerialisedPacket(serialised);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.REL_ENTITY_MOVE_LOOK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerRelEntityMoveLook packet = new WrapperPlayServerRelEntityMoveLook(event.getPacket());
                UUID uuid = ids.get(packet.getEntityID());
                if (uuid != null) {
                    //standManager.getStand(uuid).relLook(packet.getDx(), packet.getDy(), packet.getDz(), (double) packet.getYaw(), (double) packet.getPitch(), packet.getOnGround());
                    String serialised = PacketSerialiser.REL_ENTITY_MOVE_LOOK(uuid, packet.getDx(), packet.getDy(), packet.getDz(), (double) packet.getYaw(), (double) packet.getPitch(), packet.getOnGround());
                    sendSerialisedPacket(serialised);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_LOOK) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerEntityLook packet = new WrapperPlayServerEntityLook(event.getPacket());
                UUID uuid = ids.get(packet.getEntityID());
                if (uuid != null) {
                    //standManager.getStand(uuid).look((double) packet.getYaw(), (double) packet.getPitch(), packet.getOnGround());
                    String serialised = PacketSerialiser.ENTITY_LOOK(uuid, (double) packet.getYaw(), (double) packet.getPitch(), packet.getOnGround());
                    sendSerialisedPacket(serialised);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_HEAD_ROTATION) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerEntityHeadRotation packet = new WrapperPlayServerEntityHeadRotation(event.getPacket());
                UUID uuid = ids.get(packet.getEntityID());
                if (uuid != null) {
                    //standManager.getStand(uuid).headRot((double) packet.getHeadYaw());
                    String serialised = PacketSerialiser.ENTITY_HEAD_ROTATION(uuid, (double) packet.getHeadYaw());
                    sendSerialisedPacket(serialised);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_TELEPORT) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerEntityTeleport packet = new WrapperPlayServerEntityTeleport(event.getPacket());
                UUID uuid = ids.get(packet.getEntityID());
                if (uuid != null) {
                    //standManager.getStand(uuid).teleport((int) packet.getX(), (int) packet.getY(), (int) packet.getZ(), (double) packet.getYaw(), (double) packet.getPitch(), packet.getOnGround());
                    String serialised = PacketSerialiser.ENTITY_TELEPORT(uuid, (int) packet.getX(), (int) packet.getY(), (int) packet.getZ(), (double) packet.getYaw(), (double) packet.getPitch(), packet.getOnGround());
                    sendSerialisedPacket(serialised);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ENTITY_METADATA) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerEntityMetadata packet = new WrapperPlayServerEntityMetadata(event.getPacket());
                UUID uuid = ids.get(packet.getEntityID());
                if (uuid != null) {
                    //standManager.getStand(uuid).teleport((int) packet.getX(), (int) packet.getY(), (int) packet.getZ(), (double) packet.getYaw(), (double) packet.getPitch(), packet.getOnGround());
                    String serialised = PacketSerialiser.ENTITY_METADATA(uuid, (byte) packet.getMetadata().get(0).getValue());
                    System.out.println(serialised);
                    sendSerialisedPacket(serialised);
                }
            }
        });
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(this, ListenerPriority.NORMAL, PacketType.Play.Server.ANIMATION) {
            @Override
            public void onPacketSending(PacketEvent event) {
                WrapperPlayServerAnimation packet = new WrapperPlayServerAnimation(event.getPacket());
                UUID uuid = ids.get(packet.getEntityID());
                if (uuid != null) {
                    String serialised = PacketSerialiser.ANIMATION(uuid, (byte) packet.getAnimation());
                    System.out.println(serialised);
                    sendSerialisedPacket(serialised);
                }
            }
        });
    }

    public void sendSerialisedPacket(String data) {
        //standManager.sendSerialisedPacket(data);
        System.out.println(data);
        commands.publish(getConfig().getString("send-name"), data);
    }

    public StandManager getStandManager() {
        return standManager;
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {
        ids.put(e.getPlayer().getEntityId(), e.getPlayer().getUniqueId());
        sendSerialisedPacket("CREATE" + "|" + e.getPlayer().getUniqueId() + "|" + e.getPlayer().getName() + "|" + (int) e.getPlayer().getLocation().getX() + "|" + (int) e.getPlayer().getLocation().getY() + "|" + (int) e.getPlayer().getLocation().getZ());

        for (Stand stand : getStandManager().stands.values()) {
            PacketUtils.showEntityPlayer(stand.player, e.getPlayer());
        }
        //standManager.createStand(e.getPlayer().getUniqueId(), e.getPlayer().getName(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY(), e.getPlayer().getLocation().getZ());
    }
    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent e) {
        ids.remove(e.getPlayer().getUniqueId());
        //standManager.createStand(e.getPlayer().getUniqueId(), e.getPlayer().getName(), e.getPlayer().getLocation().getX(), e.getPlayer().getLocation().getY(), e.getPlayer().getLocation().getZ());
    }
}
