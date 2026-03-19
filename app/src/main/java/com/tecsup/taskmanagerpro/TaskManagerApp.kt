package com.tecsup.taskmanagerpro

import android.app.Application
import dagger.hilt.android.HiltAndroidApp

/**
 * Clase Application de la aplicación
 * @HiltAndroidApp genera el código necesario para Hilt
 * Esta anotación es requerida para que Hilt funcione en la aplicación
 */
@HiltAndroidApp
class TaskManagerApp : Application() {
    
    override fun onCreate() {
        super.onCreate()
        // Aquí se puede inicializar cualquier configuración global
        // Por ejemplo, configuración de logs, analytics, etc.
    }
}
