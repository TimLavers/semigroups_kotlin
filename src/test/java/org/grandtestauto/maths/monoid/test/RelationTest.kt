package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.*
import org.junit.Assert
import org.junit.Test

import java.util.HashSet

/**
 * @author Tim Lavers
 */
class RelationTest : TestBase() {

    @Test
    fun createRelationTest() {
        val ints = s(1,2,3,4,5)
        val gt : ((Int), (Int)) -> (Boolean) = {a, b -> a > b }
        val created = createRelation(ints, gt)
        Assert.assertEquals(10, created.size())
        assert(created.contains(tu(2, 1)))
        assert(created.contains(tu(3, 1)))
        assert(created.contains(tu(4, 1)))
        assert(created.contains(tu(5, 1)))
        assert(created.contains(tu(3, 2)))
        assert(created.contains(tu(4, 2)))
        assert(created.contains(tu(5, 2)))
        assert(created.contains(tu(4, 3)))
        assert(created.contains(tu(5, 3)))
        assert(created.contains(tu(5, 4)))
    }

    @Test
    fun transitiveClosureTest() {
        val empty = relation()
        Assert.assertEquals(0, empty.transitiveClosure().size())

        var generators = relation(tu(1, 1))
        var closure = generators.transitiveClosure()
        Assert.assertEquals(1, closure.size())
        Assert.assertTrue(closure.contains(tu(1, 1)))

        generators = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(5, 5))
        closure = generators.transitiveClosure()
        Assert.assertEquals(5, closure.size())
        Assert.assertTrue(closure.contains(tu(1, 1)))
        Assert.assertTrue(closure.contains(tu(2, 2)))
        Assert.assertTrue(closure.contains(tu(3, 3)))
        Assert.assertTrue(closure.contains(tu(4, 4)))
        Assert.assertTrue(closure.contains(tu(5, 5)))

        generators = relation(tu(1, 2), tu(2, 3), tu(3, 4), tu(4, 5), tu(5, 6))
        closure = generators.transitiveClosure()
        Assert.assertEquals(15, closure.size())
        for (i in 1..5) {
            for (j in i + 1..6) {
                Assert.assertTrue(closure.contains(tu(i, j)))
            }
        }

        //12,13,14,15,25,35,45
        generators = relation(tu(1, 2), tu(1, 3), tu(1, 4), tu(2, 5), tu(3, 5), tu(4, 5))
        closure = generators.transitiveClosure()
        val expected = relation(tu(1, 2), tu(1, 3), tu(1, 4), tu(2, 5), tu(3, 5), tu(4, 5), tu(1, 5))
        Assert.assertEquals(expected, closure)

