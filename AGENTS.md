# Copycat Bar Extrusion

Keep the patch limited to Create's standard Copycat Panel bar-material renderer.
Preserve material choice, the two-pixel panel envelope and saved data. Use Java 21
and pinned Minecraft, NeoForge and Create inputs. Keep game-independent transforms
separate from Mixin/render adapters. Do not bundle upstream code, assets or JARs.

Run `./gradlew check build` after code changes. Geometry checks do not establish
Mixin application or visual compatibility: verify materials, six facings, adjacency,
resource reload and shader-off/on rendering in the pack's disposable client.

README owns installation and scope; docs own development and evidence. Keep writing
direct. Do not commit, publish, release or modify playing worlds without authorization.
Use conventional commits and short tagged body bullets, following Create Sky.
