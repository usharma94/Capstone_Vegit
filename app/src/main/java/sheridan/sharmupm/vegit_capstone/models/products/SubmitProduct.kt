package sheridan.sharmupm.vegit_capstone.models.products

import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName

data class SubmitProduct(
        var name:String ?= "",
        var diet_type:Int ?= 0,
        var category:String ?= "",
        var img_url:String ?= "",
        var created_date:String ?= "",
        var updated_date:String ?= "",
        var ingredientNames:List<IngredientName>
)
