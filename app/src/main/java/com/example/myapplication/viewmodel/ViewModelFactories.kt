package com.example.myapplication.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.myapplication.data.repository.CanalRepository
import com.example.myapplication.data.repository.VideoRepository

class CanalViewModelFactory(private val repository: CanalRepository, private val videosRepository: VideoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(CanalViewModel::class.java))
        @Suppress("UNCHECKED_CAST") return CanalViewModel(repository, videosRepository) as T
    }
}
class VideoViewModelFactory(private val repository: VideoRepository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        require(modelClass.isAssignableFrom(VideoViewModel::class.java))
        @Suppress("UNCHECKED_CAST") return VideoViewModel(repository) as T
    }
}
