package com.example.gk1.viewmodel

import android.app.Application
import android.content.Context
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import com.example.gk1.model.Subject
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class MainViewModel(application: Application) : AndroidViewModel(application) {

    // 1. KHỞI TẠO SHAREDPREFERENCES & GSON ĐẦU TIÊN
    private val prefs = application.getSharedPreferences("ued_app_data", Context.MODE_PRIVATE)
    private val gson = Gson()

    // 2. LƯU TRẠNG THÁI "ĐANG ĐĂNG KÝ" (FORM GÕ DỞ)
    // Khi gõ chữ, nó vừa cập nhật lên UI, vừa tự động lưu ngầm vào ổ cứng
    private val _subjectName = mutableStateOf(prefs.getString("draft_name", "") ?: "")
    var subjectName: String
        get() = _subjectName.value
        set(value) {
            _subjectName.value = value
            prefs.edit().putString("draft_name", value).apply()
        }

    private val _subjectCredits = mutableStateOf(prefs.getString("draft_credits", "") ?: "")
    var subjectCredits: String
        get() = _subjectCredits.value
        set(value) {
            _subjectCredits.value = value
            prefs.edit().putString("draft_credits", value).apply()
        }

    // Biến quản lý Switch (Giữ nguyên tên isPractice theo ý bạn)
    // Mặc định true (On) = Lý thuyết
    private val _isPractice = mutableStateOf(prefs.getBoolean("draft_is_practice", true))
    var isPractice: Boolean
        get() = _isPractice.value
        set(value) {
            _isPractice.value = value
            prefs.edit().putBoolean("draft_is_practice", value).apply()
        }

    // 3. QUẢN LÝ DANH SÁCH & TRẠNG THÁI SỬA
    var editingSubjectId: String? by mutableStateOf(null)
    val subjectList = mutableStateListOf<Subject>()

    init {
        // Mở app lên là tự động load danh sách đã lưu
        loadData()
    }

    // 4. LƯU & TẢI DANH SÁCH MÔN HỌC ("ĐÃ ĐĂNG KÝ")
    private fun saveData() {
        val jsonString = gson.toJson(subjectList)
        prefs.edit().putString("saved_subject_list", jsonString).apply()
    }

    private fun loadData() {
        val jsonString = prefs.getString("saved_subject_list", null)
        if (jsonString != null) {
            val type = object : TypeToken<List<Subject>>() {}.type
            val savedList: List<Subject> = gson.fromJson(jsonString, type)
            subjectList.clear()
            subjectList.addAll(savedList)
        }
    }

    // 5. CÁC HÀM XỬ LÝ LOGIC (CRUD)
    fun addSubjectOrUpdate() {
        val credits = subjectCredits.toIntOrNull() ?: 0
        if (subjectName.isNotBlank() && credits > 0) {
            if (editingSubjectId == null) {
                subjectList.add(
                    Subject(
                        name = subjectName,
                        credits = credits,
                        // NGHỊCH ĐẢO: Switch đang On (true) -> Lưu vào List là Lý thuyết (isPractice = false)
                        isPractice = !isPractice
                    )
                )
            } else {
                val index = subjectList.indexOfFirst { it.id == editingSubjectId }
                if (index != -1) {
                    val oldSubject = subjectList[index]
                    if (oldSubject.name != subjectName || oldSubject.credits != credits || oldSubject.isPractice != !isPractice) {
                        subjectList[index] = Subject(
                            id = editingSubjectId!!,
                            name = subjectName,
                            credits = credits,
                            isPractice = !isPractice // LÚC LƯU: Nghịch đảo
                        )
                    }
                }
            }
            saveData() // Lưu danh sách
            resetForm() // Xóa trắng form (cũng tự động xóa dữ liệu gõ dở trong ổ cứng)
        }
    }

    fun startEditing(subject: Subject) {
        editingSubjectId = subject.id
        subjectName = subject.name
        subjectCredits = subject.credits.toString()
        // LÚC LẤY LÊN FORM: Môn là Thực hành (true) -> Switch phải Off (false)
        isPractice = !subject.isPractice
    }

    fun resetForm() {
        editingSubjectId = null
        subjectName = ""
        subjectCredits = ""
        isPractice = true // Đưa Switch về trạng thái On (Lý thuyết)
    }

    fun deleteSubject(subject: Subject) {
        subjectList.remove(subject)
        saveData() // Xóa xong phải lưu lại để cập nhật ổ cứng
        if (subject.id == editingSubjectId) {
            resetForm()
        }
    }

    // 6. TÍNH TOÁN TỔNG TÍN CHỈ
    fun getTotalTheoryCredits(): Int = subjectList.filter { !it.isPractice }.sumOf { it.credits }
    fun getTotalPracticeCredits(): Int = subjectList.filter { it.isPractice }.sumOf { it.credits }
}