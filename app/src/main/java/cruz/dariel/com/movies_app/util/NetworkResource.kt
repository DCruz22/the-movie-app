package cruz.dariel.com.movies_app.util

enum class Status {
    SUCCESS,
    ERROR,
    LOADING
}

data class NetworkResource<out T>(val status: Status, val data: T?, val message: String?) {
    companion object {
        fun <T> success(data: T): NetworkResource<T> =
            NetworkResource(status = Status.SUCCESS, data = data, message = null)

        fun <T> error(data: T?, message: String?): NetworkResource<T> =
            NetworkResource(status = Status.ERROR, data = data, message = message)

        fun <T> loading(data: T?): NetworkResource<T> =
            NetworkResource(status = Status.LOADING, data = data, message = null)
    }
}