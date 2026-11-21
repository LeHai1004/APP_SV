package com.ute.app_sv

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.ute.app_sv.data.AppDatabase
import com.ute.app_sv.repository.StudentRepository
import com.ute.app_sv.ui.StudentListFragment
import com.ute.app_sv.viewmodel.StudentViewModel
import com.ute.app_sv.viewmodel.StudentViewModelFactory

class MainActivity : AppCompatActivity() {

    private val database by lazy { AppDatabase.getDatabase(this) }
    private val repository by lazy { StudentRepository(database.studentDao()) }

    private val viewModel: StudentViewModel by viewModels {
        StudentViewModelFactory(repository)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        // Phải gọi viewModel ở đây để tạo nó bằng Factory trước,
        // các Fragment dùng activityViewModels() sau đó mới lấy được đúng instance
        viewModel

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.fragment_container_view, StudentListFragment())
                .commit()
        }
    }
}