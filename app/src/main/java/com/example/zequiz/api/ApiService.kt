package com.example.zequiz.api

import com.example.zequiz.models.*
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*

interface ApiService {

    companion object {
        private const val BASE_URL = "http://192.168.1.7:8080/zequiz/"  // Ganti IP sesuai backend kamu

        val instance: ApiService by lazy {
            val retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .build()

            retrofit.create(ApiService::class.java)
        }
    }

    @POST("auth/login")
    fun login(@Body loginRequest: LoginRequest): Call<LoginResponse>

    @POST("auth/register")
    fun register(@Body registerRequest: RegisterRequest): Call<Void>

    @GET("kuis/siswa")
    fun getKuisSiswa(@Header("Authorization") token: String): Call<List<KuisResponse>>

    @GET("kuis/guru")
    fun getKuisGuru(@Header("Authorization") token: String): Call<List<KuisResponse>>

    @GET("soal/topik/{topikId}")
    fun getSoalByTopik(
        @Header("Authorization") token: String,
        @Path("topikId") topikId: Long
    ): Call<List<SoalResponse>>

    @POST("kuis/submit")
    fun submitJawaban(
        @Header("Authorization") token: String,
        @Body submitRequest: SubmitJawabanRequest
    ): Call<SubmitJawabanResponse>

    @GET("skor/kuis/{kuisId}")
    fun getSkorSiswa(
        @Header("Authorization") token: String,
        @Path("kuisId") kuisId: Long
    ): Call<List<SkorSiswaResponse>>

    @GET("topik")
    fun getAllTopik(@Header("Authorization") token: String): Call<List<TopikResponse>>

    @POST("topik/tambah")
    fun tambahTopik(
        @Header("Authorization") token: String,
        @Body topikRequest: TopikRequest
    ): Call<Void>

    @POST("soal/tambah")
    fun tambahSoal(
        @Header("Authorization") token: String,
        @Body soalRequest: SoalRequest
    ): Call<Void>

    @POST("kuis/buat")
    fun buatKuis(
        @Header("Authorization") token: String,
        @Body kuisRequest: BuatKuisRequest
    ): Call<Void>
}
