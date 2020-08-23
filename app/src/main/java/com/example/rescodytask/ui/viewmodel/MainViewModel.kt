package com.example.rescodytask.ui.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.rescodytask.data.models.FeedModel
import com.example.rescodytask.data.models.Messages
import com.example.rescodytask.data.repo.FeedRepo
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel(var feedRepo: FeedRepo) : ViewModel() {
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String>
        get() = _errorMessage


    private  val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    private val _feedResponse = MutableLiveData<List<Messages>>()
    val feedResponse: LiveData<List<Messages>>
        get() = _feedResponse

    fun getFeeds() {
        _isLoading.postValue(true)
        feedRepo.getFeeds().enqueue(object : Callback<FeedModel>{
            override fun onFailure(call: Call<FeedModel>, t: Throwable) {
                _isLoading.postValue(false)
                _errorMessage.postValue(t.message)
            }

            override fun onResponse(call: Call<FeedModel>, response: Response<FeedModel>) {
                if(response.isSuccessful){
                    val messages = ArrayList<Messages>()//Creating an empty arraylist
                    response.body()?.feed?.entry?.forEach {
                       val title =  it.content.`$t`;
                        messages.add(Messages(
                            title.substringAfter("messageid: ").substringBefore(", message:").trim(),
                            title.substringAfter("message:").substringBefore(", sentiment:").trim(),
                            title.substringAfter("sentiment: ").trim(),
                            null
                        ))
                    }
                    _feedResponse.postValue(messages)
                }else{
                    _errorMessage.postValue(response.message())
                }
                _isLoading.postValue(false)
            }

        })
    }

}