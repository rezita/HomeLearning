package com.github.rezita.homelearning.model

import android.graphics.Color

class ReadingRule(val word: String, val subWord: String, private val ruleName: String) {
    private val _silentEndE: String = "silente"
    private val _underline = "ul"

    fun getRuleColor(): Int{
        return when(ruleName) {
            //Angry Red A
            "red" -> Color.RED
            //Green Frog
            "green" -> Color.GREEN
            //Yellow Y
            "yellow" -> Color.YELLOW
            //Miss Oh No
            "pink" -> Color.rgb(255, 182, 193)
            //U-Hoo
            "purple" -> Color.rgb(99,66,203)
            //Cool Blue
            "blue" -> Color.rgb(0, 204, 255)
            //Brown Owl
            "brown" -> Color.rgb(150,75,0)
            //Ghosts
            "white" -> Color.WHITE
            "silent" -> Color.WHITE
            //Tricky Witch
            "gold" -> Color.rgb(212,175,55)
            //Black Cat
            else -> Color.BLACK
        }
    }

    fun isSilentEndE(): Boolean{
        return ruleName == _silentEndE
    }

    fun isUnderline(): Boolean{
        return ruleName == _underline
    }
}