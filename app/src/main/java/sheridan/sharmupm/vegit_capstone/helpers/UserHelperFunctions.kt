package sheridan.sharmupm.vegit_capstone.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sheridan.sharmupm.vegit_capstone.App
import sheridan.sharmupm.vegit_capstone.models.UserModel
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient.cache
import java.util.concurrent.TimeUnit

// time to keep user logged in for
val LOGIN_TIMEOUT = TimeUnit.DAYS.toMillis(7)

fun isUserCached(): Boolean {
    if (getUserFromCache() != null) return true
    return false
}

suspend fun isUserInRoom() = withContext(Dispatchers.IO) {
    App.db.userDao().hasUser(LOGIN_TIMEOUT)
}

fun setUserInCache(user: LoggedInUserView) {
    cache["user"] = user
}

fun getUserFromCache(): Any? {
    return cache.get("user")
}

fun removeUserFromCache() {
    cache.remove("user")
}

fun LoggedInUserView.toUserModel() = UserModel(
    id = id,
    email = "$email",
    firstName = "$firstName",
    lastName = "$lastName",
    manufacturer = manufacturer,
    admin = admin
)

fun UserModel.toLoggedInUserView() = LoggedInUserView(
    id = id,
    email = "$email",
    firstName = "$firstName",
    lastName = "$lastName",
    manufacturer = manufacturer,
    admin = admin
)