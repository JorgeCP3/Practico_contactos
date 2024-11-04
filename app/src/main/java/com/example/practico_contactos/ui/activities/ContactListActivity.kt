package com.example.practico_contactos.ui.activities

import android.content.Intent
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.practico_contactos.R
import com.example.practico_contactos.models.Contact
import com.example.practico_contactos.repository.ContactRepository
import com.example.practico_contactos.ui.adapters.ContactAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton

class ContactListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var contactAdapter: ContactAdapter
    private val contactRepository = ContactRepository()
    private val contacts = mutableListOf<Contact>()
    private lateinit var fabAddContact: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_contact_list)

        recyclerView = findViewById(R.id.recyclerViewContacts)
        recyclerView.layoutManager = LinearLayoutManager(this)

        contactAdapter = ContactAdapter(contacts, ::onContactClick, ::onContactDelete)
        recyclerView.adapter = contactAdapter
        fabAddContact = findViewById(R.id.fabAddContact)

        loadContacts()
        setupClickListeners()
        setupSwipeToDelete()
    }

    override fun onResume() {
        super.onResume()
        loadContacts()
    }

    private fun setupClickListeners() {
        fabAddContact.setOnClickListener {
            val intent = Intent(this, NewEditContactActivity::class.java)
            startActivity(intent)
        }
    }

    private fun loadContacts() {
        contactRepository.getContacts { fetchedContacts ->
            fetchedContacts?.let {
                contacts.clear()
                contacts.addAll(it)
                contactAdapter.notifyDataSetChanged()
            }
        }
    }

    private fun onContactClick(contact: Contact) {
        val intent = Intent(this, ContactDetailActivity::class.java).apply {
            putExtra("CONTACT", contact)
        }
        startActivity(intent)
    }

    private fun onContactDelete(contact: Contact) {
        val position = contacts.indexOf(contact)
        val alertDialog = AlertDialog.Builder(this)
            .setTitle("Eliminar contacto")
            .setMessage("¿Está seguro que desea eliminar este contacto?")
            .setPositiveButton("Sí") { dialog, which ->

                contactRepository.deleteContact(contact.id) { success ->
                    if (success) {
                        contacts.removeAt(position)
                        contactAdapter.notifyItemRemoved(position)
                    } else {
                    }
                }
            }
            .setNegativeButton("No") { dialog, which ->
                contactAdapter.notifyItemChanged(position)
            }
            .create()

        alertDialog.show()
    }



    private fun setupSwipeToDelete() {
        val itemTouchHelper = object : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val position = viewHolder.adapterPosition
                val contact = contacts[position]
                onContactDelete(contact)
            }

            override fun onChildDraw(c: Canvas, recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder, dX: Float, dY: Float, actionState: Int, isCurrentlyActive: Boolean) {
                val itemView = viewHolder.itemView
                if (dX < 0) {
                    val background = ColorDrawable(Color.RED)
                    background.setBounds(itemView.right + dX.toInt(), itemView.top, itemView.right, itemView.bottom)
                    background.draw(c)
                }
                super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
            }
        }

        ItemTouchHelper(itemTouchHelper).attachToRecyclerView(recyclerView)
    }
}
