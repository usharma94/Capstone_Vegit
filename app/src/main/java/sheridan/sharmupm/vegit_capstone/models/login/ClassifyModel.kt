package sheridan.sharmupm.vegit_capstone.models.login

import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName

data class ClassifyModel (
    var itemName:String ?= "",
    var searchList:List<IngredientName> ?= null
)