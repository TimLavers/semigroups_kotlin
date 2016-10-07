package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

/**
 * @author Tim Lavers
 */
class IntSetTest : TestBase() {

    @Test
    fun transformTest() {
        assertEquals(s(1, 3, 5), s(2, 4, 6) transform t(1, 1, 3, 3, 5, 5))
        assertEquals(s(1, 3), s(2, 4, 6) transform t(1, 1, 3, 3))
        assertEquals(s(1, 2, 3), s(1, 2, 3) transform t(2, 1, 3))
        assertEquals(s(1, 7), s(1, 2, 4) transform t(7, 1, 3, 7, 5, 5, 5))
    }

    @Test
    fun powerSetTest() {
        var powerSet = powerSet(1)
        assertEquals(2, powerSet.size)
        assertTrue(powerSet.contains(s(1)))
        assertTrue(powerSet.contains(s()))

        powerSet = powerSet(2)
        assertEquals(4, powerSet.size)
        assertTrue(powerSet.contains(s()))
        assertTrue(powerSet.contains(s(1)))
        assertTrue(powerSet.contains(s(2)))
        assertTrue(powerSet.contains(s(1, 2)))

        powerSet = powerSet(4)
        assertEquals(16, powerSet.size)
        assertTrue(powerSet.contains(s()))
        assertTrue(powerSet.contains(s(1)))
        assertTrue(powerSet.contains(s(2)))
        assertTrue(powerSet.contains(s(3)))
        assertTrue(powerSet.contains(s(4)))
        assertTrue(powerSet.contains(s(1, 2)))
        assertTrue(powerSet.contains(s(1, 3)))
        assertTrue(powerSet.contains(s(1, 4)))
        assertTrue(powerSet.contains(s(2, 3)))
        assertTrue(powerSet.contains(s(2, 4)))
        assertTrue(powerSet.contains(s(3, 4)))
        assertTrue(powerSet.contains(s(1, 2, 3)))
        assertTrue(powerSet.contains(s(1, 2, 4)))
        assertTrue(powerSet.contains(s(1, 3, 4)))
        assertTrue(powerSet.contains(s(2, 3, 4)))
        assertTrue(powerSet.contains(s(1, 2, 3, 4)))
    }

    @Test
    fun unionCompositionTest() {
        val semigroup2 = Semigroup(intsets( s(1, 2), s(1,4), s(1, 2, 4)), UnionComposition)
        assertEquals(3, semigroup2.size())
    }

    @Test
    fun intersectionCompositionTest() {
        val semigroup2 = Semigroup(intsets( s(1), s(1, 2), s(1,4), s(1, 2, 4)), IntersectionComposition)
        assertEquals(4, semigroup2.size())
    }

    @Test
    fun isASubsetOfTest() {
        //Assert.assertFalse(s().isASubsetOf(s()))
        assertTrue(s() isASubsetOf s(1))
        Assert.assertFalse(s(1) isASubsetOf s())
        assertTrue(s(1, 3, 5, 7).isASubsetOf(s(1, 2, 3, 4, 5, 6, 7, 8)))
    }
}