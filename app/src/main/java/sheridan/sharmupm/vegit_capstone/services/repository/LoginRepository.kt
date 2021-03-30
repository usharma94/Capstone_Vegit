package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel
import sheridan.sharmupm.vegit_capstone.services.cache.PerpetualCache
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface

class LoginRepository(private val api: ApiInterface, private val cache: PerpetualCache) : BaseRepository() {

    suspend fun loginUser(loginModel: LoginModel): LoggedInUserView? {
        val loginResponse = safeApiCall(
            call = {api.loginUserAsync(loginModel).await()},
            errorMessage = "Invalid email or password"
        )

        if (loginResponse != null) {
            cache["user"] = loginResponse
        }

        return loginResponse
    }
}