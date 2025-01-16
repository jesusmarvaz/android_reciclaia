# Android Recicla IA

Código fuente de la aplicación nativa ReciclaIA para Android.

En este documento se explicará la implementación del proyecto, detallando en la medida de lo posible las técnicas y patrones de diseño, tecnologías y configuración y uso de las principales tecnologías, frameworks y librerías.

## 1. Configuración del proyecto

| **Característica**                | Valor                                                                    |
| --------------------------------- | ------------------------------------------------------------------------ |
| **Versión de Android Studio**     | Android Studio Ladybug - 2024.2.1 Patch 1                                |
| **Gradle**                        | 8.9 (`gradle-wrapper.properties`)                                        |
| AGP (Android Gradle Plugin)       | `8.7.3`                                                                  |
| **Lenguaje configuración Gradle** | Kotlin (Groovy está en vías de desaparecer), ficheros `build.gradle.kts` |
| **Lenguaje código fuente**        | Kotlin `2.0.0`                                                           |
| **Plugins**                       | Jetpack View Binding, Jetpack Compose                                             |
| Dependencias adicionales          | Jetpack-lifecycle-viewmodel, jetpack-navigation, jetpack-room, dagger-hilt            |
| Gestor de dependencias            | Version catalog (archivo toml)                                           |
| Procesador de anotaciones         | ksp (en lugar de kapt)                                                   |
| Versión del compilador            | Kotlin K2                                                                |

### 1.1 Versiones de Gradle y AGP

![gradle](/media/02_gradle_versions.png)

### 1.2 Sistema de gestor de dependencias - Version catalog

- Este nuevo sistema se ha adoptado para este proyecto, facilita el control de versiones desde un único fichero `libs.versions.toml`. 
- La declaración de dependencias en el fichero `build.gradle.kts` se simplifica

[Información oficial](https://developer.android.com/build/migrate-to-catalogs)

![alt text](/media/04_toml.png)
![alt text](/media/03_toml.png)

### 1.3 Procesado de anotaciones ksp

- El plugin de procesado de anotaciones en el código KSP en sustitución de KAPT, mejora el rendimiento.
- En las nuevas versiones del IDE Android Studio, se incluye por defecto, de manera que no hay que configurar nada.
- A la hora de utilizarlo:

```kotlin
dependencies {
    kapt("androidx.room:room-compiler:2.5.0") //Obsoleto Kapt
    ksp("androidx.room:room-compiler:2.5.0") //KSP
}
```

### 1.4 Versión del compilador K2 y Kotlin 2

![alt text](/media/05_k2.png)

Esta nueva versión del compilador está en fase beta, no obstante, lo probaré y documentaré posibles incompatibilidades, en ese caso se desactivará para este proyecto.

### 1.5 Uso de Jetpack Compose en el proyecto

Las vistas se generarán principalmente de la manerá clásica basadas en `View` y XML. Sin embargo, y debido a la popularidad del framework de UI Compose, se harán integraciones puntuales para estudiar sus ventajas y que sirva este proyecto para ilustrar sus características.

#### ¿Cómo configurar Jetpack Compose en nuestro proyecto?

Al iniciar un proyecto nuevo con la versión del IDE siguiente `Android Studio Ladybug | 2024.2.1 Patch 1` ya se autoconfigura para usar el framework Jetpack Compose. Además con el uso de Kotlin 2, la configuración de Compose se simplifica, al coincidir el número de versión de Kotlin con el del framework. En este caso se configura mediante el plugin de Gradle del Compilador de Compose:

En `libs.versions.toml`:

```toml
[versions]
kotlin = "2.0.0"

[plugins]
org-jetbrains-kotlin-android = { id = "org.jetbrains.kotlin.android", version.ref = "kotlin" }

// Add this line
compose-compiler = { id = "org.jetbrains.kotlin.plugin.compose", version.ref = "kotlin" }
```

En `build.gradle.kts` (nivel proyecto):

```kotlin
plugins {
    alias(libs.plugins.android.application) apply false
    alias(libs.plugins.kotlin.android) apply false
    alias(libs.plugins.kotlin.compose) apply false
}
```

En `build.gradle.kts` (nivel módulo o aplicación):

```kotlin
android {
    ...
    buildFeatures {
        compose = true
        viewBinding = true
    }
}
plugins {
   // Existing plugins
   alias(libs.plugins.compose.compiler)
}
```

[Más información oficial](https://developer.android.com/jetpack/androidx/releases/compose-kotlin)

#### ¿Cómo agregar una vista de Compose en una vista basada en `View`?

[Información oficial](https://developer.android.com/develop/ui/compose/migrate/interoperability-apis/compose-in-views#compose-in-fragments)

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent">

  <TextView
      android:id="@+id/text"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content" />

  <androidx.compose.ui.platform.ComposeView
      android:id="@+id/compose_view"
      android:layout_width="match_parent"
      android:layout_height="match_parent" />
</LinearLayout>
```

```kotlin
class ExampleFragmentXml : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val view = inflater.inflate(R.layout.fragment_example, container, false)
        val composeView = view.findViewById<ComposeView>(R.id.compose_view)
        composeView.apply {
            // Dispose of the Composition when the view's LifecycleOwner
            // is destroyed
            setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
            setContent {
                // In Compose world
                MaterialTheme {
                    Text("Hello Compose!")
                }
            }
        }
        return view
    }
}
```
### 1.6 Configuración de Jetpack Navigation

La navegación principal entre pantallas de la aplicación se hará utilizando el framework **Jetpack Navigation** con una única actividad que contendrá un objeto `FragmentContainerView` donde se dibujará todas las pantallas principales de la aplicación mediante distintos fragmentos `Fragment`.

#### Configuración de la actividad principal

Al crear un nuevo proyecto, el IDE Android Studio ya configura por defecto una composición de las vistas con el framework **Jetpack Compose**, y si bien en nuestro proyecto diseñaremos algunas vistas así, el diseño principal seguirá siendo con `View`. En este caso vamos a cambiar la clase principal generada para ser utilizada con Compose, para ello debemos hacer que la actividad principal herede de la clase `AppCompatActivity` en lugar de `ComponentActivity`:

Fichero `build.gradle.kts`:
```kotlin
dependencies {
    ...
    //custom
    implementation(libs.androidx.appcompat)
}
```

Fichero `libs.versions.toml`:
```toml
[versions]
...
appcompat = "1.7.0"
navigationUiKtx = "2.8.5"
safeArgs = "2.8.5"


[libraries]
...
androidx-appcompat = { module = "androidx.appcompat:appcompat", version.ref = "appcompat" }
androidx-navigation-dynamic-features-fragment = { module = "androidx.navigation:navigation-dynamic-features-fragment", version.ref = "navigationUiKtx" }
androidx-navigation-fragment-ktx = { module = "androidx.navigation:navigation-fragment-ktx", version.ref = "navigationUiKtx" }
androidx-navigation-ui-ktx = { module = "androidx.navigation:navigation-ui-ktx", version.ref = "navigationUiKtx" }

[plugins]
...
safeargs = { id = "androidx.navigation.safeargs.kotlin", version.ref = "safeArgs"}
```

Actividad configurada para **Compose**:
```kotlin
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //enableEdgeToEdge()
        setContent {
            ReciclaIATheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}
```
Actividad configurada con **View** y **View Binding**:
```kotlin
class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }
}
```



### 1.7 Inyección de dependencias con Hilt

[Documentación oficial](https://developer.android.com/training/dependency-injection/hilt-android#kts)

Se trata de la librería oficial para la inyección de dependencias de Android.

#### Agregar las dependencias al proyecto Android
