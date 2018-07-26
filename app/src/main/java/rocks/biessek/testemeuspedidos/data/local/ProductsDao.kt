package rocks.biessek.testemeuspedidos.data.local

import androidx.room.*
import rocks.biessek.testemeuspedidos.data.model.Product

@Dao
interface ProductsDao {
    @Query("SELECT * FROM Product WHERE id = :id")
    fun getProductById(id: Long): Product?

    @Query("SELECT * FROM Product")
    fun getAllProducts(): List<Product>

    @Query("SELECT * FROM Product WHERE categoryId = :categoryId")
    fun getProductsFromCategory(categoryId: Long): List<Product>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProduct(product: Product): Long

    @Delete
    fun deleteProducts(vararg product: Product): Int

    @Insert
    fun saveAllProducts(products: Array<Product>): Array<Long>
}