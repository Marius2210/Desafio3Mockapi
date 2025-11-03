package sv.edu.desafio3mockapi.ui

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import sv.edu.desafio3mockapi.model.Recurso
import sv.edu.desafio3mockapi.repository.RecursosRepository

class RecursosViewModel(private val repository: RecursosRepository) : ViewModel() {

    val recursosList = MutableLiveData<List<Recurso>>()
    val isLoading = MutableLiveData<Boolean>()
    val message = MutableLiveData<String?>()
    val operationStatus = MutableLiveData<Boolean>() // Para POST y PUT

    fun fetchRecursos() {
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.fetchRecursos()
            isLoading.value = false

            result.onSuccess {
                recursosList.value = it
            }.onFailure {
                message.value = "Error al cargar recursos: ${it.message}"
            }
        }
    }

    fun createRecurso(recurso: Recurso) {
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.createRecurso(recurso)
            isLoading.value = false

            result.onSuccess {
                operationStatus.value = true // Indica éxito a AddEditActivity
                message.value = "Recurso creado con éxito."
            }.onFailure {
                operationStatus.value = false
                message.value = "Error al crear: ${it.message}"
            }
        }
    }

    fun updateRecurso(recurso: Recurso) {
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.updateRecurso(recurso)
            isLoading.value = false

            result.onSuccess {
                operationStatus.value = true // Indica éxito a AddEditActivity
                message.value = "Recurso actualizado con éxito."
            }.onFailure {
                operationStatus.value = false
                message.value = "Error al actualizar: ${it.message}"
            }
        }
    }

    fun deleteRecurso(id: String) {
        isLoading.value = true
        viewModelScope.launch {
            val result = repository.deleteRecurso(id)
            isLoading.value = false

            result.onSuccess {
                message.value = "Recurso eliminado con éxito."
                fetchRecursos() // Recargar la lista para reflejar el cambio
            }.onFailure {
                message.value = "Error al eliminar: ${it.message}"
            }
        }
    }

    fun clearMessage() {
        message.value = null
    }
}