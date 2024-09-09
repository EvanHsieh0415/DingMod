package io.github.nitiaonstudio.ding;

import io.github.nitiaonstudio.ding.data.blockstate.BlockStateGson;
import io.github.nitiaonstudio.ding.data.ding.provider.BlockStatesProvider;
import io.github.nitiaonstudio.ding.data.ding.provider.DingLanguageProvider;
import io.github.nitiaonstudio.ding.data.ding.provider.ModelProvider;
import io.github.nitiaonstudio.ding.data.lang.Languages;
import io.github.nitiaonstudio.ding.data.ding.provider.TexturesProvider;
import io.github.nitiaonstudio.ding.data.sounds.SoundGeneration;
import io.github.nitiaonstudio.ding.data.tag.BlockTagGeneration;
import io.github.nitiaonstudio.ding.data.tag.ItemTagGeneration;
import io.github.nitiaonstudio.ding.registry.BlockRegistry;
import io.github.nitiaonstudio.ding.registry.SoundRegistry;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.PackOutput;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.common.data.ExistingFileHelper;
import net.neoforged.neoforge.common.data.SoundDefinitionsProvider;
import net.neoforged.neoforge.data.event.GatherDataEvent;

import java.util.concurrent.CompletableFuture;

import static io.github.nitiaonstudio.ding.Ding.MODID;


@EventBusSubscriber(modid = MODID, bus = EventBusSubscriber.Bus.MOD)
public class DingData {
    @SubscribeEvent
    public static void data(GatherDataEvent event) {
        DataGenerator generator = event.getGenerator();
        PackOutput packOutput = generator.getPackOutput();
        CompletableFuture<HolderLookup.Provider> lookupProvider = event.getLookupProvider();
        ExistingFileHelper existingFileHelper = event.getExistingFileHelper();
        generator.addProvider(event.includeClient(), new TexturesProvider(packOutput, MODID));

        generator.addProvider(event.includeClient(), new DingLanguageProvider(packOutput, MODID)
                .languageSelect(Languages.en_us)
                .add(BlockRegistry.forge_anvil_block, "forge anvil block")
                .add(SoundRegistry.ding, "ding~ding")

                .languageSelect(Languages.zh_cn)
                .add(BlockRegistry.forge_anvil_block, "锻造砧"))
                .add(SoundRegistry.ding, "叮~叮")
        ;
        generator.addProvider(event.includeClient(), new ModelProvider(packOutput, MODID)
                .addGeckolibBlockModel(BlockRegistry.forge_anvil_block.get(), 256, 256));
        BlockStateGson.Variant variant = new BlockStateGson.Variant().setModel(Ding.id("block/forge_anvil_block"));
        generator.addProvider(event.includeClient(), new BlockStatesProvider(packOutput, MODID)
                .addBlockStates(BlockRegistry.forge_anvil_block.get(), new BlockStateGson()
                        .add("facing=east", variant.copy().setY(270))
                        .add("facing=south", variant.copy())
                        .add("facing=west", variant.copy().setY(90))
                        .add("facing=north", variant.copy().setY(180))));
        generator.addProvider(event.includeClient(), new SoundGeneration(packOutput, MODID, existingFileHelper));
//        generator.addProvider(event.includeClient(), new EnglishUnitedStatesOfAmerica(packOutput));
//        generator.addProvider(event.includeClient(), new SimpleChinese(packOutput));
        final var blocks = new BlockTagGeneration(packOutput, lookupProvider, existingFileHelper);
        generator.addProvider(event.includeServer(), blocks);
        generator.addProvider(event.includeServer(), new ItemTagGeneration(packOutput, lookupProvider, existingFileHelper, blocks));


    }
}
