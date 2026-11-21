package com.ute.app_sv.network

import com.google.gson.annotations.SerializedName
import com.ute.app_sv.model.Student

// Dạng dữ liệu JSON nhận từ server
data class StudentDto(
    @SerializedName("id") val id: Int = 0,
    @SerializedName("fullName") val fullName: String,
    @SerializedName("studentCode") val studentCode: String,
    @SerializedName("email") val email: String,
    @SerializedName("gpa") val gpa: Double,
    @SerializedName("major") val major: String = "Công Nghệ Thông Tin"
)

// Chuyển DTO (mạng) sang Entity (Room)
fun StudentDto.toEntity(): Student = Student(
    id = id,
    fullName = fullName,
    studentCode = studentCode,
    email = email,
    gpa = gpa,
    major = major
)