package sheridan.sharmupm.vegit_capstone.helpers

import sheridan.sharmupm.vegit_capstone.models.DietModel
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient

enum class DietTypes(val value: Int) {
    VEGAN(1),
    VEGAN_CAUTION(2),
    VEGETARIAN(3),
    VEGETARIAN_CAUTION(4),
    UNSPECIFIED(5),
    NON_VEGETARIAN(6)
}

fun setDietInCache(diet: DietModel) {
    CacheClient.cache["diet"] = diet
}

fun getDietFromCache(): DietModel? {
    val diet = CacheClient.cache.get("diet")
    if (diet != null)
        return diet as DietModel
    return null
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