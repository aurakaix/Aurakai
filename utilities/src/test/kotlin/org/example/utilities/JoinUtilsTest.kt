package dev.aurakai.auraframefx.utilities

import dev.aurakai.auraframefx.list.LinkedList
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertDoesNotThrow
import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTimeout
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Nested
import org.junit.jupiter.api.Test
import java.util.LinkedList

/**
 * Tests for JoinUtils.join which concatenates LinkedList elements separated by a single space.
 *
 * Framework: JUnit 5 (Jupiter)
 */
class JoinUtilsTest {

    private fun linkedListOf(vararg items: String): LinkedList {
        val list = LinkedList()
        for (item in items) {
            // Assuming LinkedList has an add method from Gradle init sample.
            list.add(item)
        }
        return list
    }

    @Nested
    @DisplayName("Happy paths")
    inner class HappyPaths {
        @Test
        fun `empty list returns empty string`() {
            val list = LinkedList()
            val result = JoinUtils.join(list)
            assertEquals("", result)
        }

        @Test
        fun `single element list returns the element without extra spaces`() {
            val list = linkedListOf("hello")
            val result = JoinUtils.join(list)
            assertEquals("hello", result)
        }

        @Test
        fun `two elements result in single space between them`() {
            val list = linkedListOf("hello", "world")
            val result = JoinUtils.join(list)
            assertEquals("hello world", result)
        }

        @Test
        fun `multiple elements join with single spaces only`() {
            val list = linkedListOf("a", "b", "c", "d")
            val result = JoinUtils.join(list)
            assertEquals("a b c d", result)
        }
    }

    @Nested
    @DisplayName("Edge cases")
    inner class EdgeCases {
        @Test
        fun `elements that are empty strings are preserved, yielding consecutive spaces`() {
            val list = linkedListOf("a", "", "b", "")
            val result = JoinUtils.join(list)
            // "a" + " " + "" + " " + "b" + " " + "" -> "a  b "
            assertEquals("a  b ", result)
        }

        @Test
        fun `elements that contain spaces are preserved verbatim`() {
            val list = linkedListOf("hello", "big world", "!")
            val result = JoinUtils.join(list)
            assertEquals("hello big world !", result)
        }

        @Test
        fun `non-ascii characters are handled correctly`() {
            val list = linkedListOf("こんにちは", "世界", "👋")
            val result = JoinUtils.join(list)
            assertEquals("こんにちは 世界 👋", result)
        }

        @Test
        fun `large list joins correctly and does not throw`() {
            val items = (1..1000).map { "v$it" }.toTypedArray()
            val list = linkedListOf(*items)
            assertEquals(1000, parts.size)
            assertEquals("v1", parts.first())
            assertEquals("v500", parts[499])
            assertEquals("v1000", parts.last())
        }
    }

    @Nested
    @DisplayName("Defensive behavior")
    inner class DefensiveBehavior {
        @Test
        fun `does not prepend or append extra spaces`() {
            val list = linkedListOf("x", "y", "z")
            val result = JoinUtils.join(list)
            // Ensure no leading or trailing whitespace
            assertEquals(result, result.trim())
            assertEquals("x y z", result)
        }
    }
}

/**
 * Additional tests for JoinUtils.join
 *
 * Testing library/framework: JUnit 5 (Jupiter)
 *
 * Focus: Expand coverage with more happy paths, edge cases, stress, concurrency,
 * and special-content scenarios while preserving existing conventions.
 */
class JoinUtilsExtendedTest {

    // Local helper mirrors the helper in JoinUtilsTest
    private fun linkedListOf(vararg items: String): LinkedList {
        val list = LinkedList()
        for (item in items) {
            list.add(item)
        }
        return list
    }

    @Nested
    @DisplayName("Additional happy paths")
    inner class AdditionalHappyPaths {
        @Test
        fun `three elements with punctuation`() {
            val list = linkedListOf("hello,", "world!", ":-)")
            val result = JoinUtils.join(list)
            assertEquals("hello, world! :-)", result)
        }

        @Test
        fun `elements containing internal spaces are preserved`() {
            val list = linkedListOf("foo bar", "baz qux")
            val result = JoinUtils.join(list)
            assertEquals("foo bar baz qux", result)
        }
    }

    @Nested
    @DisplayName("Additional edge cases")
    inner class AdditionalEdgeCases {
        @Test
        fun `elements with tabs and newlines are preserved verbatim`() {
            val list = linkedListOf("line1\nline2", "tab\here", "end")
            val result = JoinUtils.join(list)
            assertEquals("line1\nline2 tab\here end", result)
        }

        @Test
        fun `whitespace-only elements are preserved`() {
            val list = linkedListOf("", " ", "  ")
            val result = JoinUtils.join(list)
            org.junit.jupiter.api.Assertions.assertTrue(result.all { it == ' ' })
        }

        @Test
        fun `leading and trailing empty elements are preserved`() {
            val list = linkedListOf("", "a", "")
            val result = JoinUtils.join(list)
            assertEquals(" a ", result)
        }

        @Test
        fun `zero-width characters are preserved`() {
            val list = linkedListOf("a\u200B", "b\u200C", "c\u200D")
            val result = JoinUtils.join(list)
            assertEquals("a\u200B b\u200C c\u200D", result)
        }
    }

    @Nested
    @DisplayName("Stress and concurrency")
    inner class StressAndConcurrency {
        @Test
        fun `joins 5000 elements under timeout`() {
            val items = (1..5000).map { "x$it" }.toTypedArray()
            val list = linkedListOf(*items)
            val out =
                org.junit.jupiter.api.Assertions.assertTimeout(java.time.Duration.ofSeconds(2)) {
                    JoinUtils.join(list)
        }

        @Test
        fun `repeated joins produce consistent results`() {
            val list = linkedListOf("a", "b", "c")
            val results = (1..100).map { JoinUtils.join(list) }
            assertEquals(setOf("a b c"), results.toSet())
        }

        @Test
        fun `concurrent calls produce consistent results`() {
            val pool = java.util.concurrent.Executors.newFixedThreadPool(4)
            try {
                val tasks = (1..12).map { i ->
                    java.util.concurrent.Callable {
                        val l = linkedListOf("thread$i", "value$i")
                        JoinUtils.join(l)
                    }
                }
                val results = pool.invokeAll(tasks).map { it.get() }
                val expected = (1..12).map { i -> "thread$i value$i" }.toSet()
                assertEquals(expected, results.toSet())
            } finally {
                pool.shutdownNow()
            }
        }
    }

    @Nested
    @DisplayName("Special content handling")
    inner class SpecialContent {
        @Test
        fun `HTML-like fragments`() {
            val list = linkedListOf("<div>", "content", "</div>")
            val result = JoinUtils.join(list)
            assertEquals("<div> content </div>", result)
        }

        @Test
        fun `JSON-like fragments`() {
            val list = linkedListOf("{\"key\":", "\"value\"}")
            val result = JoinUtils.join(list)
            assertEquals("{\"key\": \"value\"}", result)
        }
    }
}