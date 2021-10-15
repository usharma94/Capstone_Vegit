package sheridan.sharmupm.vegit_capstone.models.products

import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientId

data class SubmitProduct(
        var name:String ?= "",
        var diet_type:Int ?= 0,
        var created_date:String ?= "",
        var updated_date:String ?= "",
        var ingredientIds:List<IngredientId>
)
