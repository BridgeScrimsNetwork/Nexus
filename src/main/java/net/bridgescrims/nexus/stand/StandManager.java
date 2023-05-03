package net.bridgescrims.nexus.stand;

import net.minecraft.server.v1_8_R3.DataWatcher;

import java.util.HashMap;
import java.util.UUID;

public class StandManager {
    public HashMap<UUID, Stand> stands;

    public StandManager() {
        stands = new HashMap<>();
    }

    public void createStand(UUID uuid, String username, double x, double y, double z) {
        Stand stand = new Stand(uuid, username, x, y, z);
        stands.put(uuid, stand);
    }

    public Stand getStand(UUID uuid) {
        return stands.get(uuid);
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
        }
    }
}
