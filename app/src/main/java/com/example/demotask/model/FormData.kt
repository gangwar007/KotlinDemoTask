package com.example.demotask.model



data class FormData(val data: List<Data>?)

data class Data(val id: String?, val type: String?, val label: String?, val hintText: String?, val required: String?, val validator: List<Validator>?)

data class Validator(val type: String?, val Value: String?)