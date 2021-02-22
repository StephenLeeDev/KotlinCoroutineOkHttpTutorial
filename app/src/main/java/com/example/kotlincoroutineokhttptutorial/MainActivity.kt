package com.example.kotlincoroutineokhttptutorial

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import com.example.kotlincoroutineokhttptutorial.databinding.ActivityMainBinding
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import okhttp3.*
import java.io.IOException

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        binding.activity = this

//        coroutine()
        getHtmlString()
    }

    private fun coroutine() {
        CoroutineScope(Dispatchers.Main).launch {
            val html = CoroutineScope(Dispatchers.Default).async {
                getHtml()
            }.await()
            binding.textViewMain.text = html
        }
    }

    private fun getHtml() : String {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url("https://www.google.com").build()
        client.newCall(request).execute().use {
            response ->  return if (response.body != null) {
                response.body!!.string()
            } else {
                "body is null"
            }
        }
    }

    private fun getHtmlString() {
        val client = OkHttpClient.Builder().build()
        val request = Request.Builder().url("https://www.google.com").build()
        client.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                binding.textViewMain.text = "failed"
            }

            override fun onResponse(call: Call, response: Response) {
                CoroutineScope(Dispatchers.Main).launch {
                    if (response.body != null) {
                        binding.textViewMain.text = response.body!!.string()
                    } else {
                        binding.textViewMain.text = "body is null"
                    }
                }
            }
        })
    }
}