data class Student(val firstName: String, val lastName: String) {

    // This holds a map of all assignments. The key is the assignment number, and the value is a Grade object to hold the student's scores.
    var assignmentList = mutableMapOf<Int, Grade>()

    // This will be added to assignmentList as the key.
    var assignmentNumber = 1

    var studentSelector = 0
}

class Grade {

    // actual score for one assignment
    var score = 0f

    // score possible for one assignment
    var scorePossible = 0f

    // cumulative score for all assignments
    var totalScore = 0f

        set(newTotalScore) {
            field += newTotalScore
        }

    // total possible for all assignments
    var totalPossible = 0f

        set(newTotalPossible) {
            field += newTotalPossible
        }
    // This method takes two arguments (score and scorePossible) and returns the student's percentage
    fun calculateGrade (score: Float, scorePossible: Float): Float {

        // This converts the result to a percentage, rather than a decimal.
        val newGrade: Float = (score/scorePossible) * 100

        return newGrade
    }
}

fun main() {

    val studentMap = mutableMapOf<Student, Grade>()

    val masterStudent = Student("Master", "Master")
    val masterGrade = Grade()

    studentMap[masterStudent] = masterGrade

    var studentSelector = 1

    var menuNumber = ""

    while (!menuNumber.equals("6", true)) {

        freshScreen()
        println("1. Check All Grades\n2. Add New Student\n3. Add Assignment\n4. Add Student Grade")
        print("What would you like to do? ")
        menuNumber = readln()

        when (menuNumber) {

            // Check all student grades
            "1" -> {
                listStudents(studentMap, false)

                println("Total Possible: ${masterGrade.totalPossible}")
            }

            // Enter the information for a new student
            "2" -> {

                val newStudent = addNewStudent(studentSelector)

                // This sets a Student object as the key and a Grade object as the value. They are then added to studentMap.
                studentMap[newStudent] = Grade()

                // This updates the new student's total possible to match the master total possible.

                newStudent.assignmentList = masterStudent.assignmentList.toMutableMap()
                studentMap[newStudent]?.totalPossible = masterGrade.totalPossible

                newStudent.assignmentNumber = masterStudent.assignmentNumber
                studentSelector += 1
            }

            // Add an assignment in the form of a possible score
            "3" -> {

                if (studentMap.size < 2) {
                    println("Assignments cannot be added until students have been added.")
                }

                else {
                    // This call adds to the total possible score for the class
                    val newScore = createPossibleScore()

                    masterGrade.totalPossible = newScore

                    if (newScore > 0) {
                        addAssignment(studentMap, newScore)
                    }
                }
            }

            "4" -> {
                if (studentMap.size < 2) {
                    println("Scores cannot be added until students have been added.")
                }

                else {
                    val selectedStudent = selectStudent(studentMap)

                    if (selectedStudent != 0) {
                        val newScore = addScore(studentMap, selectedStudent)

                        for (student in studentMap.keys) {

                            val grades = studentMap[student]

                            if (student.studentSelector == selectedStudent){

                                grades!!.totalScore = newScore
                            }
                        }
                    }
                }
            }
        }
    }
}
// __________________________________________FUNCTIONS__________________________________________________ //

// This function accepts a map of Student and Grade objects. It prints out each student's name and grade.
fun listStudents(classList: MutableMap<Student, Grade>, addGrade: Boolean) {

    freshScreen()

    var totalPossible = 0F
//    var totalScore = 0F

    if (classList.isEmpty()){
        println("No students to display.")
    }

    else {
        println("Student Name, Grade")
        println("--------------------")
        for (student in classList.keys) {

            // returns the Grade object that is associated with the key
            val grade = classList[student]
            if (student.firstName == "Master") {
                totalPossible = grade!!.totalPossible
            }

            var gradePercentage = 0F

            if (student.firstName != "Master") {

                val studentsGrade = classList[student]

//                for (assignment in student.assignmentList.keys) {
//
//                    var studentScore = student.assignmentList[assignment]?.score
//
//                    totalScore += studentScore!!
//                }

                if (gradePercentage.isNaN()) {
                    gradePercentage = 0F
                }

                else {
                    gradePercentage = studentsGrade!!.calculateGrade(studentsGrade.totalScore, totalPossible)
                }

                if (addGrade) {
                    println("${student.studentSelector}. ${student.firstName} ${student.lastName}")
                } else {
                    println("${student.studentSelector}. ${student.firstName} ${student.lastName}, ${getLetterGrade(gradePercentage)} (${"%.2f".format(gradePercentage)}%)")
                }
            }
        }
    }
    println("--------------------")
}

