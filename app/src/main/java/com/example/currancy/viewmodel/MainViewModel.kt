package com.example.currancy.viewmodel

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.currancy.apiInterface.ApiService
import com.example.currancy.model.CurrancyBO
import com.example.currancy.model.RateBO
import com.google.gson.JsonArray
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {
    var currencyListResponse:List<RateBO> by mutableStateOf(listOf())
    var cur= mutableListOf<RateBO>()
    var errorMessage: String by mutableStateOf("")
    fun getCurrencyList(selectCur:String) {
        viewModelScope.launch {
            val apiService = ApiService.getInstance()
            try {
                val curList = apiService.getCurrency(selectCur)
                for(i in curList.rates){
                    val rateBO=RateBO(i.key,i.value)
                    cur.add(rateBO)
                }
                currencyListResponse=cur
            }
            catch (e: Exception) {
                errorMessage = e.message.toString()
            }
        }
    }
}