package sv.edu.desafio3mockapi.remote
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path
import sv.edu.desafio3mockapi.model.Recurso

interface RecursosApiService {

    @GET("recursos")
    suspend fun getRecursos(): Response<List<Recurso>>

    @POST("recursos")
    suspend fun createRecurso(@Body recurso: Recurso): Response<Recurso>

    @PUT("recursos/{id}")
    suspend fun updateRecurso(@Path("id") id: String, @Body recurso: Recurso): Response<Recurso>

    @DELETE("recursos/{id}")
    suspend fun deleteRecurso(@Path("id") id: String): Response<Unit>
}