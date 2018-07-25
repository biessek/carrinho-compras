package rocks.biessek.testemeuspedidos.ui.model

import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

data class ProductCategory(
        override val id: Long,
        override val name: String
) : ProductCategory