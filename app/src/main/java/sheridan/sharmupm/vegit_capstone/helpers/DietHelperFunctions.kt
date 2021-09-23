package sheridan.sharmupm.vegit_capstone.helpers

import sheridan.sharmupm.vegit_capstone.App
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

enum class DietSafety() {
    SAFE,
    CAUTION,
    AVOID,
    UNKNOWN
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

fun determineSafety(diet: DietModel?, dietType: Int) : DietSafety {
    if (diet == null) return DietSafety.UNKNOWN

    if (diet.dietType == DietTypes.VEGAN.value) {
        return when {
            dietType == DietTypes.VEGAN.value -> {
                DietSafety.SAFE
            }
            dietType == DietTypes.VEGAN_CAUTION.value -> {
                DietSafety.CAUTION
            }
            dietType == DietTypes.UNSPECIFIED.value -> {
                DietSafety.CAUTION
            }
            else -> {
                DietSafety.AVOID
            }
        }
    }

    if (diet.dietType == DietTypes.VEGETARIAN.value) {
        return when {
            dietType <= DietTypes.VEGETARIAN.value -> {
                DietSafety.SAFE
            }
            dietType == DietTypes.VEGETARIAN_CAUTION.value -> {
                DietSafety.CAUTION
            }
            dietType == DietTypes.UNSPECIFIED.value -> {
                DietSafety.CAUTION
            }
            else -> {
                DietSafety.AVOID
            }
        }
    }

    return DietSafety.UNKNOWN
}

fun setDiet(diet: DietModel) {
    // save in cache
    setDietInCache(diet)
    // clear table
    App.db.dietDao().deleteDiet()
    // save to room
    App.db.dietDao().insertDiet(diet)
}

fun getDiet(): DietModel? {
    var diet = getDietFromCache()
    if (diet != null) {
        return diet
    } else {
        diet = App.db.dietDao().getDiet()
        if (diet != null) {
            setDietInCache(diet)
            return diet
        }
        return null
    }
}