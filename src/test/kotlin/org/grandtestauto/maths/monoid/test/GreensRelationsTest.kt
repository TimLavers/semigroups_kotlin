package org.grandtestauto.maths.monoid.test

import org.junit.Assert
import org.grandtestauto.maths.monoid.*
import org.junit.Test

import java.util.*

/**
 * @author Tim Lavers
 */
class GreensRelationsTest : TestBase() {

    @Test
    fun rClassesTest() {
        checkRClasses(transformationMonoid(3))
        checkRClasses(transformationMonoid(4))
        checkRClasses(transformationMonoid(5))
        //        checkRClasses(Semigroup.transformationMonoid(6));
        //        System.out.println("Checked T_6");
        checkRClasses(cyclicGroup(6))
        checkRClasses(orderPreservingTransformationMonoid(7))
    }

    private fun checkRClasses(semigroup: Semigroup<Transformation>) {
        val greens = GreensRelations(semigroup)
        val problems = Collections.synchronizedSet(HashSet<Transformation>())
        val rClasses = greens.rClasses()
        //First check that for each r-class, each pair of elements have the same kernel.
        rClasses.subsets().forEach { rClass ->
            rClass.forEach { s ->
                rClass.forEach { t ->
                    if (s.kernel != t.kernel) {
                        problems.add(s)
                        problems.add(t)
                    }
                }
            }
        }
        Assert.assertTrue("Found these problem elements: " + problems, problems.isEmpty())
        //Now check that if two transformations have the same kernel,
        //then they are contained in the same r-class.
        val transformationToRClass = Collections.synchronizedMap(HashMap<Transformation, Set<Transformation>>())
        rClasses.subsets().forEach { rClass -> rClass.forEach { t -> transformationToRClass.put(t, rClass) } }
        semigroup.forEach { s ->
            semigroup.forEach { t ->
                if (s.kernel == t.kernel) {
                    if (transformationToRClass[s] != transformationToRClass[t]) {
                        problems.add(s)
                        problems.add(t)
                    }
                }
            }
        }
        Assert.assertTrue("Found these problem elements: " + problems, problems.isEmpty())
    }

    @Test
    fun are_R_RelatedTest() {
        val t_3 = transformationMonoid(3)
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 1), t(1, 1, 1)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 1), t(2, 2, 2)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 1), t(3, 3, 3)))
        Assert.assertTrue(are_R_Related(t_3, t(2, 2, 2), t(3, 3, 3)))

        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 2), t(3, 3, 1)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 2), t(3, 3, 2)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 2), t(1, 1, 2)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 2), t(1, 1, 3)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 2), t(2, 2, 1)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 1, 2), t(2, 2, 3)))

        Assert.assertTrue(are_R_Related(t_3, t(1, 3, 3), t(1, 2, 2)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 3, 3), t(2, 1, 1)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 3, 3), t(3, 2, 2)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 3, 3), t(3, 1, 1)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 3, 3), t(3, 2, 2)))
        Assert.assertTrue(are_R_Related(t_3, t(1, 3, 3), t(3, 1, 1)))
    }

    @Test
    fun are_L_RelatedTest() {
        val o_5 = orderPreservingTransformationMonoid(5)
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 1, 1, 1), t(1, 1, 1, 1, 1)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 2), t(1, 2, 2, 2, 2)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 2), t(1, 1, 1, 1, 2)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 3), t(1, 2, 2, 3, 3)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 3), t(1, 2, 2, 2, 3)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 3), t(1, 1, 1, 2, 3)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 4), t(1, 1, 1, 2, 4)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 4), t(1, 1, 2, 2, 4)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 2, 4), t(1, 2, 2, 2, 4)))
        Assert.assertTrue(are_L_Related(o_5, t(1, 1, 2, 4, 5), t(1, 2, 2, 4, 5)))

        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 1, 1, 1), t(1, 1, 1, 1, 2)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 2), t(1, 2, 2, 2, 3)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 2), t(1, 1, 1, 1, 4)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 3), t(1, 2, 2, 3, 5)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 3), t(1, 2, 2, 2, 5)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 3), t(1, 1, 1, 2, 2)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 4), t(1, 1, 1, 2, 2)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 4), t(2, 2, 2, 2, 4)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 2, 4), t(2, 2, 2, 2, 4)))
        Assert.assertFalse(are_L_Related(o_5, t(1, 1, 2, 4, 5), t(1, 2, 3, 4, 5)))

        val t_3 = transformationMonoid(3)
        Assert.assertTrue(are_L_Related(t_3, t(1, 1, 1), t(1, 1, 1)))
        Assert.assertTrue(are_L_Related(t_3, t(1, 1, 2), t(1, 2, 1)))
        Assert.assertTrue(are_L_Related(t_3, t(1, 2, 2), t(1, 2, 1)))
        Assert.assertTrue(are_L_Related(t_3, t(2, 2, 1), t(1, 2, 2)))

        Assert.assertFalse(are_L_Related(t_3, t(3, 2, 1), t(1, 2, 1)))
        Assert.assertFalse(are_L_Related(t_3, t(3, 2, 1), t(1, 3, 1)))

        val s_5 = symmetricGroup(5)
        val ok = booleanArrayOf(true)
        s_5.forEach { s -> s_5.forEach { t -> ok[0] = ok[0] and are_L_Related(s_5, s, t) } }
        Assert.assertTrue("Problem found testing with s_5.", ok[0])

        val c_8 = symmetricGroup(5)
        ok[0] = true
        c_8.forEach { s -> c_8.forEach { t -> ok[0] = ok[0] and are_L_Related(c_8, s, t) } }
        Assert.assertTrue("Problem found testing with c_8.", ok[0])
    }

    @Test
    fun lClassesTest() {
        val t2 = transformationMonoid(2)
        val greens_t2 = GreensRelations(t2)
        var expected = SetPartition(t2.elements)
        expected.addSubset(set(t(1, 2), t(2, 1)))
        expected.addSubset(set(t(1, 1)))
        expected.addSubset(set(t(2, 2)))
        Assert.assertEquals(expected, greens_t2.lClasses())

        val t3 = transformationMonoid(3)
        val greens_t3 = GreensRelations(t3)
        expected = SetPartition(t3.elements)
        expected.addSubset(set(t(1, 2, 3), t(1, 3, 2), t(2, 1, 3), t(3, 2, 1), t(2, 3, 1), t(3, 1, 2)))//6
        expected.addSubset(set(t(1, 2, 2), t(2, 1, 2), t(2, 2, 1), t(1, 1, 2), t(1, 2, 1), t(2, 1, 1)))//6
        expected.addSubset(set(t(1, 3, 3), t(3, 1, 3), t(3, 3, 1), t(1, 1, 3), t(1, 3, 1), t(3, 1, 1)))//6
        expected.addSubset(set(t(2, 3, 3), t(3, 2, 3), t(3, 3, 2), t(2, 2, 3), t(2, 3, 2), t(3, 2, 2)))//6
        expected.addSubset(set(t(1, 1, 1)))
        expected.addSubset(set(t(2, 2, 2)))
        expected.addSubset(set(t(3, 3, 3)))
        Assert.assertEquals(expected, greens_t3.lClasses())
    }
}
