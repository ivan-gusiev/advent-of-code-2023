package util

import java.math.BigInteger

class AocArithm {
    companion object {
        fun gcd(a: Int, b: Int): Int {
            return if (b == 0) a else gcd(b, a % b)
        }

        fun gcd(a: BigInteger, b: BigInteger): BigInteger {
            return if (b == BigInteger.ZERO) a else gcd(b, a % b)
        }

        fun lcm(a: Int, b: Int): Int {
            return a * b / gcd(a, b)
        }

        fun lcm(a: BigInteger, b: BigInteger): BigInteger {
            return a * b / gcd(a, b)
        }
    }
}