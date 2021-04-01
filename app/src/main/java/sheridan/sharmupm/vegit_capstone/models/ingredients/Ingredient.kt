package sheridan.sharmupm.vegit_capstone.models.ingredients

data class Ingredient(
        var id:Int ?= 0,
        var name:String ?= "",
        var diet_type:Int ?= 0,
        var diet_name:String ?= "",
        var diet_level:Int ?= 0,
        var description:String ?= "",
        var data_source:Int ?= 0
)
