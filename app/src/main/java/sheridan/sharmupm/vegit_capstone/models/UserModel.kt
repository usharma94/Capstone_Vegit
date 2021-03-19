package sheridan.sharmupm.vegit_capstone.models

data class UserModel (
    var id:Int ?= 0,
    var email:String ?= "",
    var password:String ?= "",
    var firstName:String ?= "",
    var lastName:String ?= "",
)