package com.example.practico_contactos.models

data class Phone(
    val id: Int,
    val number: String,
    val persona_id: Int,
    val label: String
)