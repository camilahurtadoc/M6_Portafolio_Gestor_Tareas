package com.tecsup.taskmanagerpro.ui.auth.login

/**
 * Estado de la UI de Login
 * Representa todos los posibles estados de la pantalla de login
 */
data class LoginUiState(
    val email: String = "",
    val password: String = "",
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val isLoginSuccessful: Boolean = false
)
