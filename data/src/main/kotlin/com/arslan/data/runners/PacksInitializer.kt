package com.arslan.data.runners

import com.arslan.data.entity.CraftingMaterial
import com.arslan.data.entity.CraftingMaterialID
import com.arslan.data.entity.Pack
import com.arslan.data.entity.Recipe
import com.arslan.data.repository.*
import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.context.annotation.Profile
import org.springframework.context.i18n.LocaleContextHolder
import org.springframework.core.annotation.Order
import org.springframework.stereotype.Component
import java.util.*

@Profile("dev")
@Component
@Order(3)
class PacksInitializer(
    private val packRepository: PackRepository,
    private val craftingMaterialRepository: CraftingMaterialRepository,
    private val locationRepository: LocationRepository,
    private val recipeRepository: RecipeRepository,
    private val itemRepository: ItemRepository
) : ApplicationRunner{
    override fun run(args: ApplicationArguments?) {
        LocaleContextHolder.setLocale(Locale("ru","RU"))

        if(packRepository.count() != 0L) return

        var pack = packRepository.save(Pack(locationRepository.findByName("радужные пески"),"маринованые яйца",""))
        var recipe = recipeRepository.save(Recipe(pack))
        var ingredient = itemRepository.findByName("молотые специи")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("яйцо")
        craftingMaterialRepository.save(CraftingMaterial(10,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("махадеби"),"банановое повидло",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("нашинковонные овощи")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("банан")
        craftingMaterialRepository.save(CraftingMaterial(5,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("плато соколиной охоты"),"набивка для тюфяка",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("лекарственый порошок")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("гусиное перо")
        craftingMaterialRepository.save(CraftingMaterial(1,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("плато соколиной охоты"),"яблочные оладьи",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("измельченные злаки")
        craftingMaterialRepository.save(CraftingMaterial(180,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("яблоко")
        craftingMaterialRepository.save(CraftingMaterial(5,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("тигриный хребет"),"хрустящие лепешки",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("измельченные злаки")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("молоко")
        craftingMaterialRepository.save(CraftingMaterial(5,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("тигриный хребет"),"виноградный джем",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("нашинковонные овощи")
        craftingMaterialRepository.save(CraftingMaterial(180,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("виноград")
        craftingMaterialRepository.save(CraftingMaterial(5,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("махадеби"),"ароматная древесина махадеби",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("свежевыжатый сок")
        craftingMaterialRepository.save(CraftingMaterial(180,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("инжир")
        craftingMaterialRepository.save(CraftingMaterial(5,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("полуостров рассвета"),"пуховик для лица",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("лекарственый порошок")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("шерсть ятты")
        craftingMaterialRepository.save(CraftingMaterial(10,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("полуостров рассвета"),"притирания для кожи",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("лекарственый порошок")
        craftingMaterialRepository.save(CraftingMaterial(180,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("финик")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("поющая земля"),"терпкое вишневое вино",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("измельченные лепестки цветов")
        craftingMaterialRepository.save(CraftingMaterial(180,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("овечья шерсть")
        craftingMaterialRepository.save(CraftingMaterial(10,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("поющая земля"),"ароматические подушечки",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("измельченные лепестки цветов")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("вишня")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("древний лес"),"фруктовое молоко",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("свежевыжатый сок")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("молоко")
        craftingMaterialRepository.save(CraftingMaterial(5,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("древний лес"),"смесь специй для вина",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("молотые специи")
        craftingMaterialRepository.save(CraftingMaterial(180,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("гранат")
        craftingMaterialRepository.save(CraftingMaterial(3,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("инистра"),"апельсиновый джем",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("нашинковонные овощи")
        craftingMaterialRepository.save(CraftingMaterial(300,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("апельсин")
        craftingMaterialRepository.save(CraftingMaterial(5,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("дельфийская звезда")
        craftingMaterialRepository.save(CraftingMaterial(2,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        pack = packRepository.save(Pack(locationRepository.findByName("инистра"),"оливкое масло",""))
        recipe = recipeRepository.save(Recipe(pack))
        ingredient = itemRepository.findByName("молотые специи")
        craftingMaterialRepository.save(CraftingMaterial(180,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))
        ingredient = itemRepository.findByName("оливки")
        craftingMaterialRepository.save(CraftingMaterial(3,ingredient,recipe, CraftingMaterialID(ingredient.id!!,recipe.id!!)))

        LocaleContextHolder.setLocale(Locale.ENGLISH)
    }

}