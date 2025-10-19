package dev.aurakai.auraframefx.utilities

import org.example.list.LinkedList
import org.example.utilities.JoinUtils
import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test

class JoinUtilsDevTest {
    private fun linkedListOf(vararg items: String): LinkedList {
        val list = LinkedList()
        for (s in items) list.add(s)
        return list
    }

    @Nested
    @DisplayName("Happy paths")
    inner class HappyPaths {
        @Test
        fun emptyList_returnsEmptyString() {
            assertEquals("", JoinUtils.join(LinkedList()))
        }

        @Test
        fun singleElement_returnsThatElement() {
            assertEquals("one", JoinUtils.join(linkedListOf("one")))
        }

        @Test
        fun multipleElements_joinWithSingleSpaces() {
            assertEquals("a b c", JoinUtils.join(linkedListOf("a", "b", "c")))
        }
    }

    @Nested
    @DisplayName("Edge cases")
    inner class EdgeCases {
        @Test
        fun emptyElementsArePreservedBetweenTokens() {
            val result = JoinUtils.join(linkedListOf("a", "", "b"))
            assertEquals("a  b", result) // two spaces between a and b
        }

        @Test
        fun trailingEmptyElement_yieldsTrailingSpace() {
            val result = JoinUtils.join(linkedListOf("a", "b", ""))
            assertEquals("a b ", result)
        }

        @Test
        fun elementsContainingSpaces_arePreservedVerbatim() {
            val result = JoinUtils.join(linkedListOf("hello", "big world", "!"))
            assertEquals("hello big world !", result)
        }

        @Test
        fun nonAsciiCharacters_supported() {
            assertEquals("こんにちは 世界", JoinUtils.join(linkedListOf("こんにちは", "世界")))
        }
    }
}
