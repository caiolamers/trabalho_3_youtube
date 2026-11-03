package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.myapplication.data.Class_video
import com.example.myapplication.data.repository.VideoRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class VideoUiState(
    val videos: List<Class_video> = emptyList(),
    val mostrandoFormularioVideo: Boolean = false,
    val carregando: Boolean = true,
    val erro: String? = null,
    val tituloFormulario: String = "",
    val visualizacoesFormulario: String = "",
    val curtidasFormulario: String = "",
    val descricaoFormulario: String = "",
    val comentarioFormulario: String = "",
    val tituloEdicao: String = "",
    val descricaoEdicao: String = "",
    val modoEdicao: Boolean = false
)

class VideoViewModel(private val repository: VideoRepository): ViewModel() {
    private val _uiState = MutableStateFlow(VideoUiState())
    val uiState: StateFlow<VideoUiState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            try { repository.buscarTodos().collect { itens ->
                _uiState.update { it.copy(videos = itens, carregando = false) }
            } } catch (e: Exception) { _uiState.update { it.copy(erro = e.message, carregando = false) } }
        }
    }
    fun mostrarFormulario(mostrar: Boolean) {
        _uiState.update { it.copy(mostrandoFormularioVideo = mostrar, tituloFormulario = "", visualizacoesFormulario = "", curtidasFormulario = "", descricaoFormulario = "", comentarioFormulario = "", erro = null) }
    }
    fun alterarTitulo(t: String) { _uiState.update { it.copy(tituloFormulario = t, erro = null) } }
    fun alterarVisualizacoes(t: String) { _uiState.update { it.copy(visualizacoesFormulario = t) } }
    fun alterarCurtidas(t: String) { _uiState.update { it.copy(curtidasFormulario = t) } }
    fun alterarDescricao(t: String) { _uiState.update { it.copy(descricaoFormulario = t) } }
    fun alterarComentario(t: String) { _uiState.update { it.copy(comentarioFormulario = t) } }
    fun salvarVideo(canalId: Int, canalNome: String) {
        val estado = _uiState.value
        if (estado.tituloFormulario.isBlank()) {
            _uiState.update { it.copy(erro = "Informe o título do vídeo.") }
            return
        }
        val views = estado.visualizacoesFormulario.trim()
        val cores = intArrayOf(0xFF6D7B8D.toInt(), 0xFF4A5560.toInt(), 0xFF1B3A6B.toInt(), 0xFF5B3A6B.toInt(), 0xFF2E5E4E.toInt(), 0xFF6B3A3A.toInt())
        val video = Class_video(
            titulo = estado.tituloFormulario.trim(), canal = canalNome,
            visualizacoes = when { views.isEmpty() -> "0 visualizações"; views.all { it.isDigit() } -> "$views visualizações"; else -> views },
            curtidas = estado.curtidasFormulario.trim().ifBlank { "0" },
            descricao = estado.descricaoFormulario.trim().ifBlank { "Sem descrição." },
            totalComentarios = 0,
            comentarioDestaque = estado.comentarioFormulario.trim().ifBlank { "Ainda não há comentários." },
            corMiniaturaArgb = cores[(System.currentTimeMillis() % cores.size).toInt()],
            canalId = canalId
        )
        viewModelScope.launch {
            try { repository.inserir(video); mostrarFormulario(false) }
            catch (e: Exception) { _uiState.update { it.copy(erro = e.message ?: "Erro ao salvar vídeo") } }
        }
    }
    fun iniciarEdicao(video: Class_video) { _uiState.update { it.copy(modoEdicao = true, tituloEdicao = video.titulo, descricaoEdicao = video.descricao, erro = null) } }
    fun cancelarEdicao() { _uiState.update { it.copy(modoEdicao = false, erro = null) } }
    fun alterarTituloEdicao(t: String) { _uiState.update { it.copy(tituloEdicao = t, erro = null) } }
    fun alterarDescricaoEdicao(t: String) { _uiState.update { it.copy(descricaoEdicao = t) } }
    fun salvarEdicao(video: Class_video) {
        val state = _uiState.value
        if (state.tituloEdicao.isBlank()) { _uiState.update { it.copy(erro = "O título não pode ficar vazio.") }; return }
        viewModelScope.launch {
            try { repository.atualizar(video.copy(titulo = state.tituloEdicao.trim(), descricao = state.descricaoEdicao.trim())); cancelarEdicao() }
            catch (e: Exception) { _uiState.update { it.copy(erro = e.message ?: "Erro ao editar vídeo") } }
        }
    }
    fun excluirVideo(video: Class_video, concluido: () -> Unit = {}) = viewModelScope.launch {
        try { repository.deletar(video); concluido() }
        catch (e: Exception) { _uiState.update { it.copy(erro = e.message ?: "Erro ao excluir vídeo") } }
    }
}
