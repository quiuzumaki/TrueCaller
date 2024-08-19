package com.example.truecaller.defaultcall

data class ContactDetail(
    val name: String,
    val phoneNumber: String,
    val infoStatus: INFO_STATUS
)

enum class INFO_STATUS {
    KNOWN,
    UNKNOWN,
    SPAM
}