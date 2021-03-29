package sheridan.sharmupm.vegit_capstone.services.cache

interface Cache {
    val size: Int
    operator fun get(key: Any): Any?
    operator fun set(key: Any, value: Any)
    fun remove(key: Any): Any?
    fun clear()
}