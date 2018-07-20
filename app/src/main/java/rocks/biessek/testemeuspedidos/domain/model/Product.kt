package rocks.biessek.testemeuspedidos.domain.model

data class Product(
        val name: String,
        val description: String,
        val photo: String,
        val price: Number,
        val categoryId: Int,
        val favorite: Boolean
)