package com.tecsup.taskmanagerpro.domain.usecase

import com.tecsup.taskmanagerpro.data.model.User
import com.tecsup.taskmanagerpro.domain.repository.AuthRepository
import javax.inject.Inject

/**
 * Caso de uso para iniciar sesión
 * Parte de la capa de dominio (MVVM)
 * Encapsula la lógica de negocio del login
 */
class LoginUseCase @Inject constructor(
    private val authRepository: AuthRepository
) {
    /**
     * Ejecuta el caso de uso de login
     * Valida los datos antes de llamar al repositorio
     */
    suspend operator fun invoke(email: String, password: String): Result<User> {
        // Validaciones
        if (email.isBlank()) {
            return Result.failure(Exception("El correo electrónico es requerido"))
        }

        if (!isValidEmail(email)) {
            return Result.failure(Exception("El correo electrónico no es válido"))
        }

        if (password.isBlank()) {
            return Result.failure(Exception("La contraseña es requerida"))
        }

        if (password.length < 6) {
            return Result.failure(Exception("La contraseña debe tener al menos 6 caracteres"))
        }

        // Llamar al repositorio
        return authRepository.login(email, password)
    }

    /**
     * Valida el formato del email
     */
    private fun isValidEmail(email: String): Boolean {
        val emailRegex = "^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+\\.[A-Za-z]{2,}$".toRegex()
        return emailRegex.matches(email)
    }
}
