package sheridan.sharmupm.vegit_capstone.controllers.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient
import sheridan.sharmupm.vegit_capstone.services.network.APIClient
import sheridan.sharmupm.vegit_capstone.services.repository.UserRepository
import kotlin.coroutines.CoroutineContext

class UserProfileViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    private val repository : UserRepository = UserRepository(APIClient.apiInterface, CacheClient.cache)

    val loggedInUser = MutableLiveData<LoggedInUserView>()

    fun fetchUser() {
        // fetch user from cache
        var cachedUser = CacheClient.cache.get("user")
        var userId = -1

        if (cachedUser != null) {
            cachedUser = cachedUser as LoggedInUserView
            userId = cachedUser.id!!
        } else {
            // fetch user from room
            userId = -1
        }

        if (userId < 0) {
            // no user logged in, should not be able to view user profile page
            // ether disable user profile page if no logged in user or logout user here?
            println("No user logged in from cache or room data")
            return
        }

        scope.launch {
            val user = repository.fetchUser(userId)
            loggedInUser.postValue(user)
        }
    }

    fun logoutUser() {
        CacheClient.cache.remove("user")
    }

    fun cancelRequest() = coroutineContext.cancel()
}