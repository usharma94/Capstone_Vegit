package sheridan.sharmupm.vegit_capstone.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sheridan.sharmupm.vegit_capstone.App
import sheridan.sharmupm.vegit_capstone.models.UserModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient
import java.util.concurrent.TimeUnit

// time to keep user logged in for
val LOGIN_TIMEOUT = TimeUnit.DAYS.toMillis(7)

fun isUserCached(): Boolean {
    if (CacheClient.cache.get("user") != null) return true
    return false
}

suspend fun isUserInRoom() = withContext(Dispatchers.IO) {
    App.db.userDao().hasUser(LOGIN_TIMEOUT)
}

fun LoggedInUserView.toUserModel() = UserModel(
    id = id,
    email = "$email",
    firstName = "$firstName",
    lastName = "$lastName"
)

fun UserModel.toLoggedInUserView() = LoggedInUserView(
    id = id,
    email = "$email",
    firstName = "$firstName",
    lastName = "$lastName"
)