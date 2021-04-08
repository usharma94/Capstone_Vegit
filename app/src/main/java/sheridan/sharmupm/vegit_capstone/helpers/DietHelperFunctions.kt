package sheridan.sharmupm.vegit_capstone.helpers

import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient

fun setDietInCache(diet: DietModel) {
    CacheClient.cache["diet"] = diet
}

fun getDietFromCache(): Any? {
    return CacheClient.cache.get("diet")
}

fun removeDietFromCache() {
    CacheClient.cache.remove("diet")
}

// temporary
//fun setDiet(diet: DietModel) {
//    // save in cache
//    setDietInCache(diet)
//    // clear table
//    App.db.dietDao().deleteDiet()
//    // save to room
//    App.db.dietDao().insertDiet(diet)
//}

// temporary
//fun getDiet(): DietModel? {
//    var diet = getDietFromCache()
//    if (diet != null) {
//        return diet as DietModel
//    } else {
//        diet = App.db.dietDao().getDiet()
//        if (diet != null) return diet
//        return null
//    }
//}