package view

import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.entity.Device
import model.usecase.StartScrcpyUseCase
import model.usecase.StopScrcpyUseCase
import resource.Strings

@Composable
fun DeviceCard(device: Device, startScrcpyUseCase: StartScrcpyUseCase, stopScrcpyUseCase: StopScrcpyUseCase) {
    var running by remember { mutableStateOf(false) }

    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(8.dp)) {
        Box(modifier = Modifier.padding(8.dp)) {
            Button(
                onClick = {
                    if (running) {
                        running = false
                        stopScrcpyUseCase.execute(device)
                    } else {
                        running = true
                        startScrcpyUseCase.execute(device, null, onDestroy = { running = false })
                    }
                },
                modifier = Modifier.wrapContentSize().align(Alignment.BottomEnd)
            ) {
                Text(if (running) Strings.STOP else Strings.RUN)
            }

            Text(
                device.getDeviceLabel(),
                style = TextStyle(color = Color.Black, fontSize = 24.sp),
            )
        }
    }
}

private fun Device.getDeviceLabel(): String {
    return "$name ($id)"
}