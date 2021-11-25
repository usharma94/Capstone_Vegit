package sheridan.sharmupm.vegit_capstone.models.groceryList

data class SubmitGrocery(
    var name:String ?= "",
    var due:String ?= "",
    var status:Int ?= 0

)