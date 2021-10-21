package sheridan.sharmupm.vegit_capstone.models.products

import sheridan.sharmupm.vegit_capstone.models.ingredients.Ingredient

data class Product(
        var id:Int ?= 0,
        var name:String ?= "",
        var diet_type:Int ?= 0,
        var diet_name:String ?= "",
        var diet_level:Int ?= 0,
        var category:String ?= "",
        var img_url:String ?= "",
        var views:Int ?= 0,
        var reason:String ?= "",
        var ingredients:List<Ingredient>
)
