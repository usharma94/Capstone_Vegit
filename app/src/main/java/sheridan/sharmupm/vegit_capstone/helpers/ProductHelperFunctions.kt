package sheridan.sharmupm.vegit_capstone.helpers

import sheridan.sharmupm.vegit_capstone.models.products.Product
import sheridan.sharmupm.vegit_capstone.services.cache.CacheClient

fun setApproveProductList(names: List<Product>) {
    CacheClient.cache["approveProductList"] = names
}

fun getApproveProductList(): Any? {
    val list = CacheClient.cache.get("approveProductList")
    if (list != null) {
        return list as List<Product>
    }
    return null
}

fun clearApproveProductList() {
    CacheClient.cache.remove("approveProductList")
}
