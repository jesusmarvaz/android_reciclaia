# Android Recicla IA

Código fuente de la aplicación nativa ReciclaIA para Android.

En este documento se explicará la implementación del proyecto, detallando en la medida de lo posible las técnicas y patrones de diseño, tecnologías y configuración y uso de las principales tecnologías, frameworks y librerías.

## 1 Configuración del proyecto

| **Característica**                | Valor                                                                    |
| --------------------------------- | ------------------------------------------------------------------------ |
| **Versión de Android Studio**     | Android Studio Ladybug - 2024.2.1 Patch 1                                |
| **Gradle**                        | 8.10.2 ( `gradle-wrapper.properties` )                                        |
| AGP (Android Gradle Plugin)       | `8.8.0` |
| **Lenguaje configuración Gradle** | Kotlin (Groovy está en vías de desaparecer), ficheros `build.gradle.kts` |
| **Lenguaje código fuente**        | Kotlin `2.0.0` |
| **Plugins**                       | Jetpack View Binding, Jetpack Compose                                             |
| Dependencias adicionales          | Jetpack-lifecycle-viewmodel, jetpack-navigation, jetpack-room, dagger-hilt            |
| Gestor de dependencias            | Version catalog (archivo toml)                                           |
| Procesador de anotaciones         | ksp (en lugar de kapt)                                                   |
| Versión del compilador            | Kotlin K2                                                                |

### 1.1 Versiones de Gradle y AGP

![gradle](/media/02_gradle_versions.png)

### 1.2 Sistema de gestor de dependencias - Version catalog

* Este nuevo sistema se ha adoptado para este proyecto, facilita el control de versiones desde un único fichero `libs.versions.toml`. 
* La declaración de dependencias en el fichero `build.gradle.kts` se simplifica

