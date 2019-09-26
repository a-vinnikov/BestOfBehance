package com.example.bestofbehance.extension

import java.math.RoundingMode
import java.text.DecimalFormat

object MathObject{

    fun decimal(numberString: String): String {
        var number = numberString
        if (number.toInt() > 1000) {
            var round = number.toInt()
            val df = DecimalFormat("#.#")
            round /= 1000
            df.roundingMode = RoundingMode.CEILING
            number = (df.format(round) + "k")
        }
        return number
    }
}