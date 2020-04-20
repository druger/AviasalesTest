package com.example.aviaselstest.domain.repository

import android.util.Log
import retrofit2.Response
import com.example.aviaselstest.data.remote.Result
import java.io.IOException

open class BaseRepository {

    suspend fun <T : Any> apiCall(
        call: suspend () -> Response<T>,
        error: String
    ): T? {
        val result = apiResult(call, error)
        var data: T? = null

        when(result) {
            is Result.Success -> data = result.data
            is Result.Error ->
                Log.e(BaseRepository::class.simpleName,
                    "$error with Exception: ${result.exception}")
        }
        return data
    }

    private suspend fun <T : Any> apiResult(
        call: suspend () -> Response<T>,
        error: String
    ): Result<T> {

        val response = call.invoke()
        if (response.isSuccessful) return Result.Success(response.body()!!)
        return Result.Error(IOException("Remote error: $error"))
    }
}