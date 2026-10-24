# Copycat Bar Extrusion

Adds texture-mask depth to supported bar materials applied to Create's standard
Copycat Panel. It keeps the panel's two-pixel envelope and uses the active material
texture, including resource-pack replacements.

## Installation

For Minecraft 1.21.1, NeoForge 21.1.263+ and Create 6.0.10.
Install `copycat-bar-extrusion-0.1.0.jar` on the client only. No server installation,
new blocks, recipes or saved-data migration is required.

Supported materials are vanilla iron bars and Create/Create Deco blocks ending in
`_bars`. Other materials retain Create's implementation. This does not make arbitrary
Copycats+ shapes reproduce arbitrary block models.

Minecraft's item-model extrusion supplies silhouette edges around the texture.
The result follows the material artwork, not the original block's post/connection
geometry. Textures larger than 64×64 or meshes exceeding 2,048 quads fall back to
Create's native flat rendering. No source textures or upstream JARs are bundled.

## Development

Use Java 21 and `./gradlew check build`. See [development](docs/development.md) for
the hook, cache, verification and update boundaries. Native panel, reload and shader
checks are recorded in the development guide. Download versioned artifacts from
[GitHub releases](https://github.com/mannie-exe/copycat-bar-extrusion/releases).

## License

Original project code is MIT licensed; see [LICENSE](LICENSE). Minecraft and Create
remain subject to their own licenses. This project calls Minecraft's implementation;
it does not redistribute its source.
