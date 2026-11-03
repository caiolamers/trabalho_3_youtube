package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Class_Canal
import com.example.myapplication.data.repository.CanalRepository
import com.example.myapplication.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class CanalUiState(
    val canais: List<Class_Canal> = emptyList(),
    val mostrandoFormularioCanal: Boolean = false,
    val carregando: Boolean = true,
    val erro: String? = null,
    val nomeFormulario: String = "",
    val identificadorFormulario: String = "",
    val descricaoFormulario: String = ""
)

class CanalViewModel(private val repository: CanalRepository, private val videosRepository: VideoRepository): ViewModel() {
    private val _uiState = MutableStateFlow(CanalUiState())
    val uiState: StateFlow<CanalUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try { repository.buscarTodos().collect { itens ->
                _uiState.update { it.copy(canais = itens, carregando = false) }
            } } catch (e: Exception) { _uiState.update { it.copy(erro = e.message, carregando = false) } }
        }
    }
    fun mostrarFormulario(mostrar: Boolean) {
        _uiState.update { it.copy(mostrandoFormularioCanal = mostrar, nomeFormulario = "", identificadorFormulario = "", descricaoFormulario = "", erro = null) }
    }
    fun alterarNome(nome: String) { _uiState.update { it.copy(nomeFormulario = nome, erro = null) } }
    fun alterarIdentificador(identificador: String) { _uiState.update { it.copy(identificadorFormulario = identificador, erro = null) } }
    fun alterarDescricao(descricao: String) { _uiState.update { it.copy(descricaoFormulario = descricao) } }
    fun alternarInscricao(canal: Class_Canal) = viewModelScope.launch {
        repository.atualizar(canal.copy(inscrito = !canal.inscrito))
    }
    fun salvarCanal() {
        val estado = _uiState.value
        val nome = estado.nomeFormulario.trim()
        val identificador = estado.identificadorFormulario.trim()
        if (nome.isBlank() || identificador.isBlank()) {
            _uiState.update { it.copy(erro = "Preencha o nome e o identificador do canal.") }
            return
        }
        viewModelScope.launch {
            try {
                repository.inserir(Class_Canal(nome = nome, identificador = if (identificador.startsWith("@")) identificador else "@$identificador", inscritos = "0 inscritos", descricao = estado.descricaoFormulario.trim().ifBlank { "Sem descrição ainda." }, inscrito = true))
                mostrarFormulario(false)
            } catch (e: Exception) { _uiState.update { it.copy(erro = e.message ?: "Erro ao salvar canal") } }
        }
    }
    fun excluirCanal(canal: Class_Canal) = viewModelScope.launch {
        try {
            videosRepository.buscarPorCanalId(canal.id).forEach { videosRepository.deletar(it) }
            repository.deletar(canal)
        } catch (e: Exception) { _uiState.update { it.copy(erro = e.message ?: "Erro ao excluir canal") } }
    }
}
