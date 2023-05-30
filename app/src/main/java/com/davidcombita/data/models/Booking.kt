package com.davidcombita.data.models

import java.util.*

data class Booking (
    val idBooking: Int,
    val nameUser: String,
    val emailUser: String,
    val phoneUser: String,
    val dateBooking: Date,
    val idTattoo: Int,
)