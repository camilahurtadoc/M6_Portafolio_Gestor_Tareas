package com.tecsup.taskmanagerpro.data.repository

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseAuthException
import com.tecsup.taskmanagerpro.data.model.User
import com.tecsup.taskmanagerpro.domain.repository.AuthRepository
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Implementación del repositorio de autenticación usando Firebase Auth
 * Esta clase es parte de la capa de datos (MVVM)
 */
@Singleton
class AuthRepositoryImpl @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) : AuthRepository {

    /**
     * Inicia sesión con email y contraseña usando Firebase Authentication
     */
    override suspend fun login(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Error al iniciar sesión"))
            }
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(getAuthErrorMessage(e.errorCode)))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    /**
     * Registra un nuevo usuario con email y contraseña
     */
    override suspend fun register(email: String, password: String): Result<User> {
        return try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            val firebaseUser = authResult.user
            
            if (firebaseUser != null) {
                val user = User(
                    uid = firebaseUser.uid,
                    email = firebaseUser.email ?: "",
                    displayName = firebaseUser.displayName ?: ""
                )
                Result.success(user)
            } else {
                Result.failure(Exception("Error al registrar usuario"))
            }
        } catch (e: FirebaseAuthException) {
            Result.failure(Exception(getAuthErrorMessage(e.errorCode)))
        } catch (e: Exception) {
            Result.failure(Exception("Error inesperado: ${e.message}"))
        }
    }

    /**
     * Cierra la sesión del usuario actual
     */
    override suspend fun logout(): Result<Unit> {
        return try {
            firebaseAuth.signOut()
            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(Exception("Error al cerrar sesión: ${e.message}"))
        }
    }

    /**
     * Obtiene el usuario actual autenticado
     */
    override fun getCurrentUser(): User? {
        val firebaseUser = firebaseAuth.currentUser
        return firebaseUser?.let {
            User(
                uid = it.uid,
                email = it.email ?: "",
                displayName = it.displayName ?: ""
            )
        }
    }

    /**
     * Observa el estado de autenticación del usuario usando Flow
     */
    override fun isUserAuthenticated(): Flow<Boolean> = callbackFlow {
        val authStateListener = FirebaseAuth.AuthStateListener { auth ->
            trySend(auth.currentUser != null)
        }
        firebaseAuth.addAuthStateListener(authStateListener)
        
        awaitClose {
            firebaseAuth.removeAuthStateListener(authStateListener)
        }
    }

    /**
     * Convierte códigos de error de Firebase en mensajes legibles
     */
    private fun getAuthErrorMessage(errorCode: String): String {
        return when (errorCode) {
            "ERROR_INVALID_EMAIL" -> "El correo electrónico no es válido"
            "ERROR_USER_NOT_FOUND" -> "No existe una cuenta con este correo"
            "ERROR_WRONG_PASSWORD" -> "Contraseña incorrecta"
            "ERROR_EMAIL_ALREADY_IN_USE" -> "Este correo ya está registrado"
            "ERROR_WEAK_PASSWORD" -> "La contraseña es muy débil"
            "ERROR_USER_DISABLED" -> "Esta cuenta ha sido deshabilitada"
            "ERROR_TOO_MANY_REQUESTS" -> "Demasiados intentos. Intenta más tarde"
            "ERROR_NETWORK_REQUEST_FAILED" -> "Error de red. Verifica tu conexión"
            else -> "Error de autenticación: $errorCode"
        }
    }
}
