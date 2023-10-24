package com.example.crypto.ui.screens

import android.content.Context
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.datastore.dataStore
import com.example.crypto.ui.UserSettings
import com.example.crypto.ui.manager.CryptoManager
import com.example.crypto.ui.serializer.UserSettingSerializer
import com.example.crypto.ui.theme.CryptoTheme
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch

class LoginActivity : ComponentActivity() {

    private val Context.dataStore by dataStore(
        fileName = "usersettings.json",
        serializer = UserSettingSerializer(CryptoManager())
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CryptoTheme {
                var username by remember {
                    mutableStateOf("")
                }

                var password by remember {
                    mutableStateOf("")
                }

                var savedMessage by remember {
                    mutableStateOf(UserSettings())
                }

                val scope = rememberCoroutineScope()

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    TextField(value = username,
                        onValueChange = {
                            username = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Username")
                        })
                    Spacer(modifier = Modifier.height(10.dp))
                    TextField(value = password,
                        onValueChange = {
                            password = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Password")
                        })
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Button(modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(5.dp),
                            onClick = {
                                scope.launch {
                                    dataStore.updateData {
                                        UserSettings(
                                            username, password
                                        )
                                    }
                                }
                            }) {
                            Text(text = "Save")
                        }
                        Button(modifier = Modifier
                            .fillMaxWidth(1f)
                            .padding(5.dp),
                            onClick = {
                                scope.launch {
                                    savedMessage = dataStore.data.first()
                                }
                            }) {
                            Text(text = "Load")
                        }
                    }
                    Text(text = savedMessage.toString())
                }
            }
        }
    }
}