package design.inherent.copycatbars;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class PanelTransformTest {
    @Test void allFacingsPreserveTheTwoPixelEnvelopeAndDistances() {
        for (String face : new String[]{"south", "north", "east", "west", "up", "down"}) {
            for (float x : new float[]{0, 1}) for (float y : new float[]{0, 1}) for (float z : new float[]{.5f/16, 1.5f/16}) {
                float[] p = PanelTransform.apply(face, x, y, z);
                for (float v : p) assertTrue(v >= 0 && v <= 1);
                int axis = switch (face) { case "up", "down" -> 1; case "east", "west" -> 0; default -> 2; };
                float normal = p[axis];
                if (face.equals("north") || face.equals("east") || face.equals("down")) normal = 1 - normal;
                assertEquals(z, normal, 1e-6);
            }
            float[] a = PanelTransform.apply(face, .2f, .3f, .5f/16);
            float[] b = PanelTransform.apply(face, .2f, .3f, 1.5f/16);
            float distance = 0;
            for (int i=0;i<3;i++) distance += (a[i]-b[i])*(a[i]-b[i]);
            assertEquals(1f/256, distance, 1e-6);
        }
    }
    @Test void unknownFacingFailsClearly() {
        assertThrows(IllegalArgumentException.class, () -> PanelTransform.apply("invalid", 0, 0, 0));
    }
}
