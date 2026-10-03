package org.carpentry.citrine.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

public interface ColoredStackItem{

    int getHexColor(ItemStack stack);

default Text getColoredName(ItemStack stack) {
    MutableText name = Text.translatable(stack.getItem().getTranslationKey(stack));

    int hexColor = getHexColor(stack);
    int r = (hexColor >> 16) & 0xFF;
    int g = (hexColor >> 8) & 0xFF;
    int b = hexColor & 0xFF;

    return name.setStyle(name.getStyle().withColor((r << 16) | (g << 8) | b));
    }
}
