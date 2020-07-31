package com.example.demotask.model.request

data class FormRequest(var data: List<Data>?)

data class Data(var id: String?, var text: String?)