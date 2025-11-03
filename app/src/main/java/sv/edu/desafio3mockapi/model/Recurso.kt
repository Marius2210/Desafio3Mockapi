package sv.edu.desafio3mockapi.model
import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Recurso(
    @SerializedName("id")
    val id: String,

    @SerializedName("titulo")
    val titulo: String,

    @SerializedName("descripcion")
    val descripcion: String,

    @SerializedName("tipo")
    val tipo: String,

    @SerializedName("urlEnlace")
    val urlEnlace: String,

    @SerializedName("imagen")
    val urlImagen: String,
) : Parcelable