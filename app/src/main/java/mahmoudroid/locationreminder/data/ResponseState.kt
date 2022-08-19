package mahmoudroid.locationreminder.data

sealed class ResponseState<out T>(
    val data: T? = null,
){
    class Success<T>(data: T?= null): ResponseState<T>(data)
    class Error<T>(): ResponseState<T>()
    object Loading: ResponseState<Nothing>()
}
