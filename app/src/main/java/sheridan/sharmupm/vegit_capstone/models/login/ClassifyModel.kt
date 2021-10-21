package sheridan.sharmupm.vegit_capstone.models.login

import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName

data class ClassifyModel (
    var itemName:String ?= "",
    var category:String ?= "",
    var img_url:String ?= "",
    var searchList:List<IngredientName> ?= null
)