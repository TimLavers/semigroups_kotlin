package org.grandtestauto.maths.monoid.test

import org.junit.Test

/**
 * Created by tim on 7/06/2016.
 */
class JunkTest {
    @Test
    fun j() {
        listOf(1, 2, 3, 4).asSequence().map{print("map($it)"); it * it}.filter{print("filter($it)"); it % 2 == 0}.toList()
    }
}