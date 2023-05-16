package model

import java.net.URL

data class PackageDescriptor(
    val name: String,
    val description: String,
    val links: List<String>,
    val images: List<URL>
)