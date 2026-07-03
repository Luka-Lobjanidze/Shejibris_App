package com.example.finaluri_shejibris_app.screen

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.finaluri_shejibris_app.vm.ParticipantsViewModel


@Composable
fun FinalScreen(viewModel: ParticipantsViewModel,onBackToLogin: () -> Unit)
{
    val winners  by viewModel.participantsWon.collectAsState()
    var secondPlace= if (winners[0]!=winners[1])winners[1]else winners[2]
    var firstTherdPlace= if (winners[2]!=winners[5])winners[5]else winners[6]
    var secondTherdPlace= if (winners[1]!=winners[3])winners[3]else winners[4]

    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center){


        Column(Modifier, horizontalAlignment = Alignment.CenterHorizontally) {
            Text("საბოლოო შედეგები:", fontSize = 32.sp)
            Spacer(Modifier.height(50.dp))

            Text("#1  ${winners.get(0)}", fontSize = 28.sp)
            Spacer(Modifier.height(10.dp))
            Text("#2  ${secondPlace}",fontSize = 24.sp)
            Spacer(Modifier.height(10.dp))
            Text("#3  ${firstTherdPlace}",fontSize = 20.sp)
            Spacer(Modifier.height(10.dp))
            Text("#3  ${secondTherdPlace}",fontSize = 20.sp)


            Spacer(Modifier.height(50.dp))
            Button( onClick ={
                viewModel.resetCompetition()
                onBackToLogin()
            } ) {
                Text("უკან დაბრუნება")
            }
        }


    }



}