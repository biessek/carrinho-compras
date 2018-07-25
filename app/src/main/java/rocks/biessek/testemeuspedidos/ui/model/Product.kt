package rocks.biessek.testemeuspedidos.ui.model

import rocks.biessek.testemeuspedidos.domain.model.Product

data class Product(
        override var id: Long? = null,
        override val name: String,
        override val description: String,
        override val photo: String,
        override val price: Double,
        override val categoryId: Long,
        override var favorite: Boolean = false
) : Product