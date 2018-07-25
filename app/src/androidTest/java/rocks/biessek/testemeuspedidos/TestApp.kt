package rocks.biessek.testemeuspedidos

import android.os.StrictMode
import androidx.room.Room
import okhttp3.mockwebserver.MockWebServer
import org.kodein.di.Kodein
import org.kodein.di.generic.bind
import org.kodein.di.generic.provider
import org.kodein.di.generic.singleton
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import rocks.biessek.testemeuspedidos.data.local.ProductsDatabase
import rocks.biessek.testemeuspedidos.data.remote.ServiceApi

class TestApp : App() {
    private val server = MockWebServer()

    override fun onTerminate() {
        server.shutdown()
        super.onTerminate()
    }

    override fun createAppModule() = Kodein.Module("testapp") {
        import(super.createAppModule(), true)
        import(testModule(), true)
    }

    private fun testModule() = Kodein.Module("test", true) {
        bind<MockWebServer>() with provider { server }
        bind<ProductsDatabase>() with singleton {
            Room.inMemoryDatabaseBuilder(applicationContext, ProductsDatabase::class.java).build()
        }
        bind<ServiceApi>() with provider {
            /** server.hostname faz uma requisição, uso StrictMode para permitir rede na thread principal.
             * Depois é bloqueada novamente*/
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().permitAll().build())
            val retrofit = Retrofit.Builder()
                    .baseUrl("http://${server.hostName}:${server.port}")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build()
            StrictMode.setThreadPolicy(StrictMode.ThreadPolicy.Builder().build())
            retrofit.create(ServiceApi::class.java)
        }
    }

}