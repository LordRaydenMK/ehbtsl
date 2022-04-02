package io.github.lordraydenmk.errorhandling

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.material.MaterialTheme
import io.github.lordraydenmk.errorhandling.presentation.SignUpScreen
import io.github.lordraydenmk.errorhandling.presentation.SignUpViewModel

class MainActivity : ComponentActivity() {

    private val viewModel by viewModels<SignUpViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MaterialTheme {
                SignUpScreen(viewModel)
            }
        }
    }
}