package com.tecsup.taskmanagerpro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.taskmanagerpro.ui.navigation.AppNavigation
import com.tecsup.taskmanagerpro.ui.theme.TaskManagerProTheme
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

/**
 * MainActivity principal de la aplicación
 * Punto de entrada de la aplicación con Jetpack Compose
 * 
 * @AndroidEntryPoint permite la inyección de dependencias con Hilt
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        
        setContent {
            TaskManagerProTheme {
                // Surface contenedor con el color de fondo del tema
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigation(firebaseAuth = firebaseAuth)
                }
            }
        }
    }
}
