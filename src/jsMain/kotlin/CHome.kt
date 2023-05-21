import components.CFooter
import csstype.Color
import csstype.FontFamily
import csstype.TextAlign
import csstype.px
import emotion.react.css
import react.FC
import react.Props
import react.dom.html.ReactHTML
import react.dom.html.ReactHTML.div
import styles.Styles

val CHome = FC<Props> {
    ReactHTML.div {
css{
    textAlign = TextAlign.center
}
        ReactHTML.h1 {
            ReactHTML.style {
                +Styles.animat
            }
            css {
                fontSize = 70.px
                fontFamily = FontFamily.cursive
                textEmphasisColor = Color("fadadd")
                textAlign = TextAlign.center
            }
            +"Кафедральное расписание"
        }
    }
    div{
        ReactHTML.style {
            +Styles.buttonStyle
        }
            ReactHTML.form {
                action = "/#/unity"
                ReactHTML.button {
                    +"Получить расписание"
                }
        }
    }

    ReactHTML.div{
        ReactHTML.style {
            +Styles.buttonStyle
        }
        ReactHTML.form{
            action = "/#/home"
            ReactHTML.button{
                +"Загрузить расписание"
            }

        }
    }
    ReactHTML.div{
        ReactHTML.style {
            +Styles.buttonStyle
        }
        ReactHTML.form{
           action = "/#/editOpt"
            ReactHTML.button{
                +"Редактировать расписание"
            }

        }
    }
    CFooter{}
}