package sheridan.sharmupm.vegit_capstone.models.login

data class LoggedInUserView(
    var id:Int ?= -1,
    val email: String,
    val firstName: String,
    val lastName: String
    //... other data fields that may be accessible to the UI
)
