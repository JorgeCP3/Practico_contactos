package com.example.practico_contactos.models

data class Email(
    val id: Int,
    val email: String,
    val persona_id: Int,
    val label: String
)