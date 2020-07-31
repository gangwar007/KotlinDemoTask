package com.example.demotask.view_model

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.example.demotask.model.Data
import com.example.demotask.model.FormData
import com.example.demotask.utils.Utils
import com.google.gson.Gson


class MainViewModel(application: Application): AndroidViewModel(application){

    val formdata = MutableLiveData<FormData>()
    val data = MutableLiveData<List<Data>>()
    val submit = MutableLiveData<Boolean>()
    val jsonFileString = Utils().getJsonDataFromAsset(getApplication(), "form.json")


    val gson = Gson()

    init {
        formdata.value = gson.fromJson(jsonFileString, FormData::class.java)
        data.value = formdata.value?.data
    }

    fun onSubmit() {
        submit.value = true
    }


}