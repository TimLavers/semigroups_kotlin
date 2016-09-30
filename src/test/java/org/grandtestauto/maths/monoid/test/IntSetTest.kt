package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Test

/**
 * @author Tim Lavers
 */
class IntSetTest : TestBase() {

    @Test
    fun transformTest() {
        Assert.assertEquals(s(1, 3, 5), s(2, 4, 6).transform(t(1, 1, 3, 3, 5, 5)))
        Assert.assertEquals(s(1, 3), s(2, 4, 6).transform(t(1, 1, 3, 3)))
        Assert.assertEquals(s(1, 2, 3), s(1, 2, 3).transform(t(2, 1, 3)))
        Assert.assertEquals(s(1, 7), s(1, 2, 4).transform(t(7, 1, 3, 7, 5, 5, 5)))
    }

    @Test
    fun powerSetTest() {
        var powerSet = powerSet(1)
        Assert.assertEquals(2, powerSet.size)
        Assert.assertTrue(powerSet.contains(s(1)))
        Assert.assertTrue(powerSet.contains(s()))

        powerSet = powerSet(2)
        Assert.assertEquals(4, powerSet.size)
        Assert.assertTrue(powerSet.contains(s()))
        Assert.assertTrue(powerSet.contains(s(1)))
        Assert.assertTrue(powerSet.contains(s(2)))
        Assert.assertTrue(powerSet.contains(s(1, 2)))

        powerSet = powerSet(4)
        Assert.assertEquals(16, powerSet.size)
        Assert.assertTrue(powerSet.contains(s()))
        Assert.assertTrue(powerSet.contains(s(1)))
        Assert.assertTrue(powerSet.contains(s(2)))
        Assert.assertTrue(powerSet.contains(s(3)))
        Assert.assertTrue(powerSet.contains(s(4)))
        Assert.assertTrue(powerSet.contains(s(1, 2)))
        Assert.assertTrue(powerSet.contains(s(1, 3)))
        Assert.assertTrue(powerSet.contains(s(1, 4)))
        Assert.assertTrue(powerSet.contains(s(2, 3)))
        Assert.assertTrue(powerSet.contains(s(2, 4)))
        Assert.assertTrue(powerSet.contains(s(3, 4)))
        Assert.assertTrue(powerSet.contains(s(1, 2, 3)))
        Assert.assertTrue(powerSet.contains(s(1, 2, 4)))
        Assert.assertTrue(powerSet.contains(s(1, 3, 4)))
        Assert.assertTrue(powerSet.contains(s(2, 3, 4)))
        Assert.assertTrue(powerSet.contains(s(1, 2, 3, 4)))
    }

    @Test
    fun withTest() {
        Assert.assertEquals(s(1, 2, 6), s(6, 2, 1).with(2))
        Assert.assertEquals(s(1, 2, 6), s(6, 2, 1).with(2))
        Assert.assertEquals(s(1, 2, 6), s(6, 1).with(2))
    }

    @Test
    fun equalsTest() {
        Assert.assertTrue(s(1, 2, 6).equals(s(6, 2, 1)))
        Assert.assertTrue(s().equals(s()))

        Assert.assertFalse(s(2, 6).equals(s(6, 2, 1)))
        Assert.assertFalse(s().equals(s(6, 2, 1)))
    }

    @Test
    fun hashCodeTest() {
        val s = s(1, 3, 5, 7)
        val t = s(1, 3, 5, 7)
        Assert.assertEquals(s.hashCode(), t.hashCode())
    }

    @Test
    fun toStringTest() {
        Assert.assertEquals("IntSet{1,3,7}", s(7, 3, 1).toString())
    }

    @Test
    fun unionCompositionTest() {
        val semigroup2 = Semigroup(intsets( s(1, 2), s(1,4), s(1, 2, 4)), UnionComposition)
        Assert.assertEquals(3, semigroup2.size())
    }

    @Test
    fun intersectionCompositionTest() {
        val semigroup2 = Semigroup(intsets( s(1), s(1, 2), s(1,4), s(1, 2, 4)), IntersectionComposition)
        Assert.assertEquals(4, semigroup2.size())
    }

    @Test
    fun containsTest() {
        Assert.assertTrue(s(1, 2, 3, 5, 7, 11).contains(1))
        Assert.assertTrue(s(1, 2, 3, 5, 7, 11).contains(3))
        Assert.assertTrue(s(1, 2, 3, 5, 7, 11).contains(5))
        Assert.assertFalse(s(1, 2, 3, 5, 7, 11).contains(6))
        Assert.assertFalse(s().contains(6))
    }

    @Test
    fun sizeTest() {
        Assert.assertEquals(6, s(1, 2, 3, 5, 7, 11).size())
    }

    @Test
    fun unionTest() {
        val union = s(1, 2, 3, 5, 8, 13).union(s(1, 2, 3, 5, 7, 11))
        Assert.assertEquals(8, union.size())
        Assert.assertTrue(union.contains(1))
        Assert.assertTrue(union.contains(2))
        Assert.assertTrue(union.contains(3))
        Assert.assertTrue(union.contains(5))
        Assert.assertTrue(union.contains(7))
        Assert.assertTrue(union.contains(8))
        Assert.assertTrue(union.contains(11))
        Assert.assertTrue(union.contains(13))
    }

    @Test
    fun intersectionTest() {
        val intersection = s(1, 2, 3, 5, 8, 13).intersection(s(1, 2, 3, 5, 7, 11))
        Assert.assertEquals(4, intersection.size())
        Assert.assertTrue(intersection.contains(1))
        Assert.assertTrue(intersection.contains(2))
        Assert.assertTrue(intersection.contains(3))
        Assert.assertTrue(intersection.contains(5))
    }

    @Test
    fun isASubsetOfTest() {
        //Assert.assertFalse(s().isASubsetOf(s()))
        Assert.assertTrue(s().isASubsetOf(s(1)))
        Assert.assertFalse(s(1).isASubsetOf(s()))
        Assert.assertTrue(s(1, 3, 5, 7).isASubsetOf(s(1, 2, 3, 4, 5, 6, 7, 8)))
    }
}