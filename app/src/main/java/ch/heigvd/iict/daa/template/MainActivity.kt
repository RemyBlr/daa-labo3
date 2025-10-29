/**
 * DAA - labo3
 * Auteurs : Bleuer Rémy, Changanaqui Yoann, Rajadurai Thirusan
 * Date : 21.10.2025
 * Description : Manage the view data
 */

package ch.heigvd.iict.daa.labo3

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

import java.util.Calendar

import java.text.SimpleDateFormat
import java.util.Locale
class MainActivity : AppCompatActivity() {

    private var person: Person? = null;

    // Common fields
    private lateinit var nameField: EditText
    private lateinit var firstNameField: EditText
    private lateinit var birthdayField: EditText
    private lateinit var nationalitySpinner: Spinner
    private lateinit var emailField: EditText
    private lateinit var commentField: EditText

    // Student or Worker selection
    private lateinit var studentChoice: RadioButton
    private lateinit var workerChoice: RadioButton

    // Worker fields
    private lateinit var companyField: EditText
    private lateinit var sectorSpinner: Spinner
    private lateinit var experienceField: EditText

    // Student fields
    private lateinit var universityField: EditText
    private lateinit var gradYearField: EditText

    // Buttons
    private lateinit var cancelBtn: Button
    private lateinit var okBtn: Button

    override fun onCreate(savedInstanceState: Bundle?) {
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
        nationalitySpinner = findViewById(R.id.nationalitySpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.nationalities,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            nationalitySpinner.adapter = adapter
        }
        emailField = findViewById(R.id.emailField)
        commentField = findViewById(R.id.commentField)
        workerChoice = findViewById(R.id.workerChoice)
        studentChoice = findViewById(R.id.studentChoice)
        companyField = findViewById(R.id.companyField)
        sectorSpinner = findViewById(R.id.sectorSpinner)
        ArrayAdapter.createFromResource(
            this,
            R.array.sectors,
            android.R.layout.simple_spinner_item
        ).also { adapter ->
            // Specify the layout to use when the list of choices appears.
            adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            // Apply the adapter to the spinner.
            sectorSpinner.adapter = adapter
        }
        experienceField = findViewById(R.id.experienceField)
        universityField = findViewById(R.id.universityField)
        gradYearField = findViewById(R.id.gradYearField)
        cancelBtn = findViewById(R.id.cancelBtn)
        okBtn = findViewById(R.id.okBtn)

        Log.d("Init", "Init view data");

        // Setup listeners
        cancelBtn.setOnClickListener { onCancel(); }
        okBtn.setOnClickListener { onValidate(); }

        workerChoice.setOnCheckedChangeListener { view, isChecked -> if(isChecked) showWorkerFields() }
        studentChoice.setOnCheckedChangeListener { view, isChecked -> if(isChecked) showStudentFields() }
    }

    // Load existing data into the Form (if any)
    fun onLoad() {
        if (person == null) return; // No data to load

        nameField.setText(person?.name);
        firstNameField.setText(person?.firstName);
        //birthdayField.setText(person?.birthDay);
        //nationalitySpinner.
        emailField.setText(person?.email);
        commentField.setText(person?.remark)

        if (person is Worker) {
            workerChoice.isChecked = true
            val worker = person as Worker;
            companyField.setText(worker.company);
            //sectorSpinner.setSelection(worker.sector);
            experienceField.setText(worker.experienceYear.toString());
        } else if (person is Student) {
            studentChoice.isChecked = true
            val student = person as Student;
            universityField.setText(student.university);
            gradYearField.setText(student.graduationYear.toString());
        }
    }

    private fun showWorkerFields() {
        companyField.visibility = View.VISIBLE
        sectorSpinner.visibility = View.VISIBLE
        experienceField.visibility = View.VISIBLE

        universityField.visibility = View.GONE
        gradYearField.visibility = View.GONE
    }

    private fun showStudentFields() {
        companyField.visibility = View.GONE
        sectorSpinner.visibility = View.GONE
        experienceField.visibility = View.GONE

        universityField.visibility = View.VISIBLE
        gradYearField.visibility = View.VISIBLE
    }

    // Empty Form fields
    fun onCancel() {
        Log.d("Cancel", "Emptying data from Form");

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
    }

    // Display log of Person
    fun onValidate() {
        val name = nameField.text.toString()
        if (name.isBlank()) {
            nameField.error = "Name cannot be empty"
            return;
        }

        val firstname = firstNameField.text.toString()
        if (firstname.isBlank()) {
            firstNameField.error = "First name cannot be empty"
            return;
        }

        val birthdayStr = birthdayField.text.toString()
        val format = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        format.isLenient = false 
        val birthday: Calendar = Calendar.getInstance()
        try {
            val date = format.parse(birthdayStr)
            if (date != null) {
                birthday.time = date
            } else {
                birthdayField.error = "Invalid date format (dd/MM/yyyy)"
                return;
            }
        } catch (e: Exception) {
            birthdayField.error = "Invalid date format (dd/MM/yyyy)"
            return;
        }


        val email = emailField.text.toString()
        if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailField.error = "Invalid email address";
            return;
        }

        if (!workerChoice.isChecked && !studentChoice.isChecked) { return; }

        val nationality: String = nationalitySpinner.selectedItem.toString();
        val comment: String = commentField.text.toString();

        if (workerChoice.isChecked) {
            var company: String = companyField.getText().toString();
            var sector: String = sectorSpinner.selectedItem.toString();
            var experience: Int = experienceField.getText().toString().toIntOrNull() ?: 0;

            person = Worker(
                name,
                firstname,
                birthday,
                nationality,
                company,
                sector,
                experience,
                email,
                comment
            );
        } else if (studentChoice.isChecked) {
            var university: String = universityField.getText().toString();
            var gradyear: Int = gradYearField.getText().toString().toIntOrNull() ?: 0;

            person = Student(
                name,
                firstname,
                birthday,
                nationality,
                university,
                gradyear,
                email,
                comment
            );
        }

        Log.d("Person", person.toString());
    }
}
