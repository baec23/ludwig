package com.baec23.ludwig.component.inputfield

class InputValidator constructor(
    val predicate: (Char) -> Boolean,
) {
    companion object {
        val All = InputValidator { true }
        val TextWithSpacesAndNewLine =
            InputValidator { it.isLetterOrDigit() || it == ' ' || it == '\n' }
        val TextWithSpaces = InputValidator { it.isLetterOrDigit() || it == ' ' }
        val TextNoSpaces = InputValidator { it.isLetterOrDigit() }
        val Number = InputValidator { it.isDigit() }
        val Email = InputValidator { it.isLetterOrDigit() || it == '@'|| it == '.' }
    }

    fun filter(s: String): String {
        return s.filter { predicate(it) }
    }
}