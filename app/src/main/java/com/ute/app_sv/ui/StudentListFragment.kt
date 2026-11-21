package com.ute.app_sv.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.ute.app_sv.R
import com.ute.app_sv.databinding.FragmentStudentListBinding
import com.ute.app_sv.model.Student
import com.ute.app_sv.ui.adapter.StudentAdapter
import com.ute.app_sv.viewmodel.StudentViewModel

class StudentListFragment : Fragment() {

    // Backing property: giữ binding an toàn theo vòng đời của view
    private var _binding: FragmentStudentListBinding? = null
    private val binding get() = _binding!!

    private val viewModel: StudentViewModel by activityViewModels()
    private lateinit var studentAdapter: StudentAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentStudentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        observeData()
        setupActions()
    }

    private fun setupRecyclerView() {
        studentAdapter = StudentAdapter(
            onStudentClick = { showDetail(it) },
            onDeleteClick = { confirmDelete(it) }
        )
        binding.rvStudents.layoutManager = LinearLayoutManager(requireContext())
        binding.rvStudents.adapter = studentAdapter
        binding.rvStudents.setHasFixedSize(true)
    }

    private fun observeData() {
        // Room có thay đổi là danh sách tự cập nhật
        viewModel.allStudents.observe(viewLifecycleOwner) { list ->
            studentAdapter.submitList(list)
            binding.layoutEmpty.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
            binding.tvStudentCountBadge.text = "${list.size} sinh viên"
        }

        viewModel.statusMessage.observe(viewLifecycleOwner) { msg ->
            if (!msg.isNullOrEmpty()) {
                Toast.makeText(requireContext(), msg, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupActions() {
        binding.fabAdd.setOnClickListener {
            AddStudentDialogFragment().show(childFragmentManager, "ADD_STUDENT_DIALOG")
        }

        binding.toolbar.setOnMenuItemClickListener { item ->
            if (item.itemId == R.id.action_sync) {
                viewModel.syncFromCloud()
                true
            } else {
                false
            }
        }
    }

    private fun confirmDelete(student: Student) {
        AlertDialog.Builder(requireContext())
            .setTitle("Xác Nhận Xóa")
            .setMessage("Bạn có chắc muốn xóa sinh viên ${student.fullName} khỏi CSDL Room?")
            .setPositiveButton("Xóa") { _, _ -> viewModel.delete(student) }
            .setNegativeButton("Hủy", null)
            .show()
    }

    private fun showDetail(student: Student) {
        AlertDialog.Builder(requireContext())
            .setTitle("Thông Tin Sinh Viên")
            .setMessage(
                "ID: ${student.id}\n" +
                        "Họ Tên: ${student.fullName}\n" +
                        "MSSV: ${student.studentCode}\n" +
                        "Email: ${student.email}\n" +
                        "Điểm GPA: ${student.gpa}"
            )
            .setPositiveButton("Đóng", null)
            .show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Bắt buộc: giải phóng binding để tránh rò rỉ bộ nhớ
        _binding = null
    }
}