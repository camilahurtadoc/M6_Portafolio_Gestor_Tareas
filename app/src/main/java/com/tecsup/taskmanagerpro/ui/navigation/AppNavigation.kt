package com.tecsup.taskmanagerpro.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.google.firebase.auth.FirebaseAuth
import com.tecsup.taskmanagerpro.ui.auth.login.LoginScreen
import com.tecsup.taskmanagerpro.ui.auth.register.RegisterScreen
import com.tecsup.taskmanagerpro.ui.tasks.create.CreateTaskScreen
import com.tecsup.taskmanagerpro.ui.tasks.edit.EditTaskScreen
import com.tecsup.taskmanagerpro.ui.tasks.list.TaskListScreen
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

/**
 * Sistema de navegación principal de la aplicación
 * Gestiona la navegación entre todas las pantallas
 */
@Composable
fun AppNavigation(
    navController: NavHostController = rememberNavController(),
    firebaseAuth: FirebaseAuth
) {
    // Determinar la pantalla inicial según el estado de autenticación
    val startDestination = if (firebaseAuth.currentUser != null) {
        Screen.TaskList.route
    } else {
        Screen.Login.route
    }

    NavHost(
        navController = navController,
        startDestination = startDestination
    ) {
        // Pantalla de Login
        composable(route = Screen.Login.route) {
            LoginScreen(
                onNavigateToRegister = {
                    navController.navigate(Screen.Register.route)
                },
                onLoginSuccess = {
                    navController.navigate(Screen.TaskList.route) {
                        // Limpiar el back stack para que no se pueda volver al login
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Registro
        composable(route = Screen.Register.route) {
            RegisterScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onRegisterSuccess = {
                    navController.navigate(Screen.TaskList.route) {
                        // Limpiar el back stack para que no se pueda volver al registro
                        popUpTo(Screen.Login.route) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Lista de Tareas
        composable(route = Screen.TaskList.route) {
            TaskListScreen(
                onNavigateToCreateTask = {
                    navController.navigate(Screen.CreateTask.route)
                },
                onNavigateToEditTask = { taskId ->
                    navController.navigate(Screen.EditTask.createRoute(taskId))
                },
                onLogout = {
                    navController.navigate(Screen.Login.route) {
                        // Limpiar todo el back stack al cerrar sesión
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        // Pantalla de Crear Tarea
        composable(route = Screen.CreateTask.route) {
            CreateTaskScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTaskCreated = {
                    navController.popBackStack()
                }
            )
        }

        // Pantalla de Editar Tarea
        composable(
            route = Screen.EditTask.route,
            arguments = listOf(
                navArgument(Screen.TASK_ID_ARG) {
                    type = NavType.StringType
                }
            )
        ) {
            EditTaskScreen(
                onNavigateBack = {
                    navController.popBackStack()
                },
                onTaskUpdated = {
                    navController.popBackStack()
                }
            )
        }
    }
}
