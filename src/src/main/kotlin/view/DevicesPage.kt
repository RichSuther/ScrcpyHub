package view

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Card
import androidx.compose.material.FloatingActionButton
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.imageFromResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import model.entity.Device
import resource.Images
import resource.Strings
import view.extention.onCreated
import view.extention.onDestroyed
import viewmodel.DevicesPageViewModel

@Composable
fun DevicesPage(viewModel: DevicesPageViewModel = DevicesPageViewModel()) {
    onCreated(viewModel)
    onDrawPage(viewModel)
    onDestroyed(viewModel)
}

@Composable
private fun onDrawPage(viewModel: DevicesPageViewModel) {
    val states: List<Pair<Device, Boolean>> by viewModel.states.collectAsState()

    Box(modifier = Modifier.fillMaxSize().padding(8.dp)) {
        if (states.isEmpty()) {
            Text(
                Strings.NOT_FOUND_ANDROID_DEVICES,
                style = TextStyle(color = Color.Black, fontSize = 20.sp),
                modifier = Modifier.align(Alignment.Center)
            )
        } else {
            LazyColumn {
                items(states, itemContent = { device -> DeviceCard(device.first, device.second, viewModel) })
            }
        }

        FloatingActionButton(
            onClick = { viewModel.refresh() },
            modifier = Modifier.align(Alignment.BottomEnd).width(50.dp).height(50.dp)
        ) {
            Image(
                imageFromResource(Images.RESTART_BLACK),
                Images.RESTART_BLACK,
                contentScale = ContentScale.FillHeight,
                modifier = Modifier.padding(8.dp)
            )
        }
    }
}

@Composable
private fun DeviceCard(
    device: Device,
    isRunning: Boolean,
    viewModel: DevicesPageViewModel
) {
    Card(modifier = Modifier.wrapContentHeight().fillMaxWidth().padding(start = 8.dp, end = 8.dp, bottom = 8.dp)) {
        Box(modifier = Modifier.padding(8.dp)) {
            Button(
                onClick = { if (!isRunning) viewModel.startScrcpy(device) else viewModel.stopScrcpy(device) },
                modifier = Modifier.wrapContentSize().align(Alignment.BottomEnd)
            ) {
                Text(if (!isRunning) Strings.RUN else Strings.STOP)
            }

            Text(
                device.id,
                style = TextStyle(color = Color.Black, fontSize = 20.sp),
                modifier = Modifier.wrapContentSize().align(Alignment.CenterStart)
            )
        }
    }
}

