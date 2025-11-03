package sv.edu.desafio3mockapi.repository

import sv.edu.desafio3mockapi.model.Recurso
import sv.edu.desafio3mockapi.remote.RecursosApiService

class RecursosRepository(private val apiService: RecursosApiService) {

    suspend fun fetchRecursos(): Result<List<Recurso>> {
        return try {
            val response = apiService.getRecursos()
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al obtener recursos: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun createRecurso(recurso: Recurso): Result<Recurso> {
        return try {
            val response = apiService.createRecurso(recurso)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al crear recurso: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun updateRecurso(recurso: Recurso): Result<Recurso> {
        return try {
            // Se usa recurso.id para el path de la URL en la API
            val response = apiService.updateRecurso(recurso.id, recurso)
            if (response.isSuccessful && response.body() != null) {
                Result.success(response.body()!!)
            } else {
                Result.failure(Exception("Error al actualizar recurso: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun deleteRecurso(id: String): Result<Unit> {
        return try {
            val response = apiService.deleteRecurso(id)
            if (response.isSuccessful) {
                Result.success(Unit)
            } else {
                Result.failure(Exception("Error al eliminar recurso: ${response.code()}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}