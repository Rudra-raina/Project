package com.example.shopperista.models

import android.os.Parcel
import android.os.Parcelable

data class Product (
    val user_id : String? = "", // Id of the owner of the product
    val user_name : String? = "",
    val title : String? ="",
    val price : String? = "",
    val description : String? = "",
    val stock_quantity : String? = "",
    val image : String? = "",
    var image_uri : String? = "",
    val category : String? ="",
    var product_id : String? ="",

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString()
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(user_id)
        parcel.writeString(user_name)
        parcel.writeString(title)
        parcel.writeString(price)
        parcel.writeString(description)
        parcel.writeString(stock_quantity)
        parcel.writeString(image)
        parcel.writeString(image_uri)
        parcel.writeString(category)
        parcel.writeString(product_id)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Product> {
        override fun createFromParcel(parcel: Parcel): Product {
            return Product(parcel)
        }

        override fun newArray(size: Int): Array<Product?> {
            return arrayOfNulls(size)
        }
    }
}
//    val user_id : String? = "",
//    val user_name : String? = "",
//    val title : String? ="",
//    val price : String? = "",
//    val description : String? = "",
//    val stock_quantity : String? = "",
//    val image : String? = "",
//    val category : String? ="",
//    var product_id : String? ="",
//        ):Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString()
//    ) {
//    }
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(user_id)
//        parcel.writeString(user_name)
//        parcel.writeString(title)
//        parcel.writeString(price)
//        parcel.writeString(description)
//        parcel.writeString(stock_quantity)
//        parcel.writeString(image)
//        parcel.writeString(category)
//        parcel.writeString(product_id)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Product> {
//        override fun createFromParcel(parcel: Parcel): Product {
//            return Product(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Product?> {
//            return arrayOfNulls(size)
//        }
//    }
//}

//    val user_id : String? = "",
//    val user_name : String? = "",
//    val title : String? ="",
//    val price : String? = "",
//    val description : String? = "",
//    val stock_quantity : String? = "",
//    val image : String? = "",
//    var product_id : String? ="",
//    ) : Parcelable {
//    constructor(parcel: Parcel) : this(
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//        parcel.readString(),
//    )
//
//    override fun writeToParcel(parcel: Parcel, flags: Int) {
//        parcel.writeString(user_id)
//        parcel.writeString(user_name)
//        parcel.writeString(title)
//        parcel.writeString(price)
//        parcel.writeString(description)
//        parcel.writeString(stock_quantity)
//        parcel.writeString(image)
//        parcel.writeString(product_id)
//    }
//
//    override fun describeContents(): Int {
//        return 0
//    }
//
//    companion object CREATOR : Parcelable.Creator<Product> {
//        override fun createFromParcel(parcel: Parcel): Product {
//            return Product(parcel)
//        }
//
//        override fun newArray(size: Int): Array<Product?> {
//            return arrayOfNulls(size)
//        }
//    }
//}
