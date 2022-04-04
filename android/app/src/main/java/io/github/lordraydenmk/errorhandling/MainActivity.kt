package io.github.lordraydenmk.errorhandling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material.MaterialTheme
import io.github.lordraydenmk.errorhandling.presentation.SignUpScreen

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SignUpScreen()
            }
        }
    }
}