package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.FiniteFunction
import org.grandtestauto.maths.monoid.allFunctionsFromTo
import org.junit.Assert
import org.junit.Test

import java.util.HashMap
import java.util.HashSet

/**
 * @author Tim Lavers
 */
class FiniteFunctionTest : TestBase() {

    private val _f = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).add("tiga", 3).build()
    private val f: FiniteFunction<String, Int>
        get() = _f

    @Test
    fun applyTest(){
        Assert.assertEquals(1, f.invoke("one"))
        Assert.assertEquals(1, f.invoke("uno"))
        Assert.assertEquals(1, f.invoke("eins"))
        Assert.assertEquals(1, f.invoke("satu"))
        Assert.assertEquals(2, f.invoke("two"))
        Assert.assertEquals(2, f.invoke("zwei"))
        Assert.assertEquals(2, f.invoke("dos"))
        Assert.assertEquals(2, f.invoke("dua"))
        Assert.assertEquals(3, f.invoke("three"))
        Assert.assertEquals(3, f.invoke("tres"))
        Assert.assertEquals(3, f.invoke("drei"))
        Assert.assertEquals(3, f.invoke("tiga"))
    }

    @Test
    fun domainTest(){
        val d = f.domain()
        Assert.assertEquals(12, d.size)
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
        Assert.assertEquals(3, range.size)
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
        Assert.assertEquals(same.hashCode(), f.hashCode())
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
        Assert.assertEquals(1, all.size)
        Assert.assertTrue(all.iterator().next().domain().isEmpty())
        Assert.assertTrue(all.iterator().next().range().isEmpty())

        //Empty not empty.
        range.add(1)
        all = allFunctionsFromTo(domain, range)
        Assert.assertEquals(1, all.size)
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
        Assert.assertEquals(1, all.size)
        val f = all.iterator().next()
        Assert.assertEquals(1, f.invoke("Berg"))

        //1, 2
        range.add(2)
        all = allFunctionsFromTo(domain, range)
        Assert.assertEquals(2, all.size)
        Assert.assertTrue(all.contains(f("Berg", 1)))
        Assert.assertTrue(all.contains(f("Berg", 2)))

        //2, 2
        domain.add("Webern")
        all = allFunctionsFromTo(domain, range)
        Assert.assertEquals(4, all.size)
        Assert.assertTrue(all.contains(f(f("Berg", 1), "Webern", 1)))
        Assert.assertTrue(all.contains(f(f("Berg", 1), "Webern", 2)))
        Assert.assertTrue(all.contains(f(f("Berg", 2), "Webern", 1)))
        Assert.assertTrue(all.contains(f(f("Berg", 2), "Webern", 2)))

        //2, 3
        range.add(3)
        all = allFunctionsFromTo(domain, range)
        Assert.assertEquals(9, all.size)
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
        Assert.assertEquals(27, all.size)
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