package com.example.practico_contactos.ui.activities

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.practico_contactos.R
import com.example.practico_contactos.repository.ContactRepository

class MainActivity : AppCompatActivity() {
    private lateinit var contactRepository: ContactRepository

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        contactRepository = ContactRepository()
        loadContacts()
    }

    private fun loadContacts() {
        contactRepository.getContacts { contacts ->
            if (contacts != null) {
            } else {
            }
        }
    }
}