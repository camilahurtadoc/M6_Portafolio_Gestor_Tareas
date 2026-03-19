package com.tecsup.taskmanagerpro.domain.repository

import com.tecsup.taskmanagerpro.data.model.User
import kotlinx.coroutines.flow.Flow

/**
 * Interfaz del repositorio de autenticación
 * Define el contrato para las operaciones de autenticación
 * Esta interfaz es parte de la capa de dominio (MVVM)
 */
interface AuthRepository {
    /**
     * Inicia sesión con email y contraseña
     * @return Result con el User si es exitoso o un error
     */
    suspend fun login(email: String, password: String): Result<User>

    /**
     * Registra un nuevo usuario con email y contraseña
     * @return Result con el User si es exitoso o un error
     */
    suspend fun register(email: String, password: String): Result<User>

    /**
     * Cierra la sesión del usuario actual
     * @return Result con Unit si es exitoso o un error
     */
    suspend fun logout(): Result<Unit>

    /**
     * Obtiene el usuario actual autenticado
     * @return User si hay un usuario autenticado, null en caso contrario
     */
    fun getCurrentUser(): User?

    /**
     * Observa el estado de autenticación del usuario
     * @return Flow que emite true si el usuario está autenticado, false en caso contrario
     */
    fun isUserAuthenticated(): Flow<Boolean>
}
