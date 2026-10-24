package design.inherent.copycatbars.mixin;

import java.util.List;
import design.inherent.copycatbars.BarMesh;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.block.model.BakedQuad;
import net.minecraft.client.renderer.texture.MissingTextureAtlasSprite;
import net.minecraft.client.renderer.texture.TextureAtlas;
import net.minecraft.core.Direction;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.RandomSource;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.neoforged.neoforge.client.model.data.ModelData;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(targets = "com.simibubi.create.content.decoration.copycat.CopycatBarsModel", remap = false)
public abstract class CopycatBarsMixin {
    @Inject(method = "getCroppedQuads", at = @At("HEAD"), cancellable = true, require = 1, expect = 1, allow = 1)
    private void copycatBars$extrude(BlockState state, Direction side, RandomSource random,
        BlockState material, ModelData data, RenderType layer, CallbackInfoReturnable<List<BakedQuad>> result) {
        var id = BuiltInRegistries.BLOCK.getKey(material.getBlock());
        if (!(id.toString().equals("minecraft:iron_bars") ||
            (id.getNamespace().equals("create") || id.getNamespace().equals("createdeco")) && id.getPath().endsWith("_bars"))) return;
        if (!state.hasProperty(BlockStateProperties.FACING)) return;
        var sprite = Minecraft.getInstance().getBlockRenderer().getBlockModel(material).getParticleIcon(data);
        // Deco uses its narrow post as the particle sprite for some materials.
        // The matching bar sheet carries the complete panel silhouette.
        if (id.getNamespace().equals("createdeco")) {
            var texture = ResourceLocation.fromNamespaceAndPath("createdeco", "block/palettes/metal_bars/" + id.getPath());
            sprite = Minecraft.getInstance().getTextureAtlas(TextureAtlas.LOCATION_BLOCKS).apply(texture);
            if (sprite.contents().name().equals(MissingTextureAtlasSprite.getLocation())) return;
        }
        var quads = BarMesh.get(sprite, state.getValue(BlockStateProperties.FACING));
        // Unsupported/high-resolution material keeps Create's original implementation.
        if (quads == null) return;
        // Interior silhouette edges must remain visible. No full solid face exists
        // at a block boundary, so these quads belong to the unculled pass.
        result.setReturnValue(side == null ? quads : List.of());
    }
}
