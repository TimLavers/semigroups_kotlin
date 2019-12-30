package org.grandtestauto.maths.monoid.test

import org.junit.Assert
import org.grandtestauto.maths.monoid.SetPartition
import org.grandtestauto.maths.monoid.areDistinct
import org.junit.Test

import java.util.HashSet

/**
 * @author Tim Lavers
 */
class SetPartitionTest : TestBase() {

    @Test
    fun subsetsTest(){
        var original = set("a", "b", "c", "d")
        var partition = SetPartition(original)
        Assert.assertTrue(partition.subsets().isEmpty())
        partition.addSubset(set("a", "b", "c", "d"))
        Assert.assertEquals(1, partition.subsets().size)
        Assert.assertTrue(partition.subsets().contains(set("a", "b", "c", "d")))

        original = set("a", "b", "c", "d")
        partition = SetPartition(original)
        partition.addSubset(set("a", "b"))
        partition.addSubset(set("c"))
        partition.addSubset(set("d"))
        Assert.assertEquals(3, partition.subsets().size)
        Assert.assertTrue(partition.subsets().contains(set("a", "b")))
        Assert.assertTrue(partition.subsets().contains(set("c")))
        Assert.assertTrue(partition.subsets().contains(set("d")))
    }

    @Test
    fun areDistinctTest(){
        Assert.assertTrue(areDistinct(HashSet<Any>(), HashSet<Any>()))
        Assert.assertTrue(areDistinct(set("a"), HashSet<Any>()))
        Assert.assertTrue(areDistinct(HashSet<Any>(), set("a")))
        Assert.assertTrue(areDistinct(set("a"), set("b")))
        Assert.assertTrue(areDistinct(set("a"), set("b", "c", "d")))
        Assert.assertTrue(areDistinct(set("a", "f", "e"), set("b", "c", "d")))
        Assert.assertFalse(areDistinct(set("a", "c", "e"), set("b", "c", "d")))
    }

    @Test
    fun addSubsetTest(){
        var original = set("a", "b", "c", "d")
        var partition = SetPartition(original)
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("a", "b", "c", "d"))
        Assert.assertTrue(partition.isComplete)

        original = set("a", "b", "c", "d")
        partition = SetPartition(original)
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("a", "b"))
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("c"))
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("d"))
        Assert.assertTrue(partition.isComplete)

        original = set("a", "b", "c", "d", "e", "f", "g")
        partition = SetPartition(original)
        partition.addSubset(set("a", "b"))
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("c", "d"))
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("e", "f"))
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("g"))
        Assert.assertTrue(partition.isComplete)
    }

    @Test
    fun addSubsetWithElementAlreadyAddedTest(){
        val original = set("a", "b", "c", "d")
        val partition = SetPartition(original)
        partition.addSubset(set("a", "b"))
        var gotError = false
        try {
            partition.addSubset(set("a", "c", "d"))
        } catch (e: AssertionError) {
            gotError = true
        }

        Assert.assertTrue(gotError)
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("c", "d"))
        Assert.assertTrue(partition.isComplete)
    }

    @Test
    fun addSubsetWithElementNotInCoverSetTest(){
        val original = set("a", "b", "c", "d")
        val partition = SetPartition(original)
        partition.addSubset(set("a", "b"))
        var gotError = false
        try {
            partition.addSubset(set("c", "f"))
        } catch (e: AssertionError) {
            gotError = true
        }

        Assert.assertTrue(gotError)
        Assert.assertFalse(partition.isComplete)
        partition.addSubset(set("c", "d"))
        Assert.assertTrue(partition.isComplete)
    }

    @Test
    fun isCompleteTest() {
            val partition = SetPartition(set("a", "b", "c", "d", "e", "f"))
            Assert.assertFalse(partition.isComplete)
            partition.addSubset(set("a", "c", "e"))
            Assert.assertFalse(partition.isComplete)
            partition.addSubset(set("b", "d"))
            Assert.assertFalse(partition.isComplete)
            partition.addSubset(set("f"))
            Assert.assertTrue(partition.isComplete)
        }

    @Test
    fun equalsTest(){
        val p = SetPartition(set("a", "b", "c", "d", "e", "f"))
        val q = SetPartition(set("a", "b", "c", "d", "e", "f"))
        Assert.assertTrue(p == q)
        Assert.assertTrue(q == p)

        p.addSubset(set("a", "c", "e"))
        Assert.assertFalse(p == q)
        Assert.assertFalse(q == p)
        q.addSubset(set("a", "c", "e"))
        Assert.assertTrue(p == q)
        Assert.assertTrue(q == p)

        p.addSubset(set("b", "d"))
        Assert.assertFalse(p == q)
        Assert.assertFalse(q == p)
        q.addSubset(set("b", "d"))
        Assert.assertTrue(p == q)
        Assert.assertTrue(q == p)

        p.addSubset(set("f"))
        Assert.assertFalse(p == q)
        Assert.assertFalse(q == p)
        q.addSubset(set("f"))
        Assert.assertTrue(p == q)
        Assert.assertTrue(q == p)
    }

    @Test
    fun hashCodeTest(){
        val p = SetPartition(set("a", "b", "c", "d", "e", "f"))
        val q = SetPartition(set("a", "b", "c", "d", "e", "f"))
        Assert.assertEquals(p.hashCode(), q.hashCode())

        p.addSubset(set("a", "c", "e"))
        q.addSubset(set("a", "c", "e"))
        Assert.assertEquals(p.hashCode(), q.hashCode())

        p.addSubset(set("b", "d"))
        q.addSubset(set("b", "d"))
        Assert.assertEquals(p.hashCode(), q.hashCode())

        p.addSubset(set("f"))
        q.addSubset(set("f"))
        Assert.assertEquals(p.hashCode(), q.hashCode())
    }
}
