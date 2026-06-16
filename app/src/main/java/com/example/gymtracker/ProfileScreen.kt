package com.example.gymtracker

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.tooling.preview.Preview
import com.example.gymtracker.ui.theme.GymTrackerTheme

@Composable
fun ProfileScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(24.dp)
    ) {
        Text(
            text = "Profil korisnika",
            fontSize = 30.sp,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(16.dp))

        Card(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                Text("Ime: Mateo Gržinić", fontSize = 18.sp)
                Spacer(modifier = Modifier.height(8.dp))
                Text("Cilj: povećanje snage i redovit trening")
                Spacer(modifier = Modifier.height(8.dp))
                Text("Razina: početnik / rekreativac")
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Ovaj ekran omogućuje korisniku pregled osnovnih podataka i ciljeva treninga.",
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ProfileScreenPreview() {
    GymTrackerTheme {
        ProfileScreen()
    }
}