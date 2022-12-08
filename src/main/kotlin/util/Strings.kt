package util

import com.google.gson.Gson
import java.security.MessageDigest

fun String.remove(c: Char, ignoreCase: Boolean = false) = replace(c.toString(), "", ignoreCase)
fun Char.otherCase() = if (isUpperCase()) lowercaseChar() else uppercaseChar()
fun String.withoutSubstring(i: Int, len: Int) = substring(0, i) + substring(i + len)

fun String.fullyMatch(regex: Regex) = regex.matchEntire(this)!!.destructured.toList()
fun String.toJsonList() = Gson().fromJson(this, ArrayList::class.java)
fun String.md5() = toByteArray().md5().joinToString("") { it.toUByte().toHex() }
private fun ByteArray.md5() = MessageDigest.getInstance("MD5").digest(this)
private fun UByte.toHex() = toString(16).padStart(2, '0')
