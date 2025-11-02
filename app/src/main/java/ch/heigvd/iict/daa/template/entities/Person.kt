/**
 * DAA - labo3
 * Auteurs : Bleuer Rémy, Changanaqui Yoann, Rajadurai Thirusan
 * Date : 21.10.2025
 * Description : Classes de modèle représentant les personnes (étudiants et employés)
 */

package ch.heigvd.iict.daa.labo3

import java.text.DateFormat
import java.util.*

/**
 * Classe abstraite représentant une personne.
 * Sert de classe de base pour Student et Worker.
 *
 * @property name Nom de famille
 * @property firstName Prénom
 * @property birthDay Date de naissance
 * @property nationality Nationalité
 * @property email Adresse email
 * @property remark Commentaires supplémentaires
 */
abstract class Person(var name: String,
                      var firstName: String,
                      var birthDay : Calendar,
                      var nationality: String,
                      var email : String,
                      var remark : String) {

    /**
     * Retourne une représentation textuelle des attributs de la classe de base Person.
     *
     * @return String contenant les informations de base de la personne
     */
    protected fun superToString(): String {
        return "name: $name, firstName: $firstName, birthDay: ${dateFormatter.format(birthDay.time)}, nationality: $nationality, email: $email, remark: $remark"
    }

    companion object {
        val dateFormatter = DateFormat.getDateInstance() //depends on phone's current locale

        val exampleWorker = Worker(
            "Devost",
            "Chantal",
            Calendar.getInstance().apply {
                set(Calendar.YEAR, 1996)
                set(Calendar.MONTH, Calendar.JUNE)
                set(Calendar.DAY_OF_MONTH, 12)
            },
            "Suisse",
            "HEIG-VD",
            "Sciences",
            2,
            "c.devost@email.com",
            ""
        )
        val exampleStudent = Student(
            "Dreher",
            "Matthias",
            Calendar.getInstance().apply {
                set(Calendar.YEAR, 1998)
                set(Calendar.MONTH, Calendar.APRIL)
                set(Calendar.DAY_OF_MONTH, 8)
            },
            "Allemande",
            "HEIG-VD",
            2023,
            "m.dreher@email.com",
            "Lorem ipsum dolor sit amet, consectetur adipisicing elit, sed do eiusmod tempor incididunt ut labore et dolore magna aliqua."
        )
    }

}

/**
 * Classe représentant un étudiant.
 *
 * @property university Nom de l'université ou école
 * @property graduationYear Année du diplôme
 */
class Student(name: String,
              firstName: String,
              birthDay : Calendar,
              nationality: String,
              var university : String,
              var graduationYear : Int,
              email : String,
              remark: String) : Person(name, firstName, birthDay, nationality, email, remark) {

    override fun toString(): String {
        return "Student - ${super.superToString()}, university: $university, graduationYear: $graduationYear"
    }
}

/**
 * Classe représentant un employé.
 *
 * @property company Nom de l'entreprise
 * @property sector Secteur d'activité
 * @property experienceYear Années d'expérience
 */
class Worker(name: String,
             firstName: String,
             birthDay : Calendar,
             nationality: String,
             var company : String,
             var sector : String,
             var experienceYear : Int,
             email : String,
             remark: String) : Person(name, firstName, birthDay, nationality, email, remark) {

    override fun toString(): String {
        return "Worker - ${super.superToString()}, company: $company, sector: $sector, experienceYear: $experienceYear"
    }
}