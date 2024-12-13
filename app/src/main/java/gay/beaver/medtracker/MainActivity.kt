package gay.beaver.medtracker

import android.os.Bundle
import android.os.Parcelable
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.core.EaseInOut
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import gay.beaver.medtracker.ui.theme.MedTrackerTheme
import kotlinx.parcelize.Parcelize

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var medications: List<Medication> by remember {
                mutableStateOf(
                    listOf(
                        Medication("Prozac", 30, "mg", MedSupply(30, 30)),
                        Medication("Vyvanse", 30, "mg", MedSupply(30, 30)),
                        Medication("Gay", 30, "mg", MedSupply(30, 30))
                    )
                )
            }
            MedTrackerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(modifier = Modifier.padding(innerPadding)) {
                        MedList(
                            medications,
                            { medications = it }
                        )
                    }
                }
            }
        }
    }
}

// TODO: State
@Parcelize
data class MedSupply(val start: Int, val current: Int) : Parcelable

// TODO: Add dosage subclass that says how much, what it looks like, how many to take, and stuff like daily
@Parcelize
data class Medication(
    val name: String,
    val dosage: Int,
    val dosageUnit: String, // Drop down select
    val supply: MedSupply, /*val time: Date*/
) : Parcelable


// TODO: Move into separate file
@Composable
fun MedList(
    meds: List<Medication>,
    onMedsChange: (List<Medication>) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        items(
            meds
        ) { med ->
            var collapsed by remember { mutableStateOf(true) }
            MedInfo(med = med,
                    modifier = modifier,
                    collapsed = collapsed,
                    onCollapsedChange = { collapsed = it }
            )
        }

        // Item is needed because it's a lazy list??? idk
        item {
            FloatingActionButton(
                onClick = { 1 }
            ) {
                Icon(Icons.Rounded.Add, contentDescription = "Add medication")
            }
        }
    }
}


@Composable
fun MedInfo(
    med: Medication,
    modifier: Modifier = Modifier,
    collapsed: Boolean,
    onCollapsedChange: (Boolean) -> Unit
) {
    val height by animateDpAsState(
        if (collapsed) 120.dp else 200.dp,
        label = "Card Height",
        animationSpec = tween(
            durationMillis = 300,
            //            delayMillis = 50,
            easing = EaseInOut
        )
    )
    Card(
        elevation = CardDefaults.cardElevation(
            defaultElevation = 6.dp
        ),
        modifier = Modifier.height(height),

        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest,
        ),
        onClick = { onCollapsedChange(!collapsed) },

        ) {


        Column(modifier = Modifier.fillMaxSize()) {
            Text(
                text = med.name,
                modifier = Modifier
                    .padding(20.dp, 10.dp, 20.dp, 30.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleLarge,
                textAlign = TextAlign.Center
            )

            // TODO: Make the values clickable/tappable so you can change them. Underline the values (possibly)
            Text(
                text = "Supply left: ${med.supply.current} (started with ${med.supply.start})",
                modifier = Modifier
                    .padding(20.dp, 5.dp)
                    .fillMaxWidth(),
                style = MaterialTheme.typography.titleMedium,
                textAlign = TextAlign.Left,
            )

            if (!collapsed) {
                Text(
                    text = "Dose: ${med.dosage}${med.dosageUnit}",
                    modifier = Modifier
                        .padding(20.dp, 5.dp)
                        .fillMaxWidth(),
                    style = MaterialTheme.typography.titleMedium,
                    textAlign = TextAlign.Left,
                )
            }
        }
    }
}