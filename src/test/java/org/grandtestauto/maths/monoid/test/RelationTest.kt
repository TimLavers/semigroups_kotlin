package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.IntSet
import org.grandtestauto.maths.monoid.Relation
import org.grandtestauto.maths.monoid.Tuple
import org.grandtestauto.maths.monoid.powerSet
import org.junit.Test

import java.util.HashSet

/**
 * @author Tim Lavers
 */
class RelationTest : TestBase() {

    @Test
    fun transitiveClosureTest() {
        val empty = relation()
        Assert.aequals(0, empty.transitiveClosure().size())

        var generators = relation(tu(1, 1))
        var closure = generators.transitiveClosure()
        Assert.aequals(1, closure.size())
        Assert.azzert(closure.contains(tu(1, 1)))

        generators = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(5, 5))
        closure = generators.transitiveClosure()
        Assert.aequals(5, closure.size())
        Assert.azzert(closure.contains(tu(1, 1)))
        Assert.azzert(closure.contains(tu(2, 2)))
        Assert.azzert(closure.contains(tu(3, 3)))
        Assert.azzert(closure.contains(tu(4, 4)))
        Assert.azzert(closure.contains(tu(5, 5)))

        generators = relation(tu(1, 2), tu(2, 3), tu(3, 4), tu(4, 5), tu(5, 6))
        closure = generators.transitiveClosure()
        Assert.aequals(15, closure.size())
        for (i in 1..5) {
            for (j in i + 1..6) {
                Assert.azzert(closure.contains(tu(i, j)))
            }
        }

        //12,13,14,15,25,35,45
        generators = relation(tu(1, 2), tu(1, 3), tu(1, 4), tu(2, 5), tu(3, 5), tu(4, 5))
        closure = generators.transitiveClosure()
        val expected = relation(tu(1, 2), tu(1, 3), tu(1, 4), tu(2, 5), tu(3, 5), tu(4, 5), tu(1, 5))
        Assert.aequals(expected, closure)

