package ru.skillbranch.devintensive.utils

object Utils {
    fun parseFullName(fullName: String?): Pair<String?, String?> {
        val parts: List<String>? = when(fullName) {
            "" -> null
            " " -> null
            else -> fullName?.split(" ")
        }

        val firstName = parts?.getOrNull(0)
        val lastName = parts?.getOrNull(1)

        return firstName to lastName
    }

    fun transliteration(payload: String, divider: String = " "): String {
//        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
        return payload
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var initials = when(firstName) {
            "" -> if (lastName != null) {""} else {null}
            " " -> if (lastName != null) {""} else {null}
            null -> ""
            else -> firstName?.substring(0..0)
        }

        initials += when(lastName) {
            "" -> if (initials == null || firstName != null) {""} else {null}
            " " -> if (initials == null || firstName != null) {""} else {null}
            null -> ""
            else -> lastName?.substring(0..0)
        }
        if (initials == "") {initials=null}
        return initials
    }
}