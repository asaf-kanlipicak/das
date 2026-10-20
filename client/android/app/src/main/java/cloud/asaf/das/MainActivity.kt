package cloud.asaf.das

import android.Manifest.permission.ACCESS_LOCAL_NETWORK
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.core.content.ContextCompat.checkSelfPermission
import androidx.lifecycle.lifecycleScope
import cloud.asaf.das.ui.theme.DASTheme
import kotlinx.coroutines.withContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.io.IOException
import java.net.DatagramPacket
import java.net.DatagramSocket
import java.net.InetAddress

class MainActivity : ComponentActivity() {
    private val status = mutableStateOf("")

    private val permissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) {granted ->
        if (granted) sendPacket() else status.value = "Permission denied"
    }

    private fun ensurePermission() {
        if (Build.VERSION.SDK_INT >= 37 &&
            checkSelfPermission(this, ACCESS_LOCAL_NETWORK) == PackageManager.PERMISSION_DENIED
        ) {
            permissionLauncher.launch(ACCESS_LOCAL_NETWORK)
        }
        else {
            sendPacket()
        }
    }

    private fun sendPacket (){
        status.value = "Sending..."
        lifecycleScope.launch {
            val result = sendUdpPacket("10.0.2.2", 7777, "hello from android")
            status.value = result.fold({ "Success" }, { e -> "Failed: ${e.message}"})
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DASTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(onClick = {
                            ensurePermission()
                        }) {
                            Text("Send UDP packet")
                        }
                        Text(status.value)
                    }
                }
            }
        }
    }
}

suspend fun sendUdpPacket(
    targetIp: String,
    targetPort: Int,
    message: String
): Result<Unit> = withContext(Dispatchers.IO){
    try{
        DatagramSocket().use { socket ->
            val data = message.toByteArray()
            socket.send(
                DatagramPacket(
                    data,
                    data.size,
                    InetAddress.getByName(targetIp),
                    targetPort
                )
            )
        }
        Result.success(Unit)
    }
    catch (e: IOException){
        Result.failure(e)
    }

}