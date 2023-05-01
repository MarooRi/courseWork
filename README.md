## courseWork
Приложение для расписания.


## Точки API на сервере

### schedule/ 
Данный путь отвечает за действия, связанные с расписанием преподавателей в приложении.

        paths:
            /{teacherName}:
                get:
                    parameters:
                        - name: teacherName
                        in: query
                        description: Имя преподавателя.
                    responses:
                        '200'
                        description: Расписание преподавателя
                        type: TimeTable
            /namesFromExcel/{fileName}
                get:
                    parameters:
                        - name: fileName
                        in: query
                        description: Название файла, из которого брать имена преподавателей
                    responses:
                        '200'
                        description: Имена преподавателей
                        type: List<String>
            /loadExcel
                post:
                    parameters:
                        - name: file
                        in: query
                        description: Excel-файл, который необходимо загрузить
                    responses:
                        '200'
                        description: Перенаправляет на другую страницу
            /loadSchedule/{fileName}
                post:
                    parameters:
                        - name: fileName
                        in: query
                        description: Excel-файл, из которого необходимо загрузить расписание
                        - name: teachersName
                        in: body
                        description: Имена преподавателей, которых надо загрузить
                    responses:
                        '200'
                        description: В базе данных появятся преподаватели
            /updateLesson
                put:
                    parameters:
                        - name: UpdateSchedule
                        in: body
                        description: Новое расписание, которое нужно загрузить
                    responses:
                        '200'
                        description: В базе данных изменяется расписание

### groups/
Данный путь отвечает за действия, связанные с группами и их расписанием в приложении.

    paths:
        /:
            get:
                responses:
                    '200'
                    description: Группы в БД
                    type: List<String>
