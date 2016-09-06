package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.Transformation
import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.cycle2_3___n_1
import org.grandtestauto.maths.monoid.definesATransformation
import org.grandtestauto.maths.monoid.unit
import org.junit.Test

/**
 * @author Tim Lavers
 */
class TransformationTest {
    @Test
    fun cycle2_3___n_1Test() {
        Assert.aequals(t(2, 1), cycle2_3___n_1(2))
        Assert.aequals(t(2, 3, 1), cycle2_3___n_1(3))
        Assert.aequals(t(2, 3, 4, 5, 6, 7, 8, 9, 1), cycle2_3___n_1(9))
    }

    @Test
    fun numberOfFixedPointsTest(){
        Assert.aequals(1, Transformation(intArrayOf(1)).numberOfFixedPoints())
        Assert.aequals(1, Transformation(intArrayOf(1, 1)).numberOfFixedPoints())
        Assert.aequals(1, Transformation(intArrayOf(1, 1, 2)).numberOfFixedPoints())
        Assert.aequals(1, Transformation(intArrayOf(1, 1, 2, 1)).numberOfFixedPoints())
        Assert.aequals(2, Transformation(intArrayOf(1, 1, 2, 4)).numberOfFixedPoints())
        Assert.aequals(2, Transformation(intArrayOf(1, 1, 3, 3)).numberOfFixedPoints())
        Assert.aequals(3, Transformation(intArrayOf(1, 2, 3, 3)).numberOfFixedPoints())
        Assert.aequals(0, Transformation(intArrayOf(2, 3, 4, 1)).numberOfFixedPoints())
    }

