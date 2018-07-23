package rocks.biessek.testemeuspedidos.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import rocks.biessek.testemeuspedidos.data.model.Product
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

@Database(
        entities = [
            Product::class,
            ProductCategory::class
        ],
        version = ProductsDatabase.VERSION
)
abstract class ProductsDatabase: RoomDatabase() {
    companion object {
        const val VERSION = 1
    }

    abstract fun productsDao(): ProductsDao
}