package manager

interface PackageManager {

    fun install(id: String)

    fun remove(id: String)

}