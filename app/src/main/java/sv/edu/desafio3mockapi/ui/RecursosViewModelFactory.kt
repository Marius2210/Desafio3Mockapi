package sv.edu.desafio3mockapi.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import sv.edu.desafio3mockapi.repository.RecursosRepository

class RecursosViewModelFactory(private val repository: RecursosRepository) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RecursosViewModel::class.java)) {
            return RecursosViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}