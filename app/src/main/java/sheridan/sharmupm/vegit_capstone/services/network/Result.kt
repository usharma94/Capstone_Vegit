package sheridan.sharmupm.vegit_capstone.services.network

sealed class Result<out T: Any> {
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class SuccessEmpty<out T : Any>(val data: T) : Result<Nothing>()
    data class Error(val exception: Exception) : Result<Nothing>()
}