package sv.edu.desafio3mockapi.util

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {

    // Nombre del archivo de preferencias que se guardará en el disco
    private val PREFS_FILE_NAME = "AprendeApp_Prefs"

    // Clave para el estado de la sesión (true si está logueado, false si no)
    private val KEY_IS_LOGGED_IN = "isLoggedIn"

    // Clave para guardar el correo del usuario logueado
    private val KEY_USER_EMAIL = "userEmail"

    // Obtiene la instancia de SharedPreferences
    private val prefs: SharedPreferences = context.getSharedPreferences(PREFS_FILE_NAME, Context.MODE_PRIVATE)

    // Obtiene el Editor de SharedPreferences para realizar cambios
    private val editor: SharedPreferences.Editor = prefs.edit()

    fun createLoginSession(email: String) {
        editor.putBoolean(KEY_IS_LOGGED_IN, true)
        editor.putString(KEY_USER_EMAIL, email)
        editor.apply() // Guarda los cambios de forma asíncrona
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun getUserEmail(): String? {
        return prefs.getString(KEY_USER_EMAIL, null)
    }

    fun logout() {
        editor.clear() // Elimina todos los datos guardados en este archivo
        editor.apply()
    }
}