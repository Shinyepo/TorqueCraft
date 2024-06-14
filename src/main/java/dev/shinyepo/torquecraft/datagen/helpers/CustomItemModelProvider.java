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

import static net.minecraft.resources.ResourceLocation.fromNamespaceAndPath;

public abstract class CustomItemModelProvider extends ItemModelProvider {
    public CustomItemModelProvider(PackOutput output, String modid, ExistingFileHelper existingFileHelper) {
        super(output, modid, existingFileHelper);
    }

    public ItemModelBuilder basicComponentItem(Item item) {
        return basicItem(key(item), "components/");
    }


    public ItemModelBuilder basicMaterialItem(Item item) {
        return basicMaterialItem(key(item));
    }

    private ResourceLocation key(Item item) {
        return Objects.requireNonNull(BuiltInRegistries.ITEM.getKey(item));
    }

    private ResourceLocation key(Block item) {
        return Objects.requireNonNull(BuiltInRegistries.BLOCK.getKey(item));
    }

    public ItemModelBuilder basicBlockMaterialItem(Item item) {
        return basicBlockMaterialItem(key(item));
    }

    public ItemModelBuilder basicItem(ResourceLocation item, String folder) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", fromNamespaceAndPath(item.getNamespace(), "item/" + folder + item.getPath()));
    }

    public ItemModelBuilder basicMaterialItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", fromNamespaceAndPath(item.getNamespace(), "item/materials/" + item.getPath()));
    }

    public ItemModelBuilder basicBlockMaterialItem(ResourceLocation item) {
        return getBuilder(item.toString())
                .parent(new ModelFile.UncheckedModelFile("item/generated"))
                .texture("layer0", fromNamespaceAndPath(item.getNamespace(), "block/materials/" + item.getPath()));
    }

    public ItemModelBuilder withExistingParent(BlockItem item, String folder, Block block) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + folder + key(block).getPath();
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(BlockItem item, String folder, Block block, String itemModel) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + folder + key(block).getPath() + itemModel;
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(BlockItem item, Block block, String itemModel) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + key(block).getPath() + itemModel;
        return super.withExistingParent(name, parent);
    }

    public ItemModelBuilder withExistingParent(BlockItem item, Block block) {
        var name = String.valueOf(item).toLowerCase();
        var parent = "torquecraft:block/" + key(block).getPath();
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
