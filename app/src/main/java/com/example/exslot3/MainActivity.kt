package com.example.exslot3

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.*
import com.example.exslot3.ui.theme.ExSlot3Theme
import kotlinx.coroutines.launch
import java.lang.Math.sqrt

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ExSlot3Theme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AppNavigator()
                }
            }
        }
    }
}

@Composable
fun AppNavigator() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "login") {
        composable("login") { LoginScreen(navController) }
        composable("home") { HomeScreen(navController) }
        composable("list") { ListScreen() }
        composable("quadratic_equation") { QuadraticEquationScreen() }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(navController: NavHostController) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var loginSuccess by remember { mutableStateOf(false) }
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineLarge,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        TextField(
            value = email,
            onValueChange = { email = it },
            label = { Text("Email") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp)
        )
        TextField(
            value = password,
            onValueChange = { password = it },
            label = { Text("Password") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            visualTransformation = PasswordVisualTransformation()
        )
        Button(
            onClick = {
                if (email == "hoangsan" && password == "123456") {
                    loginSuccess = true
                    scope.launch {
                        snackbarHostState.showSnackbar("Login successful")
                    }
                    navController.navigate("home")
                } else {
                    loginSuccess = false
                    scope.launch {
                        snackbarHostState.showSnackbar("Login failed")
                    }
                }
            },
            modifier = Modifier.fillMaxWidth()
        ) {
            Text("Login")
        }
        Spacer(modifier = Modifier.height(16.dp))
        SnackbarHost(hostState = snackbarHostState)
    }
}

@Composable
fun HomeScreen(navController: NavHostController) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Button(
            onClick = { navController.navigate("list") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("View List")
        }
        Button(
            onClick = { navController.navigate("quadratic_equation") },
            modifier = Modifier.padding(8.dp)
        ) {
            Text("Quadratic Equation")
        }
    }
}

@Composable
fun ListScreen() {
    // Danh sách các sinh viên
    val studentList = listOf(
        "Nguyen Viet Hoang",
        "Tran Anh Quan",
        "Vu Huu Manh",
        "Le Phu Hoang"
        // Thêm các sinh viên khác vào đây nếu cần
    )

    LazyColumn {
        items(studentList.size) { index ->
            Text(text = studentList[index])
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun QuadraticEquationScreen() {
    var a by remember { mutableStateOf("") }
    var b by remember { mutableStateOf("") }
    var c by remember { mutableStateOf("") }
    var result by remember { mutableStateOf("") }

    // State để kiểm tra xem hộp thoại có hiển thị hay không
    var dialogShown by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OutlinedTextField(
            value = a,
            onValueChange = { a = it },
            label = { Text("Enter coefficient a") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = b,
            onValueChange = { b = it },
            label = { Text("Enter coefficient b") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        OutlinedTextField(
            value = c,
            onValueChange = { c = it },
            label = { Text("Enter coefficient c") },
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        )
        Button(
            onClick = {
                // Kiểm tra xem các hệ số đã được nhập đúng chưa
                if (a.isNotEmpty() && b.isNotEmpty() && c.isNotEmpty()) {
                    val aVal = a.toDouble()
                    val bVal = b.toDouble()
                    val cVal = c.toDouble()

                    val delta = bVal * bVal - 4 * aVal * cVal

                    if (delta < 0) {
                        result = "Không có nghiệm"
                    } else if (delta == 0.0) {
                        val root = -bVal / (2 * aVal)
                        result = "Nghiệm: $root"
                    } else {
                        val root1 = (-bVal + sqrt(delta)) / (2 * aVal)
                        val root2 = (-bVal - sqrt(delta)) / (2 * aVal)
                        result = "Nghiệm 1: $root1, Nghiệm 2: $root2"
                    }

                    // Hiển thị kết quả trong pop-up
                    dialogShown = true
                }
            },
            modifier = Modifier
                .padding(8.dp)
        ) {
            Text("Calculate")
        }

        // Hiển thị hộp thoại nếu dialogShown là true
        if (dialogShown) {
            AlertDialog(
                onDismissRequest = { dialogShown = false },
                title = { Text("Result") },
                text = { Text(result) },
                confirmButton = {
                    Button(onClick = { dialogShown = false }) {
                        Text("OK")
                    }
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun LoginScreenPreview() {
    ExSlot3Theme {
        LoginScreen(rememberNavController())
    }
}

@Preview(showBackground = true)
@Composable
fun HomeScreenPreview() {
    ExSlot3Theme {
        HomeScreen(rememberNavController())
    }
}