    @Test
    fun kernelTest(){
        var t = Transformation(intArrayOf(1))
        Assert.aequals(relation(tu(1, 1)), t.kernel())

        t = Transformation(intArrayOf(1, 2))
        Assert.aequals(relation(tu(1, 1), tu(2, 2)), t.kernel())

        t = Transformation(intArrayOf(1, 1))
        Assert.aequals(relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1)), t.kernel())

        t = Transformation(intArrayOf(1, 1, 2))
        Assert.aequals(relation(tu(1, 1), tu(2, 2), tu(1, 2), tu(2, 1), tu(3, 3)), t.kernel())

        t = Transformation(intArrayOf(2, 1, 2))
        Assert.aequals(relation(tu(1, 1), tu(2, 2), tu(1, 3), tu(3, 1), tu(3, 3)), t.kernel())

        t = Transformation(intArrayOf(2, 2, 2))
        Assert.aequals(relation(tu(1, 1), tu(1, 2), tu(1, 3), tu(2, 1), tu(2, 2), tu(2, 3), tu(3, 1), tu(3, 2), tu(3, 3)), t.kernel())

        t = Transformation(intArrayOf(2, 2, 1, 1))
        Assert.aequals(relation(tu(1, 1), tu(1, 2), tu(2, 1), tu(2, 2), tu(3, 3), tu(3, 4), tu(4, 3), tu(4, 4)), t.kernel())

        t = Transformation(intArrayOf(1, 2, 1, 1))
        Assert.aequals(relation(tu(1, 1), tu(1, 3), tu(1, 4), tu(3, 1), tu(3, 3), tu(3, 4), tu(4, 1), tu(4, 3), tu(4, 4), tu(2, 2)), t.kernel())

        t = Transformation(intArrayOf(1, 2, 3, 4))
        Assert.aequals(relation(tu(1, 1), tu(2, 2), tu(3, 3), tu(4, 4)), t.kernel())
    }

    @Test
    fun embedTest(){
        var t = Transformation(intArrayOf(1, 1, 1, 1))
        var embedded = t.embed(5)
        Assert.aequals(Transformation(intArrayOf(1, 1, 1, 1, 5)), embedded)

        embedded = t.embed(12)
        Assert.aequals(Transformation(intArrayOf(1, 1, 1, 1, 5, 6, 7, 8, 9, 10, 11, 12)), embedded)

        t = Transformation(intArrayOf(4, 3, 2, 1))
        embedded = t.embed(12)
        Assert.aequals(Transformation(intArrayOf(4, 3, 2, 1, 5, 6, 7, 8, 9, 10, 11, 12)), embedded)
    }

    @Test
    fun toStringTest(){
        val t = Transformation(intArrayOf(1, 3, 4, 1))
        Assert.aequals("[1, 3, 4, 1]", t.toString())
    }

    @Test
    fun definesATransformationTest(){
        Assert.azzert(definesATransformation(intArrayOf(1)))
        Assert.azzert(definesATransformation(intArrayOf(1, 2)))
        Assert.azzert(definesATransformation(intArrayOf(2, 1)))
        Assert.azzert(definesATransformation(intArrayOf(2, 1, 3)))
        Assert.azzert(definesATransformation(intArrayOf(2, 1, 4, 3)))
        Assert.azzert(definesATransformation(intArrayOf(5, 2, 1, 4, 3)))
        Assert.azzert(definesATransformation(intArrayOf(5, 5, 5, 5, 5, 5, 5, 5)))

        Assert.azzertFalse(definesATransformation(intArrayOf(0)))
        Assert.azzertFalse(definesATransformation(intArrayOf(2)))
        Assert.azzertFalse(definesATransformation(intArrayOf(2, 1, 6)))
        Assert.azzertFalse(definesATransformation(intArrayOf(2, 3)))
        Assert.azzertFalse(definesATransformation(intArrayOf(2, 1, 0, 3)))
        Assert.azzertFalse(definesATransformation(intArrayOf(5, 2, -1, 4, 3)))
    }

    @Test
    fun inDomainTest(){
        val t = Transformation(intArrayOf(1, 1, 1, 1))
        Assert.azzert(t.inDomain(1))
        Assert.azzert(t.inDomain(2))
        Assert.azzert(t.inDomain(3))
        Assert.azzert(t.inDomain(4))
        Assert.azzertFalse(t.inDomain(5))
    }

    @Test
    fun rankTest(){
        Assert.aequals(4, Transformation(intArrayOf(1, 1, 1, 1)).rank())
        Assert.aequals(6, Transformation(intArrayOf(1, 5, 5, 1, 1, 1)).rank())
    }

    @Test
    fun applyTest(){
        var t = Transformation(intArrayOf(1, 1, 1, 1))
        for (i in 1..4) {
            Assert.aequals(1, t.apply(i))
        }

        t = Transformation(intArrayOf(1, 4, 3, 1, 4))
        Assert.aequals(1, t.apply(1))
        Assert.aequals(4, t.apply(2))
        Assert.aequals(3, t.apply(3))
        Assert.aequals(1, t.apply(4))
        Assert.aequals(4, t.apply(5))
    }

    @Test
    fun unitTest(){
        Assert.aequals(Transformation(intArrayOf(1)), unit(1))
        Assert.aequals(Transformation(intArrayOf(1, 2)), unit(2))
        Assert.aequals(Transformation(intArrayOf(1, 2, 3, 4, 5)), unit(5))
    }

    @Test
    fun composeTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        val st = s.compose(t)
        Assert.aequals(4, st.rank())
        Assert.aequals(3, st.apply(1))
        Assert.aequals(4, st.apply(2))
        Assert.aequals(1, st.apply(3))
        Assert.aequals(2, st.apply(4))

        val u = Transformation(intArrayOf(1, 1, 1, 1))
        val su = s.compose(u)
        Assert.aequals(4, su.rank())
        Assert.aequals(1, su.apply(1))
        Assert.aequals(1, su.apply(2))
        Assert.aequals(1, su.apply(3))
        Assert.aequals(1, su.apply(4))
    }

    @Test
    fun timesTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        val st = s * t
        Assert.aequals(4, st.rank())
        Assert.aequals(3, st.apply(1))
        Assert.aequals(4, st.apply(2))
        Assert.aequals(1, st.apply(3))
        Assert.aequals(2, st.apply(4))

        val u = Transformation(intArrayOf(1, 1, 1, 1))
        val su = s * u
        Assert.aequals(4, su.rank())
        Assert.aequals(1, su.apply(1))
        Assert.aequals(1, su.apply(2))
        Assert.aequals(1, su.apply(3))
        Assert.aequals(1, su.apply(4))
    }

    @Test
    fun equalsTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        val u = Transformation(intArrayOf(2, 3, 4, 1, 5))
        Assert.azzert(s == t)
        Assert.azzert(t == s)
        Assert.azzertFalse(t == u)
        Assert.azzertFalse(u == t)
    }

    @Test
    fun hashCodeTest(){
        val s = Transformation(intArrayOf(2, 3, 4, 1))
        val t = Transformation(intArrayOf(2, 3, 4, 1))
        Assert.aequals(s.hashCode(), t.hashCode())
    }

    @Test
    fun domainTest(){
        Assert.aequals(3, t(1, 2, 1).domain().size)
        Assert.azzert(t(1, 2, 1).domain().contains(1))
        Assert.azzert(t(1, 2, 1).domain().contains(2))
        Assert.azzert(t(1, 2, 1).domain().contains(3))
    }
}
