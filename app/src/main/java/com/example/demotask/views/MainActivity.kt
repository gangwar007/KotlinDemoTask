package com.example.demotask.views

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.demotask.R
import com.example.demotask.databinding.ActivityMainBinding
import com.example.demotask.model.Data
import com.example.demotask.model.request.FormRequest
import com.example.demotask.view_model.MainViewModel
import com.google.gson.Gson
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {
    private lateinit var mainViewModel: MainViewModel
    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mainViewModel =
                ViewModelProvider.AndroidViewModelFactory(application).create(MainViewModel::class.java)
        binding = DataBindingUtil.setContentView<ActivityMainBinding>(this, R.layout.activity_main)
                .apply {
                    this.lifecycleOwner = this@MainActivity
                    this.viewModel = mainViewModel
                }

        init()
    }

    private fun init() {
        initObserver()
    }

    private fun initObserver() {
        mainViewModel.formdata.observe(this, Observer {
        })
        mainViewModel.data.observe(this, Observer {
            addDynamicForm(it)
        })

        mainViewModel.submit.observe(this, Observer {
            if (it!!) {
                submit(mainViewModel.data.value!!)
            }
        })
    }

    private fun addDynamicForm(data : List<Data>){

        for((i,d) in data.withIndex()) {

            val editText = EditText(this).apply {
                id = i
                hint = d.hintText
               inputType = inputType(d.validator!![0].Value!!)
            }
            scrllinear.addView(editText)
        }

    }


    private fun inputType(inputType: String) : Int {
        var type = InputType.TYPE_CLASS_TEXT
        when (inputType){
        "Alphabetic"-> { type = InputType.TYPE_TEXT_VARIATION_PERSON_NAME}
        "Alphanumeric"->{ type = InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS}
        }
        return type
    }

    private fun submit(data : List<Data>) {
        val requestData = mutableListOf<com.example.demotask.model.request.Data>()
        for((i,d) in data.withIndex()) {
           if (!validate(d,i)){
                Toast.makeText(this, "Please enter valid ${d.label}", Toast.LENGTH_SHORT).show()
                break
            }else{

                requestData.add(com.example.demotask.model.request.Data(d.id, scrllinear.rootView.findViewById<EditText>(i).text.toString()))

                if (i+1 == data.size) {

                    val formRequest =  FormRequest(requestData)
                    Toast.makeText(this, Gson().toJson(formRequest), Toast.LENGTH_LONG ).show()
                }

            }
        }
    }

    private fun validate(d: Data, i: Int): Boolean {
        var status = true
        val regex = d.validator!![1].Value!!.toRegex()
        if (!regex.matches(scrllinear.rootView.findViewById<EditText>(i).text) && d.required == "True") {
            status = false
        }

        return status
    }
}