package util

class Sequence {
    companion object {
        fun <T> split(input: List<T>, separator: T): List<List<T>> {
            val result: ArrayList<List<T>> = ArrayList()
            var current: ArrayList<T> = ArrayList()
            for (item in input) {
                if (item!! == separator) {
                    result.add(current)
                    current = ArrayList()
                } else {
                    current.add(item)
                }
            }

            if (current.isNotEmpty()) {
                result.add(current)
            }

            return result;
        }

        fun <T> join(input: List<T>, join: (lhs: T, rhs: T) -> T) {

        }
    }
}