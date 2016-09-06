package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.Partitioner
import org.grandtestauto.maths.monoid.SetPartition
import org.junit.Test

import java.util.HashSet
import java.util.function.BiPredicate

/**
 * @author Tim Lavers
 */
class PartitionerTest : TestBase() {

    @Test
    fun modulusTest() {
        val ints = HashSet<Int>()
        for (i in 0..99) {
            ints.add(i)
        }
        val partitioner = Partitioner<Int>(ints, BiPredicate<Int, Int> { t, u -> t % 5 == u % 5 })
        val mod0 = HashSet<Int>()
        val mod1 = HashSet<Int>()
        val mod2 = HashSet<Int>()
        val mod3 = HashSet<Int>()
        val mod4 = HashSet<Int>()
        for (i in 0..19) {
            mod0.add(i * 5)
            mod1.add(i * 5 + 1)
            mod2.add(i * 5 + 2)
            mod3.add(i * 5 + 3)
            mod4.add(i * 5 + 4)
        }
        val expected = SetPartition(ints)
        expected.addSubset(mod0)
        expected.addSubset(mod1)
        expected.addSubset(mod2)
        expected.addSubset(mod3)
        expected.addSubset(mod4)
        Assert.aequals(expected, partitioner.equivalenceClasses())
    }
}
