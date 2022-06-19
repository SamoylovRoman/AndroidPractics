package com.skillbox.github.ui.coroutine

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import kotlinx.coroutines.*
import java.math.BigInteger
import java.util.*

class BaseCoroutineFragment : Fragment() {

    private var fragmentScope = CoroutineScope(Dispatchers.Main)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        asyncExample()

    }

    private fun asyncExample() {
        var fragmentIOScope = CoroutineScope(Dispatchers.IO)

        fragmentIOScope.launch {
            val start = System.currentTimeMillis()
            (0..10).map {
                async {
                    calculateNumber()
                }
            }.map {
                it.await()
            }
/*            val deferredResult = async {
                calculateNumber()
            }
            val deferredResult2 = async {
                calculateNumber()
            }*/
/*            Log.d(
                "BaseCoroutineFragment",
                "coroutine completed = ${deferredResult.isCompleted}"
            )
            val result = deferredResult.await()
            val result2 = deferredResult2.await()*/
            val end = System.currentTimeMillis()
            Log.d("BaseCoroutineFragment", "total time = ${end - start}")
        }
    }

    private fun calculateNumberExample() {
        fragmentScope.launch {
            Log.d(
                "BaseCoroutineFragment",
                "launch inside from thread = ${Thread.currentThread().name}"
            )
            calculateNumber()
            Log.d(
                "BaseCoroutineFragment",
                "launch inside from thread = ${Thread.currentThread().name}"
            )
        }
        Log.d("BaseCoroutineFragment", "Coroutine launched")
    }

    /*fragmentScope.launch {
        while (true) {
            delay(100)
            Log.d(
                "BaseCoroutineFragment",
                "launch 2 inside from thread = ${Thread.currentThread().name}"
            )
        }
    }*/

    private fun changeThreadExample() {
        var fragmentIOScope = CoroutineScope(Dispatchers.IO)
        fragmentIOScope.launch {
            (0..200).forEach {
                Log.d(
                    "BaseCoroutineFragment",
                    "start from thread = ${Thread.currentThread().name}"
                )
                delay(100)
                Log.d(
                    "BaseCoroutineFragment",
                    "end from thread = ${Thread.currentThread().name}"
                )
            }
        }
    }

    private suspend fun calculateNumber(): BigInteger {
        return withContext(Dispatchers.Default) {
            Log.d(
                "BaseCoroutineFragment",
                "calculate number thread = ${Thread.currentThread().name}"
            )
            BigInteger.probablePrime(4000, Random())
        }
    }
}