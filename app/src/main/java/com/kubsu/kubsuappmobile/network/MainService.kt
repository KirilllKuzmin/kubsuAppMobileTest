package com.kubsu.kubsuappmobile.network

import com.kubsu.kubsuappmobile.data.model.AuthRequest
import com.kubsu.kubsuappmobile.data.model.Timetable
import com.kubsu.kubsuappmobile.data.model.User
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Query
import java.time.OffsetDateTime

interface MainService {

    @POST("users/authentication")
    suspend fun auth(@Body authRequest: AuthRequest): Response<User>

    @Headers("Content-Type: application/json")
    @GET("timetables")
    suspend fun getTimetables(@Header("Authorization") token: String,
                              @Query("start_date") startDate: OffsetDateTime,
                              @Query("end_date") endDate: OffsetDateTime): List<Timetable>
}