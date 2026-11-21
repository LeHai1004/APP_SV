package com.ute.app_sv.repository

import com.ute.app_sv.data.StudentDao
import com.ute.app_sv.model.Student
import com.ute.app_sv.network.RetrofitClient
import com.ute.app_sv.network.StudentApiService
import com.ute.app_sv.network.toEntity
import kotlinx.coroutines.flow.Flow

class StudentRepository(
    private val studentDao: StudentDao,
    private val apiService: StudentApiService = RetrofitClient.apiService
) {

    // UI chỉ quan sát dữ liệu từ Room (Single Source of Truth)
    val allStudents: Flow<List<Student>> = studentDao.getAllStudents()

    suspend fun insert(student: Student): Long = studentDao.insertStudent(student)

    suspend fun delete(student: Student) = studentDao.deleteStudent(student)

    // Lấy dữ liệu từ server, lưu vào Room, trả về số sinh viên đã đồng bộ
    suspend fun syncFromCloud(): Result<Int> {
        return try {
            val remoteStudents = apiService.getStudents()
            val entities = remoteStudents.map { it.toEntity() }
            studentDao.insertAll(entities)
            Result.success(entities.size)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}