package org.faronovama.application.database

import classes.Teacher
import classes.TimeTable
import org.apache.poi.ss.usermodel.WorkbookFactory
import readSchedules
import java.io.File

val regexp = Regex("(\\d{2}[а-я])+")

fun readTeachersFromExcel(file: String): List<Teacher> {
    val inputStream = File(file)// поток для файла

    val xlWbs = WorkbookFactory.create(inputStream) // создаем для поток для считывания
    val tables = xlWbs.getSheetAt(0)
    val teachersWithTimeTable: MutableList<Teacher> = mutableListOf()
    var currentTeacher: Teacher // Преподаватьле текущий
    var i = 0 // счетчик преподавателей

    val ListTeachers: MutableList<String> = mutableListOf()
    val ListDayClasses: MutableList<TimeTable> = mutableListOf()
    while (tables.getRow(32 * i + 6) != null) {
        currentTeacher = Teacher(
            tables.getRow(
                32 * i +
                        6
            ).getCell(1).toString(), readSchedules(i, tables)
        )
        ListTeachers.add(tables.getRow(32 * i + 6).getCell(1).toString())
        teachersWithTimeTable.add(currentTeacher)
        ListDayClasses.add(readSchedules(i, tables))
        i++
    }
    return teachersWithTimeTable
}