package com.example.repetitiontest.database.users

import android.os.Parcel
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    var id: Long? = null,
    @ColumnInfo(name = "full_name")
    var fullName: String? = null,
    @ColumnInfo(name = "phone_number")
    var phoneNumber: String? = null,
    @ColumnInfo(name = "email")
    var email: String? = null,
    @ColumnInfo(name = "password")
    var password: String? = null,
    @ColumnInfo(name = "language")
    var language: Int? = 0,
    @ColumnInfo(name = "balance")
    var balance: Int? = null,
    @ColumnInfo(name = "invited_user")
    var invitedUser: Long? = null,
    @ColumnInfo(name = "profile_photo_url")
    var profilePhotoUrl: String? = null,
    @ColumnInfo(name = "user_role")
    var userRole: Int? = 0
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Int::class.java.classLoader) as? Int,
        parcel.readValue(Long::class.java.classLoader) as? Long,
        parcel.readString(),
        parcel.readValue(Int::class.java.classLoader) as? Int
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeValue(id)
        parcel.writeString(fullName)
        parcel.writeString(phoneNumber)
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeValue(language)
        parcel.writeValue(balance)
        parcel.writeValue(invitedUser)
        parcel.writeString(profilePhotoUrl)
        parcel.writeValue(userRole)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<UserEntity> {
        override fun createFromParcel(parcel: Parcel): UserEntity {
            return UserEntity(parcel)
        }

        override fun newArray(size: Int): Array<UserEntity?> {
            return arrayOfNulls(size)
        }
    }
}