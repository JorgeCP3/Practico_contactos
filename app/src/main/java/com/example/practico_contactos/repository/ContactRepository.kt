package com.example.practico_contactos.repository

import android.util.Log
import com.example.practico_contactos.models.Contact
import com.example.practico_contactos.api.RetrofitInstance
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ContactRepository {
    fun getContacts(callback: (List<Contact>?) -> Unit) {
        RetrofitInstance.api.getContactos().enqueue(object : Callback<List<Contact>> {
            override fun onResponse(call: Call<List<Contact>>, response: Response<List<Contact>>) {
                if (response.isSuccessful) {
                    callback(response.body())
                } else {
                    Log.e("ContactRepository", "Error al obtener contactos: ${response.errorBody()?.string()}")
                    callback(null) // Retorna null en caso de error
                }
            }

            override fun onFailure(call: Call<List<Contact>>, t: Throwable) {
                Log.e("ContactRepository", "Fallo en la llamada a la API: ${t.message}")
                callback(null) // Retorna null en caso de fallo
            }
        })
    }

    fun addContact(contact: Contact, callback: (Boolean) -> Unit) {
        RetrofitInstance.api.addContact(contact).enqueue(object : Callback<Contact> {
            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                if (response.isSuccessful) {
                    callback(true) // Retorna true si se agregó exitosamente
                } else {
                    Log.e("ContactRepository", "Error al agregar contacto: ${response.errorBody()?.string()}")
                    callback(false) // Retorna false en caso de error
                }
            }

            override fun onFailure(call: Call<Contact>, t: Throwable) {
                Log.e("ContactRepository", "Fallo en la llamada a la API: ${t.message}")
                callback(false) // Retorna false en caso de fallo
            }
        })
    }

    fun deleteContact(contactId: Int, onResult: (Boolean) -> Unit) {
        RetrofitInstance.api.deleteContact(contactId).enqueue(object : Callback<Unit> {
            override fun onResponse(call: Call<Unit>, response: Response<Unit>) {
                if (response.isSuccessful) {
                    onResult(true) // Indica que la eliminación fue exitosa
                } else {
                    Log.e("ContactRepository", "Error al eliminar contacto: ${response.errorBody()?.string()}")
                    onResult(false) // Indica que hubo un error
                }
            }

            override fun onFailure(call: Call<Unit>, t: Throwable) {
                Log.e("ContactRepository", "Fallo en la llamada a la API: ${t.message}")
                onResult(false) // Indica que hubo un fallo en la conexión
            }
        })
    }

    fun updateContact(contact: Contact, onResult: (Boolean) -> Unit) {
        RetrofitInstance.api.updateContact(contact.id, contact).enqueue(object : Callback<Contact> {
            override fun onResponse(call: Call<Contact>, response: Response<Contact>) {
                if (response.isSuccessful) {
                    onResult(true) // Indica que la actualización fue exitosa
                } else {
                    Log.e("ContactRepository", "Error al actualizar contacto: ${response.errorBody()?.string()}")
                    onResult(false) // Indica que hubo un error
                }
            }

            override fun onFailure(call: Call<Contact>, t: Throwable) {
                Log.e("ContactRepository", "Fallo en la llamada a la API: ${t.message}")
                onResult(false) // Indica que hubo un fallo en la conexión
            }
        })
    }
}
