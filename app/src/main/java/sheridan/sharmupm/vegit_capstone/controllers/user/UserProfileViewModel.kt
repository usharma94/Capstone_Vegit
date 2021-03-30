package sheridan.sharmupm.vegit_capstone.controllers.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import sheridan.sharmupm.vegit_capstone.App
import sheridan.sharmupm.vegit_capstone.helpers.toLoggedInUserView
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

    private val repository : UserRepository = UserRepository(APIClient.apiInterface, CacheClient.cache, App.db.userDao())

    val loggedInUser = MutableLiveData<LoggedInUserView>()

    fun fetchUser() {
        // fetch user from cache
        val cachedUser = CacheClient.cache.get("user")
        if (cachedUser != null) {
            loggedInUser.postValue(cachedUser as LoggedInUserView)
        } else {
            // fetch user from room
            scope.launch {
                val roomUser = App.db.userDao().getUser()
                if (roomUser != null) {
                    loggedInUser.postValue(roomUser.toLoggedInUserView())
                } else {
                    println("No user logged in from cache or room data")
                }
            }
/*            scope.launch {
                val user = repository.fetchUser(userId)
                loggedInUser.postValue(user)
            }*/
        }
    }

    fun logoutUser() {
        // remove from cache
        CacheClient.cache.remove("user")

        // remove from room
        scope.launch {
            App.db.userDao().deleteUser()
        }
    }

    fun cancelRequest() = coroutineContext.cancel()
}