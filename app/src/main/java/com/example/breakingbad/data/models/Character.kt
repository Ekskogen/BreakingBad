package com.example.breakingbad.data.models

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class Character (
    @SerializedName("char_id")
    val charId: Int? = null,
    val name: String? = null,
    val occupation: List<String>? = null,
    val img: String? = null,
    val birthday: String? = null,
    val status: String? = null,
    val nickname: String? = null,
    val appearance: List<Int>? = null,
    val portrayed: String? = null,
    val category: String? = null,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readString(),
        parcel.createStringArrayList(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.createIntArray()?.toList(),
        parcel.readString(),
        parcel.readString()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(charId)
        parcel.writeString(name)
        parcel.writeStringList(occupation)
        parcel.writeString(img)
        parcel.writeString(birthday)
        parcel.writeString(status)
        parcel.writeString(nickname)
        parcel.writeIntArray(appearance?.toIntArray())
        parcel.writeString(portrayed)
        parcel.writeString(category)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Character> {
        override fun createFromParcel(parcel: Parcel): Character {
            return Character(parcel)
        }

        override fun newArray(size: Int): Array<Character?> {
            return arrayOfNulls(size)
        }
    }
}