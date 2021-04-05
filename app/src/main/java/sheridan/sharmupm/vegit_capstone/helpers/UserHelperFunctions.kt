package sheridan.sharmupm.vegit_capstone.helpers

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import sheridan.sharmupm.vegit_capstone.App
import sheridan.sharmupm.vegit_capstone.models.UserModel
import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.models.login.LoggedInUserView
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient
import java.util.concurrent.TimeUnit

// time to keep user logged in for
val LOGIN_TIMEOUT = TimeUnit.DAYS.toMillis(7)

// cache
val cache = CacheClient.cache

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

fun setSearchIngredientList(names: List<IngredientName>) {
    cache["searchList"] = names
}

fun getSearchIngredientList(): Any? {
    val list = cache.get("searchList")
    if (list != null) {
        return list as List<IngredientName>
    }
    return null
}

fun clearSearchIngredientList() {
    cache.remove("searchList")
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