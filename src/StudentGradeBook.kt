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

    // map for holding students' info.
    val studentMap = mutableMapOf<Student, Grade>()

    // master objects to keep data consistent
    val masterStudent = Student("Master", "Master")
    val masterGrade = Grade()

    // adding the master info to the map
    studentMap[masterStudent] = masterGrade

    // studentSelector starts at 1 to leave the master info out of selection options
    var studentSelector = 1

    var menuNumber = ""

    // main menu
    while (!menuNumber.equals("5", true)) {

        freshScreen()
        println("1. Check All Grades\n2. Add New Student\n3. Add Assignment\n4. Add Student Grade\n5. Quit")
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

                // This updates the new student's total possible and assignment number to match the master's.

                newStudent.assignmentList = masterStudent.assignmentList.toMutableMap()
                studentMap[newStudent]?.totalPossible = masterGrade.totalPossible

                newStudent.assignmentNumber = masterStudent.assignmentNumber

                // After the studentSelector is added to the current student, it is raised by one to add to the next student.
                studentSelector += 1
            }

            // Add an assignment in the form of a possible score
            "3" -> {

                // The master is added to the list, so it will never be empty.
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

            // Add a grade to a student's Grade object
            "4" -> {

                if (studentMap.size < 2) {
                    println("Scores cannot be added until students have been added.")
                }

                else {

                    // Int that correlates with te student's selector number
                    val selectedStudent = selectStudent(studentMap)

                    if (selectedStudent != 0) {
                        val newScore = addScore(studentMap, selectedStudent)

                        for (student in studentMap.keys) {

                            // Grade object to allow access to student's score properties
                            val grades = studentMap[student]

                            if (student.studentSelector == selectedStudent){

                                // updates the student's total score to include the added score
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

    // ensures that the total doesn't climb higher than it should when it is updated
    var totalPossible = 0F

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

            // initates/resets the gradePercentage variable
            var gradePercentage = 0F

            if (student.firstName != "Master") {

                // creates a Grade object
                val studentsGrade = classList[student]

                // ensures that "Nan" isn't returned for the percentage by making it 0 instead
                if (gradePercentage.isNaN()) {
                    gradePercentage = 0F
                }

                // calculates and returns the grade percentage
                else {
                    gradePercentage = studentsGrade!!.calculateGrade(studentsGrade.totalScore, totalPossible)
                }

                // formats
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

// This function gets input from the user and adds a new "assignment" by creating a score.
fun createPossibleScore(): Float {

    var possibleScore = ""

    // toFloatOrNull makes possibleScore a float or returns null if it can't. This is used often in the program
    while(possibleScore.toFloatOrNull() == null) {

        print("\nPossible Score: ")
        possibleScore = readln()

        if (possibleScore.toFloatOrNull() is Float) {
            break
        }

        // users can enter "back" instead of entering a score
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



// This function allows the user to add a score for each of the student's assignments
fun addAssignment(classList: MutableMap<Student, Grade>,  score: Float) {

    // loops through each student in the map that was passed in
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

// This function allows the user to select a student number from a printed list and returns the Int.
fun selectStudent(classList: MutableMap<Student, Grade>): Int {

    // this list is a list of student numbers. If the user's choice isn't in this list, the program will keep looping and asking for a student number.
    val numberList = mutableListOf<Int>()

    if (classList.isEmpty()) {
        println("No student grades to add.")
        return 0
    }
    else {

        // Master will not be included in the list
        for (student in classList.keys) {
            if (student.firstName != "Master") {
                numberList.add(student.studentSelector)
            }
        }

        var chosenStudent = ""

        // the user's entry must be an Int and in the number list
        while (chosenStudent.toIntOrNull() == null || chosenStudent.toIntOrNull() !in numberList) {

            listStudents(classList, true)
            print("Please enter the student's number or type 'back': ")

            chosenStudent = readln()

            if (chosenStudent.toIntOrNull() in numberList) {
                break

            // User's can type "back" to return to the main menu.
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

// This function takes the Int from the selectStudent function to list the student's assignements and asks for a student's score on an assignment.
// Once the score is entered, the function returns it as a Float.
fun addScore(classList: MutableMap<Student, Grade>, chosenStudent: Int): Float {

    val assignments = mutableListOf<Int>()

    var assignmentNumber = ""

    val assignmentScores = mutableListOf<Float>()

    var scorePossible = 0F

    for (student in classList.keys) {

        // If the student number matches the int, the program goes through the assignments and prints them
        if (student.studentSelector == chosenStudent){

            for (assignment in student.assignmentList.keys) {
                assignments.add(assignment)
                scorePossible = student.assignmentList[assignment]!!.scorePossible

                //This adds the assignment scores to a list that allows the possible score to be accessed easily
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

    // A student's score can only be as high as the possible score.
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

