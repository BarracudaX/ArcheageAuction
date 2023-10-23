package com.arslan.web


fun DataPackDTO.toPackDTO(prices: Map<String,List<ItemPriceDTO>>) : List<PackDTO> =
    try {
        prices[name]!!.map { itemPrice ->
            try{
                PackDTO(name,creationLocation,itemPrice.destinationLocation!!,itemPrice.price,recipes.map{ recipe -> RecipeDTO(recipe.quantity,recipe.materials.map { material -> CraftingMaterialDTO(material.quantity,material.material,prices[material.material.name]?.get(0)?.price)}.toSet(),recipe.id) }.toSet())
            }catch (ex: NullPointerException){
                throw ex
            }
        }
    }catch (ex: NullPointerException){
        throw ex
    }