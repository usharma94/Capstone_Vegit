package sheridan.sharmupm.vegit_capstone.helpers

import sheridan.sharmupm.vegit_capstone.models.ingredients.IngredientName
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient

fun setSearchIngredientList(names: List<IngredientName>) {
    CacheClient.cache["searchList"] = names
}

fun getSearchIngredientList(): Any? {
    val list = CacheClient.cache.get("searchList")
    if (list != null) {
        return list as List<IngredientName>
    }
    return null
}

fun clearSearchIngredientList() {
    CacheClient.cache.remove("searchList")
}
