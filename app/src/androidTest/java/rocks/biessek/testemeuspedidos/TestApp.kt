package rocks.biessek.testemeuspedidos

import android.os.StrictMode
import androidx.room.Room
import okhttp3.mockwebserver.MockWebServer
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

class TestApp : App() {
    private val server = MockWebServer()

    override fun onCreate() {
        super.onCreate()
        kodein = createAppModule()
    }

    override fun onTerminate() {
        server.shutdown()
        super.onTerminate()
    }

    private fun createAppModule() = Kodein {
        bind<MockWebServer>() with provider { server }
        bind<ProductsDatabase>() with singleton {
            Room.inMemoryDatabaseBuilder(applicationContext, ProductsDatabase::class.java).build()
        }
        bind<ProductsLocalDataSource>() with provider {
            val database: ProductsDatabase by kodein.instance()
            ProductsLocalDataSource(database.productsDao())
        }
        bind<ProductsRemoteDataSource>() with provider {
            /** server.hostname faz uma requisição, uso StrictMode para permitir rede na thread principal.
             * Depois é bloqueada novamente*/
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://${server.hostName}:${server.port}")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().build())

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