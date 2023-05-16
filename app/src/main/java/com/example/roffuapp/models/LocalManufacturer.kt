package com.example.roffuapp.models

import androidx.room.Embedded
import androidx.room.Relation

class LocalManufacturer (
    @Embedded
    val manufacturer: Manufacturer,
    @Relation(
        parentColumn = "id",
        entityColumn = "manufacturerId",
        entity = Product::class,
    )
    val products: List<LocalProduct>,
)