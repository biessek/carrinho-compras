package rocks.biessek.testemeuspedidos.domain.model

interface Product {
    val name: String
    val description: String
    val photo: String
    val price: Double
    val categoryId: Long
    val favorite: Boolean
}