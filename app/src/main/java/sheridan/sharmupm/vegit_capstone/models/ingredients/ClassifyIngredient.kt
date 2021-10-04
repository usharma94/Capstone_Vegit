package sheridan.sharmupm.vegit_capstone.models.ingredients

data class ClassifyIngredient(
        var name:String ?= "",
        var diet_name:String ?= "",
        var diet_safety:Int ?= 1,
        var color:String ?= ""
)
