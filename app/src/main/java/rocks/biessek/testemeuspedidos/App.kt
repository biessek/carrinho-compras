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
import rocks.biessek.testemeuspedidos.data.ProductsRepository
import rocks.biessek.testemeuspedidos.data.local.ProductsDatabase
import rocks.biessek.testemeuspedidos.data.local.ProductsLocalDataSource
import rocks.biessek.testemeuspedidos.data.remote.ProductsRemoteDataSource
import rocks.biessek.testemeuspedidos.data.remote.ServiceApi
import rocks.biessek.testemeuspedidos.domain.ProductsInteractors

open class App : Application() {
    private val BASE_URL = "https://gist.githubusercontent.com/ronanrodrigo/"
    private val retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()


    lateinit var kodein: Kodein

    override fun onCreate() {
        super.onCreate()

        kodein = createAppModule()
    }

    private fun createAppModule() = Kodein {
        bind<ProductsDatabase>() with singleton {
            Room.databaseBuilder(applicationContext, ProductsDatabase::class.java, "products_database").build()
        }
        bind<ProductsLocalDataSource>() with provider {
            val database: ProductsDatabase by kodein.instance()
            ProductsLocalDataSource(database.productsDao())
        }
        bind<ProductsRemoteDataSource>() with provider {
            ProductsRemoteDataSource(retrofit.create(ServiceApi::class.java))
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

    }

}