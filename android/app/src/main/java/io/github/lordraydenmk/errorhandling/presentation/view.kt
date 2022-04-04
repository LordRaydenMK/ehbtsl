package io.github.lordraydenmk.errorhandling.presentation

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.OutlinedButton
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel


@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    val state = viewModel.state.collectAsState()

    SignUpContent(state.value, viewModel::onSubmit, viewModel::onSwitchId)
}

@Composable
fun SignUpContent(
    viewState: ViewState,
    onSubmit: (String, String) -> Unit,
    onSwitchId: (IdType) -> Unit
) {
    var name by rememberSaveable { mutableStateOf(viewState.name.value) }
    var id by rememberSaveable { mutableStateOf(viewState.id.value) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        if (viewState.showProgress) {
            CircularProgressIndicator()
        } else {
            viewState.error?.let { error ->
                Text(text = error, color = Color.Red, modifier = Modifier.padding(8.dp))
            }
            OutlinedTextField(
                label = { Text(text = viewState.nameLabel) },
                value = name,
                onValueChange = { name = it }
            )
            viewState.name.error?.let { error ->
                Text(text = error, color = Color.Red)
            }
            OutlinedTextField(
                label = { Text(text = viewState.idLabel) },
                value = id,
                onValueChange = { id = it },
            )
            viewState.id.error?.let { error ->
                Text(text = error, color = Color.Red)
            }

            OutlinedButton(onClick = { onSwitchId(viewState.idType) }) {
                Text(text = viewState.switchButtonLabel)
            }

            Button(
                onClick = { onSubmit(name, id) },
                modifier = Modifier.padding(4.dp)
            ) {
                Text(text = "Submit")
            }
        }
    }
}

@Preview
@Composable
fun SignUpPreview() {
    SignUpContent(
        ViewState(
            false,
            null,
            IdType.EMAIL,
            FormField("Stojan"),
            FormField("qwerty", "Invalid email"),
        ),
        { _, _ -> },
        {}
    )
}