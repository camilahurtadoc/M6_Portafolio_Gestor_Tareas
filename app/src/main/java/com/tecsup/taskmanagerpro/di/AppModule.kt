package com.tecsup.taskmanagerpro.di

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.tecsup.taskmanagerpro.data.repository.AuthRepositoryImpl
import com.tecsup.taskmanagerpro.data.repository.TaskRepositoryImpl
import com.tecsup.taskmanagerpro.domain.repository.AuthRepository
import com.tecsup.taskmanagerpro.domain.repository.TaskRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Módulo de Hilt para proveer repositorios
 * Conecta las interfaces del dominio con sus implementaciones
 * Esto es clave para la arquitectura MVVM y el principio de inversión de dependencias
 */
@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    /**
     * Provee una implementación de AuthRepository
     * Hilt inyectará esta implementación cuando se requiera AuthRepository
     */
    @Provides
    @Singleton
    fun provideAuthRepository(
        firebaseAuth: FirebaseAuth
    ): AuthRepository {
        return AuthRepositoryImpl(firebaseAuth)
    }

    /**
     * Provee una implementación de TaskRepository
     * Hilt inyectará esta implementación cuando se requiera TaskRepository
     */
    @Provides
    @Singleton
    fun provideTaskRepository(
        firestore: FirebaseFirestore,
        firebaseAuth: FirebaseAuth
    ): TaskRepository {
        return TaskRepositoryImpl(firestore, firebaseAuth)
    }
}
