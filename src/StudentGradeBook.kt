data class Student(val firstName: String, val lastName: String, var grade: String)
class Grade (
    ) {

    var score : Float = 0f
    var scorePossible : Float = 0f
    var totalScore: Float = 0f
    var totalPossible : Float = 0f

    fun calculateNewGrade (student: Student): Float {

        print("What was ${student.firstName}'s score? ")
        score = readln().toFloat()

        print("What was the possible score? ")
        scorePossible = readln().toFloat()

        val newGrade: Float = (score/scorePossible) * 100

        return newGrade
    }
}

fun main() {

    val classList = mutableMapOf<Student, Grade>()

    var menuNumber: String = "";

    while (!menuNumber.equals("6", true)) {

        print("What would you like to do? ")
        menuNumber = readln();

        when (menuNumber) {
            "1" -> listStudents(classList)
        }

    }

    val student1 = Student("Jason", "Walker", "A")

    val grade = Grade()

    var currentGrade = grade.calculateNewGrade(student1)

    print("\n${student1.firstName}'s score is ${"%.2f".format(currentGrade)}%")


}
// __________________________________________FUNCTIONS__________________________________________________ //

// This function accepts a map of Student and Grade objects. It prints out each student's name and grade.
fun listStudents(classList: MutableMap<Student, Grade>) {
    var i: Int = 1
    for (key in classList.keys) {
        print("${i}. ${key}, ${key.grade}")
        i++;
    }
}