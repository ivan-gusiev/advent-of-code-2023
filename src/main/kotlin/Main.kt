import java.io.BufferedReader
import java.io.InputStreamReader
import java.util.stream.Collectors

fun main(args: Array<String>) {
    val goodClassRegex = Regex("""^Day\d+$""")
    val classes = findAllClassesUsingClassLoader("")
    val clazz = classes
        .stream()
        .filter { x -> goodClassRegex.matches(x.name) }
        .sorted { l, r -> dayIndex(r).compareTo(dayIndex(l)) }
        .findFirst()
        .get()

    println("Selected: ${clazz.name}. Running:");
    val day = clazz.getDeclaredConstructor().newInstance() as Runner
    day.run()
}

fun dayIndex(clazz: Class<*>): Int {
    return clazz.name.replace("Day", "").toInt()
}

fun findAllClassesUsingClassLoader(packageName: String): Set<Class<*>> {
    val stream = ClassLoader.getSystemClassLoader()
        .getResourceAsStream(packageName.replace("[.]".toRegex(), "/"))
    val reader = BufferedReader(InputStreamReader(stream!!))
    return reader.lines()
        .filter { line: String -> line.endsWith(".class") }
        .map { line: String ->
            getClass(
                line,
                packageName
            )
        }
        .collect(Collectors.toSet())
}

private fun getClass(className: String, packageName: String): Class<*> {
    try {
        val prefix = if (packageName.isEmpty()) "" else "$packageName.";
        return Class.forName(
            "$prefix${className.substring(0, className.lastIndexOf('.'))}"
        )
    } catch (e: ClassNotFoundException) {
        throw e
    }
}