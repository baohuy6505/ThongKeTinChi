package com.example.gk1.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.gk1.viewmodel.MainViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(navController: NavController, viewModel: MainViewModel) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Quản lý môn học") },
                actions = {
                    // X = 3 (LẺ): Nút đỏ trên TopAppBar
                    IconButton(onClick = {
                        val theory = viewModel.getTotalTheoryCredits()
                        val practice = viewModel.getTotalPracticeCredits()
                        navController.navigate("invoice/$theory/$practice")
                    }) {
                        Icon(
                            imageVector = Icons.Default.ShoppingCart,
                            contentDescription = "Thanh toán",
                            tint = Color.Red
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Form Nhập
            OutlinedTextField(
                value = viewModel.subjectName,
                onValueChange = { viewModel.subjectName = it },
                label = { Text("Tên môn học") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = viewModel.subjectCredits,
                onValueChange = { viewModel.subjectCredits = it },
                label = { Text("Số tín chỉ") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(if (viewModel.isPractice) "Loại: Thực hành" else "Loại: Lý thuyết")
                Spacer(modifier = Modifier.width(8.dp))
                Switch(
                    checked = viewModel.isPractice,
                    onCheckedChange = { viewModel.isPractice = it }
                )
            }
            Button(
                onClick = { viewModel.addSubject() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Thêm")
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(viewModel.subjectList) { subject ->
                    Card(modifier = Modifier.fillMaxWidth()) {
                        Row(
                            modifier = Modifier.padding(16.dp).fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(subject.name, style = MaterialTheme.typography.titleMedium)
                                Text("${subject.credits} tín chỉ - ${if(subject.isPractice) "Thực hành" else "Lý thuyết"}")
                            }
                            IconButton(onClick = { /* Todo sửa */ }) {
                                Icon(Icons.Default.Edit, contentDescription = "Sửa")
                            }
                            IconButton(onClick = { viewModel.deleteSubject(subject) }) {
                                Icon(Icons.Default.Delete, contentDescription = "Xóa")
                            }
                        }
                    }
                }
            }
        }
    }
}