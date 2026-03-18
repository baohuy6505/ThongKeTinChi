package com.example.gk1.model
import java.util.UUID

data class Subject(
    val id: String = UUID.randomUUID().toString(),
    val name: String,
    val credits: Int,
    val isPractice: Boolean
)