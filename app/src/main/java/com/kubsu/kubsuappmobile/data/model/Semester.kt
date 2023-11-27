package com.kubsu.kubsuappmobile.data.model

import com.google.gson.annotations.JsonAdapter
import com.kubsu.kubsuappmobile.serialization.OffsetDateTimeDeserializer
import java.time.OffsetDateTime

data class Semester(
    val id: Long,

    val name: String,

    @JsonAdapter(OffsetDateTimeDeserializer::class)
    val startDate: OffsetDateTime,

    @JsonAdapter(OffsetDateTimeDeserializer::class)
    val endDate: OffsetDateTime
)
