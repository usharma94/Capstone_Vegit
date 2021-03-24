package sheridan.sharmupm.vegit_capstone.models.login

import sheridan.sharmupm.vegit_capstone.models.UserModel

data class LoginResult(
    val success: UserModel,
    val error: String
)
