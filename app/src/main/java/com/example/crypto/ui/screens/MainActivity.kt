package com.example.crypto.ui.screens

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.crypto.ui.manager.CryptoManager
import com.example.crypto.ui.theme.CryptoTheme
import java.io.File
import java.io.FileInputStream
import java.io.FileOutputStream

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val cryptoManager = CryptoManager()
        setContent {
            CryptoTheme {
                var messageToEncrypt by remember {
                    mutableStateOf("")
                }

                var messageToDecrypt by remember {
                    mutableStateOf("")
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(32.dp)
                ) {
                    TextField(value = messageToEncrypt,
                        onValueChange = {
                            messageToEncrypt = it
                        },
                        modifier = Modifier.fillMaxWidth(),
                        placeholder = {
                            Text(text = "Encrypt string")
                        })
                    Spacer(modifier = Modifier.height(10.dp))
                    Row {
                        Button(modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(5.dp),
                            onClick = {
                                val bytes = messageToEncrypt.encodeToByteArray()
                                val file = File(filesDir, "secret.txt")
                                if (!file.exists()) {
                                    file.createNewFile()
                                }

                                val fos = FileOutputStream(file)
                                messageToDecrypt =
                                    cryptoManager.encrypt(bytes, fos).decodeToString()
                            }) {
                            Text(text = "Encrypt")
                        }
                        Button(modifier = Modifier
                            .fillMaxWidth(0.5f)
                            .padding(5.dp),
                            onClick = {
                                val file = File(filesDir, "Secret.txt")
                                messageToEncrypt = cryptoManager.decrypt(
                                    FileInputStream(file)
                                ).decodeToString()
                            }) {
                            Text(text = "Decrypt")
                        }
                    }
                    Text(text = messageToDecrypt)
                }
            }
        }
    }
}