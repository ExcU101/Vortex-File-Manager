package io.github.excu101.vortex.theme

interface Observable<T> {
    fun update(value: T)
}

inline fun <T> observable(value: T, crossinline listener: (old: T, new: T) -> Unit): Observable<T> {
    return object : Observable<T> {
        private var current: T = value
        override fun update(value: T) {
            listener(current, value)
            current = value
        }
    }
}