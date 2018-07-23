package rocks.biessek.testemeuspedidos.data.local

import androidx.room.*
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

@Dao
interface ProductCategoriesDao {
    @Query("SELECT * FROM ProductCategory")
    fun getAllCategories(): List<ProductCategory>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun saveProductCategory(product: ProductCategory): Long

    @Delete
    fun deleteProductCategories(vararg category: ProductCategory): Int
}