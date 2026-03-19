package com.tecsup.taskmanagerpro.ui.navigation

/**
 * Clase sellada que define todas las pantallas de la aplicación
 * Utilizada para la navegación con Compose Navigation
 */
sealed class Screen(val route: String) {
    /**
     * Pantalla de inicio de sesión
     */
    object Login : Screen("login")

    /**
     * Pantalla de registro
     */
    object Register : Screen("register")

    /**
     * Pantalla de lista de tareas
     */
    object TaskList : Screen("task_list")

    /**
     * Pantalla de creación de tarea
     */
    object CreateTask : Screen("create_task")

    /**
     * Pantalla de edición de tarea
     * Requiere el ID de la tarea como argumento
     */
    object EditTask : Screen("edit_task/{taskId}") {
        /**
         * Crea la ruta con el ID de la tarea
         * @param taskId ID de la tarea a editar
         * @return Ruta completa con el ID
         */
        fun createRoute(taskId: String) = "edit_task/$taskId"
    }

    companion object {
        /**
         * Nombre del argumento para el ID de tarea
         */
        const val TASK_ID_ARG = "taskId"
    }
}