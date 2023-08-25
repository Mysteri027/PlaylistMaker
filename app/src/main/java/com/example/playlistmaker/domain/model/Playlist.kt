package com.example.playlistmaker.domain.model

import android.net.Uri
import android.os.Parcel
import android.os.Parcelable

data class Playlist(
    val id: Long = 0,
    val name: String,
    val description: String,
    val imageUri: Uri? = null,
    var trackList: String,
    var countTracks: Int,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readLong(),
        parcel.readString()!!,
        parcel.readString()!!,
        parcel.readParcelable(Uri::class.java.classLoader),
        parcel.readString()!!,
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeLong(id)
        parcel.writeString(name)
        parcel.writeString(description)
        parcel.writeParcelable(imageUri, flags)
        parcel.writeString(trackList)
        parcel.writeInt(countTracks)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Playlist> {
        override fun createFromParcel(parcel: Parcel): Playlist {
            return Playlist(parcel)
        }

        override fun newArray(size: Int): Array<Playlist?> {
            return arrayOfNulls(size)
        }
    }
}