        //12,13,14,24,34 below 45,46,47,57,67
        generators = relation(tu(1, 2), tu(1, 3), tu(2, 4), tu(3, 4), tu(4, 5), tu(4, 6), tu(4, 7), tu(5, 7), tu(6, 7))
        closure = generators.transitiveClosure()
        Assert.assertEquals(19, closure.size())
        Assert.assertTrue(closure.contains(tu(1, 2)))
        Assert.assertTrue(closure.contains(tu(1, 3)))
        Assert.assertTrue(closure.contains(tu(1, 4)))
        Assert.assertTrue(closure.contains(tu(1, 5)))
        Assert.assertTrue(closure.contains(tu(1, 6)))
        Assert.assertTrue(closure.contains(tu(1, 7)))
        Assert.assertTrue(closure.contains(tu(2, 4)))
        Assert.assertTrue(closure.contains(tu(2, 5)))
        Assert.assertTrue(closure.contains(tu(2, 6)))
        Assert.assertTrue(closure.contains(tu(2, 7)))
        Assert.assertTrue(closure.contains(tu(3, 4)))
        Assert.assertTrue(closure.contains(tu(3, 5)))
        Assert.assertTrue(closure.contains(tu(3, 6)))
        Assert.assertTrue(closure.contains(tu(3, 7)))
        Assert.assertTrue(closure.contains(tu(4, 5)))
        Assert.assertTrue(closure.contains(tu(4, 6)))
        Assert.assertTrue(closure.contains(tu(4, 7)))
        Assert.assertTrue(closure.contains(tu(5, 7)))
        Assert.assertTrue(closure.contains(tu(6, 7)))
    }

    @Test
    fun sizeTest() {
        Assert.assertEquals(0, relation().size())
        Assert.assertEquals(1, relation(tu(2, 2)).size())
        Assert.assertEquals(2, relation(tu(1, 1), tu(2, 2)).size())
        Assert.assertEquals(3, relation(tu(1, 1), tu(2, 2), tu(1, 2)).size())
        Assert.assertEquals(4, relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1)).size())
    }

    @Test
    fun isAnEquivalenceTest() {
        var r = relation()
        Assert.assertTrue(r.isAnEquivalence)

        r = relation(tu(1, 1))
        Assert.assertTrue(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2))
        Assert.assertTrue(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3))
        Assert.assertTrue(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4))
        Assert.assertTrue(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(1, 3), tu(3, 1), tu(2, 4), tu(4, 2))
        Assert.assertTrue(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(1, 3), tu(3, 1), tu(2, 4), tu(4, 2), tu(1, 4))
        Assert.assertFalse(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(5, 5), tu(6, 6), tu(1, 4), tu(4, 1), tu(2, 5), tu(5, 2), tu(3, 6), tu(6, 3))
        Assert.assertTrue(r.isAnEquivalence)
    }

    @Test
    fun generateEquivalenceRelationTest() {
        var r = relation()
        Assert.assertEquals(r, r.generateEquivalenceRelation())

        r = relation(tu(1, 1))
        Assert.assertEquals(r, r.generateEquivalenceRelation())

        r = relation(tu(1, 2))
        var expected = relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1))
        var generated = r.generateEquivalenceRelation()
        Assert.assertEquals(expected, generated)
        Assert.assertTrue(generated.isAnEquivalence)

        r = relation(tu(1, 2), tu(1, 3))
        expected = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(1, 2), tu(2, 1), tu(1, 3), tu(3, 1), tu(2, 3), tu(3, 2))
        generated = r.generateEquivalenceRelation()
        Assert.assertEquals(expected, generated)
        Assert.assertTrue(generated.isAnEquivalence)

        r = relation(tu(1, 2), tu(1, 3), tu(1, 4), tu(1, 5), tu(1, 6), tu(1, 7), tu(1, 8), tu(1, 9), tu(1, 10))
        generated = r.generateEquivalenceRelation()
        val tuples = mutableSetOf<Tuple<Int, Int>>()
        val baseSet = mutableSetOf<Int>()
        for (i in 1..10) {
            baseSet.add(i)
            for (j in 1..10) {
                tuples.add(tu(i, j))
                tuples.add(tu(j, i))
            }
        }
        val expected2 = Relation(baseSet, tuples)
        Assert.assertEquals(expected2, generated)
        Assert.assertTrue(generated.isAnEquivalence)

        r = relation(tu(1, 2), tu(2, 3), tu(3, 4), tu(4, 5), tu(5, 6), tu(6, 7), tu(7, 8), tu(8, 9), tu(9, 10))
        generated = r.generateEquivalenceRelation()
        Assert.assertEquals(expected2, generated)
        Assert.assertTrue(generated.isAnEquivalence)
    }

    @Test
    fun toStringTest() {
        var r = relation()
        Assert.assertEquals("{[]}", r.toString())

        r = relation(tu(1, 1))
        Assert.assertEquals("{[<1, 1>]}", r.toString())

        r = relation(tu(1, 1), tu(1, 3), tu(2, 3))
        Assert.assertTrue(r.toString().contains(tu(1, 1).toString()))
        Assert.assertTrue(r.toString().contains(tu(1, 3).toString()))
        Assert.assertTrue(r.toString().contains(tu(2, 3).toString()))
    }

    @Test
    fun equalsTest() {
        var r = relation()
        var s = relation()
        Assert.assertTrue(r == s)

        r = relation(tu(1, 1))
        Assert.assertFalse(r == s)
        Assert.assertFalse(s == r)

        s = relation(tu(1, 1))
        Assert.assertTrue(r == s)
        Assert.assertTrue(s == r)

        r = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        Assert.assertFalse(r == s)
        Assert.assertFalse(s == r)

        s = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        Assert.assertTrue(r == s)
        Assert.assertTrue(s == r)

        //Now with the same pairs but different base sets.
        r = relation(tu(1, 1))
        val elements = mutableSetOf<Tuple<Int, Int>>()
        val baseSet = mutableSetOf<Int>()
        elements.add(tu(1, 1))
        baseSet.add(1)
        baseSet.add(2)
        val t = Relation(baseSet, elements)
        Assert.assertFalse(r == t)
        Assert.assertFalse(t == r)
    }

    @Test
    fun hashCodeTest() {
        var r = relation()
        var s = relation()
        Assert.assertEquals(r.hashCode(), s.hashCode())

        r = relation(tu(1, 1))
        s = relation(tu(1, 1))
        Assert.assertEquals(r.hashCode(), s.hashCode())

        r = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        s = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        Assert.assertEquals(r.hashCode(), s.hashCode())
    }

    @Test
    fun containsTest() {
        var relation = relation()
        Assert.assertFalse(relation.contains(tu(1, 1)))
        Assert.assertFalse(relation.contains(tu(1, 2)))

        relation = relation(tu(1, 2), tu(1, 3))
        Assert.assertFalse(relation.contains(tu(1, 1)))
        Assert.assertTrue(relation.contains(tu(1, 2)))
    }

    @Test
    fun isSymmetricTest() {
        var relation = relation()
        Assert.assertTrue(relation.isSymmetric)

        relation = relation(tu(1, 2), tu(1, 3))
        Assert.assertFalse(relation.isSymmetric)

        relation = relation(tu(1, 2), tu(2, 1))
        Assert.assertTrue(relation.isSymmetric)

        relation = relation(tu(1, 2), tu(2, 1), tu(3, 4))
        Assert.assertFalse(relation.isSymmetric)

        relation = relation(tu(4, 3), tu(1, 2), tu(2, 1), tu(3, 4))
        Assert.assertTrue(relation.isSymmetric)

        relation = relation(tu(4, 3), tu(3, 5), tu(1, 2), tu(2, 1), tu(3, 4))
        Assert.assertFalse(relation.isSymmetric)

        relation = relation(tu(4, 3), tu(3, 5), tu(1, 2), tu(5, 3), tu(2, 1), tu(3, 4))
        Assert.assertTrue(relation.isSymmetric)
    }

    @Test
    fun isReflexiveTest() {
        var relation = relation()
        Assert.assertTrue(relation.isReflexive)

        relation = relation(tu(1, 1))
        Assert.assertTrue(relation.isReflexive)

        relation = relation(tu(1, 1), tu(1, 2), tu(3, 4), tu(3, 3))
        Assert.assertFalse(relation.isReflexive)

        relation = relation(tu(1, 1), tu(1, 2), tu(3, 4), tu(2, 2), tu(4, 4), tu(3, 3))
        Assert.assertTrue(relation.isReflexive)
    }

    @Test
    fun isTransitiveTest() {
        var relation = relation()
        Assert.assertTrue(relation.isTransitive)

        relation = relation(tu(1, 1))
        Assert.assertTrue(relation.isTransitive)

        relation = relation(tu(1, 3))
        Assert.assertTrue(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1))
        Assert.assertFalse(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 3))
        Assert.assertTrue(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1))
        Assert.assertFalse(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1), tu(1, 1))
        Assert.assertFalse(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1), tu(1, 1), tu(3, 3))
        Assert.assertTrue(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1), tu(1, 1), tu(3, 3))
        Assert.assertTrue(relation.isTransitive)
    }

    @Test
    fun isTransitiveUsingLessThanTest() {
        val n = 10
        val pairs = HashSet<Tuple<Int, Int>>()
        val baseSet = HashSet<Int>()
        for (i in 1..n) {
            baseSet.add(i)
            for (j in 1..n) {
                if (i <= j) {
                    pairs.add(tu(i, j))
                }
            }
        }
        var relation = Relation(baseSet, pairs)
        Assert.assertTrue(relation.isTransitive)

        pairs.add(tu(6, 3))
        relation = Relation(baseSet, pairs)
        Assert.assertFalse(relation.isTransitive)
    }

    @Test
    fun isAPartialOrderTest() {
        val n = 20
        val pairs = HashSet<Tuple<Int, Int>>()
        val baseSet = HashSet<Int>()
        for (i in 1..n) {
            baseSet.add(i)
            for (j in 1..n) {
                if (i <= j) {
                    pairs.add(tu(i, j))
                }
            }
        }
        var relation = Relation(baseSet, pairs)
        Assert.assertTrue(relation.isAPartialOrder)

        pairs.add(tu(6, 3))
        relation = Relation(baseSet, pairs)
        Assert.assertFalse(relation.isAPartialOrder)
    }

    @Test
    fun isAPartialOrderPowerSetTest() {
        val powerSet = powerSet(6)
        val tuples = mutableSetOf<Tuple<Set<Int>, Set<Int>>>()
        val baseSet = mutableSetOf<Set<Int>>()
        powerSet.forEach { intSetLeft ->
            baseSet.add(intSetLeft)
            powerSet.forEach { intSetRight ->
                if (intSetLeft isASubsetOf intSetRight) {
                    tuples.add(Tuple(intSetLeft, intSetRight))
                }
            }
        }
        val contains = Relation(baseSet, tuples)
        Assert.assertTrue(contains.isAPartialOrder)
    }
}
