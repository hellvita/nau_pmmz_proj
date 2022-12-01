package com.university.project_1_2.domain

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.university.project_1_2.presentation.adapters.QuoteModel


class MainViewModel: ViewModel() {
    val QuotesList = MutableLiveData<List<QuoteModel>>()
}