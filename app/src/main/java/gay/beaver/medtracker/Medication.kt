package gay.beaver.medtracker

import android.os.Parcelable
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.lifecycle.viewmodel.compose.viewModel
import kotlinx.parcelize.Parcelize

// TODO: State
@Parcelize
data class MedSupply(var start: Int, var current: Int) : Parcelable {
    override fun toString(): String {
        return "{ start: $start, current: $current }"
    }
}

// TODO: Add dosage subclass that says how much, what it looks like, how many to take, and stuff like daily
//@Parcelize
class Medication(
    var name: String,
    var dosage: Int,
    var dosageUnit: String, // Drop down select
    var supply: MedSupply, /*val time: Date*/
) {
    constructor(other: Medication) : this(other.name, other.dosage, other.dosageUnit, other.supply)

    override fun toString(): String {
        return "{ name: $name, dosage: $dosage $dosageUnit, supply: $supply }"
    }

    @Composable
    fun DisplayName(modifier: Modifier) {
        val viewModel: MedViewModel = viewModel()
        var thisMed by remember { mutableStateOf(viewModel.getMed(this)) }

        if (thisMed == null) return

        var openDialog by remember { mutableStateOf(false) }
        Text(
            text = thisMed!!.name,
            modifier = modifier
                .clickable(
                    onClick = {
                        openDialog = true
                    }
                )
                .padding(20.dp, 10.dp, 20.dp, 30.dp)
                .fillMaxWidth(),
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center
        )

        if (openDialog) {
            Dialog(
                onDismissRequest = { openDialog = false }
            ) {

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(200.dp)
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                ) {
                    OutlinedTextField(
                        value = thisMed!!.name,
                        onValueChange = {
                            thisMed = viewModel.getMed(viewModel.updateMed(thisMed!!, name = it))!!
                        },
                        label = { Text("Medication Name") }
                    )
                }
            }
        }
    }
}


