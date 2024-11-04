package com.example.practico_contactos.ui.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.practico_contactos.R
import com.example.practico_contactos.models.Contact

class ContactDetailActivity : AppCompatActivity() {

    private lateinit var textViewName: TextView
    private lateinit var textViewLastName: TextView
    private lateinit var textViewCompany: TextView
    private lateinit var textViewAddress: TextView
    private lateinit var textViewCity: TextView
    private lateinit var textViewState: TextView
    private lateinit var textViewPhonesList: TextView
    private lateinit var textViewEmailsList: TextView
    private lateinit var buttonEdit: Button
    private lateinit var imageViewProfilePicture: ImageView
    private lateinit var buttonAddPhoto: Button

    private val REQUEST_IMAGE_GALLERY = 100
    private val REQUEST_IMAGE_CAMERA = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_detail)

        // Inicializar las vistas
        textViewName = findViewById(R.id.textViewName)
        textViewLastName = findViewById(R.id.textViewLastName)
        textViewCompany = findViewById(R.id.textViewCompany)
        textViewAddress = findViewById(R.id.textViewAddress)
        textViewCity = findViewById(R.id.textViewCity)
        textViewState = findViewById(R.id.textViewState)
        textViewPhonesList = findViewById(R.id.textViewPhonesList)
        textViewEmailsList = findViewById(R.id.textViewEmailsList)
        buttonEdit = findViewById(R.id.buttonEdit)
        imageViewProfilePicture = findViewById(R.id.imageViewProfilePicture)
        buttonAddPhoto = findViewById(R.id.buttonAddPhoto)

        // Obtener el contacto
        val contact = intent.getSerializableExtra("CONTACT") as? Contact
        contact?.let {
            displayContactDetails(it)
        }

        buttonAddPhoto.setOnClickListener {
            openImageSelectionDialog()
        }

        buttonEdit.setOnClickListener {
            editContact(contact)
        }
    }

    private fun displayContactDetails(contact: Contact) {
        textViewName.text = contact.name
        textViewLastName.text = contact.last_name
        textViewCompany.text = contact.company ?: "No disponible"
        textViewAddress.text = contact.address ?: "No disponible"
        textViewCity.text = contact.city ?: "No disponible"
        textViewState.text = contact.state ?: "No disponible"

        val phonesText = if (contact.phones.isNullOrEmpty()) {
            "No disponible"
        } else {
            contact.phones.joinToString("\n")
        }
        textViewPhonesList.text = phonesText

        val emailsText = if (contact.emails.isNullOrEmpty()) {
            "No disponible"
        } else {
            contact.emails.joinToString("\n")
        }
        textViewEmailsList.text = emailsText
    }

    private fun editContact(contact: Contact?) {
        contact?.let {
            val intent = Intent(this, NewEditContactActivity::class.java)
            intent.putExtra("CONTACT", it)
            startActivityForResult(intent, REQUEST_CODE_EDIT)
        }
    }

    private fun openImageSelectionDialog() {
        val options = arrayOf("Galería", "Cámara")
        val builder = android.app.AlertDialog.Builder(this)
        builder.setTitle("Selecciona una opción")
        builder.setItems(options) { _, which ->
            when (which) {
                0 -> openGallery()
                1 -> openCamera()
            }
        }
        builder.show()
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(intent, REQUEST_IMAGE_GALLERY)
    }

    private fun openCamera() {
        val intent = Intent(MediaStore.ACTION_IMAGE_CAPTURE)
        startActivityForResult(intent, REQUEST_IMAGE_CAMERA)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK) {
            when (requestCode) {
                REQUEST_IMAGE_GALLERY -> {
                    val imageUri: Uri? = data?.data
                    imageViewProfilePicture.setImageURI(imageUri)
                }
                REQUEST_IMAGE_CAMERA -> {
                    val imageBitmap = data?.extras?.get("data") as? Bitmap
                    imageBitmap?.let {
                        imageViewProfilePicture.setImageBitmap(it)
                    }
                }
                REQUEST_CODE_EDIT -> {
                    val updatedContact = data?.getSerializableExtra("UPDATED_CONTACT") as? Contact
                    updatedContact?.let {
                        displayContactDetails(it)
                    }
                }
            }
        }
    }

    companion object {
        const val REQUEST_CODE_EDIT = 1
    }
}
