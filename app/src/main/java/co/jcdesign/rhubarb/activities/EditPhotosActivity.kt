package co.jcdesign.rhubarb.activities

import android.app.Activity
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.os.Bundle
import android.provider.MediaStore
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.RecyclerView
import android.support.v7.widget.helper.ItemTouchHelper
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import co.jcdesign.rhubarb.R
import co.jcdesign.rhubarb.models.CurrentUser
import co.jcdesign.rhubarb.models.Photo
import co.jcdesign.rhubarb.util.OnDeletePhotoClickedInterface
import co.jcdesign.rhubarb.util.PhotoPicker
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CenterCrop
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.google.android.gms.tasks.OnFailureListener
import com.google.android.gms.tasks.OnSuccessListener
import com.xwray.groupie.*
import kotlinx.android.synthetic.main.activity_edit_photos.*
import kotlinx.android.synthetic.main.item_edit_photos.*
import kotlinx.android.synthetic.main.item_edit_photos.view.*

class EditPhotosActivity : AppCompatActivity(), OnDeletePhotoClickedInterface {

    private val addPhotoAlertDialogBuilder: AlertDialog by lazy {
        val builder = AlertDialog.Builder(this)
        builder.setItems(arrayOf("Take Photo", "Pick from Gallery"), DialogInterface.OnClickListener { dialog, which ->
            when (which) {
                0 -> {
                    takePhoto()
                }
                1 -> {
                    choosePhoto()
                }
            }
        })
        builder.create()
    }

    val photoPicker: PhotoPicker by lazy {
        PhotoPicker(this)
    }

    val section: Section by lazy {
        val section = Section()
        section.setHeader(HeaderItem())
        for (photo in photos) {
            val photoItem = PhotoItem(photo, baseContext, this)
            section.add(photoItem)
        }
        section
    }

    private var cancelMenuItem: MenuItem? = null
    private var doneMenuItem: MenuItem? = null
    private var photoDeviceUri = null

    private var photos = CurrentUser.photos

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_photos)

        addPhtoFloatingActionButton.setOnClickListener {
            addPhotoAlertDialogBuilder.show()
        }

        val adapter = GroupAdapter<ViewHolder>()
        adapter.spanCount = 2

        val gridLayoutManager = GridLayoutManager(this, adapter.spanCount)
        gridLayoutManager.spanSizeLookup = adapter.spanSizeLookup

        photosRecyclerView.layoutManager = GridLayoutManager(this, adapter.spanCount)

        val itemTouchHelperCallback = ItemTouchHelperCallback(adapter)

        val itemTouchHelper = ItemTouchHelper(itemTouchHelperCallback)
        itemTouchHelper.attachToRecyclerView(photosRecyclerView)

        adapter.add(section)

        photosRecyclerView.adapter = adapter
        photosRecyclerView.layoutManager = gridLayoutManager
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

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            0 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {

                }
            }
            1 -> {
                if (resultCode == Activity.RESULT_OK && data != null) {
                    val uri = data.data
                    val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
                    CurrentUser.uploadImage(bitmap,
                            OnSuccessListener { photo ->
                                photos.add(photo)
                                section.add(PhotoItem(photo, this, this))
                            },
                            OnFailureListener {
                                Toast.makeText(this, "Could not upload item_edit_photos. Please try again.", Toast.LENGTH_LONG).show()
                            })
                }
            }
        }
    }

    override fun onDeletePhotoClicked(position: Int) {
        section.remove(section.getGroup(position))
    }

    private fun saveChanges() {

    }

    private fun choosePhoto() {
        val intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent, 1)
    }

    private fun takePhoto() {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}

class ItemTouchHelperCallback(val adapter: GroupAdapter<ViewHolder>): ItemTouchHelper.Callback() {

    override fun isLongPressDragEnabled(): Boolean {
        return true
    }

    override fun getMovementFlags(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?): Int {
        val flags = ItemTouchHelper.UP or ItemTouchHelper.DOWN or ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        return makeMovementFlags(flags, 0)
    }

    override fun onMove(recyclerView: RecyclerView?, viewHolder: RecyclerView.ViewHolder?, target: RecyclerView.ViewHolder?): Boolean {
        if (viewHolder != null && target != null) {
            adapter.notifyItemMoved(viewHolder.adapterPosition, target.adapterPosition)
        }
        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder?, direction: Int) {

    }
}

class PhotoItem(private val photo: Photo, val context: Context, private val onDeletePhotoClickedInterface: OnDeletePhotoClickedInterface): Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

        val imageView = viewHolder.itemView.photoImageView

        viewHolder.itemView.deletePhotoImageButton.setOnClickListener {
            onDeletePhotoClickedInterface.onDeletePhotoClicked(position)
        }

        val requestOptions = RequestOptions()
        requestOptions
                .transforms(CenterCrop(), RoundedCorners(48))

        photo.ref?.downloadUrl?.addOnSuccessListener { uri ->
            Glide.with(context)
                    .load(uri)
                    .apply(requestOptions)
                    .into(imageView)
        }
    }

    override fun getLayout(): Int {
        return R.layout.item_edit_photos
    }

    override fun getSpanSize(spanCount: Int, position: Int): Int {
        return spanCount / 2
    }
}

class HeaderItem: Item<ViewHolder>() {

    override fun bind(viewHolder: ViewHolder, position: Int) {

    }

    override fun getLayout(): Int {
        return R.layout.item_edit_photos_header
    }
}