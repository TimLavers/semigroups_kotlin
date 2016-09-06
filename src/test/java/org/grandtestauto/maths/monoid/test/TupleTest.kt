package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.IntSet
import org.grandtestauto.maths.monoid.Tuple
import org.junit.Test

import java.util.HashSet

/**
 * @author Tim Lavers
 */
class TupleTest {

    @Test
    fun leftTest() {
        val t = Tuple("left", 99)
        Assert.aequals("left", t.left())
    }

    @Test
    fun rightTest() {
        val t = Tuple("left", 99)
        Assert.aequals(99, t.right())
    }

    @Test
    fun flipTest() {
        val t = Tuple("left", 99)
        Assert.aequals(Tuple(99, "left"), t.flip())
    }

    @Test
    fun equalsTest() {
        Assert.aequals(Tuple("junk", 99), Tuple("junk", 99))
        Assert.azzertFalse(Tuple("junk", 100) == Tuple("junk", 99))
        Assert.azzertFalse(Tuple("junkkkkk", 99) == Tuple("junk", 99))
    }

    @Test
    fun hashCodeTest() {
        Assert.aequals(Tuple("junk", 99).hashCode(), Tuple("junk", 99).hashCode())
    }

    @Test
    fun toStringTest() {
        Assert.aequals("<junk, 99>", Tuple("junk", 99).toString())
    }
}
