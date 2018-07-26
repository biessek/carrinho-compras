package rocks.biessek.testemeuspedidos

import android.app.Application
import androidx.room.Room
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.instance
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rocks.biessek.testemeuspedidos.data.ProductCategoriesRepository
import rocks.biessek.testemeuspedidos.data.ProductsRepository
import rocks.biessek.testemeuspedidos.data.local.ProductCategoriesLocalDataSource
import rocks.biessek.testemeuspedidos.data.local.ProductsDatabase
import rocks.biessek.testemeuspedidos.data.local.ProductsLocalDataSource
import rocks.biessek.testemeuspedidos.data.remote.ProductCategoriesRemoteDataSource
import rocks.biessek.testemeuspedidos.data.remote.ProductsRemoteDataSource
import rocks.biessek.testemeuspedidos.data.remote.ServiceApi
import rocks.biessek.testemeuspedidos.domain.CategoriesInteractors
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors

open class App : Application() {
    companion object {
        const val BASE_URL = "https://gist.githubusercontent.com/ronanrodrigo/"
    }

    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    lateinit var kodein: Kodein

    override fun onCreate() {
        super.onCreate()

        kodein = Kodein {
            import(createAppModule(), true)
        }
    }

    open fun createAppModule() = Kodein.Module("app", true) {
        bind<ProductsDatabase>() with singleton {
            Room.databaseBuilder(applicationContext, ProductsDatabase::class.java, "products_database").build()
        }
        bind<ServiceApi>() with provider {
            retrofit.create(ServiceApi::class.java)
        }
        bind<ProductsLocalDataSource>() with provider {
            val database: ProductsDatabase by kodein.instance()
            ProductsLocalDataSource(database.productsDao())
        }
        bind<ProductsRemoteDataSource>() with provider {
            val serviceApi: ServiceApi by kodein.instance()
            ProductsRemoteDataSource(serviceApi)
        }
        bind<ProductsRepository>() with provider {
            val localDataSource: ProductsLocalDataSource by kodein.instance()
            val remoteDataSource: ProductsRemoteDataSource by kodein.instance()
            ProductsRepository(localDataSource, remoteDataSource)
        }
        bind<ProductsInteractors>() with provider {
            val repository: ProductsRepository by kodein.instance()
            ProductsInteractors(repository)
        }

        bind<ProductCategoriesLocalDataSource>() with provider {
            val database: ProductsDatabase by kodein.instance()
            ProductCategoriesLocalDataSource(database.categoriesDao())
        }
        bind<ProductCategoriesRemoteDataSource>() with provider {
            val serviceApi: ServiceApi by kodein.instance()
            ProductCategoriesRemoteDataSource(serviceApi)
        }
        bind<ProductCategoriesRepository>() with provider {
            val localDataSource: ProductCategoriesLocalDataSource by kodein.instance()
            val remoteDataSource: ProductCategoriesRemoteDataSource by kodein.instance()
            ProductCategoriesRepository(localDataSource, remoteDataSource)
        }

        bind<CategoriesInteractors>() with provider {
            val repository: ProductCategoriesRepository by kodein.instance()
            CategoriesInteractors(repository)
        }
    }

}