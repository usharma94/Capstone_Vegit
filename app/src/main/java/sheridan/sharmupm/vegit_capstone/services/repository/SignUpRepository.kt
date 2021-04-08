package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.helpers.setUserInCache
import sheridan.sharmupm.vegit_capstone.helpers.toUserModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.signup.SignUpModel
import sheridan.sharmupm.vegit_capstone.services.cache.PerpetualCache
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface
import sheridan.sharmupm.vegit_capstone.services.roomDao.UserDao

class SignUpRepository(private val api: ApiInterface,
                       private val cache: PerpetualCache,
                       private val userDao: UserDao) : BaseRepository() {

    suspend fun signUpUser(signUpModel: SignUpModel): LoggedInUserView? {
        val signUpResponse = safeApiCall(
                call = {api.signUpUserAsync(signUpModel).await()},
                errorMessage = "User with that email already exists"
        )

        if (signUpResponse != null) {
            // save in cache
            setUserInCache(signUpResponse)

            // clear user table for new logged in user
            userDao.deleteUser()
            // save user to room
            userDao.insertUser(signUpResponse.toUserModel())
        }

        return signUpResponse
    }
}