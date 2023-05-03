package net.bridgescrims.nexus.stand;

import com.comphenix.protocol.wrappers.EnumWrappers;
import net.bridgescrims.nexus.Nexus;
import net.minecraft.server.v1_8_R3.DataWatcher;
import org.bukkit.entity.Entity;

import java.util.HashMap;
import java.util.UUID;

public class StandManager {
    public HashMap<UUID, Stand> stands;
    public HashMap<Integer, Stand> standIds;
    public HashMap<Integer, UUID> idUuids;

    public StandManager() {
        stands = new HashMap<>();
        standIds = new HashMap<>();
        idUuids = new HashMap<>();
    }

    public void createStand(UUID uuid, String username, double x, double y, double z) {
        Stand stand = new Stand(uuid, username, x, y, z);
        stands.put(uuid, stand);
        standIds.put(stand.player.getId(), stand);
        idUuids.put(stand.player.getId(), uuid);
    }

    public Stand getStand(UUID uuid) {
        return stands.get(uuid);
    }

    public Stand getStandById(Integer id) {
        return stands.get(id);
    }

    public UUID getUUIDById(Integer id) {
        return idUuids.get(id);
    }

    public void sendSerialisedPacket(String packet) {
        String[] split = packet.split("\\|");
        String type = split[0];
        UUID uuid = UUID.fromString(split[1]);

        double dX, dY, dZ, yaw, pitch;
        int x, y, z;
        boolean onGround;

        switch (type) {
            case "CREATE":
                String username = split[2];
                x = Integer.valueOf(split[3]);
                y = Integer.valueOf(split[4]);
                z = Integer.valueOf(split[5]);

                createStand(uuid, username, x, y, z);
                break;
            case "REL_ENTITY_MOVE":
                dX = Double.parseDouble(split[2]);
                dY = Double.parseDouble(split[3]);
                dZ = Double.parseDouble(split[4]);
                onGround = Boolean.valueOf(split[5]);

                getStand(uuid).relMove(dX, dY, dZ, onGround);
                break;
            case "REL_ENTITY_MOVE_LOOK":
                dX = Double.parseDouble(split[2]);
                dY = Double.parseDouble(split[3]);
                dZ = Double.parseDouble(split[4]);
                yaw = Double.parseDouble(split[5]);
                pitch = Double.parseDouble(split[6]);
                onGround = Boolean.valueOf(split[7]);

                getStand(uuid).relLook(dX, dY, dZ, yaw, pitch, onGround);
                break;
            case "ENTITY_LOOK":
                yaw = Double.parseDouble(split[2]);
                pitch = Double.parseDouble(split[3]);
                onGround = Boolean.valueOf(split[4]);

                getStand(uuid).look(yaw, pitch, onGround);
                break;
            case "ENTITY_HEAD_ROTATION":
                yaw = Double.parseDouble(split[2]);

                getStand(uuid).headRot(yaw);
                break;
            case "ENTITY_TELEPORT":
                x = Integer.valueOf(split[2]);
                y = Integer.valueOf(split[3]);
                z = Integer.valueOf(split[4]);
                yaw = Double.parseDouble(split[5]);
                pitch = Double.parseDouble(split[6]);
                onGround = Boolean.valueOf(split[7]);

                getStand(uuid).teleport(x, y, z, yaw, pitch, onGround);
                break;
            case "ENTITY_METADATA":
                byte metadata = Byte.parseByte(split[2]);

                getStand(uuid).metadata(metadata);
                break;
            case "ANIMATION":
                byte animation = Byte.parseByte(split[2]);

                getStand(uuid).animation(animation);
                break;
            // THIS IS SO FUCKED LOL
//        case "USE_ENTITY":
//            UUID source = UUID.fromString(split[2]);
//            EnumWrappers.EntityUseAction action = EnumWrappers.EntityUseAction.valueOf(split[3]);
//
//            if (action.equals(EnumWrappers.EntityUseAction.ATTACK)) {
//                Nexus.INSTANCE.getServer().getPlayer(uuid).damage(0, Nexus.INSTANCE.getStandManager().getStand(source).player.getBukkitEntity());
//                Nexus.INSTANCE.getServer().getPlayer(uuid).damage(0);
//                Nexus.INSTANCE.getServer().getPlayer(uuid).setVelocity(Nexus.INSTANCE.getStandManager().getStand(source).player.getBukkitEntity().getLocation().getDirection().multiply(2));
//            }
//            break;
//        } /\
        }
    }
}
