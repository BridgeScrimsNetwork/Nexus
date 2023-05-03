package net.bridgescrims.nexus.utils;

import net.bridgescrims.nexus.Nexus;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

public class PacketUtils {
    public static void showEntityPlayer(EntityPlayer npc, Player player) {  // https://www.spigotmc.org/threads/how-to-create-and-modify-npcs.400753/
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutPlayerInfo(PacketPlayOutPlayerInfo.EnumPlayerInfoAction.ADD_PLAYER, npc)); // "Adds the player data for the client to use when spawning a player" - https://wiki.vg/Protocol#Spawn_Player
        connection.sendPacket(new PacketPlayOutNamedEntitySpawn(npc)); // Spawns the NPC for the player client.
        connection.sendPacket(new PacketPlayOutEntityHeadRotation(npc, (byte) (npc.yaw * 256 / 360))); // Correct head rotation when spawned in player look direction.
    }

    public static void destroyEntityPlayer(EntityPlayer npc, Player player) {  // https://www.spigotmc.org/threads/how-to-create-and-modify-npcs.400753/
        PlayerConnection connection = ((CraftPlayer)player).getHandle().playerConnection;
        connection.sendPacket(new PacketPlayOutEntityDestroy(npc.getId()));
    }

    public static void pushPacketGlobally(Packet packet) {
        for (Player onlinePlayer : Nexus.INSTANCE.getServer().getOnlinePlayers()) {
            ((CraftPlayer) onlinePlayer).getHandle().playerConnection.sendPacket(packet);
        }
    }
}
