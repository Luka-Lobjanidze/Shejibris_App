package com.example.finaluri_shejibris_app.screen

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.sp
import com.example.finaluri_shejibris_app.vm.ParticipantsViewModel


data class Match(
    val p1: String,
    val p2: String,
    val winner: String? = ""
)


@Composable
fun MatchCard(
    player1: String,
    player2: String,
    selected: String,
    onSelect: (String) -> Unit
) {
    var textStyle1 by remember { mutableStateOf(
        TextDecoration.None
        )
     }
    var textStyle2 by remember { mutableStateOf(
        TextDecoration.None

    ) }
    var showDialogP1 by remember { mutableStateOf(false) }
    var showDialogP2 by remember { mutableStateOf(false) }
    var isP1Winner by remember { mutableStateOf(false) }
    var isP2Winner by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .height(120.dp)
            .width(200.dp)
            .background(color = Color.Red, shape = RoundedCornerShape(30.dp))
            .border(4.dp, Color.Black, RoundedCornerShape(10.dp)),


        ) {

        Column {
            Box(
                modifier = Modifier.padding(vertical = 12.dp, horizontal = 20.dp).fillMaxWidth()
                    .combinedClickable(onClick = {
                        if (textStyle1== TextDecoration.LineThrough){
                            isP1Winner=false
                        }
                        else {
                            if (player1 == "") {
                                isP2Winner = true
                                isP1Winner = false
                            } else {
                                isP1Winner = !isP1Winner
                                if (isP1Winner) {
                                    onSelect(player1)
                                    isP2Winner = false
                                } else if (!isP2Winner && !isP1Winner) {
                                    onSelect("")
                                }

                            }
                        }
                    }, onLongClick = { showDialogP1 = true })
                    .background(color = if (isP1Winner) Color.Green else Color.Transparent)
            ) {


                Text(player1, textDecoration = textStyle1,modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp))
            }
        }
        HorizontalDivider(thickness = 4.dp, modifier = Modifier.fillMaxWidth(), color = Color.Black)

        Box(
            modifier = Modifier.padding(vertical = 12.dp, horizontal = 15.dp).fillMaxWidth()
                .combinedClickable(onClick = {
                    if (textStyle2== TextDecoration.LineThrough){
                        isP2Winner=false
                    }
                    else {
                        if (player2 == "") {
                            onSelect(player1)
                            isP1Winner = true
                            isP2Winner = false
                        } else {
                            isP2Winner = !isP2Winner
                            if (isP2Winner) {
                                onSelect(player2)
                                isP1Winner = false
                            } else if (!isP2Winner && !isP1Winner) {
                                onSelect("")
                            }

                        }
                    }
                }, onLongClick = { showDialogP2 = true })
                .background(color = if (isP2Winner) Color.Green else Color.Transparent)
        ) {


            Text(player2, textDecoration = textStyle2 ,modifier = Modifier.padding(horizontal = 20.dp, vertical = 5.dp))
        }




        if (showDialogP1) {
            AlertDialog(
                onDismissRequest = { showDialogP1 = false },

                title = { Text("დისკვალიფიკაცია") },
                text = { Text("გინდა რომ $player1 დისკვალიფიცირდეს?") },

                confirmButton = {
                    Button(onClick = {
                        if (player2!="" && textStyle2!= TextDecoration.LineThrough){
                            onSelect(player2)
                            isP2Winner=true
                            isP1Winner=false
                        }
                        else {
                            isP2Winner=false
                            isP1Winner=false
                            onSelect("")
                        }
                        textStyle1= TextDecoration.LineThrough

                        showDialogP1 = false
                    }) {


                        Text("დიახ")
                    }
                },

                dismissButton = {
                    Button(onClick = {
                        textStyle1= TextDecoration.None
                        onSelect("")
                        isP2Winner=false
                        isP1Winner=false
                        showDialogP1 = false
                    }) {
                        Text("არა")
                    }
                }
            )
        }
        if (showDialogP2) {
            AlertDialog(
                onDismissRequest = { showDialogP2 = false },

                title = { Text("დისკვალიფიკაცია") },
                text = { Text("გინდა რომ $player2 დისკვალიფიცირდეს?") },

                confirmButton = {
                    Button(onClick = {
                        if (player1!=""&&textStyle1!= TextDecoration.LineThrough){
                            onSelect(player1)
                            isP1Winner=true
                            isP2Winner=false
                        }
                        else {
                            isP1Winner=false
                            isP2Winner=false
                            onSelect("")
                        }

                        textStyle2=TextDecoration.LineThrough
                        showDialogP2 = false
                    }) {
                        Text("დიახ")
                    }
                },

                dismissButton = {
                    Button(onClick = {
                        textStyle2= TextDecoration.None
                        onSelect("")
                        isP2Winner=false
                        isP1Winner=false
                        showDialogP2 = false
                    }) {
                        Text("არა")
                    }
                }
            )
        }
    }
}


    @Composable
    fun Round16(
        viewModel: ParticipantsViewModel,
        onFinish: () -> Unit
    ) {
        var compatitionEnd by remember { mutableStateOf(false) }
        val participantsItems by viewModel.seededParticipantsItems.collectAsState()
        val winners by viewModel.participantsWon.collectAsState()
        val scrollStateHorizontal = rememberScrollState()
        val scrollStateVetical = rememberScrollState()


        Row(
            modifier = Modifier
                .fillMaxSize()
                .horizontalScroll(scrollStateHorizontal)
                .verticalScroll(scrollStateVetical)
        ) {
            Spacer(Modifier.width(30.dp))
            Column() {
                Spacer(Modifier.height(40.dp))
                Text(text = "Round Of 16", fontSize = 20.sp)
                Spacer(modifier = Modifier.height(16.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Column() {

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column() {


                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column() {
                                        Row(verticalAlignment = Alignment.CenterVertically) {

                                            MatchCard(
                                                player1 = participantsItems.getOrNull(0) ?: "",
                                                player2 = participantsItems.getOrNull(15) ?: "",
                                                selected = winners[14],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(14, winner)
                                                })



                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }

                                        Spacer(
                                            modifier = Modifier.height(20.dp)
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            MatchCard(
                                                player1 = participantsItems.getOrNull(1) ?: "",
                                                player2 = participantsItems.getOrNull(14) ?: "",
                                                selected = winners[13],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(13, winner)
                                                })
                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }


                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(145.dp)
                                            .width(5.dp)
                                            .background(
                                                color = Color.Blue,
                                                shape = RoundedCornerShape(
                                                    topEnd = 5.dp,
                                                    bottomEnd = 5.dp
                                                )
                                            )
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                    MatchCard(
                                        player1 = winners[14],
                                        player2 = winners[13],
                                        selected = winners[6],
                                        onSelect = { winner ->
                                            viewModel.setWinner(6, winner)
                                        }
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))


                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column() {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            MatchCard(
                                                player1 = participantsItems.getOrNull(2) ?: "",
                                                player2 = participantsItems.getOrNull(13) ?: "",
                                                selected = winners[12],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(12, winner)
                                                })
                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }

                                        Spacer(
                                            modifier = Modifier.height(20.dp)
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            MatchCard(
                                                player1 = participantsItems.getOrNull(3) ?: "",
                                                player2 = participantsItems.getOrNull(12) ?: "",
                                                selected = winners[11],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(11, winner)
                                                })
                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }


                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(145.dp)
                                            .width(5.dp)
                                            .background(
                                                color = Color.Blue,
                                                shape = RoundedCornerShape(
                                                    topEnd = 5.dp,
                                                    bottomEnd = 5.dp
                                                )
                                            )
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                    MatchCard(
                                        player1 = winners[12],
                                        player2 = winners[11],
                                        selected = winners[5],
                                        onSelect = { winner ->
                                            viewModel.setWinner(5, winner)
                                        }
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                }


                            }
                            Spacer(
                                modifier = Modifier
                                    .height(285.dp)
                                    .width(5.dp)
                                    .background(
                                        color = Color.Blue,
                                        shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                                    )
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(5.dp)
                                    .width(140.dp)
                                    .background(color = Color.Blue)
                            )
                            MatchCard(
                                player1 = winners[6],
                                player2 = winners[5],
                                selected = winners[2],
                                onSelect = { winner ->
                                    viewModel.setWinner(2, winner)
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(5.dp)
                                    .width(140.dp)
                                    .background(color = Color.Blue)
                            )

                        }
                        Spacer(Modifier.height(20.dp))


                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Column() {


                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column() {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            MatchCard(
                                                player1 = participantsItems.getOrNull(4) ?: "",
                                                player2 = participantsItems.getOrNull(11) ?: "",
                                                selected = winners[10],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(10, winner)
                                                })
                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }

                                        Spacer(
                                            modifier = Modifier.height(20.dp)
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            MatchCard(
                                                player1 = participantsItems.getOrNull(5) ?: "",
                                                player2 = participantsItems.getOrNull(10) ?: "",
                                                selected = winners[9],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(9, winner)
                                                })
                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }


                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(145.dp)
                                            .width(5.dp)
                                            .background(
                                                color = Color.Blue,
                                                shape = RoundedCornerShape(
                                                    topEnd = 5.dp,
                                                    bottomEnd = 5.dp
                                                )
                                            )
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                    MatchCard(
                                        player1 = winners[10],
                                        player2 = winners[9],
                                        selected = winners[0],
                                        onSelect = { winner ->
                                            viewModel.setWinner(4, winner)
                                        }
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                }

                                Spacer(modifier = Modifier.height(20.dp))


                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Column() {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            MatchCard(
                                                player1 = participantsItems.getOrNull(6) ?: "",
                                                player2 = participantsItems.getOrNull(9) ?: "",
                                                selected = winners[8],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(8, winner)
                                                })
                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }

                                        Spacer(
                                            modifier = Modifier.height(20.dp)
                                        )

                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            MatchCard(
                                                player1 = participantsItems.getOrNull(7) ?: "",
                                                player2 = participantsItems.getOrNull(8) ?: "",
                                                selected = winners[7],
                                                onSelect = { winner ->
                                                    viewModel.setWinner(7, winner)
                                                })
                                            Spacer(
                                                modifier = Modifier
                                                    .height(5.dp)
                                                    .width(140.dp)
                                                    .background(color = Color.Blue)
                                            )
                                        }


                                    }
                                    Spacer(
                                        modifier = Modifier
                                            .height(145.dp)
                                            .width(5.dp)
                                            .background(
                                                color = Color.Blue,
                                                shape = RoundedCornerShape(
                                                    topEnd = 5.dp,
                                                    bottomEnd = 5.dp
                                                )
                                            )
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                    MatchCard(
                                        player1 = winners[8],
                                        player2 = winners[7],
                                        selected = winners[3],
                                        onSelect = { winner ->
                                            viewModel.setWinner(3, winner)
                                        }
                                    )
                                    Spacer(
                                        modifier = Modifier
                                            .height(5.dp)
                                            .width(140.dp)
                                            .background(color = Color.Blue)
                                    )
                                }


                            }
                            Spacer(
                                modifier = Modifier
                                    .height(285.dp)
                                    .width(5.dp)
                                    .background(
                                        color = Color.Blue,
                                        shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                                    )
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(5.dp)
                                    .width(140.dp)
                                    .background(color = Color.Blue)
                            )
                            MatchCard(
                                player1 = winners[4],
                                player2 = winners[3],
                                selected = winners[1],
                                onSelect = { winner ->
                                    viewModel.setWinner(1, winner)
                                }
                            )
                            Spacer(
                                modifier = Modifier
                                    .height(5.dp)
                                    .width(140.dp)
                                    .background(color = Color.Blue)
                            )

                        }


                    }
                    Spacer(
                        modifier = Modifier
                            .height(565.dp)
                            .width(5.dp)
                            .background(
                                color = Color.Blue,
                                shape = RoundedCornerShape(topEnd = 5.dp, bottomEnd = 5.dp)
                            )
                    )
                    Spacer(
                        modifier = Modifier
                            .height(5.dp)
                            .width(140.dp)
                            .background(color = Color.Blue)
                    )
                    MatchCard(
                        player1 = winners[2],
                        player2 = winners[1],
                        selected = winners[0],
                        onSelect = { winner ->
                            viewModel.setWinner(0, winner)
                        }
                    )

                    LaunchedEffect(viewModel.participantsWon.value[0]) {
                        val winner = viewModel.participantsWon.value[0]

                        if (winner.isNotBlank()) {
                            viewModel.showFinishDialog = true
                        }
                    }
                    if (viewModel.showFinishDialog) {
                        AlertDialog(
                            onDismissRequest = { viewModel.showFinishDialog = false },
                            title = { Text("ჩემპიონატი დასრულდა!") },
                            text = { Text("გინდა შედეგების ნახვა/დასრულება?") },

                            confirmButton = {
                                Button(onClick = {
                                    viewModel.showFinishDialog = false
                                    onFinish()

                                }) {
                                    Text("დიახ")
                                }
                            },

                            dismissButton = {
                                Button(onClick = {
                                    viewModel.showFinishDialog = false
                                }) {
                                    Text("არა")
                                }
                            }
                        )

                    }
                }
                Spacer(Modifier.height(50.dp))
            }

        }
    }
//
