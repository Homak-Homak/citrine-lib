package org.carpentry.citrine.api.item;

import net.minecraft.item.ItemStack;
import net.minecraft.text.MutableText;
import net.minecraft.text.Text;

import java.awt.*;

public interface ColoredStackItem{

    Color getColor(ItemStack stack);

default Text getColoredName(ItemStack stack) {
    MutableText name = Text.translatable(stack.getItem().getTranslationKey(stack));
    Color col = getColor(stack);

    return name.setStyle(name.getStyle().withColor(col.getRGB()));
    }
}
