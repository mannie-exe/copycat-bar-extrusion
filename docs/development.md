# Development

## Rendering

A client-only HEAD injection replaces `CopycatBarsModel.getCroppedQuads` for the
explicitly supported material families. Deco materials resolve their full bar sheet
from the active atlas instead of the particle sprite, which can contain only a post.
A missing sheet retains native rendering. Create's panel model already routes bar
materials through that helper; other Copycat materials do not enter this patch.

`ItemModelGenerator.processFrames` creates depth around the active sprite's alpha
mask, using Minecraft's handling of animation frames and edge runs. `FaceBakery`
bakes those elements. `PanelTransform` places the mesh in the native panel envelope
and rotates its positions to the six panel facings; normals are recomputed afterward.
Silhouette faces use the unculled pass because the panel does not present a complete
opaque boundary face. This can retain extra faces between neighboring panels.

The synchronized LRU holds at most 128 sprite/facing meshes. Sprite identity isolates
resource reloads, and `ModelEvent.BakingCompleted` clears retained references. High
resolution or over-budget meshes retain native rendering. `-Dcopycatbars.debug=true`
logs generated sprite/facing/quad counts for disposable-client verification only.

## Build and evidence

`./gradlew check build` passes with Java 21 and NeoForge 21.1.263. Two JVM tests check
all six transforms, panel bounds, thickness and invalid facings. `verifyCreate`
checks the exact SHA-512 of Modrinth version `UjX6dr61` (Create 6.0.10).

Native review on 2026-10-24 used Minecraft 1.21.1/NeoForge 21.1.264, Create 6.0.10,
Copycats+ 3.0.9 and Deco 2.1.3. All six facings executed for vanilla iron,
Create andesite/brass/copper and Deco industrial iron/zinc (10–40 quads each).
Shader-off and Complementary Unbound 5.9.3/Euphoria 1.10.5 inspections covered
panel depth, adjacent brass panels and stone/unfilled native fallbacks. A resource
reload through the pack-selection screen rebuilt the meshes without stale sprites.
The first prototype incorrectly extruded Deco's post-only particle; the verified
build selects its complete bar artwork. No moving-contraption or performance claim
is made. Tested JAR SHA-256:
`ba32ee265b9bcf838c9cc177398081759e870aadffdadee8bf41fd207bfb99eb`.

When upgrading Create, inspect the target method and panel delegation before
widening the version range. Keep the Mixin required; silently missing the target
would conceal a stale compatibility patch. No release or distribution is authorized
by a successful local build.
