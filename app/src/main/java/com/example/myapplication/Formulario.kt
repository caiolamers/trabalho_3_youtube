package com.example.myapplication

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

private val CorErro = Color(0xFFFF6B6B)

/**
 * Campo de texto padronizado para os formulários do app (tema escuro).
 * Usado em Inscricoes, Ver_Canal e Video_Aberto.
 */
@Composable
fun CampoTexto(
    valor: String,
    onValorChange: (String) -> Unit,
    rotulo: String,
    modifier: Modifier = Modifier,
    singleLine: Boolean = true,
    erro: Boolean = false
) {
    OutlinedTextField(
        value = valor,
        onValueChange = onValorChange,
        label = { Text(rotulo) },
        singleLine = singleLine,
        isError = erro,
        modifier = modifier,
        colors = OutlinedTextFieldDefaults.colors(
            focusedTextColor = Color.White,
            unfocusedTextColor = Color.White,
            errorTextColor = Color.White,
            focusedBorderColor = Color.White,
            unfocusedBorderColor = Color.Gray,
            errorBorderColor = CorErro,
            focusedLabelColor = Color.White,
            unfocusedLabelColor = Color.LightGray,
            errorLabelColor = CorErro,
            cursorColor = Color.White,
            errorCursorColor = Color.White
        )
    )
}

/** Mensagem de validação exibida abaixo dos campos do formulário. */
@Composable
fun TextoErro(mensagem: String) {
    Text(
        text = mensagem,
        color = CorErro,
        style = MaterialTheme.typography.bodySmall,
        modifier = Modifier.padding(top = 2.dp)
    )
}
