package sv.edu.desafio3mockapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import sv.edu.desafio3mockapi.databinding.ActivityAddEditRecursoBinding
import sv.edu.desafio3mockapi.model.Recurso
import sv.edu.desafio3mockapi.remote.RetrofitClient
import sv.edu.desafio3mockapi.remote.RecursosApiService
import sv.edu.desafio3mockapi.repository.RecursosRepository
import sv.edu.desafio3mockapi.ui.RecursosViewModel
import sv.edu.desafio3mockapi.ui.RecursosViewModelFactory

class AddEditRecursoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditRecursoBinding
    private var recursoParaEditar: Recurso? = null // Null si es POST, tiene datos si es PUT

    private val viewModel: RecursosViewModel by viewModels {
        val apiService = RetrofitClient.getService(RecursosApiService::class.java)
        val repository = RecursosRepository(apiService)
        RecursosViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditRecursoBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Verificar si estamos en modo Edición (PUT)
        recursoParaEditar = intent.getParcelableExtra("RECURSO_EDITAR")

        setupMode()
        setupListeners()
        setupObservers()
    }

    private fun setupMode() {
        if (recursoParaEditar != null) {
            // Modo Edición: Prellenar campos
            supportActionBar?.title = getString(sv.edu.desafio3mockapi.R.string.titulo_editar)
            binding.btnGuardar.text = getString(sv.edu.desafio3mockapi.R.string.btn_actualizar)

            recursoParaEditar?.let { recurso ->
                binding.etTitulo.setText(recurso.titulo)
                binding.etDescripcion.setText(recurso.descripcion)
                binding.etTipo.setText(recurso.tipo)
                binding.etEnlace.setText(recurso.urlEnlace)
                binding.etUrlImagen.setText(recurso.urlImagen)
            }
        } else {
            // Modo Creación (POST)
            supportActionBar?.title = getString(sv.edu.desafio3mockapi.R.string.titulo_agregar)
            binding.btnGuardar.text = getString(sv.edu.desafio3mockapi.R.string.btn_guardar_recurso)
        }
    }

    private fun setupListeners() {
        binding.btnGuardar.setOnClickListener {
            handleSave()
        }
    }

    private fun handleSave() {
        val titulo = binding.etTitulo.text.toString()
        val descripcion = binding.etDescripcion.text.toString()
        val tipo = binding.etTipo.text.toString()
        val urlEnlace = binding.etEnlace.text.toString()
        val urlImagen = binding.etUrlImagen.text.toString()

        // Validación de campos básicos
        if (titulo.isEmpty() || urlEnlace.isEmpty() || urlImagen.isEmpty()) {
            Toast.makeText(this, "Debe completar al menos Título, Enlace e Imagen.", Toast.LENGTH_SHORT).show()
            return
        }

        // Crear el objeto Recurso
        val recurso = Recurso(
            id = recursoParaEditar?.id ?: "", // Si es nuevo, el ID será ignorado por el POST de la API
            titulo = titulo,
            descripcion = descripcion,
            tipo = tipo,
            urlEnlace = urlEnlace,
            urlImagen = urlImagen
        )

        // Llamada al ViewModel (POST o PUT)
        if (recursoParaEditar != null) {
            // Llamada para Edición (PUT)
            viewModel.updateRecurso(recurso)
        } else {
            // Llamada para Creación (POST)
            viewModel.createRecurso(recurso)
        }
    }

    private fun setupObservers() {
        // Observa si la operación (POST/PUT) fue exitosa
        viewModel.operationStatus.observe(this) { success ->
            if (success) {
                Toast.makeText(this, "Recurso guardado exitosamente.", Toast.LENGTH_SHORT).show()
                setResult(RESULT_OK) // Indica a MainActivity que recargue la lista
                finish()
            }
        }

        // Observa mensajes de error del ViewModel
        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearMessage()
            }
        }
    }
}