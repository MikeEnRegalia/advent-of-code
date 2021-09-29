package util

fun String.remove(c: Char, ignoreCase: Boolean = false) = replace(c.toString(), "", ignoreCase)
fun Char.otherCase() = if (isUpperCase()) lowercaseChar() else uppercaseChar()
fun String.withoutSubstring(i: Int, len: Int) = substring(0, i) + substring(i + len)