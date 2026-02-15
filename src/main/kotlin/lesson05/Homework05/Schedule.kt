package lesson05.Homework05

import java.time.LocalTime
import java.time.format.DateTimeFormatter

//Твоя задача - дополнить класс Schedule методами, реализующими DSL, необходимый для удобного заполнения данными.
//Подсказки:
    //1 Для создания диапазона через две точки переопредели оператор rangeTo
    //2 Для передачи нужного дня недели используй внешнюю относительно метода переменную var в которую нужно сохранить используемый в текущем блоке день недели и потом обнулить по завершении блока.

data class ScheduleEntity(val lesson: String, val startTime: LocalTime, val endTime: LocalTime)

enum class Days {
    MONDAY,
    TUESDAY,
    WEDNESDAY,
    THURSDAY,
    FRIDAY,
    SATURDAY,
    SUNDAY
}

class Schedule {

    private val scheduleOfWeek = mutableMapOf<Days, MutableList<ScheduleEntity>>()
    private val timeFormatter = DateTimeFormatter.ofPattern("HH:mm")

    fun addSchedule(day: Days, scheduleEntity: ScheduleEntity) {
        scheduleOfWeek.getOrPut(day) { mutableListOf() }.add(scheduleEntity)
    }

    override fun toString(): String {
        return scheduleOfWeek.toSortedMap()
            .map { (day, list) ->
                list.sortedBy { it.startTime }
                    .joinToString("\n") {
                        "%-15s${it.startTime.format(timeFormatter)} - ${
                            it.endTime.format(
                                timeFormatter
                            )
                        }".format("\t${it.lesson}:")
                    }.let {
                        "${day.name.lowercase().replaceFirstChar { day.name[0].uppercase() }}:\n$it"
                    }
            }.joinToString("\n\n")
    }

// РЕШЕНИЕ:

    operator fun invoke(arg: Schedule.() -> Unit) {
        arg()
    }
    private var currentDay: Days? = null
    fun monday(arg: Schedule.() -> Unit){
        currentDay = Days.MONDAY
        arg()
        currentDay = null

    }

    infix fun Pair<String, String>.schedule(lesson: String){
    val day = requireNotNull(currentDay)
    val startTime = LocalTime.parse(first, timeFormatter)
    val endTime = LocalTime.parse(second, timeFormatter)
    val entity = ScheduleEntity(lesson, startTime, endTime)
        addSchedule(day, entity)
    }

//

}


fun main() {

    val schedule = Schedule()

    // Так добавляется расписание без DSL
    schedule.addSchedule(Days.MONDAY, ScheduleEntity("Biology", LocalTime.of(10, 30), LocalTime.of(11, 10)))
    schedule.addSchedule(Days.MONDAY, ScheduleEntity("Chemistry", LocalTime.of(11, 15), LocalTime.of(11, 55)))

    // Так добавляется расписание с использованием DSL
//    schedule {
//
//        monday {
//            "10:30".."11:10" schedule "Biology"
//            "11:15".."11:55" schedule "Chemistry"
//            "09:00".."09:40" schedule "Mathematics"
//            "09:45".."10:25" schedule "History"
//        }
//
//        tuesday {
//            "09:00".."09:40" schedule "English"
//            "09:45".."10:25" schedule "Geography"
//            "11:15".."11:55" schedule "Art"
//            "10:30".."11:10" schedule "Physics"
//        }
//
//        wednesday {
//            "11:15".."11:55" schedule "Biology"
//            "09:00".."09:40" schedule "Literature"
//            "10:30".."11:10" schedule "History"
//            "09:45".."10:25" schedule "Mathematics"
//        }
//

    println(schedule.toString())
}