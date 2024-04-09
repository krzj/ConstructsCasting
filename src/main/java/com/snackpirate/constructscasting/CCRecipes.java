package com.snackpirate.constructscasting;

import com.snackpirate.constructscasting.fluids.CCFluids;
import com.snackpirate.constructscasting.items.CCItems;
import com.snackpirate.constructscasting.materials.CCMaterials;
import com.snackpirate.constructscasting.modifiers.CCModifiers;
import io.redspace.ironsspellbooks.IronsSpellbooks;
import io.redspace.ironsspellbooks.registries.ItemRegistry;
import net.minecraft.data.DataGenerator;
import net.minecraft.data.recipes.FinishedRecipe;
import net.minecraft.data.recipes.RecipeProvider;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraftforge.client.event.RegisterColorHandlersEvent;
import net.minecraftforge.common.crafting.conditions.IConditionBuilder;
import net.minecraftforge.fluids.FluidStack;
import slimeknights.mantle.fluid.UnplaceableFluid;
import slimeknights.mantle.recipe.data.IRecipeHelper;
import slimeknights.mantle.recipe.ingredient.FluidIngredient;
import slimeknights.mantle.registration.object.FluidObject;
import slimeknights.tconstruct.common.TinkerTags;
import slimeknights.tconstruct.fluids.TinkerFluids;
import slimeknights.tconstruct.library.data.recipe.IMaterialRecipeHelper;
import slimeknights.tconstruct.library.data.recipe.ISmelteryRecipeHelper;
import slimeknights.tconstruct.library.recipe.FluidValues;
import slimeknights.tconstruct.library.recipe.alloying.AlloyRecipeBuilder;
import slimeknights.tconstruct.library.recipe.casting.ItemCastingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.melting.MeltingRecipeBuilder;
import slimeknights.tconstruct.library.recipe.modifiers.adding.ModifierRecipeBuilder;
import slimeknights.tconstruct.library.tools.SlotType;
import slimeknights.tconstruct.smeltery.TinkerSmeltery;

import java.util.function.Consumer;

public class CCRecipes extends RecipeProvider implements IConditionBuilder, IMaterialRecipeHelper, ISmelteryRecipeHelper, IRecipeHelper {
	public CCRecipes(DataGenerator pGenerator) {
		super(pGenerator);
	}

	private static Consumer<FinishedRecipe> consumer;
	private static final String castingFolder = "smeltery/casting/";
	private static final String alloyFolder = "smeltery/alloys/";
	private static final String materialFolder = "tools/materials/";
	private static final String modifierFolder = "tools/modifiers/";

	@Override
	public String getModId() {
		return ConstructsCasting.MOD_ID;
	}

