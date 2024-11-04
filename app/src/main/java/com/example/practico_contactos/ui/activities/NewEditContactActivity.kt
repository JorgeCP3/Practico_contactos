package com.example.practico_contactos.ui.activities

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.practico_contactos.R
import com.example.practico_contactos.models.Contact
import com.example.practico_contactos.models.Email
import com.example.practico_contactos.models.Phone
import com.example.practico_contactos.repository.ContactRepository

class NewEditContactActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextLastName: EditText
    private lateinit var editTextCompany: EditText
    private lateinit var editTextAddress: EditText
    private lateinit var editTextCity: EditText
    private lateinit var editTextState: EditText
    private lateinit var buttonSave: Button
    private lateinit var buttonAddPhone: Button
    private lateinit var buttonAddEmail: Button
    private lateinit var phoneContainer: LinearLayout
    private lateinit var emailContainer: LinearLayout

    private val contactRepository = ContactRepository()
    private var contact: Contact? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_edit_contact)

        editTextName = findViewById(R.id.editTextName)
        editTextLastName = findViewById(R.id.editTextLastName)
        editTextCompany = findViewById(R.id.editTextCompany)
        editTextAddress = findViewById(R.id.editTextAddress)
        editTextCity = findViewById(R.id.editTextCity)
        editTextState = findViewById(R.id.editTextState)
        buttonSave = findViewById(R.id.buttonSave)
        buttonAddPhone = findViewById(R.id.buttonAddPhone)
        buttonAddEmail = findViewById(R.id.buttonAddEmail)
        phoneContainer = findViewById(R.id.phoneContainer)
        emailContainer = findViewById(R.id.emailContainer)

        // Verifica si estamos editando un contacto existente
        contact = intent.getSerializableExtra("CONTACT") as? Contact
        contact?.let {
            populateFields(it)
        }

        buttonSave.setOnClickListener {
            saveContact()
        }

        buttonAddPhone.setOnClickListener {
            addPhoneField()
        }

        buttonAddEmail.setOnClickListener {
            addEmailField()
        }
    }

    private fun populateFields(contact: Contact) {
        editTextName.setText(contact.name)
        editTextLastName.setText(contact.last_name)
        editTextCompany.setText(contact.company)
        editTextAddress.setText(contact.address)
        editTextCity.setText(contact.city)
        editTextState.setText(contact.state)

        // Poblar los teléfonos
        contact.phones.forEach { phone ->
            val phoneView = LayoutInflater.from(this).inflate(R.layout.item_phone, phoneContainer, false)
            val editTextPhone = phoneView.findViewById<EditText>(R.id.editTextPhoneNumber)
            val editTextPhoneLabel = phoneView.findViewById<EditText>(R.id.editTextPhoneLabel)
            editTextPhone.setText(phone.number)
            editTextPhoneLabel.setText(phone.label)
            phoneContainer.addView(phoneView)
        }

        // Poblar los correos
        contact.emails.forEach { email ->
            val emailView = LayoutInflater.from(this).inflate(R.layout.item_email, emailContainer, false)
            val editTextEmail = emailView.findViewById<EditText>(R.id.editTextEmail)
            val editTextEmailLabel = emailView.findViewById<EditText>(R.id.editTextEmailLabel)
            editTextEmail.setText(email.email)
            editTextEmailLabel.setText(email.label)
            emailContainer.addView(emailView)
        }
    }

    private fun addPhoneField() {
        val phoneView = LayoutInflater.from(this).inflate(R.layout.item_phone, phoneContainer, false)
        phoneContainer.addView(phoneView)
    }

    private fun addEmailField() {
        val emailView = LayoutInflater.from(this).inflate(R.layout.item_email, emailContainer, false)
        emailContainer.addView(emailView)
    }

    private fun saveContact() {
        val name = editTextName.text.toString().trim()
        val lastName = editTextLastName.text.toString().trim()
        val company = editTextCompany.text.toString().trim()
        val address = editTextAddress.text.toString().trim()
        val city = editTextCity.text.toString().trim()
        val state = editTextState.text.toString().trim()

        if (name.isEmpty() || lastName.isEmpty()) {
            Toast.makeText(this, "Por favor, completa todos los campos", Toast.LENGTH_SHORT).show()
            return
        }

        val phones = mutableListOf<Phone>()
        for (i in 0 until phoneContainer.childCount) {
            val phoneView = phoneContainer.getChildAt(i)
            val editTextPhone = phoneView.findViewById<EditText>(R.id.editTextPhoneNumber)
            val editTextPhoneLabel = phoneView.findViewById<EditText>(R.id.editTextPhoneLabel)
            val phoneNumber = editTextPhone.text.toString().trim()
            val phoneLabel = editTextPhoneLabel.text.toString().trim()
            if (phoneNumber.isNotEmpty()) {
                phones.add(Phone(id = 0, number = phoneNumber, persona_id = contact?.id ?: 0, label = phoneLabel))
                // log para ver cuándo se está guardando un teléfono
                Log.d("NewEditContactActivity", "Guardando teléfono: $phoneNumber con etiqueta: $phoneLabel")
            }
        }

        val emails = mutableListOf<Email>()
        for (i in 0 until emailContainer.childCount) {
            val emailView = emailContainer.getChildAt(i)
            val editTextEmail = emailView.findViewById<EditText>(R.id.editTextEmail)
            val editTextEmailLabel = emailView.findViewById<EditText>(R.id.editTextEmailLabel)
            val email = editTextEmail.text.toString().trim()
            val emailLabel = editTextEmailLabel.text.toString().trim()
            if (email.isNotEmpty()) {
                emails.add(Email(id = 0, email = email, persona_id = contact?.id ?: 0, label = emailLabel))
            }
        }

        val newContact = Contact(
            id = contact?.id ?: 0,
            name = name,
            last_name = lastName,
            company = company,
            address = address,
            city = city,
            state = state,
            profile_picture = contact?.profile_picture,
            phones = phones,
            emails = emails
        )

        // Actualizar o agregar
        if (contact != null) {
            contactRepository.updateContact(newContact) { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(this, "Contacto actualizado", Toast.LENGTH_SHORT).show()
                    Log.d("NewEditContactActivity", "Contacto actualizado: ${newContact.name}")
                    finish()
                } else {
                    Toast.makeText(this, "Error al actualizar el contacto", Toast.LENGTH_SHORT).show()
                }
            }
        } else {
            contactRepository.addContact(newContact) { isSuccess ->
                if (isSuccess) {
                    Toast.makeText(this, "Contacto guardado con éxito", Toast.LENGTH_SHORT).show()
                    Log.d("NewEditContactActivity", "Contacto guardado: ${newContact.name}")
                    finish()
                } else {
                    Toast.makeText(this, "Error al guardar el contacto", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }
}
