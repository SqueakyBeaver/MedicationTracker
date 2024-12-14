package gay.beaver.medtracker

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class MedViewModel : ViewModel() {
    private var _medList: MutableStateFlow<MutableList<Medication>> =
        MutableStateFlow(mutableListOf())
    val medList = _medList.asStateFlow()

    fun getAllMeds(): List<Medication> {
        return medList.value
    }

    fun addMeds(meds: List<Medication>) {
        medList.value.addAll(meds)
    }

    fun updateMeds(meds: List<Medication>) {
        medList.value.clear()
        medList.value.addAll(meds)
    }

    fun addMed(medication: Medication) {
        medList.value.add(medication)
    }

    fun removeMed(medication: Medication): Medication {
        medList.value.remove(medication)

        // TODO: Use this for undo button
        return medication
    }

    fun updateMed(
        old: Medication,
        name: String = "",
        dosage: Int = -1,
        dosageUnit: String = "",
        supply: MedSupply = MedSupply(-1, -1),
    ): Medication {
        println(medList.toString())
        println(old)
        val idx = medList.value.indexOf(old)

        var new = Medication(old)

        when {
            name != "" -> new.name = name
            dosage != -1 -> new.dosage = dosage
            dosageUnit != "" -> new.dosageUnit = dosageUnit
            supply.start != -1 -> new.supply.start = supply.start
            supply.current != -1 -> new.supply.current = supply.current
        }

        medList.value[idx] = new

        return new
    }

    fun getMed(medication: Medication): Medication? {
        val idx = medList.value.indexOf(medication)
        if (idx == -1) return null

        return medList.value[medList.value.indexOf(medication)]
    }
}