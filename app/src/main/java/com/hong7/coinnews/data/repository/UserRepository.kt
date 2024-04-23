package com.hong7.coinnews.data.repository

import java.util.Calendar

interface UserRepository {

    fun getLastUpdateDate(): Calendar?

    fun updateLastUpdateDate(date: Calendar)
}