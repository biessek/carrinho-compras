package rocks.biessek.testemeuspedidos.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import rocks.biessek.testemeuspedidos.domain.model.Product

@Entity
data class Product(
        @PrimaryKey(autoGenerate = true)
        override var id: Long? = null,
        override val name: String,
        override val description: String,
        override val photo: String,
        override val price: Double,
        override val categoryId: Long,
        override val favorite: Boolean = false
) : Product