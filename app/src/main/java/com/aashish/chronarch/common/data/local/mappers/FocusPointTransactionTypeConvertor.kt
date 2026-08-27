package com.aashish.chronarch.common.data.local.mappers

import androidx.room.TypeConverter
import com.aashish.chronarch.common.domain.model.FocusPointTransactionType

class FocusPointTransactionTypeConvertor {

    @TypeConverter
    fun fromType(type: FocusPointTransactionType): String = type.name

    @TypeConverter
    fun toType(name: String): FocusPointTransactionType =
        FocusPointTransactionType.entries.first { it.name == name }
}