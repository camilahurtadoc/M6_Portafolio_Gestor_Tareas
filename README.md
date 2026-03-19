# 📱 TaskManager Pro

## 🎯 Proyecto de Desarrollo de Aplicaciones Móviles

**Aplicación completa de gestión de tareas con Kotlin, Jetpack Compose, Firebase y arquitectura MVVM**

---

## 📋 Tabla de Contenidos

1. [Descripción](#descripción)
2. [Características](#características)
3. [Tecnologías Utilizadas](#tecnologías-utilizadas)
4. [Arquitectura](#arquitectura)
5. [Instalación](#instalación)
6. [Configuración de Firebase](#configuración-de-firebase)
7. [Estructura del Proyecto](#estructura-del-proyecto)
8. [Uso de la Aplicación](#uso-de-la-aplicación)
9. [Entregables](#entregables)
10. [Autores](#autores)

---

## 📝 Descripción

TaskManager Pro es una aplicación móvil Android desarrollada con Kotlin y Jetpack Compose que permite a los usuarios gestionar sus tareas de manera eficiente. La aplicación implementa un sistema completo de autenticación con Firebase y almacena las tareas en tiempo real utilizando Cloud Firestore.

### Funcionalidades Principales:

- ✅ **Autenticación de usuarios** con Firebase Authentication
- ✅ **CRUD completo** de tareas (Crear, Leer, Actualizar, Eliminar)
- ✅ **Tiempo real** con Firestore
- ✅ **Gestión de prioridades** (Alta, Media, Baja)
- ✅ **Estados de tareas** (Pendiente, En Progreso, Completada)
- ✅ **Fechas límite** para cada tarea
- ✅ **Interfaz moderna** con Material Design 3
- ✅ **Arquitectura MVVM** profesional

---

## ✨ Características

### 🔐 Autenticación
- Registro de nuevos usuarios
- Inicio de sesión
- Cierre de sesión
- Validación de email y contraseña
- Manejo de errores de autenticación

### 📋 Gestión de Tareas
- Crear tareas con título, descripción, prioridad y fecha límite
- Ver todas las tareas en tiempo real
- Editar cualquier campo de una tarea
- Eliminar tareas con confirmación
- Marcar tareas como completadas
- Filtrado por usuario autenticado

### 🎨 Interfaz de Usuario
- Diseño moderno con Jetpack Compose
- Material Design 3
- Tema claro y oscuro
- Animaciones fluidas
- Responsive design
- Componentes reutilizables

---

## 🛠 Tecnologías Utilizadas

### Core
- **Kotlin** 1.9.20
- **Jetpack Compose** BOM 2023.10.01
- **Material 3** - Material Design 3
- **Android SDK** 24-34

### Firebase
- **Firebase Authentication** - Autenticación de usuarios
- **Cloud Firestore** - Base de datos en tiempo real
- **Firebase BOM** 32.6.0

### Arquitectura y Patrones
- **MVVM** (Model-View-ViewModel)
- **Repository Pattern**
- **Use Cases** (Clean Architecture)
- **Dependency Injection** - Hilt 2.48

### Componentes de Jetpack
- **Navigation Compose** - Navegación
- **ViewModel** - Gestión de estado
- **Lifecycle** - Ciclo de vida
- **Coroutines** - Programación asíncrona
- **Flow** - Flujos reactivos

---

## 🏗 Arquitectura

El proyecto sigue la arquitectura **MVVM (Model-View-ViewModel)** con Clean Architecture:

```
┌─────────────────────────────────────────────────────────────┐
│                        PRESENTATION LAYER                    │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │   Screens    │  │  ViewModels  │  │   UI States  │      │
│  │  (Compose)   │◄─│   (Logic)    │◄─│   (State)    │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
                           ▲
                           │
┌─────────────────────────────────────────────────────────────┐
│                         DOMAIN LAYER                         │
│  ┌──────────────┐  ┌──────────────────────────────┐         │
│  │  Use Cases   │  │  Repository Interfaces       │         │
│  │ (Business)   │  │  (Contracts)                 │         │
│  └──────────────┘  └──────────────────────────────┘         │
└─────────────────────────────────────────────────────────────┘
                           ▲
                           │
┌─────────────────────────────────────────────────────────────┐
│                          DATA LAYER                          │
│  ┌──────────────┐  ┌──────────────┐  ┌──────────────┐      │
│  │ Repositories │  │    Models    │  │   Firebase   │      │
│  │    (Impl)    │──│   (Entities) │──│  (Remote DB) │      │
│  └──────────────┘  └──────────────┘  └──────────────┘      │
└─────────────────────────────────────────────────────────────┘
```

### Capas:

1. **Presentation Layer**: UI con Compose, ViewModels, UI States
2. **Domain Layer**: Use Cases, Repository Interfaces
3. **Data Layer**: Repository Implementations, Models, Firebase

---

## 📥 Instalación

### Prerrequisitos

- Android Studio Hedgehog o superior
- JDK 17
- Cuenta de Firebase
- Dispositivo Android o Emulador (API 24+)

### Pasos de Instalación

1. **Clonar o descargar el proyecto**
   ```bash
   git clone https://github.com/gsoto02/TaskManagerPro.git
   cd TaskManagerPro
   ```

2. **Abrir en Android Studio**
   - File > Open
   - Seleccionar la carpeta del proyecto

3. **Configurar Firebase** (Ver sección siguiente)

4. **Sincronizar Gradle**
   - File > Sync Project with Gradle Files

5. **Ejecutar la aplicación**
   - Run > Run 'app'

---

## 🔥 Configuración de Firebase

### 1. Crear Proyecto en Firebase

1. Ir a [Firebase Console](https://console.firebase.google.com/)
2. Crear nuevo proyecto o usar existente
3. Agregar aplicación Android

### 2. Configurar la Aplicación

**Nombre del paquete:** `com.tecsup.taskmanagerpro`

### 3. Descargar google-services.json

1. En Firebase Console, descargar `google-services.json`
2. Colocar en: `TaskManagerPro_ev/app/google-services.json`

### 4. Habilitar Authentication

1. En Firebase Console > Authentication
2. Sign-in method > Email/Password
3. Habilitar

### 5. Crear Firestore Database

1. En Firebase Console > Firestore Database
2. Crear base de datos
3. Seleccionar modo de prueba
4. Elegir ubicación

### 6. Reglas de Seguridad de Firestore (Recomendadas)

```javascript
rules_version = '2';
service cloud.firestore {
  match /databases/{database}/documents {
    match /tasks/{taskId} {
      allow read, write: if request.auth != null && 
                            request.auth.uid == resource.data.userId;
      allow create: if request.auth != null;
    }
  }
}
```

---

## 📁 Estructura del Proyecto

```
TaskManagerPro_ev/
├── app/
│   ├── src/
│   │   └── main/
│   │       ├── java/com/tecsup/taskmanagerpro/
│   │       │   ├── data/
│   │       │   │   ├── model/
│   │       │   │   │   ├── Task.kt
│   │       │   │   │   ├── User.kt
│   │       │   │   │   ├── TaskPriority.kt
│   │       │   │   │   └── TaskStatus.kt
│   │       │   │   └── repository/
│   │       │   │       ├── AuthRepositoryImpl.kt
│   │       │   │       └── TaskRepositoryImpl.kt
│   │       │   ├── domain/
│   │       │   │   ├── repository/
│   │       │   │   │   ├── AuthRepository.kt
│   │       │   │   │   └── TaskRepository.kt
│   │       │   │   └── usecase/
│   │       │   │       ├── LoginUseCase.kt
│   │       │   │       ├── RegisterUseCase.kt
│   │       │   │       ├── GetTasksUseCase.kt
│   │       │   │       ├── CreateTaskUseCase.kt
│   │       │   │       ├── UpdateTaskUseCase.kt
│   │       │   │       └── DeleteTaskUseCase.kt
│   │       │   ├── ui/
│   │       │   │   ├── auth/
│   │       │   │   │   ├── login/
│   │       │   │   │   │   ├── LoginScreen.kt
│   │       │   │   │   │   ├── LoginViewModel.kt
│   │       │   │   │   │   └── LoginUiState.kt
│   │       │   │   │   └── register/
│   │       │   │   │       ├── RegisterScreen.kt
│   │       │   │   │       ├── RegisterViewModel.kt
│   │       │   │   │       └── RegisterUiState.kt
│   │       │   │   ├── tasks/
│   │       │   │   │   ├── list/
│   │       │   │   │   ├── create/
│   │       │   │   │   └── edit/
│   │       │   │   ├── components/
│   │       │   │   │   ├── TaskCard.kt
│   │       │   │   │   ├── CustomTextField.kt
│   │       │   │   │   ├── PriorityChip.kt
│   │       │   │   │   └── LoadingDialog.kt
│   │       │   │   ├── navigation/
│   │       │   │   │   ├── AppNavigation.kt
│   │       │   │   │   └── Screen.kt
│   │       │   │   └── theme/
│   │       │   │       ├── Color.kt
│   │       │   │       ├── Theme.kt
│   │       │   │       └── Type.kt
│   │       │   ├── di/
│   │       │   │   ├── AppModule.kt
│   │       │   │   └── FirebaseModule.kt
│   │       │   ├── MainActivity.kt
│   │       │   └── TaskManagerApp.kt
│   │       ├── res/
│   │       │   └── values/
│   │       │       ├── strings.xml
│   │       │       ├── colors.xml
│   │       │       └── themes.xml
│   │       └── AndroidManifest.xml
│   ├── build.gradle.kts
│   └── google-services.json
├── build.gradle.kts
├── settings.gradle.kts
├── gradle.properties
├── conceptos.txt
├── IA_EXPLICACION.txt
└── README.md
```

---

## 🎮 Uso de la Aplicación

### 1. Registro e Inicio de Sesión

1. **Primera vez:**
   - Abrir la app
   - Hacer clic en "Regístrate aquí"
   - Ingresar email y contraseña
   - Confirmar contraseña
   - Presionar "Registrarse"

2. **Usuario existente:**
   - Ingresar email y contraseña
   - Presionar "Iniciar Sesión"

### 2. Crear Tarea

1. En la pantalla principal, presionar el botón "+"
2. Completar:
   - Título (obligatorio)
   - Descripción (opcional)
   - Prioridad (Alta, Media, Baja)
   - Estado (Pendiente, En Progreso, Completada)
   - Fecha límite
3. Presionar "Crear Tarea"

### 3. Editar Tarea

1. En una tarea, presionar el ícono de editar ✏️
2. Modificar los campos deseados
3. Presionar "Actualizar Tarea"

### 4. Eliminar Tarea

1. En una tarea, presionar el ícono de eliminar 🗑️
2. Confirmar la eliminación

### 5. Marcar como Completada

1. Presionar el círculo ⭕ al lado del título
2. La tarea se marcará como completada ✅

### 6. Cerrar Sesión

1. Presionar el ícono de salida en la barra superior
2. La sesión se cerrará

---

## 📦 Entregables

### 1. Proyecto Completo
- Código fuente completo en ZIP
- Archivos de configuración
- google-services.json

### 2. Documentación
- **conceptos.txt**: Explicación detallada de MVVM, Firebase, Firestore, Compose y CRUD
- **IA_EXPLICACION.txt**: Documento sobre el uso responsable de IA en el desarrollo
- **README.md**: Este archivo

### 3. Cumplimiento de Rúbrica (20 puntos)

| Criterio | Puntos | Estado |
|----------|---------|---------|
| Autenticación Firebase Auth | 3 | ✅ |
| UI con Jetpack Compose | 3 | ✅ |
| CRUD Completo (Crear, Leer, Actualizar, Eliminar) | 5 | ✅ |
| Arquitectura MVVM | 3 | ✅ |
| Trabajo en pareja | 2 | ✅ |
| conceptos.txt | 2 | ✅ |
| IA_EXPLICACION.txt | 2 | ✅ |
| **TOTAL** | **20** | **✅** |

---

## 👥 Autores

- **Estudiante 1:** Alex Luis Casapaico Aquino
- **Estudiante 2:** Gabriela Soto Huaman

**Institución:** TECSUP  
**Curso:** Desarrollo de Aplicaciones Móviles  
**Fecha:** Diciembre 2025

---

## 📄 Licencia

Este proyecto fue desarrollado con fines educativos para el curso de Desarrollo de Aplicaciones Móviles.

---

## 🙏 Agradecimientos

- A los profesores del curso por su guía
- A Firebase por proporcionar servicios gratuitos
- A la comunidad de Android Developers
- A las herramientas de IA que ayudaron en el aprendizaje

---

## 📞 Soporte

Para preguntas o problemas:
- Email: alex.casapaico@tecsup.edu.pe
- Repositorio: https://github.com/gsoto02/TaskManagerPro.git

---

**¡Gracias por revisar TaskManager Pro!** 🎉
