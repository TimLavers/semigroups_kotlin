package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
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
        var partition = SetPartition<String>(original)
        Assert.azzert(partition.subsets().isEmpty())
        partition.addSubset(set("a", "b", "c", "d"))
        Assert.aequals(1, partition.subsets().size)
        Assert.azzert(partition.subsets().contains(set("a", "b", "c", "d")))

        original = set("a", "b", "c", "d")
        partition = SetPartition<String>(original)
        partition.addSubset(set("a", "b"))
        partition.addSubset(set("c"))
        partition.addSubset(set("d"))
        Assert.aequals(3, partition.subsets().size)
        Assert.azzert(partition.subsets().contains(set("a", "b")))
        Assert.azzert(partition.subsets().contains(set("c")))
        Assert.azzert(partition.subsets().contains(set("d")))
    }

    @Test
    fun areDistinctTest(){
        Assert.azzert(areDistinct(HashSet<Any>(), HashSet<Any>()))
        Assert.azzert(areDistinct(set("a"), HashSet<Any>()))
        Assert.azzert(areDistinct(HashSet<Any>(), set("a")))
        Assert.azzert(areDistinct(set("a"), set("b")))
        Assert.azzert(areDistinct(set("a"), set("b", "c", "d")))
        Assert.azzert(areDistinct(set("a", "f", "e"), set("b", "c", "d")))
        Assert.azzertFalse(areDistinct(set("a", "c", "e"), set("b", "c", "d")))
    }

    @Test
    fun addSubsetTest(){
        var original = set("a", "b", "c", "d")
        var partition = SetPartition<String>(original)
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("a", "b", "c", "d"))
        Assert.azzert(partition.isComplete)

        original = set("a", "b", "c", "d")
        partition = SetPartition<String>(original)
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("a", "b"))
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("c"))
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("d"))
        Assert.azzert(partition.isComplete)

        original = set("a", "b", "c", "d", "e", "f", "g")
        partition = SetPartition<String>(original)
        partition.addSubset(set("a", "b"))
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("c", "d"))
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("e", "f"))
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("g"))
        Assert.azzert(partition.isComplete)
    }

    @Test
    fun addSubsetWithElementAlreadyAddedTest(){
        val original = set("a", "b", "c", "d")
        val partition = SetPartition<String>(original)
        partition.addSubset(set("a", "b"))
        var gotError = false
        try {
            partition.addSubset(set("a", "c", "d"))
        } catch (e: AssertionError) {
            gotError = true
        }

        Assert.azzert(gotError)
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("c", "d"))
        Assert.azzert(partition.isComplete)
    }

    @Test
    fun addSubsetWithElementNotInCoverSetTest(){
        val original = set("a", "b", "c", "d")
        val partition = SetPartition<String>(original)
        partition.addSubset(set("a", "b"))
        var gotError = false
        try {
            partition.addSubset(set("c", "f"))
        } catch (e: AssertionError) {
            gotError = true
        }

        Assert.azzert(gotError)
        Assert.azzertFalse(partition.isComplete)
        partition.addSubset(set("c", "d"))
        Assert.azzert(partition.isComplete)
    }

    @Test
    fun isCompleteTest() {
            val partition = SetPartition(set("a", "b", "c", "d", "e", "f"))
            Assert.azzertFalse(partition.isComplete)
            partition.addSubset(set("a", "c", "e"))
            Assert.azzertFalse(partition.isComplete)
            partition.addSubset(set("b", "d"))
            Assert.azzertFalse(partition.isComplete)
            partition.addSubset(set("f"))
            Assert.azzert(partition.isComplete)
        }

    @Test
    fun equalsTest(){
        val p = SetPartition(set("a", "b", "c", "d", "e", "f"))
        val q = SetPartition(set("a", "b", "c", "d", "e", "f"))
        Assert.azzert(p == q)
        Assert.azzert(q == p)

        p.addSubset(set("a", "c", "e"))
        Assert.azzertFalse(p == q)
        Assert.azzertFalse(q == p)
        q.addSubset(set("a", "c", "e"))
        Assert.azzert(p == q)
        Assert.azzert(q == p)

        p.addSubset(set("b", "d"))
        Assert.azzertFalse(p == q)
        Assert.azzertFalse(q == p)
        q.addSubset(set("b", "d"))
        Assert.azzert(p == q)
        Assert.azzert(q == p)

        p.addSubset(set("f"))
        Assert.azzertFalse(p == q)
        Assert.azzertFalse(q == p)
        q.addSubset(set("f"))
        Assert.azzert(p == q)
        Assert.azzert(q == p)
    }

    @Test
    fun hashCodeTest(){
        val p = SetPartition(set("a", "b", "c", "d", "e", "f"))
        val q = SetPartition(set("a", "b", "c", "d", "e", "f"))
        Assert.aequals(p.hashCode(), q.hashCode())

        p.addSubset(set("a", "c", "e"))
        q.addSubset(set("a", "c", "e"))
        Assert.aequals(p.hashCode(), q.hashCode())

        p.addSubset(set("b", "d"))
        q.addSubset(set("b", "d"))
        Assert.aequals(p.hashCode(), q.hashCode())

        p.addSubset(set("f"))
        q.addSubset(set("f"))
        Assert.aequals(p.hashCode(), q.hashCode())
    }
}
