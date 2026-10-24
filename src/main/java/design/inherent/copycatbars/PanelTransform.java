package design.inherent.copycatbars;

/** The six native copycat panel orientations, in normalized block coordinates. */
public final class PanelTransform {
    private PanelTransform() {}

    public static float[] apply(String facing, float x, float y, float z) {
        return switch (facing) {
            case "south" -> new float[]{x, y, z};
            case "north" -> new float[]{1 - x, y, 1 - z};
            case "east" -> new float[]{1 - z, y, x};
            case "west" -> new float[]{z, y, 1 - x};
            case "up" -> new float[]{x, z, 1 - y};
            case "down" -> new float[]{x, 1 - z, y};
            default -> throw new IllegalArgumentException("Unknown panel facing: " + facing);
        };
    }
}
