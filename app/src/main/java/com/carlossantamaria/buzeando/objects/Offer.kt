package com.carlossantamaria.buzeando.objects

import android.os.Parcel
import android.os.Parcelable
import java.time.LocalDateTime
import java.time.ZoneOffset

data class Offer(
    val idOferta: Int,
    val idUsr: Int,
    val tipo: String,
    val fecha: LocalDateTime,
    val titulo: String,
    val descripcion: String,
    val coste: Double,
    val codPostal: String,
    val rutaImg1: String,
    val rutaImg2: String,
    val rutaImg3: String,
    val coordsLat: Double,
    val coordsLong: Double
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readInt(),
        parcel.readString().orEmpty(),
        LocalDateTime.ofEpochSecond(parcel.readLong(), 0, ZoneOffset.UTC),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readDouble(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readString().orEmpty(),
        parcel.readDouble(),
        parcel.readDouble()
    )

    override fun describeContents(): Int {
        return 0
    }

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeInt(idOferta)
        dest.writeInt(idUsr)
        dest.writeString(tipo)
        dest.writeLong(fecha.toEpochSecond(ZoneOffset.UTC)) // Convert LocalDateTime to epoch seconds
        dest.writeString(titulo)
        dest.writeString(descripcion)
        dest.writeDouble(coste)
        dest.writeString(codPostal)
        dest.writeString(rutaImg1)
        dest.writeString(rutaImg2)
        dest.writeString(rutaImg3)
        dest.writeDouble(coordsLat)
        dest.writeDouble(coordsLong)
    }

    companion object CREATOR : Parcelable.Creator<Offer> {
        override fun createFromParcel(parcel: Parcel): Offer {
            return Offer(parcel)
        }

        override fun newArray(size: Int): Array<Offer?> {
            return arrayOfNulls(size)
        }
    }
}