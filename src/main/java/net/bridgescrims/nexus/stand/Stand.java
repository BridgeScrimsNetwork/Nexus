package net.bridgescrims.nexus.stand;

import com.mojang.authlib.GameProfile;
import net.bridgescrims.nexus.Nexus;
import net.bridgescrims.nexus.utils.PacketUtils;
import net.minecraft.server.v1_8_R3.*;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.craftbukkit.v1_8_R3.CraftServer;
import org.bukkit.craftbukkit.v1_8_R3.CraftWorld;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.util.UUID;

public class Stand {
    private UUID uuid;
    private MinecraftServer nmsServer;
    private WorldServer nmsWorld;
    private GameProfile gameProfile;
    private EntityPlayer player;

    public Stand(UUID uuid, String username, double x, double y, double z) {
        nmsServer = ((CraftServer) Bukkit.getServer()).getServer();;
        nmsWorld = ((CraftWorld)Bukkit.getWorld("world")).getHandle();;
        gameProfile = new GameProfile(uuid, username);
        player = new EntityPlayer(nmsServer, nmsWorld, gameProfile, new PlayerInteractManager(nmsWorld));
        player.setLocation(x, y, z, 0f, 0f);

        for (Player onlinePlayer: Nexus.INSTANCE.getServer().getOnlinePlayers()) {
            PacketUtils.showEntityPlayer(player, onlinePlayer);
        }
    }

    public void relMove(Double x, Double y, Double z, boolean onGround) {
        PacketUtils.pushPacketGlobally(
                    new PacketPlayOutEntity.PacketPlayOutRelEntityMove(player.getId(), x.byteValue(), y.byteValue(), z.byteValue(), onGround)
        );
    }

    public void headRot(Double yaw) {
        PacketUtils.pushPacketGlobally(
                new PacketPlayOutEntityHeadRotation(player, yaw.byteValue())
        );
    }

    public void look(Double yaw, Double pitch, boolean onGround) {
        PacketUtils.pushPacketGlobally(
                new PacketPlayOutEntity.PacketPlayOutEntityLook(player.getId(), yaw.byteValue(), pitch.byteValue(), onGround)
        );
    }

    public void relLook(Double x, Double y, Double z, Double yaw, Double pitch, boolean onGround) {
        PacketUtils.pushPacketGlobally(
                new PacketPlayOutEntity.PacketPlayOutRelEntityMoveLook(player.getId(), x.byteValue(), y.byteValue(), z.byteValue(), yaw.byteValue(), pitch.byteValue(), onGround)
        );
    }

    public void teleport(int x, int y, int z, Double yaw, Double pitch, boolean onGround) {
        PacketUtils.pushPacketGlobally(
                new PacketPlayOutEntityTeleport(player.getId(), x, y, z, yaw.byteValue(), pitch.byteValue(), onGround)
        );
    }
}
