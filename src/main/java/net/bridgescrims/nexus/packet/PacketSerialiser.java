package net.bridgescrims.nexus.packet;

import java.util.UUID;

public class PacketSerialiser {
    public static String REL_ENTITY_MOVE(UUID uuid, double dX, double dY, double dZ, boolean onGround) {
        return "REL_ENTITY_MOVE" + "|" + uuid.toString() + "|" + dX + "|" + dY + "|" + dZ + "|" + onGround;
    }
    public static String REL_ENTITY_MOVE_LOOK(UUID uuid, double dX, double dY, double dZ, double yaw, double pitch, boolean onGround) {
        return "REL_ENTITY_MOVE_LOOK" + "|" + uuid.toString() + "|" + dX + "|" + dY + "|" + dZ + "|" + yaw + "|" + pitch + "|" + onGround;
    }
    public static String ENTITY_LOOK(UUID uuid, double yaw, double pitch, boolean onGround) {
        return "ENTITY_LOOK" + "|" + uuid.toString() + "|" + yaw + "|" + pitch + "|" + onGround;
    }
    public static String ENTITY_HEAD_ROTATION(UUID uuid, double yaw) {
        return "ENTITY_HEAD_ROTATION" + "|" + uuid.toString() + "|" + yaw;
    }
    public static String ENTITY_TELEPORT(UUID uuid, int x, int y, int z, double yaw, double pitch, boolean onGround) {
        return "ENTITY_TELEPORT" + "|" + uuid.toString() + "|" + x + "|" + y + "|" + z + "|" + yaw + "|" + pitch + "|" + onGround;
    }
}
