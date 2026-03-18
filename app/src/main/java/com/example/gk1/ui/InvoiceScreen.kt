package com.example.gk1.ui

import CertificateStamp
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import java.text.NumberFormat
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InvoiceScreen(navController: NavController, theoryCredits: Int, practiceCredits: Int) {
    val X = 1//SBD
    val theoryPrice = 500_000 + (X * 10_000)
    val practicePrice = theoryPrice + 50_000

    val totalTheoryCost = theoryCredits * theoryPrice
    val totalPracticeCost = practiceCredits * practicePrice
    val totalAmount = totalTheoryCost + totalPracticeCost

    // Format thành tiền VN
    val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
    val formattedTheory = formatter.format(totalTheoryCost)
    val formattedPractice = formatter.format(totalPracticeCost)
    val formattedTotal = formatter.format(totalAmount)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Chi tiết Hóa đơn") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(defaultElevation = 6.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(20.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        text = "THỐNG KÊ HỌC PHÍ",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )

                    HorizontalDivider(color = Color.Black)

                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "1. Tổng số tín chỉ Lý thuyết: $theoryCredits",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Thành tiền: $formattedTheory VNĐ",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Dòng 2: Thực hành
                    Column(modifier = Modifier.fillMaxWidth()) {
                        Text(
                            text = "2. Tổng số tín chỉ Thực hành: $practiceCredits",
                            fontWeight = FontWeight.Medium,
                            fontSize = 16.sp,
                            color = Color.Black
                        )
                        Text(
                            text = "Thành tiền: $formattedPractice VNĐ",
                            modifier = Modifier.fillMaxWidth(),
                            textAlign = TextAlign.End,
                            color = Color.DarkGray,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    HorizontalDivider(color = Color.LightGray)

                    // Dòng 3: Tổng tiền
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "TỔNG TIỀN:",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 18.sp,
                            color = Color.Red
                        )
                        Text(
                            text = "$formattedTotal VNĐ",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = Color.Red
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(40.dp))

            // 2. PHẦN CUSTOM VIEW - TEM CHỨNG NHẬN
            val radiusDp = (36 + X).dp
            val diameterDp = radiusDp * 2

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                CertificateStamp()
            }
        }
    }
}

