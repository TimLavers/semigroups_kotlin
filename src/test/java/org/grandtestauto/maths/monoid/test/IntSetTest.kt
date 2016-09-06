package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.powerSet
import org.junit.Test

/**
 * @author Tim Lavers
 */
class IntSetTest : TestBase() {

    @Test
    fun transformTest() {
        Assert.aequals(s(1, 3, 5), s(2, 4, 6).transform(t(1, 1, 3, 3, 5, 5)))
        Assert.aequals(s(1, 3), s(2, 4, 6).transform(t(1, 1, 3, 3)))
        Assert.aequals(s(1, 2, 3), s(1, 2, 3).transform(t(2, 1, 3)))
        Assert.aequals(s(1, 7), s(1, 2, 4).transform(t(7, 1, 3, 7, 5, 5, 5)))
    }

    @Test
    fun powerSetTest() {
        var powerSet = powerSet(1)
        Assert.aequals(2, powerSet.size)
        Assert.azzert(powerSet.contains(s(1)))
        Assert.azzert(powerSet.contains(s()))

        powerSet = powerSet(2)
        Assert.aequals(4, powerSet.size)
        Assert.azzert(powerSet.contains(s()))
        Assert.azzert(powerSet.contains(s(1)))
        Assert.azzert(powerSet.contains(s(2)))
        Assert.azzert(powerSet.contains(s(1, 2)))

        powerSet = powerSet(4)
        Assert.aequals(16, powerSet.size)
        Assert.azzert(powerSet.contains(s()))
        Assert.azzert(powerSet.contains(s(1)))
        Assert.azzert(powerSet.contains(s(2)))
        Assert.azzert(powerSet.contains(s(3)))
        Assert.azzert(powerSet.contains(s(4)))
        Assert.azzert(powerSet.contains(s(1, 2)))
        Assert.azzert(powerSet.contains(s(1, 3)))
        Assert.azzert(powerSet.contains(s(1, 4)))
        Assert.azzert(powerSet.contains(s(2, 3)))
        Assert.azzert(powerSet.contains(s(2, 4)))
        Assert.azzert(powerSet.contains(s(3, 4)))
        Assert.azzert(powerSet.contains(s(1, 2, 3)))
        Assert.azzert(powerSet.contains(s(1, 2, 4)))
        Assert.azzert(powerSet.contains(s(1, 3, 4)))
        Assert.azzert(powerSet.contains(s(2, 3, 4)))
        Assert.azzert(powerSet.contains(s(1, 2, 3, 4)))


    }

    @Test
    fun withTest() {
        Assert.aequals(s(1, 2, 6), s(6, 2, 1).with(2))
        Assert.aequals(s(1, 2, 6), s(6, 2, 1).with(2))
        Assert.aequals(s(1, 2, 6), s(6, 1).with(2))
    }

    @Test
    fun equalsTest() {
        Assert.azzert(s(1, 2, 6).equals(s(6, 2, 1)))
        Assert.azzert(s().equals(s()))

        Assert.azzertFalse(s(2, 6).equals(s(6, 2, 1)))
        Assert.azzertFalse(s().equals(s(6, 2, 1)))
    }

    @Test
    fun hashCodeTest() {
        val s = s(1, 3, 5, 7)
        val t = s(1, 3, 5, 7)
        Assert.aequals(s.hashCode(), t.hashCode())
    }

    @Test
    fun toStringTest() {
        Assert.aequals("IntSet{1,3,7}", s(7, 3, 1).toString())
    }

    @Test
    fun containsTest() {
        Assert.azzert(s(1, 2, 3, 5, 7, 11).contains(1))
        Assert.azzert(s(1, 2, 3, 5, 7, 11).contains(3))
        Assert.azzert(s(1, 2, 3, 5, 7, 11).contains(5))
        Assert.azzertFalse(s(1, 2, 3, 5, 7, 11).contains(6))
        Assert.azzertFalse(s().contains(6))
    }

    @Test
    fun sizeTest() {
        Assert.aequals(6, s(1, 2, 3, 5, 7, 11).size())
    }

    @Test
    fun unionTest() {
        val union = s(1, 2, 3, 5, 8, 13).union(s(1, 2, 3, 5, 7, 11))
        Assert.aequals(8, union.size())
        Assert.azzert(union.contains(1))
        Assert.azzert(union.contains(2))
        Assert.azzert(union.contains(3))
        Assert.azzert(union.contains(5))
        Assert.azzert(union.contains(7))
        Assert.azzert(union.contains(8))
        Assert.azzert(union.contains(11))
        Assert.azzert(union.contains(13))
    }

    @Test
    fun intersectionTest() {
        val intersection = s(1, 2, 3, 5, 8, 13).intersection(s(1, 2, 3, 5, 7, 11))
        Assert.aequals(4, intersection.size())
        Assert.azzert(intersection.contains(1))
        Assert.azzert(intersection.contains(2))
        Assert.azzert(intersection.contains(3))
        Assert.azzert(intersection.contains(5))
    }

    @Test
    fun isASubsetOfTest() {
        //Assert.azzert(s().isASubsetOf(s()))
        Assert.azzert(s().isASubsetOf(s(1)))
        Assert.azzertFalse(s(1).isASubsetOf(s()))
        Assert.azzert(s(1, 3, 5, 7).isASubsetOf(s(1, 2, 3, 4, 5, 6, 7, 8)))
    }
}