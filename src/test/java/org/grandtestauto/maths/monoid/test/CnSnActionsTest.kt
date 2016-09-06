package org.grandtestauto.maths.monoid.test

import org.grandtestauto.assertion.Assert
import org.grandtestauto.maths.monoid.*
import org.grandtestauto.test.tools.Stopwatch
import org.grandtestauto.test.tools.Waiting

import java.util.HashSet
import java.util.LinkedList
import java.util.concurrent.*

/**
 * Check the actions by which Sn is a zappa (general, bilateral semidirect) product of its subgroup
 * fixing n and its cyclic subgroup Cn.
 *
 * @author Tim Lavers
 */
internal class CnSnActionsTest : TestBase() {
/*
    //        private final int n = 7;//.2 s
    //    private final int n = 8;//5 s
    private val n = 9 //360 s
    private val cyc_n: Semigroup<Transformation>
    private val gamma: Transformation
    private val fix_n: Semigroup<Transformation>
    private val actionLambda: Action<Transformation, Transformation>//The left action of cyc_n on fix_n.
    private val actionRho: Action<Transformation, Transformation>//The right action of fix_n on cyc_n.
    private var problemFound: Boolean = false
    constructor() {
        problemFound = false
//        if (cyc_n != null) return //Already done.
        //The symmetric group we use is the sub-group of Sym_n that fixes n.
        val symPreimage = symmetricGroup(n - 1)
        val symElements = HashSet<Transformation>()
        for (g in symPreimage) {
            symElements.add(g.embed(n))
        }
        fix_n = Semigroup(symElements, TransformationComposition)

        cyc_n = cyclicGroup(n)
        gamma = cycle2_3___n_1(n)

        class SM : SelfMap<Transformation> {
            override fun apply(t: Transformation): Transformation {
                return t
            }

        }
        class ActionLeft : Action<Transformation, Transformation> {
            override fun action(s: Transformation): SelfMap<Transformation> {
                return SM()
                }
            }
        }

        actionLambda = { cyclicGroupElement ->
            { permutation ->
                val nImage = permutation.apply(cyclicGroupElement.apply(n))
                val index = n - nImage
                val cycle = if (index == 0) unit(n) else cyc_n!!.composition().powerOf(gamma, index)
                cyclicGroupElement.compose(permutation.compose(cycle))
            }
        }
/*
        actionRho = { permutation ->
            { cyclicGroupElement ->
                val nImage = permutation.apply(cyclicGroupElement.apply(n))
                cyc_n!!.composition().powerOf(gamma, nImage)
            }
        }
        //        verification();
  */
    }

    fun gammaTest(): Boolean {
        val g1 = cyc_n!!.composition().powerOf(gamma!!, 1)
        println("g1 = " + g1)
        val g2 = cyc_n!!.composition().powerOf(gamma, 2)
        println("g2 = " + g2)
        val g3 = cyc_n!!.composition().powerOf(gamma, 3)
        val g4 = cyc_n!!.composition().powerOf(gamma, 4)
        val g5 = cyc_n!!.composition().powerOf(gamma, 5)
        println("g5 = " + g5)
        val g6 = cyc_n!!.composition().powerOf(gamma, 6)
        val g7 = cyc_n!!.composition().powerOf(gamma, 7)
        val g8 = cyc_n!!.composition().powerOf(gamma, 8)
        Assert.aequals(g1, g8)
        Assert.aequals(c(g2, g4), g6)
        Assert.aequals(c(g1, g5), g6)
        Assert.aequals(c(g2, g2, g2), g6)
        Assert.aequals(c(g2, g1, g3), g6)
        Assert.aequals(c(g2, g1, g4), g7)
        return true
    }
/*
    @Throws(Exception::class)
    fun lambdaIsALeftActionTest(): Boolean {
        val watch = Stopwatch()
        watch.start()
        val executorService = Executors.newFixedThreadPool(8)
        val tasks = LinkedList<Callable<Boolean>>()
        for (x in cyc_n!!) {
            tasks.add({ leftActionTestForParticularCycle(x) })
        }
        val futures = executorService.invokeAll(tasks)
        futures.forEach { voidFuture ->
            try {
                val result = voidFuture.get()
                //                System.out.println("result = " + result);
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }

        watch.stop()
        println("time: " + watch.times()[0])
        return !problemFound
    }
*/
    private fun leftActionTestForParticularCycle(x: Transformation): Boolean {
        for (y in cyc_n!!) {
            for (a in fix_n!!) {
                if (problemFound) return false
                val xy = c(x, y)
                val lambda_xy_a = actionLambda!!.action(xy).apply(a)

                val lambda_y_a = actionLambda!!.action(y).apply(a)
                val lambda_x_lambda_y_a = actionLambda!!.action(x).apply(lambda_y_a)

                if (lambda_xy_a == lambda_x_lambda_y_a) {
                    //                    System.out.print(".");
                } else {
                    println("Non-actionLambda found....")
                    println("x = " + x)
                    println("y = " + y)
                    println("a = " + a)
                    println("xy = " + xy)
                    println("lambda_xy_a = " + lambda_xy_a)
                    println("lambda_y_a = " + lambda_y_a)
                    println("lambda_x_lambda_y_a = " + lambda_x_lambda_y_a)
                    problemFound = true
                    return false
                }
            }
        }
        return true
    }
