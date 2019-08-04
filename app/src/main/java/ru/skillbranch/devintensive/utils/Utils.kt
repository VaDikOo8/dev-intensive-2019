package ru.skillbranch.devintensive.utils

import android.content.res.Resources

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
        var sb = StringBuilder(payload.trim().length)
        for (i: Int in 1..payload.length) {
            val l = payload.substring(i - 1, i)
            sb.append(when (l) {
                "а" -> "a"
                "б" -> "b"
                "в" -> "v"
                "г" -> "g"
                "д" -> "d"
                "е" -> "e"
                "ё" -> "e"
                "ж" -> "zh"
                "з" -> "z"
                "и" -> "i"
                "й" -> "i"
                "к" -> "k"
                "л" -> "l"
                "м" -> "m"
                "н" -> "n"
                "о" -> "o"
                "п" -> "p"
                "р" -> "r"
                "с" -> "s"
                "т" -> "t"
                "у" -> "u"
                "ф" -> "f"
                "х" -> "h"
                "ц" -> "c"
                "ч" -> "ch"
                "ш" -> "sh"
                "щ" -> "sh'"
                "ъ" -> ""
                "ы" -> "i"
                "ь" -> ""
                "э" -> "e"
                "ю" -> "yu"
                "я" -> "ya"
                "А" -> "A"
                "Б" -> "B"
                "В" -> "V"
                "Г" -> "G"
                "Д" -> "D"
                "Е" -> "E"
                "Ё" -> "E"
                "Ж" -> "Zh"
                "З" -> "Z"
                "И" -> "I"
                "Й" -> "I"
                "К" -> "K"
                "Л" -> "L"
                "М" -> "M"
                "Н" -> "N"
                "О" -> "O"
                "П" -> "P"
                "Р" -> "R"
                "С" -> "S"
                "Т" -> "T"
                "У" -> "U"
                "Ф" -> "F"
                "Х" -> "Kh"
                "Ц" -> "C"
                "Ч" -> "Ch"
                "Ш" -> "Sh"
                "Щ" -> "Sch"
                "Ъ" -> "'"
                "Ы" -> "Y"
                "Э" -> "E"
                "Ю" -> "Yu"
                "Я" -> "Ya"
                else -> l
            })
        }
        var payload = sb.toString()
        return payload.trim().replace(" ", divider)
    }

    fun toInitials(firstName: String?, lastName: String?): String? {
        var initials = when (firstName?.trim()) {
            "" -> if (lastName != null) {
                ""
            } else {
                null
            }
            " " -> if (lastName != null) {
                ""
            } else {
                null
            }
            null -> ""
            else -> firstName.toUpperCase().substring(0..0)
        }

        initials += when (lastName?.trim()) {
            "" -> if (initials == null || firstName != null) {
                ""
            } else {
                null
            }
            " " -> if (initials == null || firstName != null) {
                ""
            } else {
                null
            }
            null -> ""
            else -> lastName?.toUpperCase().substring(0..0)
        }
        if (initials == "") {
            initials = null
        }
        return initials
    }

    fun dpToPx(borderWidthByDp: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (borderWidthByDp * scale + 0.5f).toInt()
    }

    fun pxToDp(borderWidthByPx: Int): Int {
        val scale = Resources.getSystem().displayMetrics.density
        return (borderWidthByPx / scale + 0.5f).toInt()
    }

    fun spToPx(borderWidthBySp: Int) = borderWidthBySp * Resources.getSystem().displayMetrics.scaledDensity.toInt()

    fun isRepositoryValid(repository: String): Boolean {
        val ignored = setOf("enterprise", "features", "topics",
            "collections", "trending", "events", "marketplace", "pricing", "nonprofit",
            "customer-stories", "security", "login", "join")
        val regex = Regex("^(https://)?(www\\.)?(github\\.com/)(?!(${ignored.joinToString("|")})(?=/|\$))[a-zA-Z\\d](?:[a-zA-Z\\d]|-(?=[a-zA-Z\\d])){0,38}(/)?\$")
        return repository.isEmpty() || regex.matches(repository)
    }
}