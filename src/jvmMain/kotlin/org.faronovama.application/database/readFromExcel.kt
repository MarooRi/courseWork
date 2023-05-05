package org.faronovama.application.database

import classes.Teacher
import classes.TimeTable
import org.apache.poi.ss.usermodel.WorkbookFactory
import org.litote.kmongo.set
import org.litote.kmongo.upsert
import readSchedules
import java.io.File
import java.io.FileInputStream

val regexp = Regex("(\\d{2}[а-я])+")

fun loadSelectTeachers(listTeachers: List<String>, filepath: String){
    val fileInputsStream = FileInputStream(filepath)
    val xlWbs = WorkbookFactory.create(fileInputsStream)
    val tables = xlWbs.getSheetAt(0)
    val xlWs = tables
    val listTeacherAll: MutableList<String> = mutableListOf()
    val teachers: MutableList<Teacher> = mutableListOf()
    var i = 0 // счетчик преподавателей
    while (tables.getRow(32 * i + 6) != null) {
        listTeacherAll.add(xlWs.getRow(32 * i + 6).getCell(1).toString())
        i++
    }
    listTeacherAll.mapIndexed { idTeacher, teaher ->
        listTeachers.map { currentTeacher ->
            if (teaher == currentTeacher) {
                val currentTeacherss = Teacher(
                    tables.getRow(
                        32 * idTeacher +
                                6
                    ).getCell(1).toString(), readSchedules(idTeacher, tables)
                )
                teachers.add(currentTeacherss)
            }
        }
    }
    teachersCollection.insertMany(teachers)
}