// This function gets student information and creates a new Student object.
fun addNewStudent(studentSelector: Int): Student {

    freshScreen()
    print("What is the student's first name? ")
    val firstName: String = readln()

    print("What is ${firstName}'s last name? ")
    val lastName: String = readln()

    val newStudent = Student(firstName, lastName)

    newStudent.studentSelector = studentSelector

    return newStudent
}

// This function adds a new space to refresh the screen.
fun freshScreen() {
    println("\n")
    println("--------------------")
}

// This function is passed a Float representing the student's grade percentage. It returns the student's letter grade.
fun getLetterGrade(gradePercentage: Float): String {

    val letterGrade = when (gradePercentage) {

        in 100.00..500.00 -> "A"
        in 93.00..100.00 -> "A"
        in 90.00..92.99 -> "A-"
        in 87.10..89.99 -> "B+"
        in 83.00..87.00 -> "B"
        in 80.00..82.99 -> "B-"
        in 77.10..79.99 -> "C+"
        in 73.00..77.00 -> "C"
        in 70.00..72.99 -> "C-"
        in 67.10..69.99 -> "D+"
        in 60.00..67.00 -> "D"
        else -> "F"
    }

    return letterGrade
}

fun createPossibleScore(): Float {

    var possibleScore = ""

    while(possibleScore.toFloatOrNull() == null) {

        print("\nPossible Score: ")
        possibleScore = readln()

        if (possibleScore.toFloatOrNull() is Float) {
            break
        }

        else if (possibleScore == "back"){

            possibleScore = "0"
            possibleScore.toFloat()

            break
        }

        else {
            println("That is not a number.")
        }
    }

    return possibleScore.toFloat()
}



// This function adds the***********************************************************************************************************
fun addAssignment(classList: MutableMap<Student, Grade>,  score: Float) {

    for (student in classList.keys) {

        // creates a new Grade object to be the value of the map stored in the Student object
        val newAssignment = Grade()

        newAssignment.score = 0F
        newAssignment.scorePossible = score

        // populates the map stored in the Student object
        student.assignmentList[student.assignmentNumber] = newAssignment

        student.assignmentNumber += 1
    }
}

fun selectStudent(classList: MutableMap<Student, Grade>): Int {

    val numberList = mutableListOf<Int>()

    if (classList.isEmpty()) {
        println("No student grades to add.")
        return 0
    }
    else {

        for (student in classList.keys) {
            if (student.firstName != "Master") {
                numberList.add(student.studentSelector)
            }
        }

        var chosenStudent = ""

        while (chosenStudent.toIntOrNull() == null || chosenStudent.toIntOrNull() !in numberList) {

            listStudents(classList, true)
            print("Please enter the student's number or type 'back': ")

            chosenStudent = readln()

            if (chosenStudent.toIntOrNull() in numberList) {
                break

            } else if (chosenStudent == "back") {
                chosenStudent = "0"
                break

            } else {
                print("That is not an option.\n")
                chosenStudent = ""
            }
        }

        return chosenStudent.toInt()
    }
}

fun addScore(classList: MutableMap<Student, Grade>, chosenStudent: Int): Float {

    val assignments = mutableListOf<Int>()

    var assignmentNumber = ""

    val assignmentScores = mutableListOf<Float>()

    var scorePossible = 0F

    for (student in classList.keys) {

        if (student.studentSelector == chosenStudent){

            for (assignment in student.assignmentList.keys) {
                assignments.add(assignment)
                scorePossible = student.assignmentList[assignment]!!.scorePossible
                assignmentScores.add(scorePossible)
                println("${assignment}. ${scorePossible} pts. possible")
            }
        }
    }

    while (assignmentNumber.toIntOrNull() == null || assignmentNumber.toIntOrNull() !in assignments) {
        print("\nPlease type the number of the assignment to add the score: ")

        assignmentNumber = readln()

        if (assignmentNumber.toIntOrNull() != null && assignmentNumber.toIntOrNull() in assignments) {
            break
        }
        else {
            print("That is not an option.\n")
        }
    }

    var newScore = ""
    scorePossible = assignmentScores[assignmentNumber.toInt().minus(1)]
    while (newScore.toFloatOrNull() == null || newScore.toFloat() > scorePossible) {
        print("Please enter the score: ")

        newScore = readln()

        if (newScore.toFloatOrNull() is Float && newScore.toFloat() <= scorePossible) {
            break
        }

        else {
            print("That is not a number or the score is greater than the possible score.\n")
        }
    }
    return newScore.toFloat()
}

