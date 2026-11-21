package org.carpentry.citrine.dev;

import net.minecraft.entity.effect.StatusEffect;
import net.minecraft.entity.effect.StatusEffectCategory;
import net.minecraft.item.Item;
import org.carpentry.citrine.Citrine;
import org.carpentry.citrine.api.registration.ItemRegistrant;
import org.carpentry.citrine.api.registration.StatusEffectRegistrant;
import org.carpentry.citrine.dev.item.ManyModelsItem;

public class RegistrantTest {
    static ItemRegistrant ITEMS = new ItemRegistrant(Citrine.MOD_ID);
    static StatusEffectRegistrant EFS = new StatusEffectRegistrant(Citrine.MOD_ID);

    public static final Item TEST = ITEMS.register("regtest", ManyModelsItem::new, new Item.Settings());

    public static final StatusEffect EFF = EFS.register("regtest", new UnclearableEffect(StatusEffectCategory.BENEFICIAL, 0xffffff));

    public static void init(){
    }
}
