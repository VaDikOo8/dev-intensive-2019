package ru.skillbranch.devintensive.extensions

fun String.truncate(i: Int = 16): String {
    val string = this.substring(0, i).trim()
    return "$string${if (string.length < 10) "" else "..."}"
}

fun String.stripHtml(): String {
    return this
        .replace("</(.+)>".toRegex(), "")
        .replace("<(.+)>".toRegex(),"")
        .replace("[\\s]{2,}".toRegex()," ")
}