package rocks.biessek.testemeuspedidos.data.local

import androidx.room.Dao
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import rocks.biessek.testemeuspedidos.data.model.ProductCategory

@Dao
interface ProductCategoriesDao {
    @Query("SELECT * FROM ProductCategory")
    fun getAllCategories(): List<ProductCategory>

    @Update(onConflict = OnConflictStrategy.REPLACE)
    fun saveProductCatefory(product: ProductCategory): Int
}