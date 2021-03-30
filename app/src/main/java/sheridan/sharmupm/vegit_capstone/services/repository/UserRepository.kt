package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.helpers.toLoggedInUserView
import sheridan.sharmupm.vegit_capstone.helpers.toUserModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.services.cache.PerpetualCache
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface
import sheridan.sharmupm.vegit_capstone.services.roomDao.UserDao
import java.util.concurrent.TimeUnit

class UserRepository(private val api: ApiInterface,
                     private val cache: PerpetualCache,
                     private val userDao: UserDao): BaseRepository() {

    suspend fun fetchUser(id: Int): LoggedInUserView? {
        val cachedUser: Any? = cache.get("user")
        if (cachedUser != null) {
            println("Fetching user from cache")
            return cachedUser as LoggedInUserView
        }

        if (refreshUser(id)) {
            // room user is old and requires refreshing
            println("Fetching fresh user from database")
            val userResponse = safeApiCall(
                call = {api.fetchUser(id).await()},
                errorMessage = "Error fetching user"
            )


            if (userResponse != null) {
                // clear user table for new logged in user
                userDao.deleteUser()
                // save user to room
                userDao.insertUser(userResponse.toUserModel())

                // save in cache
                cache["user"] = userResponse

            }
            return userResponse
        } else {
            println("Fetching user from room")
            // room user is fresh
            return userDao.getUser().toLoggedInUserView()
        }
    }

    private fun refreshUser(id: Int): Boolean {
        val userExists = userDao.hasUserWithId(id, FRESH_TIMEOUT)
        if (userExists < 1) {
            return true
        }
        return false
    }

    companion object {
        val FRESH_TIMEOUT = TimeUnit.DAYS.toMillis(1)
    }
}