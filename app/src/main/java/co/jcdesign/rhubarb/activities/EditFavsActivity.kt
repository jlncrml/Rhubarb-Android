package co.jcdesign.rhubarb.activities

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.text.Editable
import android.view.Menu
import android.view.MenuItem
import android.widget.ProgressBar
import android.widget.Toast
import co.jcdesign.rhubarb.R
import co.jcdesign.rhubarb.models.CurrentUser
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import kotlinx.android.synthetic.main.activity_edit_favs.*

class EditFavsActivity : AppCompatActivity() {

    private var cancelMenuItem: MenuItem? = null
    private var doneMenuItem: MenuItem? = null

    private val editableFactory = Editable.Factory.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_favs)
        setup()
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

    private fun setup() {
        favFoodEditText.text = editableFactory.newEditable(CurrentUser.favFood)
        favMusicEditText.text = editableFactory.newEditable(CurrentUser.favMusic)
        favMovieEditText.text = editableFactory.newEditable(CurrentUser.favMovie)
        favBookEditText.text = editableFactory.newEditable(CurrentUser.favBook)
    }

    private fun saveChanges() {

        val favFood = favFoodEditText.text.toString()
        val favMusic = favMusicEditText.text.toString()
        val favMovie = favMovieEditText.text.toString()
        val favBook = favBookEditText.text.toString()

        cancelMenuItem?.isEnabled = false
        doneMenuItem?.actionView = ProgressBar(this)

        CurrentUser.updateFavs(favFood, favMusic, favMovie, favBook,
                OnSuccessListener {
                    finish()
                },
                OnFailureListener {
                    cancelMenuItem?.isEnabled = true
                    Toast.makeText(this, "Something went wrong. Please try again.", Toast.LENGTH_SHORT).show()
                })
    }
}
