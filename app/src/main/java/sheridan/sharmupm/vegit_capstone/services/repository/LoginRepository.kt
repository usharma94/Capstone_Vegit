package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.helpers.setUserInCache
import sheridan.sharmupm.vegit_capstone.helpers.toUserModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoginModel
import sheridan.sharmupm.vegit_capstone.services.cache.PerpetualCache
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface
import sheridan.sharmupm.vegit_capstone.services.roomDao.UserDao

class LoginRepository(private val api: ApiInterface,
                      private val cache: PerpetualCache,
                      private val userDao: UserDao) : BaseRepository() {

    suspend fun loginUser(loginModel: LoginModel): LoggedInUserView? {
        val loginResponse = safeApiCall(
            call = {api.loginUserAsync(loginModel).await()},
            errorMessage = "Invalid email or password"
        )

        if (loginResponse != null) {
            // save in cache
            setUserInCache(loginResponse)

            if (loginModel.rememberMe) {
                // clear user table for new logged in user
                userDao.deleteUser()
                // save user to room
                userDao.insertUser(loginResponse.toUserModel())
            }
        }

        return loginResponse
    }
}