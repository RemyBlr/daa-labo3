/**
 * DAA - labo3
 * Auteurs : Bleuer Rémy, Changanaqui Yoann, Rajadurai Thirusan
 * Date : 21.10.2025
 * Description : Manage the view data
 */

package ch.heigvd.iict.daa.labo3

import android.app.DatePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.Spinner
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.ImageButton
import android.widget.TextView
import androidx.activity.viewModels

import java.util.Calendar
import java.text.SimpleDateFormat
import java.util.Locale
import ch.heigvd.iict.daa.template.PersonViewModel
import org.w3c.dom.Text

class MainActivity : AppCompatActivity() {

    private val personViewModel: PersonViewModel by viewModels()

    // Common fields
    private lateinit var nameField: EditText
    private lateinit var firstNameField: EditText
    private lateinit var birthdayField: EditText
    private lateinit var birthdayBtn: ImageButton
    private val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
    private var datePickerDialog: DatePickerDialog? = null
    private lateinit var nationalitySpinner: Spinner
    private lateinit var emailField: EditText
    private lateinit var commentField: EditText

    // Student or Worker selection
    private lateinit var studentChoice: RadioButton
    private lateinit var workerChoice: RadioButton

    // Worker fields
    private lateinit var companyLabel: TextView
    private lateinit var companyField: EditText
    private lateinit var sectorLabel: TextView
    private lateinit var sectorSpinner: Spinner
    private lateinit var experienceLabel: TextView
    private lateinit var experienceField: EditText

    // Student fields
        private lateinit var universityLabel : TextView
    private lateinit var universityField: EditText
    private lateinit var gradYearLabel : TextView
    private lateinit var gradYearField: EditText

    // Buttons
    private lateinit var cancelBtn: Button
    private lateinit var okBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        Log.d("Info", "CREATING ACTIVITY...")

        super.onCreate(savedInstanceState)

        // depuis android 15 (sdk 35), le mode edge2edge doit être activé
        enableEdgeToEdge()

        // on spécifie le layout à afficher
        setContentView(R.layout.activity_main)

        // comme edge2edge est activé, l'application doit garder un espace suffisant pour la barre système
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // la barre d'action doit être définie dans le layout, on la lie à l'activité
        setSupportActionBar(findViewById(R.id.toolbar))

