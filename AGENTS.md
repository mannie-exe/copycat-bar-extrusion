# Copycat Bar Extrusion

Keep the patch limited to Create's standard Copycat Panel bar-material renderer.
Preserve material choice, the two-pixel panel envelope and saved data. Use Java 21
and pinned Minecraft, NeoForge and Create inputs. Keep game-independent transforms
separate from Mixin/render adapters. Do not bundle upstream code, assets or JARs.

Run `./gradlew check build` after code changes. Geometry checks do not establish
Mixin application or visual compatibility: verify materials, six facings, adjacency,
resource reload and shader-off/on rendering in the pack's disposable client.

Distinguish texture-mask extrusion, authored block models and UV fitting before
choosing a fix. Copycat material rendering is a runtime path; pack overrides alone
do not establish that it uses an ordinary block model. Preserve bounded mesh cost,
fallback behavior and reload-aware caches. Static models and artwork belong to
their resource-pack authoring pipeline, not this renderer patch.

README owns installation and scope; docs own development and evidence. Keep writing
direct. Do not commit, publish, release or modify playing worlds without authorization.
Use conventional commits with relevant `- [tag]: message` body bullets. Keep each
message at most 40 characters, lowercase except names, without a final period.
Scale detail to the change rather than padding the message.
