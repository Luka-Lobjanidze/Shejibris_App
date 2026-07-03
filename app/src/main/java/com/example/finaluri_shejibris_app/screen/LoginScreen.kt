package com.example.finaluri_shejibris_app.screen


import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finaluri_shejibris_app.vm.ParticipantsViewModel
import kotlinx.coroutines.delay


@Composable
fun LoginScreen(
    onLoginSuccess: () -> Unit,
    viewModel: ParticipantsViewModel
) {
    val inputText by viewModel.inputText.collectAsState()
    val participantsItems by viewModel.participantsItems.collectAsState()
    val seededParticipantsItems by viewModel.seededParticipantsItems.collectAsState()


    val query by viewModel.query.collectAsState()
    val results by viewModel.searchResults.collectAsState()

    var expanded by remember { mutableStateOf(false) }
    var checked by remember { mutableStateOf(false) }
    var showPopup by remember { mutableStateOf(false) }
    var minParticipants by remember { mutableStateOf(false) }
    var maxParticipants by remember { mutableStateOf(false) }


    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp, horizontal = 30.dp)
    ) {
Spacer(Modifier.height(20.dp))
        Text("შეჯიბრის მონაწილეები", style = MaterialTheme.typography.headlineSmall)

        Spacer(Modifier.height(16.dp))

        // 🔥 SEARCH AREA (IMPORTANT FIX)
        Box(modifier = Modifier.fillMaxWidth()) {

            Column {

                OutlinedTextField(
                    value = inputText,
                    onValueChange = {
                        viewModel.onTextChange(it)
                        viewModel.onQueryChange(it)
                        expanded = true
                    },
                    modifier = Modifier.fillMaxWidth(),
                    label = { Text("მონაწილის სახელი") },
                    singleLine = true
                )

                // 🔽 DROPDOWN
                if (expanded && query.isNotBlank()) {

                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        elevation = CardDefaults.cardElevation(6.dp)
                    ) {

                        Column {

                            if (results.isNotEmpty()) {

                                results.forEach { item ->

                                    Text(
                                        text = item.name,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clickable {
                                                viewModel.onTextChange(item.name)
                                                viewModel.onQueryChange("")
                                                expanded = false
                                            }
                                            .padding(12.dp)
                                    )
                                }

                            } else {

                                Text(
                                    text = "No results found",
                                    modifier = Modifier.padding(12.dp)
                                )
                            }
                        }
                    }
                }
            }
        }

        Spacer(Modifier.height(12.dp))

        // ➕ ADD BUTTON
        Button(
            onClick = {
                if (inputText.isNotBlank()) {
                    if (!(inputText in seededParticipantsItems ) && !(inputText in participantsItems )) {
                        if (checked) {
                            viewModel.addSeededItem()
                        } else {
                            viewModel.addItem()
                        }

                        viewModel.addParticipant(inputText)
                        viewModel.onTextChange("")
                        expanded = false
                    } else {
                        showPopup = true
                    }
                }
            }
        ) {
            Text("დამატება")
        }

        // checkbox
        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(checked = checked, onCheckedChange = { checked = it })
            Text("განთესვა")
        }

        Spacer(Modifier.height(20.dp))

        Text(
            "მონაწილეები: ${participantsItems.size + seededParticipantsItems.size}"
        )

        Spacer(Modifier.height(12.dp))

        // 🔥 LISTS (unchanged but cleaner)
        LazyColumn (modifier = Modifier.fillMaxWidth().height(430.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)){
            items(seededParticipantsItems) { item ->

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item, style = MaterialTheme.typography.bodyLarge)

                        IconButton(onClick = { viewModel.removeSeededItem(item) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "წაშლა",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }




            items(participantsItems) { item ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = item, style = MaterialTheme.typography.bodyLarge)

                        IconButton(onClick = { viewModel.removeItem(item) }) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "წაშლა",
                                tint = MaterialTheme.colorScheme.error
                            )
                        }
                    }

                }

            }
        }

        // 🔥 BUTTON bottom
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.BottomCenter
        ) {
            Button(onClick = {
                if (seededParticipantsItems.size+participantsItems.size<8)minParticipants=true
                else if(seededParticipantsItems.size+participantsItems.size>16)maxParticipants=true
                else{

                    viewModel.startCompetition()
                    onLoginSuccess()


                }
            }, modifier = Modifier.padding(bottom = 40.dp)) {
                Text("შეჯიბრის დაწყება", fontSize =20.sp)
            }
        }



    }
    LaunchedEffect(showPopup) {
        delay(3000)
        showPopup = false
    }
    LaunchedEffect(showPopup) {
        delay(3000)
        minParticipants = false
    }

    LaunchedEffect(showPopup) {
        delay(3000)
        maxParticipants = false
    }

    if (showPopup) {
        Box( modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .padding(20.dp), contentAlignment = Alignment.TopCenter )
        {
            Text( "იგივე სახელით უკვე არსებობს მონაწილე!!!",
                modifier = Modifier
                    .background( color = Color.Red, shape = RoundedCornerShape(40.dp))
                    .padding(start = 32.dp, top = 16.dp, bottom = 16.dp, end = 32.dp) , color = Color.White )
        }
    }
    if (minParticipants) {
        Box( modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .padding(20.dp), contentAlignment = Alignment.TopCenter )
        {
            Text( "მინიმალური მონაწილეების რაოდენობა 8-ა!!!",
                modifier = Modifier
                    .background( color = Color.Red, shape = RoundedCornerShape(40.dp))
                    .padding(start = 32.dp, top = 16.dp, bottom = 16.dp, end = 32.dp) , color = Color.White )
        }
    }
    if (maxParticipants) {
        Box( modifier = Modifier
            .fillMaxWidth()
            .padding(top = 40.dp)
            .padding(20.dp), contentAlignment = Alignment.TopCenter )
        {
            Text( "მაქსიმალური მონაწილეების რაოდენობა 16-ა!!!",
                modifier = Modifier
                    .background( color = Color.Red, shape = RoundedCornerShape(40.dp))
                    .padding(start = 32.dp, top = 16.dp, bottom = 16.dp, end = 32.dp) , color = Color.White )
        }
    }
}



