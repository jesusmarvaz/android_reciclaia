package com.ingencode.reciclaia.ui.screens.app

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.compose.rememberNavController
import androidx.viewbinding.ViewBinding
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.databinding.FragmentAppComposeBinding
import com.ingencode.reciclaia.ui.components.FragmentBase
import com.ingencode.reciclaia.ui.navigation.compose.MyBottomNavigationBar
import com.ingencode.reciclaia.ui.navigation.compose.NavigationForBottomBar
import com.ingencode.reciclaia.ui.theme.MyComposeWrapper
import com.ingencode.reciclaia.ui.viewmodels.SettingsViewModel
import com.ingencode.reciclaia.utils.nameClass

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

class FragmentAppComposeVersion : FragmentBase() {
    private lateinit var binding: FragmentAppComposeBinding

    override fun goBack() = requireActivity().finish()
    override fun getFragmentTag(): String = this.nameClass

    override fun initProperties() {
        binding.appComposeVersion.setContent { MyComposeWrapper { AppInCompose() } }
    }

    override fun getInflatedViewBinding(): ViewBinding {
        binding = FragmentAppComposeBinding.inflate(layoutInflater)
        return binding
    }

    @Composable
    fun AppInCompose() {
        val navController = rememberNavController()
        val settingsViewModel: SettingsViewModel = hiltViewModel()
        val c = LocalContext.current
        Scaffold(
            topBar = {
                Text(
                    LocalContext.current.getString(R.string.compose_based_app),
                    style = MaterialTheme.typography.bodySmall,
                    modifier = Modifier
                        .background(
                            MaterialTheme.colorScheme.surfaceVariant
                        )
                        .fillMaxWidth()
                )
            },
            bottomBar = { MyBottomNavigationBar(navController) }) { padding ->
            Box(modifier = Modifier
                .padding(padding)
                .fillMaxSize()) {
                NavigationForBottomBar(navController = navController)
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 100.dp, horizontal = 16.dp),
                    //.weight(1f), // Este weight empuja el contenido hacia abajo
                    horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente el contenido dentro de este Column
                    verticalArrangement = Arrangement.Center // Centra verticalmente el contenido dentro de este Column
                ) {
                    // Texto en la mitad o un poco más arriba
                    Text(
                        text = c.getString(R.string.compose_sample_button_to_back_view),
                        modifier = Modifier.fillMaxWidth().weight(1f),
                    )

                    // Box o Column para el botón, empujado hacia el tercio inferior por el weight de arriba
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f), // Un weight menor para que no ocupe tanto espacio, ajusta según necesites
                        horizontalAlignment = Alignment.CenterHorizontally, // Centra horizontalmente el botón
                        verticalArrangement = Arrangement.Bottom // Alinea el botón en la parte inferior de su contenedor
                    ) {
                        Button(
                            onClick = { goView(settingsViewModel, c) },
                            modifier = Modifier.fillMaxWidth(0.8f) // Ajusta el ancho del botón si es necesario
                        ) {
                            Text(text = c.getString(R.string.view_based_app))
                        }
                    }

                    // Puedes eliminar el Box vacío si ya no lo necesitas para espaciar
                }
            }
        }
    }
}

private fun goView(s: SettingsViewModel, c: Context) {
    s.setIsViewVersion(true)
    val packageManager = c.packageManager
    val intent = packageManager.getLaunchIntentForPackage(c.packageName)

    if (intent != null) {
        // Añade flags para limpiar el stack de actividades y crear una nueva instancia
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
        c.startActivity(intent)

        // Opcional: Finaliza la actividad actual si quieres asegurarte de que no quede en segundo plano
        // Esto requiere acceso a la Activity, lo cual es más complejo desde un Composable.
        // Una alternativa sería pasar la Activity como parámetro, pero eso rompe el patrón de Composable.
        // Si necesitas esto, considera manejar la navegación/reinicios a un nivel superior (e.g., en tu Activity principal o NavHost).
    }
}