package com.kubsu.kubsuappmobile.data.model

data class Timetable(

    val id: Long,

    val classroom: Classroom,

    val lecturer: Lecturer,

    val course: Course,

    val dayOfWeek: Int,

    val numberTimeClassHeld: NumberTimeClassHeld,

    val weekType: WeekType,

    val semester: Semester,

    val timetableGroup: List<TimetableGroup>
)