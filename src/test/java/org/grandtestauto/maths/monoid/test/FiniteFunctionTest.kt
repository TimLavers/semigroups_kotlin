package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.FiniteFunction
import org.grandtestauto.maths.monoid.Tuple
import org.grandtestauto.maths.monoid.allFunctionsFromTo
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Test

import java.util.HashMap
import java.util.HashSet

/**
 * @author Tim Lavers
 */
class FiniteFunctionTest : TestBase() {

    private val f = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).add("tiga", 3).build()

    @Test
    fun plusTest() {
        //Add to empty.
        val empty = FiniteFunction<String, Int>(mapOf())
        val withEins1 = empty + Tuple("eins", 1)
        assertEquals(1, withEins1.data.size)
        assertEquals(1, withEins1.invoke("eins"))

        //Add to one already there.
        val withEins1Again = withEins1 + Tuple("eins", 1)
        assertEquals(1, withEins1Again.data.size)
        assertEquals(1, withEins1Again.invoke("eins"))

        //Add another
        val withEins1Uno1 = withEins1 + Tuple("uno", 1)
        assertEquals(2, withEins1Uno1.data.size)
        assertEquals(1, withEins1Uno1.invoke("eins"))
        assertEquals(1, withEins1Uno1.invoke("uno"))
    }

    @Test
    fun applyTest(){
        assertEquals(1, f.invoke("one"))
        assertEquals(1, f.invoke("uno"))
        assertEquals(1, f.invoke("eins"))
        assertEquals(1, f.invoke("satu"))
        assertEquals(2, f.invoke("two"))
        assertEquals(2, f.invoke("zwei"))
        assertEquals(2, f.invoke("dos"))
        assertEquals(2, f.invoke("dua"))
        assertEquals(3, f.invoke("three"))
        assertEquals(3, f.invoke("tres"))
        assertEquals(3, f.invoke("drei"))
        assertEquals(3, f.invoke("tiga"))
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
    fun allTest(){
        //Empty empty.
        val domain = HashSet<String>()
        var range: MutableSet<Int> = HashSet()
        var all = allFunctionsFromTo(domain, range)
        assertEquals(1, all.size)
        Assert.assertTrue(all.iterator().next().domain().isEmpty())
        Assert.assertTrue(all.iterator().next().range().isEmpty())

        //Empty not empty.
        range.add(1)
        all = allFunctionsFromTo(domain, range)
        assertEquals(1, all.size)
        Assert.assertTrue(all.iterator().next().domain().isEmpty())
        Assert.assertTrue(all.iterator().next().range().isEmpty())

        //Empty not empty.
        domain.add("Berg")
        range = HashSet<Int>()
        //        all = all(domain, range);
        //        Assert.assertEquals(1, all.size());
        //        Assert.assertFalse(all.iterator().next().domain().isEmpty());
        //        Assert.assertFalse(all.iterator().next().range().isEmpty());

        //1, 1
        range.add(1)
        all = allFunctionsFromTo(domain, range)
        assertEquals(1, all.size)
        val f = all.iterator().next()
        assertEquals(1, f.invoke("Berg"))

        //1, 2
        range.add(2)
        all = allFunctionsFromTo(domain, range)
        assertEquals(2, all.size)
        Assert.assertTrue(all.contains(f("Berg", 1)))
        Assert.assertTrue(all.contains(f("Berg", 2)))

        //2, 2
        domain.add("Webern")
        all = allFunctionsFromTo(domain, range)
        assertEquals(4, all.size)
        Assert.assertTrue(all.contains(f(f("Berg", 1), "Webern", 1)))
        Assert.assertTrue(all.contains(f(f("Berg", 1), "Webern", 2)))
        Assert.assertTrue(all.contains(f(f("Berg", 2), "Webern", 1)))
        Assert.assertTrue(all.contains(f(f("Berg", 2), "Webern", 2)))

        //2, 3
        range.add(3)
        all = allFunctionsFromTo(domain, range)
        assertEquals(9, all.size)
        Assert.assertTrue(all.contains(f(f("Berg", 1), "Webern", 1)))
        Assert.assertTrue(all.contains(f(f("Berg", 1), "Webern", 2)))
        Assert.assertTrue(all.contains(f(f("Berg", 1), "Webern", 3)))
        Assert.assertTrue(all.contains(f(f("Berg", 2), "Webern", 1)))
        Assert.assertTrue(all.contains(f(f("Berg", 2), "Webern", 2)))
        Assert.assertTrue(all.contains(f(f("Berg", 2), "Webern", 3)))
        Assert.assertTrue(all.contains(f(f("Berg", 3), "Webern", 1)))
        Assert.assertTrue(all.contains(f(f("Berg", 3), "Webern", 2)))
        Assert.assertTrue(all.contains(f(f("Berg", 3), "Webern", 3)))

        //3, 3
        domain.add("Schoenberg")
        all = allFunctionsFromTo(domain, range)
        assertEquals(27, all.size)
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 1), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 1), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 1), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 2), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 2), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 2), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 3), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 3), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 1), "Webern", 3), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 1), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 1), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 1), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 2), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 2), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 2), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 3), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 3), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 2), "Webern", 3), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 1), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 1), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 1), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 2), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 2), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 2), "Schoenberg", 3)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 3), "Schoenberg", 1)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 3), "Schoenberg", 2)))
        Assert.assertTrue(all.contains(f(f(f("Berg", 3), "Webern", 3), "Schoenberg", 3)))
    }

    internal fun <S, T> f(d: S, r: T): FiniteFunction<S, T> {
        return FiniteFunction.Builder<S, T>().add(d, r).build()
    }

    internal fun <S, T> f(f: FiniteFunction<S, T>, domainElement: S, rangeElement: T): FiniteFunction<S, T> {
        val data = HashMap<S, T>()
        f.domain().forEach { d -> data.put(d, f.invoke(d)) }
        data.put(domainElement, rangeElement)
        return FiniteFunction(data)
    }
}