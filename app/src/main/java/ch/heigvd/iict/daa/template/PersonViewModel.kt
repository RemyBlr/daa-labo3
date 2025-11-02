/**
 * DAA - labo3
 * Auteurs : Bleuer Rémy, Changanaqui Yoann, Rajadurai Thirusan
 * Date : 21.10.2025
 * Description : ViewModel pour gérer la persistance des données lors des changements de configuration
 */

package ch.heigvd.iict.daa.template

import android.app.DatePickerDialog
import androidx.lifecycle.ViewModel
import ch.heigvd.iict.daa.labo3.Person

/**
 * ViewModel pour stocker les données de la personne de manière persistante
 * aux changements de configuration (comme la rotation de l'écran).
 *
 * @property person L'objet Person actuellement saisi dans le formulaire, null si aucune donnée
 */
class PersonViewModel : ViewModel() {
    var person: Person? = null
}
