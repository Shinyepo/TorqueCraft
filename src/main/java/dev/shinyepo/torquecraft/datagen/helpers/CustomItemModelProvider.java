package dev.shinyepo.torquecraft.datagen.helpers;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.neoforged.neoforge.client.model.generators.ItemModelBuilder;
import net.neoforged.neoforge.client.model.generators.ItemModelProvider;
import net.neoforged.neoforge.client.model.generators.ModelFile;
import net.neoforged.neoforge.common.data.ExistingFileHelper;

import java.util.Objects;

public abstract class CustomItemModelProvider extends ItemModelProvider {
    public CustomItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    public ItemModelBuilder basicItem(Item item) {
        return super.basicItem(item);
    }

    public ItemModelBuilder basicMaterialItem(Item item) {
        return basicMaterialItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder basicBlockMaterialItem(Item item) {
        return basicBlockMaterialItem(Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item)));
    }

    public ItemModelBuilder basicMaterialItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "item/materials/" + item.getPath()));
    }

    public ItemModelBuilder basicBlockMaterialItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", new ResourceLocation(item.getNamespace(), "block/materials/" + item.getPath()));
    }

    public ItemModelBuilder withExistingParent(BlockItem item, String folder, Block block) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + folder + BuiltInRegistries.BLOCK.getKey(block).getPath();
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(BlockItem item, String folder, Block block, String itemModel) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + folder + BuiltInRegistries.BLOCK.getKey(block).getPath() + itemModel;
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(BlockItem item, Block block, String itemModel) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + itemModel;
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(BlockItem item, Block block) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + BuiltInRegistries.BLOCK.getKey(block).getPath();
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(BlockItem item, String parent) {
        var name = String.valueOf(item).toLowerCase();
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(Item item, String parent) {
        var name = String.valueOf(item).toLowerCase();
        return super.withExistingParent(name, parent);
    }
}
