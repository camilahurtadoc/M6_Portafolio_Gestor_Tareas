package com.tecsup.taskmanagerpro.ui.auth.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.tecsup.taskmanagerpro.domain.usecase.RegisterUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para la pantalla de Registro
 * Parte de la capa de presentación (MVVM)
 * Gestiona el estado y la lógica de la pantalla de registro
 */
@HiltViewModel
class RegisterViewModel @Inject constructor(
    private val registerUseCase: RegisterUseCase
) : ViewModel() {

    // Estado privado mutable
    private val _uiState = MutableStateFlow(RegisterUiState())
    
    // Estado público inmutable para la UI
    val uiState: StateFlow<RegisterUiState> = _uiState.asStateFlow()

    /**
     * Actualiza el email en el estado
     */
    fun onEmailChange(email: String) {
        _uiState.update { it.copy(email = email, errorMessage = null) }
    }

    /**
     * Actualiza la contraseña en el estado
     */
    fun onPasswordChange(password: String) {
        _uiState.update { it.copy(password = password, errorMessage = null) }
    }

    /**
     * Actualiza la confirmación de contraseña en el estado
     */
    fun onConfirmPasswordChange(confirmPassword: String) {
        _uiState.update { it.copy(confirmPassword = confirmPassword, errorMessage = null) }
    }

    /**
     * Ejecuta el registro
     */
    fun register() {
        val currentState = _uiState.value
        
        // Validación básica en el ViewModel
        if (currentState.email.isBlank() || 
            currentState.password.isBlank() || 
            currentState.confirmPassword.isBlank()) {
            _uiState.update {
                it.copy(errorMessage = "Por favor completa todos los campos")
            }
            return
        }

        if (currentState.password != currentState.confirmPassword) {
            _uiState.update {
                it.copy(errorMessage = "Las contraseñas no coinciden")
            }
            return
        }

        viewModelScope.launch {
            _uiState.update { it.copy(isLoading = true, errorMessage = null) }
            
            val result = registerUseCase(
                email = currentState.email.trim(),
                password = currentState.password,
                confirmPassword = currentState.confirmPassword
            )
            
            result.fold(
                onSuccess = {
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            isRegisterSuccessful = true,
                            errorMessage = null
                        )
                    }
                },
                onFailure = { exception ->
                    _uiState.update {
                        it.copy(
                            isLoading = false,
                            errorMessage = exception.message ?: "Error al registrar usuario"
                        )
                    }
                }
            )
        }
    }

    /**
     * Limpia el mensaje de error
     */
    fun clearError() {
        _uiState.update { it.copy(errorMessage = null) }
    }

    /**
     * Reinicia el estado de éxito del registro
     */
    fun resetRegisterSuccess() {
        _uiState.update { it.copy(isRegisterSuccessful = false) }
    }
}
