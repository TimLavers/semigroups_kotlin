package org.grandtestauto.maths.monoid.test

import org.junit.Assert
import org.grandtestauto.maths.monoid.SubsetsOfSizeN
import org.junit.Test

import java.util.HashSet

/**
 * @author Tim Lavers
 */
class SubsetsOfSizeNTest : TestBase() {

    @Test
    fun subsetsTest() {
        val original = set("a", "b", "c", "d")
        var subsetsOfSizeN = SubsetsOfSizeN(original, 0)
        var expected: MutableSet<Set<String>> = HashSet()
        expected.add(HashSet<String>())
        Assert.assertEquals(expected, subsetsOfSizeN.subsets())

        subsetsOfSizeN = SubsetsOfSizeN(original, 1)
        expected = HashSet<Set<String>>()
        expected.add(set("a"))
        expected.add(set("b"))
        expected.add(set("c"))
        expected.add(set("d"))
        Assert.assertEquals(expected, subsetsOfSizeN.subsets())

        subsetsOfSizeN = SubsetsOfSizeN(original, 2)
        expected = HashSet<Set<String>>()
        expected.add(set("a", "b"))
        expected.add(set("a", "c"))
        expected.add(set("a", "d"))
        expected.add(set("b", "c"))
        expected.add(set("b", "d"))
        expected.add(set("c", "d"))
        Assert.assertEquals(expected, subsetsOfSizeN.subsets())

        subsetsOfSizeN = SubsetsOfSizeN(original, 3)
        expected = HashSet<Set<String>>()
        expected.add(set("a", "b", "c"))
        expected.add(set("a", "b", "d"))
        expected.add(set("a", "c", "d"))
        expected.add(set("b", "c", "d"))
        Assert.assertEquals(expected, subsetsOfSizeN.subsets())

        subsetsOfSizeN = SubsetsOfSizeN(original, 4)
        expected = HashSet<Set<String>>()
        expected.add(set("a", "b", "c", "d"))
        Assert.assertEquals(expected, subsetsOfSizeN.subsets())
    }
}
