package com.baec23.ludwig.composable_components.inputfield

class InputValidator constructor(
    val predicate: (Char) -> Boolean,
) {
    companion object {
        val TextWithSpacesAndNewLine =
            InputValidator { it.isLetterOrDigit() || it == ' ' || it == '\n' }
        val TextWithSpaces = InputValidator { it.isLetterOrDigit() || it == ' ' }
        val TextNoSpaces = InputValidator { it.isLetterOrDigit() }
        val Number = InputValidator { it.isDigit() }
    }

    fun filter(s: String): String {
        return s.filter { predicate(it) }
    }
}