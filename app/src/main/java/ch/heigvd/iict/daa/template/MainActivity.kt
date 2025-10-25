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

import ch.heigvd.iict.daa.labo3.*;

class MainActivity : AppCompatActivity() {

    private var person : Person? = null;

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
        emailField = findViewById(R.id.emailField)
        commentField = findViewById(R.id.commentField)
        workerChoice = findViewById(R.id.studentChoice)
        studentChoice = findViewById(R.id.workerChoice)
        companyField = findViewById(R.id.companyField)
        sectorSpinner = findViewById(R.id.sectorSpinner)
        experienceField = findViewById(R.id.experienceField)
        universityField = findViewById(R.id.universityField)
        gradYearField = findViewById(R.id.gradYearField)
        cancelBtn = findViewById(R.id.cancelBtn)
        okBtn = findViewById(R.id.okBtn)

        // Setup listeners
        cancelBtn.setOnClickListener { onCancel(); }
        okBtn.setOnClickListener { onValidate(); }
        workerChoice.setOnCheckedChangeListener { view, isChecked -> showWorkerFields() }
        studentChoice.setOnCheckedChangeListener { view, isChecked -> showStudentFields() }
    }

    // Load existing data into the Form (if any)
    fun onLoad() {
        if(person === null) { return; } // No data to load

        nameField.setText(person?.name);
        firstNameField.setText(person?.firstName);
        //birthdayField.setText(person?.birthDay);
        //nationalitySpinner.
        emailField.setText(person?.email);
        commentField.setText(person?.remark)

        if (workerChoice.isChecked) {
            var worker = person as Worker;
            companyField.setText(worker.company);
            //sectorSpinner.setSelection(worker.sector);
            experienceField.setText(worker.experienceYear);
        } else if(studentChoice.isChecked) {
            var student = person as Student;
            universityField.setText(student.university);
            gradYearField.setText(student.graduationYear);
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
        // if(isWorker) {
        // person = new Worker()...
        // } else { person = new Student()...
    }
}