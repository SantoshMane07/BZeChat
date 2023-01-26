package com.example.bzechat.ViewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.bzechat.Repository.DataRepository

class ChattingPageViewModelFactory(val Repo: DataRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return ChattingPageViewModel(Repo) as T
    }
}