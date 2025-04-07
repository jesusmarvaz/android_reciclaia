package com.ingencode.reciclaia.ui.screens.app.settings

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.ingencode.reciclaia.R
import com.ingencode.reciclaia.ui.components.Title
import com.ingencode.reciclaia.ui.theme.MyComposeWrapper

/**
Created with ❤ by jesusmarvaz on 2025-02-19.
 */

@Composable
fun SettingsScreen(){
    //val settingsViewModel: SettingsViewModel = viewModel()
    MyComposeWrapper { SettingsScreenContent(/*settingsViewModel*/) }
}

@Composable
fun SettingsScreenContent(/*viewModelSettings: SettingsViewModel*/) {
    val c = LocalContext.current

    Column(modifier = Modifier
        .fillMaxSize()
        .background(MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally) {
        Title(title = c.getString(R.string.settings))
        AppConfigurationSection()
        IaConfigurationSection()
        AppInfoSection()
    }
}

@Composable
fun TitleSettings(title: String) {
    Text(text = title,
        style = MaterialTheme.typography.titleMedium,
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 16.dp))
}

@Composable
fun AppConfigurationSection() {
    TitleSettings(stringResource(id = R.string.app_configuration))
    //AppConfigCard(viewModelSettings)
}

@Composable
fun IaConfigurationSection() {
    TitleSettings(stringResource(id = R.string.ia_configuration))
}

@Composable
fun AppInfoSection() {
    TitleSettings(stringResource(id = R.string.app_info))
}


/*@Composable
fun AppConfigCard() {
    var skipTutorialChecked by remember { mutableStateOf(viewModelSettings.getSkipTutorial()) }
    var startupComposeChecked by remember { mutableStateOf(!viewModelSettings.getIsViewVersion()) }
    var selectedTheme by remember { mutableStateOf(viewModelSettings.getThemeMode()) }
    var launchTutorial by remember { mutableStateOf(false) }
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
        shape = ShapeDefaults.Medium
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp) // Optional: Add spacing between columns
        ) {
            // Tutorial (Left Column)
            Column(
                modifier = Modifier.weight(1f), // Takes up half the space
                verticalArrangement = Arrangement.spacedBy(8.dp) // Optional: Spacing between elements
            ) {
                Text(
                    text = stringResource(R.string.start_settings),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the text
                )
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = !skipTutorialChecked,
                        onCheckedChange = { isChecked ->
                            skipTutorialChecked = !isChecked
                            viewModelSettings.setSkipTutorial(!isChecked)
                        }
                    )
                    Text(
                        text = stringResource(R.string.startup_tutorial),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Checkbox(
                        checked = startupComposeChecked,
                        enabled = skipTutorialChecked,
                        onCheckedChange = { isChecked ->
                            startupComposeChecked = isChecked
                            viewModelSettings.setSkipTutorial(isChecked)
                        }
                    )
                    Text(
                        text = stringResource(R.string.startup_compose),
                        fontSize = 11.sp,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                }


                Button(
                    onClick = { launchTutorial = true },
                    colors = ButtonDefaults.buttonColors(contentColor = MaterialTheme.colorScheme.primary),
                    modifier = Modifier.fillMaxWidth() // Make button fill the column width
                ) {
                    Text(text = stringResource(R.string.start_tutorial), fontSize = 11.sp)
                }
            }


            // Theme (Right Column)
            Column(
                modifier = Modifier.weight(1f), // Takes up half the space
                verticalArrangement = Arrangement.spacedBy(8.dp) // Optional: Spacing between elements
            ) {
                Text(
                    text = stringResource(R.string.app_theme),
                    fontWeight = FontWeight.Bold,
                    fontSize = 12.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally) // Center the text
                )

                val themes = listOf("system", "light", "dark")
                val themeStrings = listOf(R.string.system, R.string.light, R.string.dark)

                themes.forEachIndexed { index, theme ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        RadioButton(
                            selected = selectedTheme == theme,
                            onClick = {
                                selectedTheme = theme
                                viewModelSettings.setThemeMode(theme)
                            }
                        )
                        Text(
                            text = stringResource(themeStrings[index]),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(start = 8.dp)
                        )
                    }
                }
            }
        }
    }
}
 */