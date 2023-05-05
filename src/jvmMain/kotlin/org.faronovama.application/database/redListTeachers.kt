package org.faronovama.application.database

import org.apache.poi.ss.usermodel.WorkbookFactory
import java.io.FileInputStream

fun readListTeachers(filepath: String): List<String> {
    val fileInputsStream = FileInputStream(filepath)
    val xlWbs = WorkbookFactory.create(fileInputsStream)
    val tables = xlWbs.getSheetAt(0)
    val ListTeachers: MutableList<String> = mutableListOf()
    val xlWs = tables
    var i = 0 // счетчик преподавателей
    while (tables.getRow(32 * i + 6) != null) {
        ListTeachers.add(xlWs.getRow(32 * i + 6).getCell(1).toString())
        i++
    }
    return ListTeachers.toList()
}