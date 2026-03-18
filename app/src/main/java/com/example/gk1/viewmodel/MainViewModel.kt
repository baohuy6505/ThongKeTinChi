package com.example.gk1.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.gk1.model.Subject

class MainViewModel : ViewModel() {
    var subjectName by mutableStateOf("")
    var subjectCredits by mutableStateOf("")
    var isPractice by mutableStateOf(false)

    val subjectList = mutableStateListOf<Subject>()

    fun addSubject() {
        val credits = subjectCredits.toIntOrNull() ?: 0
        if (subjectName.isNotBlank() && credits > 0) {
            subjectList.add(Subject(name = subjectName, credits = credits, isPractice = isPractice))
            // Reset form sau khi thêm
            subjectName = ""
            subjectCredits = ""
            isPractice = false
        }
    }

    fun deleteSubject(subject: Subject) {
        subjectList.remove(subject)
    }

    // Tính tổng tín chỉ để truyền sang màn hình 2
    fun getTotalTheoryCredits(): Int = subjectList.filter { !it.isPractice }.sumOf { it.credits }
    fun getTotalPracticeCredits(): Int = subjectList.filter { it.isPractice }.sumOf { it.credits }
}