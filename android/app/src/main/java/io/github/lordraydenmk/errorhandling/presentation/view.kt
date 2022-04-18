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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel

@Composable
fun SignUpScreen(viewModel: SignUpViewModel = viewModel()) {
    val state = viewModel.state.collectAsState()

    SignUpContent(
        state.value,
        onSubmit = viewModel::onSubmit,
        onNameChange = viewModel::onNameChange,
        onIdChange = viewModel::onIdChange,
        onSwitchId = viewModel::onSwitchId
    )
}

@Composable
fun SignUpContent(
    viewState: ViewState,
    onSubmit: (String, String) -> Unit,
    onNameChange: (String) -> Unit,
    onIdChange: (String) -> Unit,
    onSwitchId: (IdType) -> Unit
) {
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
                value = viewState.name.value,
                onValueChange = { onNameChange(it) },
                isError = viewState.name.error != null
            )
            viewState.name.error?.let { error ->
                Text(text = error, color = Color.Red)
            }
            OutlinedTextField(
                label = { Text(text = viewState.idLabel) },
                value = viewState.id.value,
                onValueChange = { onIdChange(it) },
                isError = viewState.id.error != null
            )
            viewState.id.error?.let { error ->
                Text(text = error, color = Color.Red)
            }

            OutlinedButton(onClick = { onSwitchId(viewState.idType) }) {
                Text(text = viewState.switchButtonLabel)
            }

            Button(
                onClick = { onSubmit(viewState.name.value, viewState.id.value) },
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
        {},
        {},
        {}
    )
}