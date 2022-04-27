package com.example.autodial

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Phone
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.autodial.ui.theme.AutoDialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp

class MainActivity : ComponentActivity() {
    private val resultLauncher = registerForActivityResult(ActivityResultContracts.RequestPermission()) {
        if (it) {
            Toast.makeText(this, "Permission granted, You can call now", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                this,
                "Permission denied, Cannot call",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            AutoDialTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    Home {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            checkPermissionAndCallNumber(it)
                        } else {
                            callNumber(it)
                        }
                    }
                }
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.M)
    private fun checkPermissionAndCallNumber(mobileNumber: String) {
        if (checkSelfPermission(android.Manifest.permission.CALL_PHONE) ==
            PackageManager.PERMISSION_GRANTED
        ) {
            callNumber(mobileNumber)
        } else {
            resultLauncher.launch(android.Manifest.permission.CALL_PHONE)
        }
    }

    private fun callNumber(mobileNumber: String) {
        val phoneNumberWithPrefix = if (!mobileNumber.startsWith("+")) {
            "+91$mobileNumber"
        } else {
            mobileNumber
        }
        val intent = Intent(Intent.ACTION_CALL)
        intent.data = Uri.parse("tel:$phoneNumberWithPrefix")
        startActivity(intent)
    }
}

@Composable
fun Home(onSubmit: (String) -> Unit) {
    MobileNumberForm { mobileNumber ->
        onSubmit(mobileNumber)
    }
}

@Composable
fun MobileNumberForm(onSubmit: (String) -> Unit) {
    var mobileNumber by remember { mutableStateOf("") }

    Column(modifier = Modifier
        .fillMaxSize()
        .padding(8.dp), verticalArrangement = Arrangement.Center ,horizontalAlignment = Alignment.CenterHorizontally) {
        OutlinedTextField(
            value = mobileNumber,
            label = { Text("Mobile Number") },
            leadingIcon = { Icon(Icons.Filled.Phone, "Phone") },
            onValueChange= {mobileNumber = it},
            modifier = Modifier.fillMaxWidth(),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone
            )
        )

        Spacer(modifier = Modifier.height(8.dp))

        Button(onClick = { onSubmit(mobileNumber) }) {
            Text(text = "Call")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    AutoDialTheme {
        Home {}
    }
}