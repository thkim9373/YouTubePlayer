package com.hoony.youtubeplayer.data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Thumbnails(
    @SerializedName("default")
    val default: Default,
    @SerializedName("high")
    val high: High,
    @SerializedName("medium")
    val medium: Medium
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readParcelable(Default::class.java.classLoader)!!,
        parcel.readParcelable(High::class.java.classLoader)!!,
        parcel.readParcelable(Medium::class.java.classLoader)!!
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeParcelable(default, flags)
        parcel.writeParcelable(high, flags)
        parcel.writeParcelable(medium, flags)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Thumbnails> {
        override fun createFromParcel(parcel: Parcel): Thumbnails {
            return Thumbnails(parcel)
        }

        override fun newArray(size: Int): Array<Thumbnails?> {
            return arrayOfNulls(size)
        }
    }
}