package sv.edu.desafio3mockapi

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import sv.edu.desafio3mockapi.databinding.ActivityRegisterBinding
import sv.edu.desafio3mockapi.util.ValidationUtil

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Botón de Registro
        binding.btnRegistrar.setOnClickListener {
            handleRegistration()
        }

        // Botón para volver al Login
        binding.btnVolverLogin.setOnClickListener {
            finish()
        }
    }

    private fun handleRegistration() {
        val email = binding.etCorreo.text.toString()
        val password = binding.etContrasena.text.toString()

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Todos los campos son obligatorios.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!ValidationUtil.isEmailValid(email)) {
            Toast.makeText(this, "Ingrese un correo electrónico válido.", Toast.LENGTH_SHORT).show()
            return
        }

        if (!ValidationUtil.isPasswordValid(password)) {
            val errors = ValidationUtil.getPasswordValidationErrors(password)
            val errorMessage = "La contraseña requiere:\n" + errors.joinToString(separator = "\n- ", prefix = "- ")
            Toast.makeText(this, errorMessage, Toast.LENGTH_LONG).show()
            return
        }

        Toast.makeText(this, "Registro exitoso (Simulado).", Toast.LENGTH_LONG).show()
        finish() // Vuelve a la pantalla de Login
    }
}