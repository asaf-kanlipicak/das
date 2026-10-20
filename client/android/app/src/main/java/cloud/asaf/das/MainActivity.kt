package cloud.asaf.das

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
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
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            DASTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Greeting(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
        lifecycleScope.launch {
            val result = sendUdpPacket("127.0.0.1", 7777, "android")
            result
                .onSuccess { Log.i("UDP", "Send succeeded") }
                .onFailure { e -> Log.e("UDP","Send failed",e) }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DASTheme {
        Greeting("Android")
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