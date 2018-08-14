package co.jcdesign.rhubarb.activities

import android.app.DatePickerDialog
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.text.format.DateFormat
import android.view.Menu
import android.view.MenuItem
import android.widget.ArrayAdapter
import android.widget.DatePicker
import android.widget.ProgressBar
import android.widget.Toast
import co.jcdesign.rhubarb.R
import co.jcdesign.rhubarb.models.CurrentUser
import co.jcdesign.rhubarb.models.Gender
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_edit_info.*
import java.util.*

class EditInfoActivity : AppCompatActivity(), DatePickerDialog.OnDateSetListener {

    private var cancelMenuItem: MenuItem? = null
    private var doneMenuItem: MenuItem? = null

    private val editableFactory = Editable.Factory.getInstance()

    private val today100YearsAgo = {
        val calendar = Calendar.getInstance()
        calendar.add(Calendar.YEAR, -100)
        calendar.time
    }()

    private val genderArrayAdapter: ArrayAdapter<Gender> by lazy {
        ArrayAdapter<Gender>(this, android.R.layout.simple_spinner_item, Gender.values())
    }

    private val datePickerDialog: DatePickerDialog by lazy {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val dayOfMonth = calendar.get(Calendar.DAY_OF_MONTH)
        val datePickerDialog = DatePickerDialog(this, this, year, month, dayOfMonth)
        datePickerDialog.datePicker.minDate = today100YearsAgo.time
        datePickerDialog.datePicker.maxDate= Date().time
        datePickerDialog
    }

    private var birthday: Date? = CurrentUser.birthday?.toDate()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_info)
        setup()

        birthdayEditText.setOnClickListener {
            datePickerDialog.show()
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.edit_menu, menu)
        cancelMenuItem = menu?.findItem(R.id.edit_menu_cancel)
        doneMenuItem = menu?.findItem(R.id.edit_menu_done)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem?): Boolean {

        when (item?.itemId) {
            R.id.edit_menu_cancel -> { finish() }
            R.id.edit_menu_done -> { saveChanges() }
        }

        return super.onOptionsItemSelected(item)
    }

    override fun onDateSet(view: DatePicker?, year: Int, month: Int, dayOfMonth: Int) {
        val time = GregorianCalendar(year, month, dayOfMonth).timeInMillis
        birthday = Date(time)
        val dateFormat = DateFormat.getMediumDateFormat(this)
        birthdayEditText.text = editableFactory.newEditable(dateFormat.format(birthday))
    }

    private fun setup() {
        genderSpinner.adapter = genderArrayAdapter
        nameEditText.text = editableFactory.newEditable(CurrentUser.name)
        birthdayEditText.text = editableFactory.newEditable(CurrentUser.formattedBirthday(this))
        if (CurrentUser.gender != null) ( genderSpinner.setSelection(CurrentUser.gender!!.ordinal) )
        aboutYouEditText.text = editableFactory.newEditable(CurrentUser.aboutYou)
    }

    private fun saveChanges() {

        val name = nameEditText.text.toString()
        val gender = genderSpinner.selectedItem as Gender
        val aboutYou = aboutYouEditText.text.toString()

        cancelMenuItem?.isEnabled = false
        doneMenuItem?.actionView = ProgressBar(this)

        CurrentUser.updateInfo(name, birthday, gender, aboutYou,
                OnSuccessListener {
                    finish()
                },
                OnFailureListener {
                    cancelMenuItem?.isEnabled = true
                    Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
                })
    }
}
