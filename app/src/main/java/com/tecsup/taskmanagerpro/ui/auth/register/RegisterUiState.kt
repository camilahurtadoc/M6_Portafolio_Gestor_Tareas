package com.tecsup.taskmanagerpro.ui.auth.register

/**
 * Estado de la UI de Registro
 * Representa todos los posibles estados de la pantalla de registro
 */
data class RegisterUiState(
    val email: String = "",
    val password: String = "",
    val confirmPassword: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isRegisterSuccessful: Boolean = false
)
