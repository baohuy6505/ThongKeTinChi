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

    var editingSubjectId: String? by mutableStateOf(null)

    val subjectList = mutableStateListOf<Subject>()

    fun addSubjectOrUpdate() {
        val credits = subjectCredits.toIntOrNull() ?: 0
        if (subjectName.isNotBlank() && credits > 0) {
            if (editingSubjectId == null) {
                subjectList.add(
                    Subject(
                        name = subjectName,
                        credits = credits,
                        isPractice = isPractice
                    )
                )
            } else {
                val index = subjectList.indexOfFirst { it.id == editingSubjectId }
                if (index != -1) {
                    val oldSubject = subjectList[index]

                    if (oldSubject.name != subjectName || oldSubject.credits != credits || oldSubject.isPractice != isPractice) {
                        subjectList[index] = Subject(
                            id = editingSubjectId!!,
                            name = subjectName,
                            credits = credits,
                            isPractice = isPractice
                        )
                    }
                }
            }
            resetForm()
        }
    }

    fun startEditing(subject: Subject) {
        editingSubjectId = subject.id
        subjectName = subject.name
        subjectCredits = subject.credits.toString()
        isPractice = subject.isPractice
    }

    fun resetForm() {
        editingSubjectId = null
        subjectName = ""
        subjectCredits = ""
        isPractice = false
    }

    fun deleteSubject(subject: Subject) {
        subjectList.remove(subject)
        if (subject.id == editingSubjectId) {
            resetForm()
        }
    }

    fun getTotalTheoryCredits(): Int = subjectList.filter { !it.isPractice }.sumOf { it.credits }
    fun getTotalPracticeCredits(): Int = subjectList.filter { it.isPractice }.sumOf { it.credits }
}