package com.baec23.ludwig.component.inputfield

class InputValidator constructor(
    private val predicate: (Char) -> Boolean,
) {
    companion object {
        val Any = InputValidator { true }
        val TextWithSpacesAndNewLine =
            InputValidator { it.isLetterOrDigit() || it == ' ' || it == '\n' }
        val TextWithSpaces = InputValidator { it.isLetterOrDigit() || it == ' ' }
        val TextNoSpaces = InputValidator { it.isLetterOrDigit() }
        val Number = InputValidator { it.isDigit() }
        val Email = InputValidator { it.isLetterOrDigit() || it == '@' || it == '.' }
        val Password = InputValidator {
            val regex = Regex("^[A-Za-z0-9!@#$%^&*(),.?\":{}|<>]*$")
            regex.matches(it.toString())
        }
    }

    fun filter(s: String): String {
        return s.filter { predicate(it) }
    }

    fun isValid(c: Char): Boolean {
        return predicate(c)
    }

    fun isValid(s: CharSequence): Boolean {
        return s.filter { predicate(it) } == s
    }
}