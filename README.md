# Citrine
An all-in-one library for items, rendering, and other general utilities.

## Setup

Add the maven repository of Modrinth under your `build.gradle` repositories: 
```
maven {
        url = "https://api.modrinth.com/maven"
    }
```
Then, add the dependency:

`modImplementation "maven.modrinth:citrine:${project.citrine_ver}"`

## Utilities Provided

- Screen Shake
- Screen Flash
- Stack-based ItemCooldownManager
- Unclearable Status Effects
- Multi-model Item system
- Item Skin system
- Custom kill source items
- On-Crit effect items
- On-kill effect items
- User death event items
- Shield-breaking items
- Use-while-sprinting items
- Custom held pose items
- Custom name color items
- Simple Cube and Quad renderers
- Supporter System
- Tick Schedulers for client and server side
- Simple Custom particle utilities
- Simple time conversion utilities

## License

This library is licensed under the MIT license.
