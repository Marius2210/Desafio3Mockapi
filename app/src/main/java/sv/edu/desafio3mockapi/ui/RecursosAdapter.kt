package sv.edu.desafio3mockapi.ui
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import sv.edu.desafio3mockapi.databinding.ListItemRecursoBinding
import sv.edu.desafio3mockapi.model.Recurso
import com.bumptech.glide.Glide

class RecursosAdapter(
    private var recursosList: List<Recurso>
) : RecyclerView.Adapter<RecursosAdapter.RecursoViewHolder>() {

    private var fullRecursosList: List<Recurso> = recursosList.toList()

    interface OnRecursoClickListener {
        fun onEditClick(recurso: Recurso)
        fun onDeleteClick(recurso: Recurso)
        fun onRecursoClick(recurso: Recurso) // Para ver el enlace/detalle
    }

    var clickListener: OnRecursoClickListener? = null

    inner class RecursoViewHolder(private val binding: ListItemRecursoBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(recurso: Recurso) {
            binding.tvTitulo.text = recurso.titulo
            binding.tvDescripcion.text = recurso.descripcion
            binding.tvTipo.text = recurso.tipo

            binding.tvEnlace.text = recurso.urlEnlace

            Glide.with(binding.imgRecurso.context)
            .load(recurso.urlImagen)
            .into(binding.imgRecurso)

            //Listeners
            binding.root.setOnClickListener {
                clickListener?.onRecursoClick(recurso)
            }

            binding.btnEdit.setOnClickListener {
                clickListener?.onEditClick(recurso)
            }

            // Listener para el botón de Eliminar
            binding.btnDelete.setOnClickListener {
                clickListener?.onDeleteClick(recurso)
            }
        }
    }

    // Overrides
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecursoViewHolder {
        val binding = ListItemRecursoBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return RecursoViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecursoViewHolder, position: Int) {
        holder.bind(recursosList[position])
    }

    override fun getItemCount(): Int = recursosList.size

    fun updateData(newRecursosList: List<Recurso>) {
        recursosList = newRecursosList
        fullRecursosList = newRecursosList.toList() // Actualizar la lista completa
        notifyDataSetChanged()
    }

    // Busqueda
    fun filter(query: String?) {
        val filteredList: List<Recurso> = if (query.isNullOrBlank()) {
            fullRecursosList // Si está vacío, mostrar toda la lista
        } else {
            val lowerCaseQuery = query.lowercase()
            fullRecursosList.filter { recurso ->
                // Búsqueda avanzada por ID, Título, o Tipo
                recurso.id.lowercase().contains(lowerCaseQuery) ||
                        recurso.titulo.lowercase().contains(lowerCaseQuery) ||
                        recurso.tipo.lowercase().contains(lowerCaseQuery)
            }
        }

        recursosList = filteredList
        notifyDataSetChanged()
    }

}