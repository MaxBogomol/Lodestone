package team.lodestar.lodestone.systems.item;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ai.attributes.Attribute;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.item.ArmorItem;
import net.minecraft.world.item.ArmorMaterial;
import net.minecraft.world.item.ItemStack;

public abstract class LodestoneArmorItem extends ArmorItem {
    private Multimap<Attribute, AttributeModifier> attributes;

    public LodestoneArmorItem(ArmorMaterial materialIn, ArmorItem.Type type, Properties builder) {
        super(materialIn, type, builder);
    }

    public ImmutableMultimap.Builder<Attribute, AttributeModifier> createExtraAttributes(EquipmentSlot slot) {
        return new ImmutableMultimap.Builder<>();
    }

    @Override
    public Multimap<Attribute, AttributeModifier> getDefaultAttributeModifiers(EquipmentSlot equipmentSlot) {
        if (attributes == null) {
            ImmutableMultimap.Builder<Attribute, AttributeModifier> attributeBuilder = new ImmutableMultimap.Builder<>();
            attributeBuilder.putAll(defaultModifiers);
            attributeBuilder.putAll(createExtraAttributes(type.getSlot()).build());
            attributes = attributeBuilder.build();
        }
        return equipmentSlot == this.type.getSlot() ? this.attributes : ImmutableMultimap.of();
    }

    public String getTexture() {
        return null;
    }

    public String getTextureLocation() {
        return null;
    }

    @Override
    public String getArmorTexture(ItemStack stack, Entity entity, EquipmentSlot slot, String type) {
        return getTextureLocation() + getTexture() + ".png";
    }
}