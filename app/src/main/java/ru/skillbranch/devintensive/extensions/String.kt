package ru.skillbranch.devintensive.extensions

fun String.truncate(count: Int = 16) : String {
    val string = this.trim()
    return if (string.length <= count) string
    else string.substring(0, count).trim() + "..."
}

fun String.stripHtml(): String {
    return this
        .replace("<(.*?)>".toRegex(), "")
        .replace("&(.*?);".toRegex(),"")
        .replace("[\\s]{2,}".toRegex()," ")
}