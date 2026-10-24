package design.inherent.copycatbars;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.block.model.FaceBakery;
import net.minecraft.client.renderer.block.model.ItemModelGenerator;
import net.minecraft.client.renderer.texture.TextureAtlasSprite;
import net.minecraft.client.resources.model.BlockModelRotation;
import net.minecraft.core.Direction;
import net.neoforged.neoforge.client.ClientHooks;
import org.joml.Vector3f;

public final class BarMesh {
    private record Key(TextureAtlasSprite sprite, Direction facing) {}
    // Chunk meshing can run on several workers. Sprite identity separates reloads;
    // the baking event also releases the old atlas references explicitly.
    private static final Map<Key, List<BakedQuad>> CACHE = new LinkedHashMap<>(32, .75f, true);
    private static final int CACHE_LIMIT = 128;
    private static final int QUAD_LIMIT = 2048;

    private BarMesh() {}

    public static synchronized void clear() { CACHE.clear(); }

    public static synchronized List<BakedQuad> get(TextureAtlasSprite sprite, Direction facing) {
        if (sprite.contents().width() > 64 || sprite.contents().height() > 64) return null;
        Key key = new Key(sprite, facing);
        if (CACHE.containsKey(key)) return CACHE.get(key);
        List<BakedQuad> result = build(sprite, facing);
        CACHE.put(key, result);
        if (CACHE.size() > CACHE_LIMIT) CACHE.remove(CACHE.keySet().iterator().next());
        return result;
    }

    private static List<BakedQuad> build(TextureAtlasSprite sprite, Direction facing) {
        var elements = new ItemModelGenerator().processFrames(-1, "bars", sprite.contents());
        int count = elements.stream().mapToInt(e -> e.faces.size()).sum();
        if (count > QUAD_LIMIT) return null;
        var quads = new ArrayList<BakedQuad>(count);
        var bakery = new FaceBakery();
        for (var element : elements) {
            // Vanilla item extrusion is one pixel deep at z=7.5..8.5.
            // Place it at z=.5..1.5, inside Create's two-pixel panel envelope.
            var from = new Vector3f(element.from).add(0, 0, -7);
            var to = new Vector3f(element.to).add(0, 0, -7);
            for (var face : element.faces.entrySet()) {
                var quad = bakery.bakeQuad(from, to, face.getValue(), sprite, face.getKey(),
                    BlockModelRotation.X0_Y0, null, true);
                int[] vertices = quad.getVertices().clone();
                for (int i = 0; i < 4; i++) {
                    int offset = i * 8;
                    float[] point = PanelTransform.apply(facing.getName(),
                        Float.intBitsToFloat(vertices[offset]), Float.intBitsToFloat(vertices[offset + 1]),
                        Float.intBitsToFloat(vertices[offset + 2]));
                    for (int axis = 0; axis < 3; axis++) vertices[offset + axis] = Float.floatToRawIntBits(point[axis]);
                }
                Direction direction = FaceBakery.calculateFacing(vertices);
                ClientHooks.fillNormal(vertices, direction);
                quads.add(new BakedQuad(vertices, -1, direction, sprite, true, false));
            }
        }
        if (Boolean.getBoolean("copycatbars.debug")) {
            com.mojang.logging.LogUtils.getLogger().info("Copycat extrusion: {} / {} / {} quads", sprite.contents().name(), facing, quads.size());
        }
        return List.copyOf(quads);
    }
}
