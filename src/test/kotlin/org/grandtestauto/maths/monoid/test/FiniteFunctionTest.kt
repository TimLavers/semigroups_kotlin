package org.grandtestauto.maths.monoid.test

import io.kotest.matchers.collections.shouldContain
import io.kotest.matchers.shouldBe
import org.grandtestauto.maths.monoid.FiniteFunction
import org.grandtestauto.maths.monoid.allBijectionsFromTo
import org.grandtestauto.maths.monoid.allFunctionsFromTo
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test
import java.lang.IllegalStateException

/**
 * @author Tim Lavers
 */
class FiniteFunctionTest : TestBase() {

    private val f = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).add("tiga", 3).build()

    @Test
    fun plusTest() {
        //Add to empty.
        val empty = FiniteFunction<String, Int>(mapOf())
        val withEins1 = empty + Pair("eins", 1)
        assertEquals(1, withEins1.data.size)
        assertEquals(1, withEins1("eins"))

        //Add to one already there.
        val withEins1Again = withEins1 + Pair("eins", 1)
        assertEquals(1, withEins1Again.data.size)
        assertEquals(1, withEins1Again("eins"))

        //Add another
        val withEins1Uno1 = withEins1 + Pair("uno", 1)
        assertEquals(2, withEins1Uno1.data.size)
        assertEquals(1, withEins1Uno1("eins"))
        assertEquals(1, withEins1Uno1("uno"))
    }

    @Test
    fun secondaryConstructor() {
        val f = FiniteFunction(1 to "one", 2 to "two")
        f.domain() shouldBe setOf(1, 2)
        f.range() shouldBe setOf("one", "two")
        f(1) shouldBe "one"
        f(2) shouldBe "two"
    }

    @Test
    fun invokeTest(){
        assertEquals(1, f("one"))
        assertEquals(1, f("uno"))
        assertEquals(1, f("eins"))
        assertEquals(1, f("satu"))
        assertEquals(2, f("two"))
        assertEquals(2, f("zwei"))
        assertEquals(2, f("dos"))
        assertEquals(2, f("dua"))
        assertEquals(3, f("three"))
        assertEquals(3, f("tres"))
        assertEquals(3, f("drei"))
        assertEquals(3, f("tiga"))
    }

    @Test
    fun applyErrorTest() {
        var gotException = false
        try {
            f.invoke("many")
        } catch (e: IllegalStateException) {
            gotException = true
            assertEquals("many ∉ domain", e.message)
        }
        assert(gotException)
    }

    @Test
    fun domainTest(){
        val d = f.domain()
        assertEquals(12, d.size)
        Assert.assertTrue(d.contains("one"))
        Assert.assertTrue(d.contains("uno"))
        Assert.assertTrue(d.contains("satu"))
        Assert.assertTrue(d.contains("eins"))
        Assert.assertTrue(d.contains("two"))
        Assert.assertTrue(d.contains("dua"))
        Assert.assertTrue(d.contains("zwei"))
        Assert.assertTrue(d.contains("dos"))
        Assert.assertTrue(d.contains("tiga"))
        Assert.assertTrue(d.contains("tres"))
        Assert.assertTrue(d.contains("drei"))
        Assert.assertTrue(d.contains("three"))
    }

    @Test
    fun rangeTest(){
        val range = f.range()
        assertEquals(3, range.size)
        Assert.assertTrue(range.contains(1))
        Assert.assertTrue(range.contains(2))
        Assert.assertTrue(range.contains(3))
    }

    @Test
    fun equalsTest(){
        val missingTiga = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).build()
        Assert.assertTrue(missingTiga != f)
        Assert.assertTrue(f != missingTiga)

        val wrong = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 12).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).build()
        Assert.assertTrue(wrong != f)
        Assert.assertTrue(f != wrong)

        val same = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).add("tiga", 3).build()
        Assert.assertTrue(same == f)
        Assert.assertTrue(f == same)
    }

    @Test
    fun hashCodeTest(){
        val same = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).add("tiga", 3).build()
        assertEquals(same.hashCode(), f.hashCode())
    }

    @Test
    fun toStringTest(){
        Assert.assertTrue(f.toString().startsWith("{"))
        Assert.assertTrue(f.toString().endsWith("}"))
    }

    @Test
    fun allFunctionsFromTo(){
        // Empty to empty.
        with (allFunctionsFromTo(setOf<String>(), setOf<Int>())) {
            size shouldBe 1
            first().domain() shouldBe emptySet()
            first().range() shouldBe emptySet()
        }

        // Empty to not empty.
        with (allFunctionsFromTo(setOf<String>(), setOf(1))) {
            size shouldBe 1
            iterator().next().domain() shouldBe emptySet()
            iterator().next().range() shouldBe emptySet()
        }

        // Not empty to empty.
        val berg = "Berg"
        with (allFunctionsFromTo(setOf(berg), setOf<Int>())) {
            size shouldBe 0
        }

        // 1 element set to 1 element set
        with (allFunctionsFromTo(setOf(berg), setOf(1))) {
            size shouldBe 1
            first()(berg) shouldBe 1
        }

        // 1 element set to 2 element set
        with (allFunctionsFromTo(setOf(berg), setOf(1, 2))) {
            size shouldBe 2
            this shouldContain FiniteFunction(berg to 1)
            this shouldContain FiniteFunction(berg to 2)
        }

        // 2 element set to 2 element set
        val webern = "Webern"
        with (allFunctionsFromTo(setOf(berg, webern), setOf(1, 2))) {
            size shouldBe 4
            this shouldContain FiniteFunction(berg to 1, webern to 1)
            this shouldContain FiniteFunction(berg to 1, webern to 2)
            this shouldContain FiniteFunction(berg to 2, webern to 2)
            this shouldContain FiniteFunction(berg to 2, webern to 1)
        }

        // 2 element set to 3 element set
        with (allFunctionsFromTo(setOf(berg, webern), setOf(1, 2, 3))) {
            size shouldBe 9
            this shouldContain FiniteFunction(berg to 1, webern to 1)
            this shouldContain FiniteFunction(berg to 1, webern to 2)
            this shouldContain FiniteFunction(berg to 1, webern to 3)
            this shouldContain FiniteFunction(berg to 2, webern to 1)
            this shouldContain FiniteFunction(berg to 2, webern to 2)
            this shouldContain FiniteFunction(berg to 2, webern to 3)
            this shouldContain FiniteFunction(berg to 3, webern to 1)
            this shouldContain FiniteFunction(berg to 3, webern to 2)
            this shouldContain FiniteFunction(berg to 3, webern to 3)
        }

        // 3 element set to 3 element set
        val schoenberg = "Schoenberg"
        with (allFunctionsFromTo(setOf(berg, webern, schoenberg), setOf(1, 2, 3))) {
            size shouldBe 27
            this shouldContain FiniteFunction(berg to 1, webern to 1, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 1, webern to 1, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 1, webern to 1, schoenberg to 3)
            this shouldContain FiniteFunction(berg to 1, webern to 2, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 1, webern to 2, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 1, webern to 2, schoenberg to 3)
            this shouldContain FiniteFunction(berg to 1, webern to 3, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 1, webern to 3, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 1, webern to 3, schoenberg to 3)

            this shouldContain FiniteFunction(berg to 2, webern to 1, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 2, webern to 1, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 2, webern to 1, schoenberg to 3)
            this shouldContain FiniteFunction(berg to 2, webern to 2, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 2, webern to 2, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 2, webern to 2, schoenberg to 3)
            this shouldContain FiniteFunction(berg to 2, webern to 3, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 2, webern to 3, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 2, webern to 3, schoenberg to 3)

            this shouldContain FiniteFunction(berg to 3, webern to 1, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 3, webern to 1, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 3, webern to 1, schoenberg to 3)
            this shouldContain FiniteFunction(berg to 3, webern to 2, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 3, webern to 2, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 3, webern to 2, schoenberg to 3)
            this shouldContain FiniteFunction(berg to 3, webern to 3, schoenberg to 1)
            this shouldContain FiniteFunction(berg to 3, webern to 3, schoenberg to 2)
            this shouldContain FiniteFunction(berg to 3, webern to 3, schoenberg to 3)
        }
    }

    @Test
    fun isInjective() {
        FiniteFunction(1 to 1).isInjective() shouldBe true
        FiniteFunction(1 to 1, 2 to 1).isInjective() shouldBe false
        FiniteFunction(1 to "a", 2 to "b", 3 to "c").isInjective() shouldBe true
        FiniteFunction(1 to "a", 2 to "b", 3 to "a").isInjective() shouldBe false
    }

    @Test
    fun allBijections() {
        with (allBijectionsFromTo(setOf(1), setOf("a"))) {
            size shouldBe 1
            forEach {
                it.domain() shouldBe set(1)
                it.range() shouldBe set("a")
                it.isInjective() shouldBe true
            }
        }
        with (allBijectionsFromTo(setOf(1,2,3), setOf("a", "b", "c"))) {
            size shouldBe 6
            forEach {
                it.domain() shouldBe set(1,2,3)
                it.range() shouldBe set("a", "b", "c")
                it.isInjective() shouldBe true
            }
        }
        with (allBijectionsFromTo(setOf(1,2,3, 4, 5), setOf(1, 2, 3, 4, 5))) {
            size shouldBe 120
            forEach {
                it.domain() shouldBe set(1,2,3, 4, 5)
                it.range() shouldBe set(1,2,3, 4, 5)
                it.isInjective() shouldBe true
            }
        }
    }
}