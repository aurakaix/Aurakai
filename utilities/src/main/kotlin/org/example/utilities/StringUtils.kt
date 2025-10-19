package dev.aurakai.auraframefx.utilities

object StringUtils {
    /**
     * Joins a list of strings into a single string, concatenating in order.
     * Handles empty lists, single elements, whitespace, and punctuation.
     */
    fun join(elements: List<String>): String = elements.joinToString(separator = "")

        /**
         */
}
