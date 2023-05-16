package com.example.roffuapp.sealed

sealed class AdvertisementType{
    object Store: AdvertisementType()
    object Product: AdvertisementType()
}