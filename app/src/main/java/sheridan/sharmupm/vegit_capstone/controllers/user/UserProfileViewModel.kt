package sheridan.sharmupm.vegit_capstone.controllers.user

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import sheridan.sharmupm.vegit_capstone.App
import sheridan.sharmupm.vegit_capstone.helpers.getUserFromCache
import sheridan.sharmupm.vegit_capstone.helpers.removeUserFromCache
import sheridan.sharmupm.vegit_capstone.helpers.toLoggedInUserView
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import kotlin.coroutines.CoroutineContext

class UserProfileViewModel : ViewModel() {

    private val parentJob = Job()

    private val coroutineContext: CoroutineContext
        get() = parentJob + Dispatchers.Default

    private val scope = CoroutineScope(coroutineContext)

    // private val repository : UserRepository = UserRepository(APIClient.apiInterface, CacheClient.cache, App.db.userDao())

    val loggedInUser = MutableLiveData<LoggedInUserView>()

    fun fetchUser() {
        // fetch user from cache
        val cachedUser = getUserFromCache()
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
        removeUserFromCache()
        // clear diet
//        removeDietFromCache()

        // remove from room
        scope.launch {
            App.db.userDao().deleteUser()
        }
    }

    fun cancelRequest() = coroutineContext.cancel()
}