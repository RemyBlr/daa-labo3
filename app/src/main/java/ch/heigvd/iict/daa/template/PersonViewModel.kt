package ch.heigvd.iict.daa.template

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModel
import ch.heigvd.iict.daa.labo3.Person

/**
 * ViewModel pour stocker les données de la personne de manière persistante
 * aux changements de configuration (comme la rotation de l'écran).
 */
class PersonViewModel : ViewModel() {
    var person: Person? = null
}