[Información oficial](https://developer.android.com/build/migrate-to-catalogs)

![alt text](/media/04_toml.png)

![alt text](/media/03_toml.png)

### 1.3 Procesado de anotaciones ksp

* El plugin de procesado de anotaciones en el código KSP en sustitución de KAPT, mejora el rendimiento.
* En las nuevas versiones del IDE Android Studio, se incluye por defecto, de manera que no hay que configurar nada.
* A la hora de utilizarlo:

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

En `libs.versions.toml` :

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

#### ¿Cómo agregar una vista de Compose en una vista basada en `View` ?

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

## 2 Jetpack Navigation

La navegación principal entre pantallas de la aplicación se hará utilizando el framework **Jetpack Navigation** con una única actividad que contendrá un objeto `FragmentContainerView` donde se dibujará todas las pantallas principales de la aplicación mediante distintos fragmentos `Fragment` .

### 2.1 Configuración de la actividad principal

Al crear un nuevo proyecto, el IDE Android Studio ya configura por defecto una composición de las vistas con el framework **Jetpack Compose**, y si bien en nuestro proyecto diseñaremos algunas vistas así, el diseño principal seguirá siendo con `View` . En este caso vamos a cambiar la clase principal generada para ser utilizada con Compose, para ello debemos hacer que la actividad principal herede de la clase `AppCompatActivity` en lugar de `ComponentActivity` :

Fichero `build.gradle.kts` :

```kotlin
dependencies {
    ...
    //custom
    implementation(libs.androidx.appcompat)
}
```

Fichero `libs.versions.toml` :

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

**Actividad configurada para *Compose*:**

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

**Actividad configurada con *View* y *View Binding*:**

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

**Elección del tema del sistema correcto**

La aplicación dara un error de runtime si no elegimos un tema que herede de `Theme.AppCompat` , pero al iniciar el proyecto generó uno que hereda de Theme:

Tema no compatible con `AppCompat` :

```xml
<?xml version="1.0" encoding="utf-8"?>
<resources>

    <style name="Theme.MyApplication" parent="android:Theme.Material.Light.NoActionBar" />
</resources>
```

Tema que sí hereda de `Theme.AppCompat` :

```xml
<resources>
    <style name="Theme.ReciclaIA" parent="Theme.MaterialComponents.DayNight.NoActionBar" />
</resources>
```

Al configurar las vistas con View partiendo de un proyecto preconfigurado para Compose, tenemos que hacer todo el trabajo manualmente, existen dos nuevos problemas que solucionar: 
1. Las vistas no evitan la zona de la cámara selfie, u otros elementos de de tipo borde o del sistema.
1. Se configura en fullscreen o sin la barra de estado visible

![caso1](/media/07_fitsystemwindows,false.png)

Solución al caso 1: 
Debemos configurar el atributo del sistema `fitsSystemWindows` a `true` en el layout principal de la aplicación, normalmente el layout padre de `activity_main.xml` :

```xml
<attr name="fitsSystemWindows" format="boolean" />
```

Uso en el layout de la actividad principal `activity_main.xml`:

```xml
<LinearLayout xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    xmlns:tools="http://schemas.android.com/tools"
    android:orientation="vertical"
    android:layout_width="match_parent"
    android:layout_height="match_parent"
    android:fitsSystemWindows="true">...</LinearLayout>
```
![solucion1](/media/06_darkstatusbar.png)

Solución al caso 2: 
Aparentemente la barra de estado no aparece, nada más lejos de la realidad, es transparente con texto e iconos blancos, debemos ajustar el tema para que sea adecuado según el fondo de la vista, manteniendo el fondo transparente (tendendia de Material3) usaremos la propiedad `android:windowLightStatusBar`:
```xml
<resources>
    <style name="Theme.ReciclaIA" parent="Theme.MaterialComponents.DayNight.NoActionBar">
        <item name="android:windowLightStatusBar">true</item>
    </style>
</resources>
```

Tema utilizando Material 3 para utilización de colores dinámicos (se explicará más adelante):
```xml
    <style name="Theme.ReciclaIA" parent="Theme.Material3.DayNight.NoActionBar">
        <item name="android:windowLightStatusBar">true</item>
    </style>
</resources>
```

![solucion2](/media/08_fitsystemwindows,true%20and%20lightstatusbar.png)

- Nota: Puede resultar confuso que el estado `windowLightStatusBar:true` ofrezca el texto en oscuro y viceversa, pero es correcto, pues el color del texto es el de contraste con el fondo claro, que es lo que aplica en esta configuración.
___

La aplicación tendrá una única actividad (AppCompatActivity) y las distintas pantallas de la aplicación se implementarán con fragmentos (Fragment), cuya navegación se gestionará con el mencionado framework Jetpack Navigation, así tendremos este componente en la actividad principal:

```xml
    <androidx.fragment.app.FragmentContainerView
        android:id="@+id/hostNavFragment"
        android:name="androidx.navigation.fragment.NavHostFragment"
        android:layout_width="match_parent"
        android:layout_height="match_parent"
        app:defaultNavHost="true"
        app:navGraph="@navigation/host"
        tools:layout_editor_absoluteX="1dp"
        tools:layout_editor_absoluteY="1dp" />
```

Una vez que la actividad principal esté preparada para trabajar con View, lo siguiente es continuar con la configuración para funcionar con Jetpack Navigation, los pasos que he seguido los describo a continuación.

### 2.2 Creación de un par de fragmentos para probar la navegación

Voy a crear un par de Fragmentos sencillos para probar que la navegación funciona correctamente. 

![alt text](media/10_fragments_test.png)

### 2.3 Creación del gráfico de navegación

Es necesario crear un fichero xml en la carpeta `navigation` que llamaremos `host.xml`:
- Creación de la carpeta
- Click derecho sobre carpeta y elegir *new Navigation File*

![host](/media/09_host.png)

La edición del gráfico de navegación, al igual que las vistas usando View, se puede realizar gráficamente o editando el xml. Lo más comodo es usar la parte gráfica y editar alguna característica concreta usando el xml cuando sea necesario.

![alt text](media/11_navitation_graph.png)

### 2.4 Realización de una navegación sencilla | **Conceptos tratados**: *[herencia](https://es.wikipedia.org/wiki/Herencia_(inform%C3%A1tica))*

Llegados a este punto, vamos a navegar desde un fragmento hacia el otro y viceversa. Voy a crear una clase abstracta de la que heredarán la mayoría de nuestras vistas, así podemos agregar funcionalidad común, se llamará `FragmentBase`:

```kotlin
abstract class FragmentBase : Fragment(), ILog {
    abstract fun goBack()
    abstract fun getFragmentTag(): String
}
...

interface ILog {
    fun logDebug(message: String) = Log.d(theTag(), message)
    fun logError(message: String) = Log.e(theTag(), message)
    fun theTag(): String
}
```

Para navegar entre fragmentos, usaremos la funcionalidad de navegación segura gracias al plugin **Safe Args** `androidx.navigation.safeargs.kotlin`:

```kotlin
 binding.textViewOrigen.setOnClickListener {
            findNavController()
                .navigate(FragmentInitialDirections.actionFragmentInitialToFragmentInitial2())
        }
```

Centrémonos ahora en el método `goBack()`:

```kotlin
class FragmentInitial2 : FragmentBase() {
    private lateinit var binding: FragmentInitial2Binding
    
    override fun goBack() {
        findNavController().popBackStack()
    }
    
    override fun initProperties() {
        binding.textViewDestino.setOnClickListener {
            goBack()
        }
    }
```

Funcionamiento correcto.

### 2.5 Configuración avanzada del botón atrás | **Conceptos tratados**: *[recursión](https://es.wikipedia.org/wiki/Recursi%C3%B3n), [herencia](https://es.wikipedia.org/wiki/Herencia_(inform%C3%A1tica)), [Principio de inversión de la dependencia (S.O.L.I.**D.**)](https://es.wikipedia.org/wiki/SOLID)*

Con el siguiente desarrollo se predende interceptar la acción del botón atrás del dispositivo, para realizar la navegación que convenga en cada pantalla de la aplicación. De manera general con el gráfico de navegación es suficiente, pero en algunas pantallas es posible que queramos capturar esa acción para por ejemplo solicitar una confirmación del usuario o realizar otras acciones antes de volver atrás. Esto **lo capturaremos en la actividad principal, MainActivity**, que sabemos que dada nuestra configuración de navegación siempre o casi siempre (puede que se abran algunas otras actividades, ya se verá más adelante):

```kotlin
//En MainActivity
    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            //acción a configurar
            return true
        }
        return false
    }
```

Ahora debemos diseñar un mecanismo para que se llame al método `goBack()` que hemos creado antes en la clase abstracta de la que heredarán todos los fragmentos, `FragmentBase`. Analizando el funcionamiento general de los fragmentos en Android, nos damos cuenta de que puede darse una estructura tipo árbol, con fragmentos hijos dentro de un padre. Además, debemos hallar el fragmento "activo" y entonces llamar a su método `goBack()` (Nótese que se tendrá que comprobar que el fragmento sea del tipo `FragmentBase`).

![nested fragments](/media/12_nested_fragments.png)

Vamos a crear una interfaz (más adelante desarrollaremos la implementación) que contenga el método que necesitamos, de la siguiente manera:

```kotlin
interface IBackPressedListener {
    fun handleBackPressed(a: AppCompatActivity, idHostFragment: Int)
}
object BackPressedListener : IBackPressedListener {
    override fun handleBackPressed(a: AppCompatActivity, idHostFragment: Int) {
        TODO("Not yet implemented")
    }
}
...
// En MainActivity
    private val backPressedListener: IBackPressedListener = BackPressedListener

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if(keyCode == KeyEvent.KEYCODE_BACK) {
            backPressedListener.handleBackPressed(this, binding.hostNavFragment.id)
            return true
        }
        return false
    }

```

#### Recorriendo el árbol de fragmentos anidados | **Conceptos tratados:** *[recursión](https://es.wikipedia.org/wiki/Recursi%C3%B3n)*





## 3 Inyección de dependencias con Hilt

[Documentación oficial](https://developer.android.com/training/dependency-injection/hilt-android#kts)

Se trata de la librería oficial para la inyección de dependencias de Android.


### 3.1 Agregar las dependencias al proyecto Android


# TO DO

- Documentar `<style name="Theme.ReciclaIA" parent="Theme.Material3.DayNight.NoActionBar">` Para colores dinámicos https://codelabs.developers.google.com/codelabs/apply-dynamic-color?hl=es-419#0


