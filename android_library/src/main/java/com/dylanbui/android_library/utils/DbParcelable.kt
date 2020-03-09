package com.dylanbui.android_library.utils

import android.os.Parcel
import android.os.Parcelable
import java.math.BigDecimal
import java.math.BigInteger
import java.util.*


/*

Base on : https://medium.com/@BladeCoder/reducing-parcelable-boilerplate-code-using-kotlin-741c3124a49a

Example :

class Person(val name: String, val age: Int) : DbParcelable {

    private constructor(p: Parcel) : this(
            name = p.readString(),
            age = p.readInt())

    override fun writeToParcel(dest: Parcel, flags: Int) {
        dest.writeString(name)
        dest.writeInt(age)
    }

    companion object {
        @JvmField val CREATOR = parcelableCreator(::Person)
    }
}

Extending Parcel :

We can now write an Enum to a Parcel using this syntax:
    dest.writeEnum(gender)

read enum
    gender = p.readEnum<Gender>()!!


These function calls can be grouped with apply(), run() or with() to reduce the verbosity to a minimum:

override fun writeToParcel(dest: Parcel, flags: Int) = with(dest) {
    writeString(name)
    writeInt(age)
    writeEnum(gender)
    writeDate(birthDate)
    writeTypedObjectCompat(address, flags)
}


* */


interface DbParcelable : Parcelable {
    override fun describeContents() = 0
    override fun writeToParcel(dest: Parcel, flags: Int)
}

// Creator factory functions

inline fun <reified T> parcelableCreator(
    crossinline create: (Parcel) -> T) =
    object : Parcelable.Creator<T> {
        override fun createFromParcel(source: Parcel) = create(source)
        override fun newArray(size: Int) = arrayOfNulls<T>(size)
    }

inline fun <reified T> parcelableClassLoaderCreator(
    crossinline create: (Parcel, ClassLoader) -> T) =
    object : Parcelable.ClassLoaderCreator<T> {
        override fun createFromParcel(source: Parcel, loader: ClassLoader) =
            create(source, loader)

        override fun createFromParcel(source: Parcel) =
            createFromParcel(source, T::class.java.classLoader!!)

        override fun newArray(size: Int) = arrayOfNulls<T>(size)
    }

// Parcel extensions

inline fun Parcel.readBoolean() = readInt() != 0

inline fun Parcel.writeBoolean(value: Boolean) = writeInt(if (value) 1 else 0)

inline fun <reified T : Enum<T>> Parcel.readEnum() =
    readString()?.let { enumValueOf<T>(it) }

inline fun <T : Enum<T>> Parcel.writeEnum(value: T?) =
    writeString(value?.name)

inline fun <T> Parcel.readNullable(reader: () -> T) =
    if (readInt() != 0) reader() else null

inline fun <T> Parcel.writeNullable(value: T?, writer: (T) -> Unit) {
    if (value != null) {
        writeInt(1)
        writer(value)
    } else {
        writeInt(0)
    }
}

fun Parcel.readDate() =
    readNullable { Date(readLong()) }

fun Parcel.writeDate(value: Date?) =
    writeNullable(value) { writeLong(it.time) }

fun Parcel.readBigInteger() =
    readNullable { BigInteger(createByteArray()) }

fun Parcel.writeBigInteger(value: BigInteger?) =
    writeNullable(value) { writeByteArray(it.toByteArray()) }

fun Parcel.readBigDecimal() =
    readNullable { BigDecimal(BigInteger(createByteArray()), readInt()) }

fun Parcel.writeBigDecimal(value: BigDecimal?) = writeNullable(value) {
    writeByteArray(it.unscaledValue().toByteArray())
    writeInt(it.scale())
}

fun <T : Parcelable> Parcel.readTypedObjectCompat(c: Parcelable.Creator<T>) =
    readNullable { c.createFromParcel(this) }

fun <T : Parcelable> Parcel.writeTypedObjectCompat(value: T?, parcelableFlags: Int) =
    writeNullable(value) { it.writeToParcel(this, parcelableFlags) }