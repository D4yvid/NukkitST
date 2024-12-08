package org.crimsonmc.cli

data class Option(
    val shortName: Char,
    val longName: String,
    val requireValue: Boolean,
    val execute: (value: String?) -> Unit
)
