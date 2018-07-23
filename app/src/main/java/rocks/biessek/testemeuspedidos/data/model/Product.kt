package rocks.biessek.testemeuspedidos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import rocks.biessek.testemeuspedidos.domain.model.Product

@Entity
data class Product(
        override val name: String,
        override val description: String,
        override val photo: String,
        override val price: Double,
        override val categoryId: Long,
        override val favorite: Boolean = false
) : Product {
    @PrimaryKey(autoGenerate = true)
    var id: Long? = null
}