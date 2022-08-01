package com.example.shopperista.models

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class SoldProduct(
    val user_id: String = "",
    val title: String = " ",
    val price: String = "",
    val sold_quantity: String = "",
    val image: String = "",
    val order_title: String = "",
    val order_date: String = " ",
    val sub_total_amount: String = "",
    val shipping_charge: String = "",
    val total_amount: String = "",
    val address: Address = Address(),
    var id: String = "",
) : Parcelable
