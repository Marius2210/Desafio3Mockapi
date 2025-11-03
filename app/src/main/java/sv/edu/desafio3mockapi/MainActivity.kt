package sv.edu.desafio3mockapi
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import sv.edu.desafio3mockapi.remote.RetrofitClient
import sv.edu.desafio3mockapi.remote.RecursosApiService
import sv.edu.desafio3mockapi.repository.RecursosRepository
import sv.edu.desafio3mockapi.databinding.ActivityMainBinding
import sv.edu.desafio3mockapi.model.Recurso
import sv.edu.desafio3mockapi.LoginActivity
import sv.edu.desafio3mockapi.R
import sv.edu.desafio3mockapi.ui.RecursosAdapter
import sv.edu.desafio3mockapi.ui.RecursosViewModel
import sv.edu.desafio3mockapi.ui.RecursosViewModelFactory
import sv.edu.desafio3mockapi.util.SessionManager

class MainActivity : AppCompatActivity(), RecursosAdapter.OnRecursoClickListener {

    private lateinit var binding: ActivityMainBinding
    private lateinit var recursosAdapter: RecursosAdapter
    private lateinit var sessionManager: SessionManager

    // Constante para identificar la solicitud de AddEditActivity
    private val ADD_EDIT_REQUEST_CODE = 1

    private val viewModel: RecursosViewModel by viewModels {
        val apiService = RetrofitClient.getService(RecursosApiService::class.java)
        val repository = RecursosRepository(apiService)
        RecursosViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        sessionManager = SessionManager(applicationContext)

        //Configurar el Toolbar personalizado como ActionBar
        val toolbar = binding.mainToolbar
        setSupportActionBar(toolbar)

        // Establecer el título de la ActionBar
        supportActionBar?.title = "Recursos de ${sessionManager.getUserEmail() ?: "Usuario"}"

        setupRecyclerView()
        setupObservers()
        viewModel.fetchRecursos()
    }

    //Manejo de menú

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // Inflar el menú que contiene el searchView
        menuInflater.inflate(R.menu.main_menu, menu)

        // Obtener el searchView e implementa el listener
        val searchItem = menu?.findItem(R.id.action_search)
        val searchView = searchItem?.actionView as? androidx.appcompat.widget.SearchView

        // Configurar el listener de búsqueda
        searchView?.setOnQueryTextListener(object : androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                performSearch(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                performSearch(newText)
                return true
            }
        })

        menu?.add(Menu.NONE, 1, Menu.NONE, getString(R.string.btn_logout))
            ?.setIcon(R.drawable.ic_logout)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        menu?.add(Menu.NONE, 2, Menu.NONE, getString(R.string.btn_add))
            ?.setIcon(R.drawable.ic_add)
            ?.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM)

        return true
    }

    private fun performSearch(query: String?) {
        recursosAdapter.filter(query)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            1 -> { performLogout(); true }
            2 -> { navigateToAddRecurso(); true }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun performLogout() {
        sessionManager.logout()
        val intent = Intent(this, LoginActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    private fun navigateToAddRecurso() {
        val intent = Intent(this, AddEditRecursoActivity::class.java)
        startActivityForResult(intent, ADD_EDIT_REQUEST_CODE)
    }

    // Capturar el resultado de AddEditRecursoActivity para recargar la lista
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == ADD_EDIT_REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            viewModel.fetchRecursos()
        }
    }

    //Setup y Observadores
    private fun setupRecyclerView() {
        recursosAdapter = RecursosAdapter(emptyList())
        recursosAdapter.clickListener = this

        binding.recyclerViewRecursos.apply {
            layoutManager = LinearLayoutManager(this@MainActivity)
            adapter = recursosAdapter
        }
    }

    private fun setupObservers() {
        viewModel.recursosList.observe(this) { recursos ->
            recursosAdapter.updateData(recursos)
        }

        viewModel.isLoading.observe(this) { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            binding.recyclerViewRecursos.visibility = if (isLoading) View.GONE else View.VISIBLE
        }

        viewModel.message.observe(this) { message ->
            message?.let {
                Toast.makeText(this, it, Toast.LENGTH_LONG).show()
                viewModel.clearMessage()
            }
        }
    }

    //CRUD

    override fun onRecursoClick(recurso: Recurso) {
        // Abrir el enlace del recurso (al hacer clic en la tarjeta/enlace)
        val url = recurso.urlEnlace
        if (url.isNotEmpty() && url.startsWith("http")) {
            try {
                val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url))
                startActivity(intent)
            } catch (e: Exception) {
                Toast.makeText(this, "No se pudo abrir el enlace.", Toast.LENGTH_SHORT).show()
            }
        } else {
            Toast.makeText(this, "Enlace no válido.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onEditClick(recurso: Recurso) {
        // Navegar a AddEditRecursoActivity en modo edición
        val intent = Intent(this, AddEditRecursoActivity::class.java).apply {
            // Es CRUCIAL que Recurso implemente Parcelable
            putExtra("RECURSO_EDITAR", recurso)
        }
        startActivityForResult(intent, ADD_EDIT_REQUEST_CODE)
    }

    override fun onDeleteClick(recurso: Recurso) {
        // Mostrar confirmación antes de llamar a la eliminación
        showDeleteConfirmationDialog(recurso)
    }

    private fun showDeleteConfirmationDialog(recurso: Recurso) {
        AlertDialog.Builder(this)
            .setTitle("Eliminar Recurso")
            .setMessage("¿Está seguro de que desea eliminar el recurso: ${recurso.titulo}?")
            .setPositiveButton("Eliminar") { dialog, which ->
                // Llama al ViewModel para eliminar
                viewModel.deleteRecurso(recurso.id)
            }
            .setNegativeButton("Cancelar", null)
            .show()
    }
}