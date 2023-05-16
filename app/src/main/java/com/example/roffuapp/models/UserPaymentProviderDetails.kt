package com.example.roffuapp.models

import androidx.room.Embedded
import androidx.room.Relation
import com.example.roffuapp.models.PaymentProvider
import com.example.roffuapp.models.UserPaymentProvider

data class UserPaymentProviderDetails(
    @Embedded
    val userPaymentProvider: UserPaymentProvider,
    @Relation(
        parentColumn = "providerId",
        entityColumn = "id",
    )
    val paymentProvider: PaymentProvider,
)
