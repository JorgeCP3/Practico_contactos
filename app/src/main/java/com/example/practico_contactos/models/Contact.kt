package com.example.practico_contactos.models

import java.io.Serializable

data class Contact(
    val id: Int,
    val name: String,
    val last_name: String,
    val company: String?,
    val address: String?,
    val city: String?,
    val state: String?,
    val profile_picture: String?,
    val phones: List<Phone>,
    val emails: List<Email>
) : Serializable