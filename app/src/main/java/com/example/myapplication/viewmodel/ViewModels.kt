package com.example.myapplication.viewmodel

import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.myapplication.data.AppDatabase
import com.example.myapplication.data.repository.CanalRepository
import com.example.myapplication.data.repository.VideoRepository

@Composable
fun canalViewModel(): CanalViewModel {
    val context = LocalContext.current
    val repository = remember(context) { CanalRepository(AppDatabase.getDatabase(context).canalDao()) }
    return viewModel(factory = CanalViewModelFactory(repository, remember(context) { VideoRepository(AppDatabase.getDatabase(context).videoDao()) }))
}
@Composable
fun videoViewModel(): VideoViewModel {
    val context = LocalContext.current
    val repository = remember(context) { VideoRepository(AppDatabase.getDatabase(context).videoDao()) }
    return viewModel(factory = VideoViewModelFactory(repository))
}
