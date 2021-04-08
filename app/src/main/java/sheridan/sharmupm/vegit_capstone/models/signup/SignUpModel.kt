package sheridan.sharmupm.vegit_capstone.models.signup

data class SignUpModel (
        var email:String ?= "",
        var password:String ?= "",
        var firstName:String ?= "",
        var lastName:String ?= ""
)