        //12,13,14,24,34 below 45,46,47,57,67
        generators = relation(tu(1, 2), tu(1, 3), tu(2, 4), tu(3, 4), tu(4, 5), tu(4, 6), tu(4, 7), tu(5, 7), tu(6, 7))
        closure = generators.transitiveClosure()
        Assert.aequals(19, closure.size())
        Assert.azzert(closure.contains(tu(1, 2)))
        Assert.azzert(closure.contains(tu(1, 3)))
        Assert.azzert(closure.contains(tu(1, 4)))
        Assert.azzert(closure.contains(tu(1, 5)))
        Assert.azzert(closure.contains(tu(1, 6)))
        Assert.azzert(closure.contains(tu(1, 7)))
        Assert.azzert(closure.contains(tu(2, 4)))
        Assert.azzert(closure.contains(tu(2, 5)))
        Assert.azzert(closure.contains(tu(2, 6)))
        Assert.azzert(closure.contains(tu(2, 7)))
        Assert.azzert(closure.contains(tu(3, 4)))
        Assert.azzert(closure.contains(tu(3, 5)))
        Assert.azzert(closure.contains(tu(3, 6)))
        Assert.azzert(closure.contains(tu(3, 7)))
        Assert.azzert(closure.contains(tu(4, 5)))
        Assert.azzert(closure.contains(tu(4, 6)))
        Assert.azzert(closure.contains(tu(4, 7)))
        Assert.azzert(closure.contains(tu(5, 7)))
        Assert.azzert(closure.contains(tu(6, 7)))
    }

    @Test
    fun sizeTest() {
        Assert.aequals(0, relation().size())
        Assert.aequals(1, relation(tu(2, 2)).size())
        Assert.aequals(2, relation(tu(1, 1), tu(2, 2)).size())
        Assert.aequals(3, relation(tu(1, 1), tu(2, 2), tu(1, 2)).size())
        Assert.aequals(4, relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1)).size())
    }

    @Test
    fun isAnEquivalenceTest() {
        var r = relation()
        Assert.azzert(r.isAnEquivalence)

        r = relation(tu(1, 1))
        Assert.azzert(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2))
        Assert.azzert(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3))
        Assert.azzert(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4))
        Assert.azzert(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(1, 3), tu(3, 1), tu(2, 4), tu(4, 2))
        Assert.azzert(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(1, 3), tu(3, 1), tu(2, 4), tu(4, 2), tu(1, 4))
        Assert.azzertFalse(r.isAnEquivalence)

        r = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4), tu(5, 5), tu(6, 6), tu(1, 4), tu(4, 1), tu(2, 5), tu(5, 2), tu(3, 6), tu(6, 3))
        Assert.azzert(r.isAnEquivalence)
    }

    @Test
    fun generateEquivalenceRelationTest() {
        var r = relation()
        Assert.aequals(r, r.generateEquivalenceRelation())

        r = relation(tu(1, 1))
        Assert.aequals(r, r.generateEquivalenceRelation())

        r = relation(tu(1, 2))
        var expected = relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1))
        var generated = r.generateEquivalenceRelation()
        Assert.aequals(expected, generated)
        Assert.azzert(generated.isAnEquivalence)

        r = relation(tu(1, 2), tu(1, 3))
        expected = relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(1, 2), tu(2, 1), tu(1, 3), tu(3, 1), tu(2, 3), tu(3, 2))
        generated = r.generateEquivalenceRelation()
        Assert.aequals(expected, generated)
        Assert.azzert(generated.isAnEquivalence)

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
        var expected2 = Relation<Int>(baseSet, tuples)
        Assert.aequals(expected2, generated)
        Assert.azzert(generated.isAnEquivalence)

        r = relation(tu(1, 2), tu(2, 3), tu(3, 4), tu(4, 5), tu(5, 6), tu(6, 7), tu(7, 8), tu(8, 9), tu(9, 10))
        generated = r.generateEquivalenceRelation()
        Assert.aequals(expected2, generated)
        Assert.azzert(generated.isAnEquivalence)
    }

    @Test
    fun toStringTest() {
        var r = relation()
        Assert.aequals("{[]}", r.toString())

        r = relation(tu(1, 1))
        Assert.aequals("{[<1, 1>]}", r.toString())

        r = relation(tu(1, 1), tu(1, 3), tu(2, 3))
        Assert.azzert(r.toString().contains(tu(1, 1).toString()))
        Assert.azzert(r.toString().contains(tu(1, 3).toString()))
        Assert.azzert(r.toString().contains(tu(2, 3).toString()))
    }

    @Test
    fun equalsTest() {
        var r = relation()
        var s = relation()
        Assert.azzert(r == s)

        r = relation(tu(1, 1))
        Assert.azzertFalse(r == s)
        Assert.azzertFalse(s == r)

        s = relation(tu(1, 1))
        Assert.azzert(r == s)
        Assert.azzert(s == r)

        r = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        Assert.azzertFalse(r == s)
        Assert.azzertFalse(s == r)

        s = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        Assert.azzert(r == s)
        Assert.azzert(s == r)

        //Now with the same pairs but different base sets.
        r = relation(tu(1, 1))
        val elements = mutableSetOf<Tuple<Int, Int>>()
        val baseSet = mutableSetOf<Int>()
        elements.add(tu(1, 1))
        baseSet.add(1)
        baseSet.add(2)
        val t = Relation(baseSet, elements)
        Assert.azzertFalse(r == t)
        Assert.azzertFalse(t == r)
    }

    @Test
    fun hashCodeTest() {
        var r = relation()
        var s = relation()
        Assert.aequals(r.hashCode(), s.hashCode())

        r = relation(tu(1, 1))
        s = relation(tu(1, 1))
        Assert.aequals(r.hashCode(), s.hashCode())

        r = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        s = relation(tu(1, 2), tu(1, 3), tu(4, 5))
        Assert.aequals(r.hashCode(), s.hashCode())
    }

    @Test
    fun containsTest() {
        var relation = relation()
        Assert.azzertFalse(relation.contains(tu(1, 1)))
        Assert.azzertFalse(relation.contains(tu(1, 2)))

        relation = relation(tu(1, 2), tu(1, 3))
        Assert.azzertFalse(relation.contains(tu(1, 1)))
        Assert.azzert(relation.contains(tu(1, 2)))
    }

    @Test
    fun isSymmetricTest() {
        var relation = relation()
        Assert.azzert(relation.isSymmetric)

        relation = relation(tu(1, 2), tu(1, 3))
        Assert.azzertFalse(relation.isSymmetric)

        relation = relation(tu(1, 2), tu(2, 1))
        Assert.azzert(relation.isSymmetric)

        relation = relation(tu(1, 2), tu(2, 1), tu(3, 4))
        Assert.azzertFalse(relation.isSymmetric)

        relation = relation(tu(4, 3), tu(1, 2), tu(2, 1), tu(3, 4))
        Assert.azzert(relation.isSymmetric)

        relation = relation(tu(4, 3), tu(3, 5), tu(1, 2), tu(2, 1), tu(3, 4))
        Assert.azzertFalse(relation.isSymmetric)

        relation = relation(tu(4, 3), tu(3, 5), tu(1, 2), tu(5, 3), tu(2, 1), tu(3, 4))
        Assert.azzert(relation.isSymmetric)
    }

    @Test
    fun isReflexiveTest() {
        var relation = relation()
        Assert.azzert(relation.isReflexive)

        relation = relation(tu(1, 1))
        Assert.azzert(relation.isReflexive)

        relation = relation(tu(1, 1), tu(1, 2), tu(3, 4), tu(3, 3))
        Assert.azzertFalse(relation.isReflexive)

        relation = relation(tu(1, 1), tu(1, 2), tu(3, 4), tu(2, 2), tu(4, 4), tu(3, 3))
        Assert.azzert(relation.isReflexive)
    }

    @Test
    fun isTransitiveTest() {
        var relation = relation()
        Assert.azzert(relation.isTransitive)

        relation = relation(tu(1, 1))
        Assert.azzert(relation.isTransitive)

        relation = relation(tu(1, 3))
        Assert.azzert(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1))
        Assert.azzertFalse(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 3))
        Assert.azzert(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1))
        Assert.azzertFalse(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1), tu(1, 1))
        Assert.azzertFalse(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1), tu(1, 1), tu(3, 3))
        Assert.azzert(relation.isTransitive)

        relation = relation(tu(1, 3), tu(3, 1), tu(1, 1), tu(3, 3))
        Assert.azzert(relation.isTransitive)
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
        Assert.azzert(relation.isTransitive)

        pairs.add(tu(6, 3))
        relation = Relation(baseSet, pairs)
        Assert.azzertFalse(relation.isTransitive)
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
        Assert.azzert(relation.isAPartialOrder)

        pairs.add(tu(6, 3))
        relation = Relation(baseSet, pairs)
        Assert.azzertFalse(relation.isAPartialOrder)
    }

    @Test
    fun isAPartialOrderPowerSetTest() {
        val powerSet = powerSet(6)
        val tuples = mutableSetOf<Tuple<IntSet, IntSet>>()
        val baseSet = mutableSetOf<IntSet>()
        powerSet.forEach { intSetLeft ->
            baseSet.add(intSetLeft)
            powerSet.forEach { intSetRight ->
                if (intSetLeft.isASubsetOf(intSetRight)) {
                    tuples.add(Tuple<IntSet, IntSet>(intSetLeft, intSetRight))
                }
            }
        }
        val contains = Relation(baseSet, tuples)
        Assert.azzert(contains.isAPartialOrder)
    }
}
