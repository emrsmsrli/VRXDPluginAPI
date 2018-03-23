package tr.edu.iyte.vrxd.api.helper

fun Double.between(low: Double, hi: Double): Boolean {
    return this >= low && this < hi
}