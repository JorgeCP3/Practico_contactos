package com.example.practico_contactos.api

import com.example.practico_contactos.models.Contact
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface ApiService {
    @GET("personas")
    fun getContactos(): Call<List<Contact>>

    @POST("personas")
    fun addContact(@Body contact: Contact): Call<Contact>

    @DELETE("personas/{id}")
    fun deleteContact(@Path("id") contactId: Int): Call<Unit>

    @PUT("personas/{id}")
    fun updateContact(@Path("id") contactId: Int, @Body contact: Contact): Call<Contact>

}