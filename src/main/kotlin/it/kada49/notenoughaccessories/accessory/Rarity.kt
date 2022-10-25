package it.kada49.notenoughaccessories.it.kada49.notenoughaccessories.accessory

enum class Rarity(val magicPower: Int, val colorPrefix: String) {
    COMMON(3, "§f"),
    UNCOMMON(5, "§a"),
    RARE(8, "§9"),
    EPIC(12, "§5"),
    LEGENDARY(16, "§6"),
    MYTHIC(22, "§d"),

    SPECIAL(3, "§c"),
    VERY_SPECIAL(5, "§c");
}