/*
    @Throws(Exception::class)
    fun rhoIsARightActionTest(): Boolean {
        val watch = Stopwatch()
        watch.start()
        val executorService = Executors.newFixedThreadPool(8)
        val tasks = LinkedList<Callable<Boolean>>()
        for (a in fix_n!!) {
            tasks.add({ rightActionTestForParticularFixNElement(a) })
        }
        println("tasks.size() = " + tasks.size)
        val futures = executorService.invokeAll(tasks)
        futures.forEach { voidFuture ->
            try {
                val result = voidFuture.get()
                //                System.out.println("result = " + result);
            } catch (e: InterruptedException) {
                e.printStackTrace()
            } catch (e: ExecutionException) {
                e.printStackTrace()
            }
        }

        watch.stop()
        println("time: " + watch.times()[0])
        return !problemFound
    }
*/
    private fun rightActionTestForParticularFixNElement(a: Transformation): Boolean {
        //        System.out.println("CnSnActionsTest.rightActionTestForParticularFixNElement");
        //        System.out.println("a = " + a);
        for (b in fix_n!!) {
            //            System.out.print("@");
            for (x in cyc_n!!) {
                //                System.out.print("&");
                if (problemFound) return false
                val ab = c(a, b)
                val rho_x_ab = actionRho!!.action(ab).apply(x)

                val rho_x_a = actionRho!!.action(a).apply(x)
                val rho_rho_x_a_b = actionRho!!.action(b).apply(rho_x_a)

                if (rho_x_ab == rho_rho_x_a_b) {
                    //                    System.out.print(".");
                } else {
                    println("Non-actionRho found....")
                    println("a = " + a)
                    println("b = " + b)
                    println("x = " + x)
                    println("ab = " + ab)
                    println("rho_x_ab = " + rho_x_ab)
                    println("rho_x_a = " + rho_x_a)
                    println("rho_rho_x_a_b = " + rho_rho_x_a_b)
                    problemFound = true
                    return true
                }
            }
        }
        return true
    }

    private fun verification() {
        Assert.aequals(7, n, "Verification assumes that n is 7.")
        val s1 = t(2, 1, 3, 4, 5, 6, 7)
        val s2 = t(1, 3, 2, 4, 5, 6, 7)
        val s3 = t(1, 2, 4, 3, 5, 6, 7)
        val s4 = t(1, 2, 3, 5, 4, 6, 7)
        val s5 = t(1, 2, 3, 4, 6, 5, 7)
        val s6 = t(1, 2, 3, 4, 5, 7, 6)

        //Warm up with some multiplications.
        Assert.aequals(c(s1, s2), t(3, 1, 2, 4, 5, 6, 7))
        Assert.aequals(c(s2, s1), t(2, 3, 1, 4, 5, 6, 7))
        Assert.aequals(c(s1, s2, s3, s4), t(5, 1, 2, 3, 4, 6, 7))

        //Test actionLambda.
        Assert.aequals(c(s1, s2, s3, s4, s5), actionLambda!!.action(gamma).apply(s1))
        Assert.aequals(s1, actionLambda!!.action(gamma).apply(s2))
        Assert.aequals(s2, actionLambda!!.action(gamma).apply(s3))
        Assert.aequals(s3, actionLambda!!.action(gamma).apply(s4))
        Assert.aequals(s4, actionLambda!!.action(gamma).apply(s5))
        Assert.aequals(s5, actionLambda!!.action(gamma).apply(s6))

        //Test actionRho.
        val x = t(6, 7, 1, 2, 3, 4, 5)
        Assert.aequals(x, cyc_n!!.composition().powerOf(gamma, 5))
        Assert.aequals(actionRho!!.action(s3).apply(x), x)
        Assert.aequals(actionRho!!.action(s4).apply(x), cyc_n!!.composition().powerOf(gamma, 4))
        Assert.aequals(actionRho!!.action(s5).apply(x), cyc_n!!.composition().powerOf(gamma, 6))
        val a = t(6, 2, 5, 3, 1, 4, 7)
        Assert.aequals(actionRho!!.action(a).apply(x), gamma)
    }

    private fun c(vararg transformations: Array<Transformation?>): Transformation {
        if (transformations.size == 1) return transformations[0]

        val sub = arrayOfNulls<Transformation>(transformations.size - 1)
        System.arraycopy(transformations, 1, sub, 0, transformations.size - 1)
        return fix_n!!.composition().compose(transformations[0], c(*sub))
    }
*/
}
