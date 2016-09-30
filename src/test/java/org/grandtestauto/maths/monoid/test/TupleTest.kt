package org.grandtestauto.maths.monoid.test

import org.grandtestauto.maths.monoid.Tuple
import org.junit.Assert
import org.junit.Test

/**
 * @author Tim Lavers
 */
class TupleTest {

    @Test
    fun leftTest() {
        val t = Tuple("left", 99)
        Assert.assertEquals("left", t.left())
    }

    @Test
    fun rightTest() {
        val t = Tuple("left", 99)
        Assert.assertEquals(99, t.right())
    }

    @Test
    fun flipTest() {
        val t = Tuple("left", 99)
        Assert.assertEquals(Tuple(99, "left"), t.flip())
    }

    @Test
    fun equalsTest() {
        Assert.assertEquals(Tuple("junk", 99), Tuple("junk", 99))
        Assert.assertFalse(Tuple("junk", 100) == Tuple("junk", 99))
        Assert.assertFalse(Tuple("junkkkkk", 99) == Tuple("junk", 99))
    }

    @Test
    fun hashCodeTest() {
        Assert.assertEquals(Tuple("junk", 99).hashCode(), Tuple("junk", 99).hashCode())
    }

    @Test
    fun toStringTest() {
        Assert.assertEquals("<junk, 99>", Tuple("junk", 99).toString())
    }
}
