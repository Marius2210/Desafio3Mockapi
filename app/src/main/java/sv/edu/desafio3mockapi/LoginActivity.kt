package sv.edu.desafio3mockapi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import sv.edu.desafio3mockapi.RegisterActivity
import sv.edu.desafio3mockapi.databinding.ActivityLoginBinding
import sv.edu.desafio3mockapi.MainActivity
import sv.edu.desafio3mockapi.util.SessionManager
import sv.edu.desafio3mockapi.util.ValidationUtil

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var sessionManager: SessionManager // Instancia del SessionManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        sessionManager = SessionManager(applicationContext)

        //Verificar si ya hay una sesión activa**
        if (sessionManager.isLoggedIn()) {
            navigateToMain()
            return // Detiene la ejecución para ir directamente a la MainActivity
        }

        // Si no hay sesión, carga el layout de Login
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Manejar el botón de Iniciar Sesión
        binding.btnLogin.setOnClickListener {
            handleLogin()
        }

        // Navegación al Registro
        binding.btnIrARegistro.setOnClickListener {
            val intent = Intent(this, RegisterActivity::class.java)
            startActivity(intent)
        }
    }

    private fun handleLogin() {
        val email = binding.etCorreo.text.toString()
        val password = binding.etContrasena.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Debe llenar todos los campos.", Toast.LENGTH_SHORT).show()
            return
        }

        //Simulación de Login exitoso y creación de sesión**
        if (ValidationUtil.isEmailValid(email)) {
            sessionManager.createLoginSession(email)
            Toast.makeText(this, "Sesión iniciada como $email", Toast.LENGTH_SHORT).show()
            navigateToMain()
        } else {
            Toast.makeText(this, "Credenciales inválidas.", Toast.LENGTH_SHORT).show()
        }
    }

    // Función de utilidad para ir a la lista de recursos
    private fun navigateToMain() {
        val intent = Intent(this, MainActivity::class.java)
        // Usar banderas para evitar que el usuario regrese al Login con el botón Atrás
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }
}