package com.example.roffuapp.sealed

sealed class Orientation {
    object Vertical : Orientation()
    object Horizontal : Orientation()
}