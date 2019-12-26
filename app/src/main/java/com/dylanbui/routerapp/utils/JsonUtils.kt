package com.dylanbui.routerapp.utils

// https://github.com/cbeust/klaxon/blob/db522180a5ebefa387afdbd5396de436a7f76ba9/src/main/kotlin/com/beust/klaxon/Lookup.kt


import com.google.gson.JsonArray
import com.google.gson.JsonObject
import java.math.BigInteger
import java.text.DecimalFormat
import java.util.ArrayList
import java.util.*

interface JsonBase {
    fun appendJsonStringImpl(result: Appendable, prettyPrint: Boolean, canonical: Boolean, level: Int)
    fun appendJsonString(result : Appendable, prettyPrint: Boolean = false, canonical: Boolean = false) =
        appendJsonStringImpl(result, prettyPrint, canonical, 0)
    fun toJsonString(prettyPrint: Boolean = false, canonical: Boolean = false) : String =
        StringBuilder().apply { appendJsonString(this, prettyPrint, canonical) }.toString()
}

//fun JsonObject(map : Map<String, Any?> = emptyMap()) : JsonObject = JsonObject(LinkedHashMap(map))

data class JsonObject(val map: MutableMap<String, Any?>) : JsonBase, MutableMap<String, Any?> by map {
//    constructor() : this(mutableMapOf<String, Any?>()) {}

    override fun appendJsonStringImpl(result: Appendable, prettyPrint: Boolean, canonical: Boolean, level: Int) {
        fun indent(a: Appendable, level: Int) {
            for (i in 1..level) {
                a.append("  ")
            }
        }

        result.append("{")
        var comma = false
        for ((k, v) in (if(canonical) map.toSortedMap() else map)) {
            if (comma) {
                result.append(",")
            } else {
                comma = true
            }

            if (prettyPrint && !canonical) {
                result.appendln()
                indent(result, level + 1)
            }

            result.append(Render.renderString(k)).append(":")
            if (prettyPrint && !canonical) {
                result.append(" ")
            }

            Render.renderValue(v, result, prettyPrint, canonical, level + 1)
        }

        if (prettyPrint && !canonical && map.isNotEmpty()) {
            result.appendln()
            indent(result, level)
        }

        result.append("}")
    }

    override fun toString() = keys.joinToString(",")

    @Suppress("UNCHECKED_CAST")
    fun <T> array(fieldName: String) : JsonArray<T>? = get(fieldName) as JsonArray<T>?

    fun obj(fieldName: String) : JsonObject? = get(fieldName) as JsonObject?

    fun int(fieldName: String) : Int? {
        val value = get(fieldName)
        return when (value) {
            is Number -> value.toInt()
            else -> value as Int?
        }
    }

    fun long(fieldName: String) : Long? {
        val value = get(fieldName)
        return when (value) {
            is Number -> value.toLong()
            else -> value as Long?
        }
    }

    fun bigInt(fieldName: String) : BigInteger? = get(fieldName) as BigInteger
    fun string(fieldName: String) : String? = get(fieldName) as String?
    fun double(fieldName: String) : Double? = get(fieldName) as Double?
    fun float(fieldName: String) : Float? = (get(fieldName) as Double?)?.toFloat()
    fun boolean(fieldName: String) : Boolean? = get(fieldName) as Boolean?


}

object Render {
    tailrec fun renderValue(v: Any?, result: Appendable, prettyPrint: Boolean, canonical: Boolean, level: Int) {
        when (v) {
            is JsonBase -> v.appendJsonStringImpl(result, prettyPrint, canonical, level)
            is String -> result.renderString(v)
            is Map<*, *> -> renderValue(
                JsonObject(v.mapKeys { it.key.toString() }.mapValues { it.value }),
                result,
                prettyPrint,
                canonical,
                level)
            is List<*> -> renderValue(JsonArray(v), result, prettyPrint, canonical, level)
            is Pair<*, *> -> renderValue(v.second, result.renderString(v.first.toString()).append(": "), prettyPrint, canonical, level)
            is Double, is Float ->
                result.append(if (canonical) decimalFormat.format(v) else v.toString())
            null -> result.append("null")
            else -> {
                // TODO - Here we are reusing Converter.toJson() logic, but loose support for prettyPrint and canonical
                result.append(Klaxon().findConverter(v).toJson(v))
            }
        }
    }

    fun renderString(s: String) = StringBuilder().renderString(s).toString()

    fun escapeString(s: String): String {
        val result = StringBuilder().apply {
            for (idx in 0..s.length - 1) {
                val ch = s[idx]
                when (ch) {
                    '"' -> append("\\").append(ch)
//                    '\'' -> append("\\").append(ch)
                    '\\' -> append(ch).append(ch)
                    '\n' -> append("\\n")
                    '\r' -> append("\\r")
                    '\t' -> append("\\t")
                    '\b' -> append("\\b")
                    '\u000c' -> append("\\f")
                    else -> {
                        if (isNotPrintableUnicode(ch)) {
                            append("\\u")
                            append(Integer.toHexString(ch.toInt()).padStart(4, '0'))
                        } else {
                            append(ch)
                        }
                    }
                }
            }
        }
        return result.toString()
    }

    private fun <A : Appendable> A.renderString(s: String): A {
        append("\"")
        append(escapeString(s))
        append("\"")
        return this
    }

    private fun isNotPrintableUnicode(c: Char): Boolean =
        c in '\u0000'..'\u001F' ||
                c in '\u007F'..'\u009F' ||
                c in '\u2000'..'\u20FF'

    private val decimalFormat = DecimalFormat("0.0####E0;-0.0####E0")
}