package sheridan.sharmupm.vegit_capstone.services.repository

import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.services.cache.PerpetualCache
import sheridan.sharmupm.vegit_capstone.services.network.ApiInterface

class UserRepository(private val api: ApiInterface, private val cache: PerpetualCache): BaseRepository() {

    suspend fun fetchUser(id: Int): LoggedInUserView? {
        val cachedUser: Any? = cache.get("user")
        if (cachedUser != null) {
            return cachedUser as LoggedInUserView?
        }

        val userResponse = safeApiCall(
                call = {api.fetchUser(id).await()},
                errorMessage = "Error fetching user"
        )

        if (userResponse != null) {
            cache["user"] = userResponse
        }

        return userResponse
    }
}