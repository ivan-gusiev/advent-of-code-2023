package util

class AocSequence {
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

        fun <T> spectrum(input: List<T>): Map<T, Int> {
            val result: HashMap<T, Int> = HashMap()
            for (item in input) {
                val count = result.getOrDefault(item, 0)
                result[item] = count + 1
            }
            return result
        }
    }
}