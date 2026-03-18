package com.example.gk1.ui

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(navController: NavController, theoryCredits: Int, practiceCredits: Int) {
    val X = 1
    val theoryPrice = 500_000 + (X * 10_000)
    val practicePrice = theoryPrice + 50_000

    val totalTheoryCost = theoryCredits * theoryPrice
    val totalPracticeCost = practiceCredits * practicePrice
    val totalAmount = totalTheoryCost + totalPracticeCost

    //format thành tiề VN
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    val formattedTheory = formatter.format(totalTheoryCost)
    val formattedPractice = formatter.format(totalPracticeCost)
    val formattedTotal = formatter.format(totalAmount)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Hóa đơn") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier.padding(paddingValues).padding(16.dp).fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally // Căn giữa toàn bộ
        ) {
            //HIỂN THỊ THỐNG KÊ
            Text("THỐNG KÊ HỌC PHÍ", fontWeight = FontWeight.Bold, fontSize = 20.sp)
            Spacer(modifier = Modifier.height(16.dp))

            Column(modifier = Modifier.fillMaxWidth()) {
                Text("1. Tổng số tín chỉ Lý thuyết: $theoryCredits (Thành tiền: $formattedTheory  VNĐ)")
                Text("2. Tổng số tín chỉ Thực hành: $practiceCredits (Thành tiền: $formattedPractice  VNĐ)")
                Spacer(modifier = Modifier.height(8.dp))
                Text("TỔNG TIỀN: $formattedTotal VNĐ", fontWeight = FontWeight.Bold, color = Color.Red)
            }

            Spacer(modifier = Modifier.height(32.dp))

            val radiusDp = (36 + X).dp
            val diameterDp = radiusDp * 2 // Kích thước Box phải bằng đường kính

            Box(
                modifier = Modifier.size(diameterDp),
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.matchParentSize()) {
                    drawCircle(
                        color = Color.Blue,
                        style = Stroke(width = 5f) // Nét liền (Stroke)
                    )
                }
                // Text in ra chữ số X (là 3)
                Text(
                    text = X.toString(),
                    color = Color.Blue,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}