package com.example.repetitiontest.database

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users_table")
data class UserEntity(
    @PrimaryKey
    @ColumnInfo(name = "id")
    val id: Long,
    @ColumnInfo(name = "first_name")
    val firstName: String,
    @ColumnInfo(name = "last_name")
    val lastName: String,
    @ColumnInfo(name = "phone_number")
    val phoneNumber: String,
    @ColumnInfo(name = "email")
    val email: String? = null,
    @ColumnInfo(name = "language")
    val language: Int? = null,
    @ColumnInfo(name = "balance")
    val balance: Int? = null,
    @ColumnInfo(name = "invited_user")
    val invitedUser: Long? = null,
    @ColumnInfo(name = "profile_photo_url")
    val profilePhotoUrl: String? = null,
    @ColumnInfo(name = "user_role")
    val userRole: Int? = null
)