	@Override
	protected void buildCraftingRecipes(Consumer<FinishedRecipe> pConsumer) {
		consumer = pConsumer;
		//thanks materialis

		//arcanium making
		String meltingFolder = "smeltery/melting/metal/";
		MeltingRecipeBuilder.melting(Ingredient.of(ItemRegistry.ARCANE_INGOT.get()),new FluidStack(CCFluids.moltenArcanium.get(), FluidValues.INGOT), 800, 30).save(consumer, ConstructsCasting.id(meltingFolder + "metal/molten_arcanium"));
		AlloyRecipeBuilder.alloy(new FluidStack(CCFluids.moltenArcanium.get(), FluidValues.INGOT), 800).addInput(new FluidStack(CCFluids.arcaneEssence.get(), 4*FluidValues.BOTTLE)).addInput(FluidIngredient.of(CCFluids.CCFluidTags.ARCANIUM_BASE, FluidValues.INGOT)).save(consumer, ConstructsCasting.id(alloyFolder + "molten_arcanium"));
		materialMeltingCasting(consumer, CCMaterials.arcanium, CCFluids.moltenArcanium, FluidValues.INGOT, materialFolder);
		castingWithCast(consumer, CCFluids.moltenArcanium, false, FluidValues.INGOT, TinkerSmeltery.ingotCast, ItemRegistry.ARCANE_INGOT.get(), castingFolder + "arcane_ingot");
		//casting ability
		ModifierRecipeBuilder.modifier(CCModifiers.CASTING).allowCrystal().exactLevel(1).setSlots(SlotType.ABILITY, 1).setTools(TinkerTags.Items.STAFFS)
				.addInput(ItemRegistry.ARCANE_INGOT.get())
				.addInput(ItemRegistry.CINDER_ESSENCE.get())
				.addInput(ItemRegistry.ARCANE_INGOT.get())
				.addInput(ItemTags.create(IronsSpellbooks.id("inscribed_rune")))
				.addInput(ItemTags.create(IronsSpellbooks.id("inscribed_rune")))
				.save(consumer, ConstructsCasting.id(modifierFolder + "ability/casting"));
		//essence making
		MeltingRecipeBuilder.melting(Ingredient.of(ItemRegistry.ARCANE_ESSENCE.get()), new FluidStack(CCFluids.arcaneEssence.get(), 250), 100, 5).save(consumer, ConstructsCasting.id(meltingFolder + "arcane_essence"));
		essenceRecipe(CCFluids.fireEssence,      new FluidStack(TinkerFluids.blazingBlood   .get(),100), "fire_essence"     );
		essenceRecipe(CCFluids.iceEssence,       new FluidStack(TinkerFluids.powderedSnow   .get(),250), "ice_essence"      );
		essenceRecipe(CCFluids.lightningEssence, new FluidStack(CCFluids.liquidLightning    .get(),250), "lightning_essence");
		essenceRecipe(CCFluids.enderEssence,     new FluidStack(TinkerFluids.moltenEnder    .get(),250), "ender_essence"    );
		essenceRecipe(CCFluids.holyEssence,      new FluidStack(CCFluids.liquidDivinity     .get(),250), "holy_essence"     );
		essenceRecipe(CCFluids.bloodEssence,     new FluidStack(TinkerFluids.meatSoup       .get(),250), "blood_essence"    );
		essenceRecipe(CCFluids.evocationEssence, new FluidStack(TinkerFluids.moltenEmerald  .get(),100), "evocation_essence");
		essenceRecipe(CCFluids.natureEssence,    new FluidStack(CCFluids.poisonousPotatoStew.get(),250), "nature_essence"   );
		//rune casting
		runeCastingRecipe(CCFluids.fireEssence,      ItemRegistry.FIRE_RUNE.get(),           "fire_rune");
		runeCastingRecipe(CCFluids.iceEssence,       ItemRegistry.ICE_RUNE.get(),             "ice_rune");
		runeCastingRecipe(CCFluids.lightningEssence, ItemRegistry.LIGHTNING_RUNE.get(), "lightning_rune");
		runeCastingRecipe(CCFluids.enderEssence,     ItemRegistry.ENDER_RUNE.get(),         "ender_rune");
		runeCastingRecipe(CCFluids.holyEssence,      ItemRegistry.HOLY_RUNE.get(),           "holy_rune");
		runeCastingRecipe(CCFluids.bloodEssence,     ItemRegistry.BLOOD_RUNE.get(),         "blood_rune");
		runeCastingRecipe(CCFluids.evocationEssence, ItemRegistry.EVOCATION_RUNE.get(), "evocation_rune");
		runeCastingRecipe(CCFluids.natureEssence,    ItemRegistry.NATURE_RUNE.get(),       "nature_rune");
		//tater stuff
		MeltingRecipeBuilder.melting(Ingredient.of(Items.POTATO), new FluidStack(CCFluids.potatoStew.get(), 50), 100, 8).save(consumer, ConstructsCasting.id(meltingFolder + "potato_stew_melting"));
		MeltingRecipeBuilder.melting(Ingredient.of(Items.POISONOUS_POTATO), new FluidStack(CCFluids.poisonousPotatoStew.get(), 50), 100, 8).save(consumer, ConstructsCasting.id(meltingFolder + "poisonous_potato_stew_melting"));
		ItemCastingRecipeBuilder.tableRecipe(CCItems.potatoStewBowl.get()).setFluidAndTime(new FluidStack(CCFluids.potatoStew.get(), FluidValues.BOWL)).setCoolingTime(1).setCast(Items.BOWL.asItem(), true).save(consumer, ConstructsCasting.id(meltingFolder + "potato_stew_casting"));
		ItemCastingRecipeBuilder.tableRecipe(CCItems.poisonousPotatoStewBowl.get()).setFluidAndTime(new FluidStack(CCFluids.poisonousPotatoStew.get(), FluidValues.BOWL)).setCoolingTime(1).setCast(Items.BOWL.asItem(), true).save(consumer, ConstructsCasting.id(meltingFolder + "poisonous_potato_stew_casting"));
		AlloyRecipeBuilder.alloy(new FluidStack(CCFluids.poisonousPotatoStew.get(), 50), 100).addInput(CCFluids.potatoStew.get(), 40).addInput(TinkerFluids.venom.get(), 10).save(consumer, ConstructsCasting.id(alloyFolder + "poisonous_potato_stew_alloying"));
		//divinity
		MeltingRecipeBuilder.melting(Ingredient.of(ItemRegistry.DIVINE_PEARL.get()), new FluidStack(CCFluids.liquidDivinity.get(), 250), 700, 5).save(consumer, ConstructsCasting.id(meltingFolder + "divinity"));
		AlloyRecipeBuilder.alloy(new FluidStack(CCFluids.liquidDivinity.get(), 250)).addInput(TinkerFluids.moltenGold.getForgeTag(), FluidValues.INGOT).addInput(TinkerFluids.moltenAmethyst.getForgeTag(), FluidValues.GEM).save(consumer, ConstructsCasting.id(alloyFolder + "divinity"));
	}
	public static void runeCastingRecipe(FluidObject<UnplaceableFluid> essence, Item result, String recipeId) {
		 ItemCastingRecipeBuilder.tableRecipe(result).setCast(ItemRegistry.BLANK_RUNE.get(), true).setFluidAndTime(new FluidStack(essence.get(), 1000)).save(consumer, ConstructsCasting.id(castingFolder + recipeId));
	}
	public static void essenceRecipe(FluidObject<?> essence, FluidStack alloyIngredient, String recipeId) {
		AlloyRecipeBuilder.alloy(new FluidStack(essence.get(), 250), 700).addInput(CCFluids.arcaneEssence.get(), 250).addInput(alloyIngredient).save(consumer, ConstructsCasting.id(alloyFolder + recipeId));
	}
}
