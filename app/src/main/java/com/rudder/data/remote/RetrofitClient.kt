package com.rudder.data.remote



import com.google.gson.GsonBuilder
import com.rudder.util.NullOnEmptyConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


object RetrofitClient {

    private lateinit var retrofit: Retrofit
    val gson = GsonBuilder()
        .setLenient()
        .create()

    fun getClient(baseUrl: String): Retrofit{

        if(!this::retrofit.isInitialized){
            val interceptor = HttpLoggingInterceptor()
            interceptor.level = HttpLoggingInterceptor.Level.BODY
//            val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
            val client = OkHttpClient.Builder().addInterceptor(interceptor).addNetworkInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                    .removeHeader("User-Agent")
                    .addHeader("User-Agent","Android")
                chain.proceed(requestBuilder.build())
            }.build()
            retrofit = Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)

                //.addConverterFactory(ScalarsConverterFactory.create())
                //.addConverterFactory(GsonConverterFactory.create(gson))
                .addConverterFactory(NullOnEmptyConverterFactory())
                .addConverterFactory(GsonConverterFactory.create()) //210811 merge issue, comment 정상적으로 오게끔 하는게 이거임.

                .build()
        }
        return retrofit
    }
}