package aoc2022

import com.google.gson.Gson
import java.security.MessageDigest

fun main() = day07(String(System.`in`.readAllBytes())).forEach(::println)

private fun day07(input: String): List<Any?> {

    return listOf(null, null)
}

private fun String.fullyMatch(regex: Regex) = regex.matchEntire(this)!!.destructured.toList()
private fun String.toJsonList() = Gson().fromJson(this, ArrayList::class.java)
private fun String.md5() = toByteArray().md5().joinToString("") { it.toUByte().toHex() }
private fun ByteArray.md5() = MessageDigest.getInstance("MD5").digest(this)
private fun UByte.toHex() = toString(16).padStart(2, '0')