        initViews();
        onLoad();
    }

    // Get fields components from the activity and set listeners to button
    fun initViews() {
        nameField = findViewById(R.id.lastNameField)
        firstNameField = findViewById(R.id.firstNameField)

        birthdayField = findViewById(R.id.birthdayField)
        birthdayBtn = findViewById(R.id.birthdayBtn)
        if(birthdayField.text.isEmpty()) {
            val today = Calendar.getInstance()
            birthdayField.setText(dateFormat.format(today.time))
        }

        // Empêcher le clavier de s’ouvrir
        birthdayField.showSoftInputOnFocus = false
        birthdayField.isFocusable = false
        birthdayField.isClickable = true

        nationalitySpinner = findViewById(R.id.nationalitySpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.nationalities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            nationalitySpinner.adapter = adapter
        }

        emailField = findViewById(R.id.emailField)
        commentField = findViewById(R.id.commentField)

        workerChoice = findViewById(R.id.workerChoice)
        studentChoice = findViewById(R.id.studentChoice)

        companyLabel = findViewById(R.id.companyLabel)
        companyField = findViewById(R.id.companyField)
        sectorLabel = findViewById(R.id.sectorLabel)
        sectorSpinner = findViewById(R.id.sectorSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.sectors,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            sectorSpinner.adapter = adapter
        }
        experienceLabel = findViewById(R.id.experienceLabel)
        experienceField = findViewById(R.id.experienceField)
        universityLabel = findViewById(R.id.universityLabel)
        universityField = findViewById(R.id.universityField)
        gradYearLabel = findViewById(R.id.gradYearLabel)
        gradYearField = findViewById(R.id.gradYearField)

        cancelBtn = findViewById(R.id.cancelBtn)
        okBtn = findViewById(R.id.okBtn)

        // Setup listeners ------------------------------------
        cancelBtn.setOnClickListener { onCancel(); }
        okBtn.setOnClickListener { onValidate(); }

        workerChoice.setOnCheckedChangeListener { view, isChecked -> if(isChecked) showWorkerFields() }
        studentChoice.setOnCheckedChangeListener { view, isChecked -> if(isChecked) showStudentFields() }

        // Ouvrir le DatePicker au clic sur champ ou bouton
        birthdayField.setOnClickListener { openDatePickerDialog() }
        birthdayBtn.setOnClickListener { openDatePickerDialog() }
    }

    // Load existing data into the Form (if any)
    fun onLoad() {
        val person = personViewModel.person ?: return // No data to load

        Log.d("Info", "Retrieving existing data...\n - " + person + "\n" + (person == null).toString());

        nameField.setText(person.name)
        firstNameField.setText(person.firstName)
        birthdayField.setText(dateFormat.format(person.birthDay.time))
        nationalitySpinner.setSelection(0)
        emailField.setText(person.email)
        commentField.setText(person.remark)

        when (person) {
            is Worker -> {
                workerChoice.isChecked = true
                companyField.setText(person.company)
                sectorSpinner.setSelection(0)
                experienceField.setText(person.experienceYear.toString())
                showWorkerFields()
            }
            is Student -> {
                studentChoice.isChecked = true
                universityField.setText(person.university)
                gradYearField.setText(person.graduationYear.toString())
                showStudentFields()
            }
        }
    }

    private fun showWorkerFields() {
        Log.d("Info", "Worker selected.");
        companyLabel.visibility = View.VISIBLE
        companyField.visibility = View.VISIBLE
        sectorLabel.visibility = View.VISIBLE
        sectorSpinner.visibility = View.VISIBLE
        experienceLabel.visibility = View.VISIBLE
        experienceField.visibility = View.VISIBLE

        universityLabel.visibility = View.GONE
        universityField.visibility = View.GONE
        gradYearLabel.visibility = View.GONE
        gradYearField.visibility = View.GONE
    }

    private fun showStudentFields() {
        Log.d("Info", "Student selected.");
        companyLabel.visibility = View.GONE
        companyField.visibility = View.GONE
        sectorLabel.visibility = View.GONE
        sectorSpinner.visibility = View.GONE
        experienceLabel.visibility = View.GONE
        experienceField.visibility = View.GONE

        universityLabel.visibility = View.VISIBLE
        universityField.visibility = View.VISIBLE
        gradYearLabel.visibility = View.VISIBLE
        gradYearField.visibility = View.VISIBLE
    }

    // Empty Form fields
    fun onCancel() {
        Log.d("Info", "Emptying data from Form");

        nameField.setText("")
        firstNameField.setText("")
        birthdayField.setText("")
        nationalitySpinner.setSelection(0)
        emailField.setText("")
        commentField.setText("")
        companyField.setText("")
        sectorSpinner.setSelection(0)
        experienceField.setText("")
        universityField.setText("")
        gradYearField.setText("")
        workerChoice.isChecked = false
        studentChoice.isChecked = false
        personViewModel.person = null

        companyField.visibility = View.GONE
        sectorSpinner.visibility = View.GONE
        experienceField.visibility = View.GONE
        universityField.visibility = View.GONE
        gradYearField.visibility = View.GONE
    }

    // Display log of Person
    private fun onValidate() {
        Log.d("Info", "Checking input validity...")

        val name = nameField.text.toString()
        if (name.isBlank()) {
            nameField.error = "Name cannot be empty"
            return
        }

        val firstname = firstNameField.text.toString()
        if (firstname.isBlank()) {
            firstNameField.error = "First name cannot be empty"
            return
        }

        val birthdayStr = birthdayField.text.toString()
        val birthday = Calendar.getInstance()
        try {
            val date = dateFormat.parse(birthdayStr)
            if (date != null) birthday.time = date
            else {
                birthdayField.error = "Invalid date format (dd/MM/yyyy)"
                return
            }
        } catch (e: Exception) {
            birthdayField.error = "Invalid date format (dd/MM/yyyy)"
            return
        }

        val email = emailField.text.toString()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.error = "Invalid email address"
            return
        }

        if (!workerChoice.isChecked && !studentChoice.isChecked) {
            return
        }

        val nationality = nationalitySpinner.selectedItem?.toString() ?: ""
        val comment = commentField.text.toString()

        val newPerson: Person = if (workerChoice.isChecked) {
            val company = companyField.text.toString()
            val sector = sectorSpinner.selectedItem?.toString() ?: ""
            val experience = experienceField.text.toString().toIntOrNull() ?: 0
            Worker(name, firstname, birthday, nationality, company, sector, experience, email, comment)
        } else {
            val university = universityField.text.toString()
            val gradYear = gradYearField.text.toString().toIntOrNull() ?: 0
            Student(name, firstname, birthday, nationality, university, gradYear, email, comment)
        }

        personViewModel.person = newPerson
        Log.d("Info", "Person created:\n\t${personViewModel.person}")
    }

    // Source: https://www.geeksforgeeks.org/android/datepickerdialog-in-android/
    private fun openDatePickerDialog() {
        val calendar = Calendar.getInstance()

        // Si une date est déjà saisie, la reprendre
        val currentText = birthdayField.text.toString()
        if (currentText.isNotEmpty()) {
            try {
                val date = dateFormat.parse(currentText)
                if (date != null) calendar.time = date
            } catch (_: Exception) {}
        }

        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePicker = DatePickerDialog(
            this,
            { _, year, month, day ->
                val calendar = Calendar.getInstance()
                calendar.set(year, month, day)
                birthdayField.setText(dateFormat.format(calendar.time))
                datePickerDialog = null // supprimer l'ancienne référence
            },
            year,
            month,
            day
        )

        // empêcher la sélection d'une date future
        datePicker.datePicker.maxDate = System.currentTimeMillis()

        // En cas d'anulation supprimer cette référence
        datePicker.setOnCancelListener {
            datePickerDialog = null
        }
        datePickerDialog = datePicker

        datePicker.show()
    }

    override fun onDestroy() {
        super.onDestroy()
        datePickerDialog?.dismiss()
    }
}
