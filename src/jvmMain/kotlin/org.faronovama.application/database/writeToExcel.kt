package org.faronovama.application.database

import classes.Teacher
import org.apache.poi.ss.usermodel.BorderStyle
import org.apache.poi.ss.usermodel.HorizontalAlignment
import org.apache.poi.ss.usermodel.VerticalAlignment
import org.apache.poi.ss.util.CellRangeAddress
import org.apache.poi.xssf.usermodel.XSSFWorkbook
import org.litote.kmongo.eq
import java.io.FileOutputStream

fun writeToExcelFile(filepath: String) {
    val xlWb = XSSFWorkbook()
    val xlWs = xlWb.createSheet()
    xlWs.autoSizeColumn(1)
    var row = 8
    var day = 0
    var day2 = 31
    var timeUpWeak = 0
    var timelowWeak = 31

    val style = xlWb.createCellStyle()
    style.wrapText = true
    style.alignment = HorizontalAlignment.CENTER
    style.verticalAlignment = VerticalAlignment.CENTER
    style.setBorderTop        (BorderStyle.MEDIUM);
    style.setBorderBottom     (BorderStyle.MEDIUM);
    style.setBorderRight      (BorderStyle.MEDIUM);
    style.setBorderLeft       (BorderStyle.MEDIUM);

    xlWs.setColumnWidth(30, 4500)

    val timeStyle = xlWb.createCellStyle()
    timeStyle.rotation = -90
    timeStyle.alignment = HorizontalAlignment.CENTER
    timeStyle.verticalAlignment = VerticalAlignment.CENTER
    timeStyle.setBorderTop        (BorderStyle.MEDIUM);
    timeStyle.setBorderBottom     (BorderStyle.MEDIUM);
    timeStyle.setBorderRight      (BorderStyle.MEDIUM);
    timeStyle.setBorderLeft       (BorderStyle.MEDIUM);
    var Teacherss = teachersCollection.find(
    ).toList()
    Teacherss.map {
        xlWs.createRow(row).createCell(30).apply {
            setCellValue(it.fullName)
            cellStyle = style
        }
        row += 1
    }.toList()
    val dayWeak = listOf(
        "Понедельник", "Вторник", "Среда", "Четверг", "Пятница", "Суббота"
    )
    val time = listOf(
        "08.00-09.30", "09.45-11.15", "11.30-13.00", "13.55-15.25", "15.40-17.10"
    )
    val rowSix = xlWs.createRow(6)
    rowSix.sheet.addMergedRegion(CellRangeAddress(6,6,0,4))
    var i = 5
    while( i <= 60){
        rowSix.sheet.addMergedRegion((CellRangeAddress(6,6,i,i+4)))
        i+=5
        when(i){
            30-> i = 31
        }
    }
    var styleCell = 1
    while (styleCell <= 60) {
        rowSix.createCell(styleCell).apply {
            cellStyle = timeStyle
        }
        styleCell++
    }

    xlWs.createRow(7).height = 1800

    dayWeak.map {
        time.map {
            xlWs.getRow(7).createCell(timeUpWeak).apply {
                setCellValue(it)
                cellStyle = timeStyle
            }
            timeUpWeak += 1
            xlWs.getRow(7).createCell(timelowWeak).apply {   setCellValue(it)
                cellStyle = style
            }
            timelowWeak += 1
        }
        xlWs.getRow(6).createCell(day).apply {
            setCellValue(it)
            cellStyle = style

        }
        day += 5
        xlWs.getRow(6).createCell(day2).apply {
            setCellValue(it)
            cellStyle = style
        }
        day2 += 5
    }
    var coll = 0
    var curT = 8
    var curTT = 31
    Teacherss.map {
        val teacher = teachersCollection.find(Teacher::fullName eq it.fullName).toList()
        teacher.map { it.table.upWeek }.map { Day ->
            Day.map { curentDay ->
                curentDay.classes.map {
                    xlWs.getRow(curT).createCell(coll).apply { setCellValue(it.fullname())
                        cellStyle = style
                    }
                    coll++
                }
            }
        }
        coll = 0
        teacher.map { it.table.lowWeek }.map { Day ->
            Day.map { curentDay ->
                curentDay.classes.map {
                    xlWs.getRow(curT).createCell(curTT).apply {
                        setCellValue(it.fullname())
                        cellStyle = style
                    }
                    curTT++
                }
            }
        }
        curTT = 31
        curT++
    }

    val outputStream = FileOutputStream(filepath)
    xlWb.write(outputStream)
    xlWb.close()
}