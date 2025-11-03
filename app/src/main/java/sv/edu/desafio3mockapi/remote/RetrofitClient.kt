package sv.edu.desafio3mockapi.remote
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://6906242fee3d0d14c134f557.mockapi.io/api/v1/"

    private val retrofit: Retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun <T> getService(serviceClass: Class<T>): T {
        return retrofit.create(serviceClass)
    }
}