package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.FiniteFunction
import org.grandtestauto.maths.monoid.allFunctionsFromTo
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
        Assert.aequals(1, f.apply("one"))
        Assert.aequals(1, f.apply("uno"))
        Assert.aequals(1, f.apply("eins"))
        Assert.aequals(1, f.apply("satu"))
        Assert.aequals(2, f.apply("two"))
        Assert.aequals(2, f.apply("zwei"))
        Assert.aequals(2, f.apply("dos"))
        Assert.aequals(2, f.apply("dua"))
        Assert.aequals(3, f.apply("three"))
        Assert.aequals(3, f.apply("tres"))
        Assert.aequals(3, f.apply("drei"))
        Assert.aequals(3, f.apply("tiga"))
    }

    @Test
    fun domainTest(){
        val d = f.domain()
        Assert.aequals(12, d.size)
        Assert.azzert(d.contains("one"))
        Assert.azzert(d.contains("uno"))
        Assert.azzert(d.contains("satu"))
        Assert.azzert(d.contains("eins"))
        Assert.azzert(d.contains("two"))
        Assert.azzert(d.contains("dua"))
        Assert.azzert(d.contains("zwei"))
        Assert.azzert(d.contains("dos"))
        Assert.azzert(d.contains("tiga"))
        Assert.azzert(d.contains("tres"))
        Assert.azzert(d.contains("drei"))
        Assert.azzert(d.contains("three"))
    }

    @Test
    fun rangeTest(){
        val range = f.range()
        Assert.aequals(3, range.size)
        Assert.azzert(range.contains(1))
        Assert.azzert(range.contains(2))
        Assert.azzert(range.contains(3))
    }

    @Test
    fun equalsTest(){
        val missingTiga = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).build()
        Assert.azzert(missingTiga != f)
        Assert.azzert(f != missingTiga)

        val wrong = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 12).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).build()
        Assert.azzert(wrong != f)
        Assert.azzert(f != wrong)

        val same = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).add("tiga", 3).build()
        Assert.azzert(same == f)
        Assert.azzert(f == same)
    }

    @Test
    fun hashCodeTest(){
        val same = FiniteFunction.Builder<String, Int>().add("one", 1).add("uno", 1).add("eins", 1).add("satu", 1).add("two", 2).add("dos", 2).add("zwei", 2).add("dua", 2).add("three", 3).add("tres", 3).add("drei", 3).add("tiga", 3).build()
        Assert.aequals(same.hashCode(), f.hashCode())
    }

    @Test
    fun toStringTest(){
        Assert.azzert(f.toString().startsWith("{"))
        Assert.azzert(f.toString().endsWith("}"))
    }

    @Test
    fun allTest(){
        //Empty empty.
        val domain = HashSet<String>()
        var range: MutableSet<Int> = HashSet()
        var all = allFunctionsFromTo(domain, range)
        Assert.aequals(1, all.size)
        Assert.azzert(all.iterator().next().domain().isEmpty())
        Assert.azzert(all.iterator().next().range().isEmpty())

        //Empty not empty.
        range.add(1)
        all = allFunctionsFromTo(domain, range)
        Assert.aequals(1, all.size)
        Assert.azzert(all.iterator().next().domain().isEmpty())
        Assert.azzert(all.iterator().next().range().isEmpty())

        //Empty not empty.
        domain.add("Berg")
        range = HashSet<Int>()
        //        all = all(domain, range);
        //        Assert.aequals(1, all.size());
        //        Assert.azzert(all.iterator().next().domain().isEmpty());
        //        Assert.azzert(all.iterator().next().range().isEmpty());

        //1, 1
        range.add(1)
        all = allFunctionsFromTo(domain, range)
        Assert.aequals(1, all.size)
        val f = all.iterator().next()
        Assert.aequals(1, f.apply("Berg"))

        //1, 2
        range.add(2)
        all = allFunctionsFromTo(domain, range)
        Assert.aequals(2, all.size)
        Assert.azzert(all.contains(f<String, Int>("Berg", 1)))
        Assert.azzert(all.contains(f<String, Int>("Berg", 2)))

        //2, 2
        domain.add("Webern")
        all = allFunctionsFromTo(domain, range)
        Assert.aequals(4, all.size)
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 2)))

        //2, 3
        range.add(3)
        all = allFunctionsFromTo(domain, range)
        Assert.aequals(9, all.size)
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 3)))

        //3, 3
        domain.add("Schoenberg")
        all = allFunctionsFromTo(domain, range)
        Assert.aequals(27, all.size)
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 1), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 1), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 1), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 2), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 2), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 2), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 3), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 3), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 1), "Webern", 3), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 1), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 1), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 1), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 2), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 2), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 2), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 3), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 3), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 2), "Webern", 3), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 1), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 1), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 1), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 2), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 2), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 2), "Schoenberg", 3)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 3), "Schoenberg", 1)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 3), "Schoenberg", 2)))
        Assert.azzert(all.contains(f<String, Int>(f<String, Int>(f<String, Int>("Berg", 3), "Webern", 3), "Schoenberg", 3)))
    }

    internal fun <S, T> f(d: S, r: T): FiniteFunction<S, T> {
        return FiniteFunction.Builder<S, T>().add(d, r).build()
    }

    internal fun <S, T> f(f: FiniteFunction<S, T>, domainElement: S, rangeElement: T): FiniteFunction<S, T> {
        val data = HashMap<S, T>()
        f.domain().forEach { d -> data.put(d, f.apply(d)) }
        data.put(domainElement, rangeElement)
        return FiniteFunction(data)
    }
}