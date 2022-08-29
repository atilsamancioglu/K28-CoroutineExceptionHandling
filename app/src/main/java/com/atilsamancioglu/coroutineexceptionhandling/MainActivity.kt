package com.atilsamancioglu.coroutineexceptionhandling

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.*
import kotlin.Exception

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
/*
        lifecycleScope.launch {
            //this won't work. exceptions propagate in coroutines and we need corutineexceptionhandler
            try {
                launch {
                    throw Exception("error")
                }
            } catch (e: Exception) {
                print(e.stackTrace)
            }
        }



        lifecycleScope.launch {
            //this will work and it won't be propagated up but it won't be feasible to do everything
            //in try and catch in a coroutine
            launch {
                try {
                    throw Exception("error")
                } catch (e: Exception) {
                    e.printStackTrace()
                }
            }
        }
        */


        val handler = CoroutineExceptionHandler {context, throwable ->
            println("exception: " + throwable)
        }

        lifecycleScope.launch(handler) {
            //this will cancel the other coroutine without executing it
            //since one error will cancel all the launches in this coroutine scope
                launch {
                    throw Exception("error")
                }
                launch {
                    delay(500L)
                    println("this is executed")
                }
        }

        lifecycleScope.launch(handler) {
            //you can use supervisorScope to deal with this
            //second launch will get executed in this case

            supervisorScope {
                launch {
                    throw Exception("error")
                }
                launch {
                    delay(500L)
                    println("this is executed")
                }
            }
        }

         //we can use the handler in other cases as well such as:

        CoroutineScope(Dispatchers.Main + handler).launch {
            launch {
                throw Exception("error in a coroutine scope")
            }
        }

    }
}