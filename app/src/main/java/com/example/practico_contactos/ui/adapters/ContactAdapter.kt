package com.example.practico_contactos.ui.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.practico_contactos.R
import com.example.practico_contactos.models.Contact
import com.bumptech.glide.Glide

class ContactAdapter(
    private val contacts: MutableList<Contact>,
    private val onContactClick: (Contact) -> Unit,
    private val onContactDelete: (Contact) -> Unit
) : RecyclerView.Adapter<ContactAdapter.ContactViewHolder>() {

    inner class ContactViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewName: TextView = itemView.findViewById(R.id.textViewName)
        val textViewCompany: TextView = itemView.findViewById(R.id.textViewCompany)
        val imageViewProfile: ImageView = itemView.findViewById(R.id.imageViewProfile)

        init {
            itemView.setOnClickListener {
                val position = adapterPosition
                if (position != RecyclerView.NO_POSITION) {
                    onContactClick(contacts[position])
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ContactViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_contact, parent, false)
        return ContactViewHolder(view)
    }

    override fun onBindViewHolder(holder: ContactViewHolder, position: Int) {
        val contact = contacts[position]
        holder.textViewName.text = "${contact.name} ${contact.last_name}"
        holder.textViewCompany.text = contact.company ?: "Sin empresa"

        Glide.with(holder.itemView.context)
            .load(contact.profile_picture)
            .into(holder.imageViewProfile)
    }

    override fun getItemCount(): Int {
        return contacts.size
    }

    fun removeContact(contact: Contact) {
        val position = contacts.indexOf(contact)
        if (position != -1) {
            contacts.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
