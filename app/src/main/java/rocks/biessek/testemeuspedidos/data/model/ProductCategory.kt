package rocks.biessek.testemeuspedidos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import rocks.biessek.testemeuspedidos.domain.model.ProductCategory

@Entity
data class ProductCategory(
        @PrimaryKey override val id: Long,
        override val name: String
) : ProductCategory