package com.example.bestofbehance.retrofit

import retrofit2.CallAdapter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class BehanceCallAdapterFactory private constructor() : CallAdapter.Factory() {

    override fun get(returnType: Type?, annotations: Array<out Annotation>?, retrofit: Retrofit?): CallAdapter<*, *>? {
        return returnType?.let {
            return try {
                val enclosingType = (it as ParameterizedType)

                if (enclosingType.rawType != NetCall::class.java)
                    null
                else {
                    val type = enclosingType.actualTypeArguments[0]
                    BehanceCallAdapter<Any>(type)
                }
            } catch (ex: ClassCastException) {
                null
            }
        }
    }

    companion object {
        @JvmStatic
        fun create() = BehanceCallAdapterFactory()
    }

}