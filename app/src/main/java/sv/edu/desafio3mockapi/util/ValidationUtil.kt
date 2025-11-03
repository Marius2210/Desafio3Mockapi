package sv.edu.desafio3mockapi.util

object ValidationUtil {

    fun isPasswordValid(password: String): Boolean {
        // Expresión regular para contraseña
        val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[!@#\$%^&*]).{8,}\$")

        return passwordPattern.matches(password)
    }

    fun getPasswordValidationErrors(password: String): List<String> {
        val errors = mutableListOf<String>()

        if (password.length < 8) {
            errors.add("Debe tener al menos 8 caracteres.")
        }
        if (!password.contains(Regex("[A-Z]"))) {
            errors.add("Debe contener al menos una letra mayúscula.")
        }
        if (!password.contains(Regex("[a-z]"))) {
            errors.add("Debe contener al menos una letra minúscula.")
        }
        if (!password.contains(Regex("[0-9]"))) {
            errors.add("Debe contener al menos un número.")
        }
        if (!password.contains(Regex("[!@#\$%^&*]"))) {
            errors.add("Debe contener al menos un carácter especial (!@#\$%^&*).")
        }

        return errors
    }

    fun isEmailValid(email: String): Boolean {
        return android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()
    }
}