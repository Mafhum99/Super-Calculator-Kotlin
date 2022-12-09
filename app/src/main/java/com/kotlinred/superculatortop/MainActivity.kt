package com.kotlinred.superculatortop

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private var addOp = false //variable operator
    private var addDec = true // variable desimal

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    fun numberAction (view: View) 
    {
        if (view is Button)
        {
            if (view.text == ".")
            { //kalau dia . maka nilai desimal
                if(addDec)
                
                    att.append(view.text)
                    addDec = false
                
            }
             else
               // kalau dia bukan . maka dia adalah operator
                att.append(view.text)
                addOp = true

        }
    }
    fun operatorAction (view: View) {
        if (view is Button && addOp)
        {
            att.append(view.text)
            addOp = false
            addDec = true
        }
    }

    fun clearInput (view: View) {
        att.text = ""
        resultU.text = ""
    }
    fun backSpace (view: View) {
        val pnjg = att.length()
        if(pnjg > 0) {
            att.text = att.text.subSequence(0,pnjg - 1)
        }
    }
    fun equalAction(view: View) {

        resultU.text=hasil()

    }

    private fun hasil():String
    {
        val digitOp = digitOp()
        if(digitOp.isEmpty()) return ""

        val waktuPenambahan = waktuPenambahan(digitOp)
        if(waktuPenambahan.isEmpty()) return ""

        val resultH = addSubCalc(waktuPenambahan)
        return resultH.toString()
    }

    private fun addSubCalc(passedList: MutableList<Any>): Float
    {
        var resultA = passedList[0] as Float
        for (i in passedList.indices)
        {
            if(passedList[i] is Char && i != passedList.lastIndex)
            {
                val operator = passedList[i]
                val nextDig = passedList[i + 1] as Float
                if (operator =='+')
                    resultA += nextDig
                if(operator == '-')
                    resultA -= nextDig

            }
        }
        return resultA
    }

    private fun waktuPenambahan (passedList: MutableList<Any>): MutableList<Any>{
        var list = passedList
        while (list.contains('x') || list.contains('÷'))
        {
            list = hitungBagian(list)
        }
        return list
    }

    private fun hitungBagian (passedList: MutableList<Any>): MutableList<Any>
    {

        val newList = mutableListOf<Any>()
        var restartIndex = passedList.size
        for(i in passedList.indices)
        {
            if(passedList[i] is Char && passedList[i] != passedList.lastIndex && i < restartIndex)
            {
                val operator = passedList[i]
                val prevDig = passedList[i-1] as Float
                val nextDig = passedList[i+1] as Float
                when(operator){
                    'x' -> {
                        newList.add(prevDig*nextDig)
                        restartIndex = i + 1
                    }
                    '÷' -> {
                        newList.add(prevDig/nextDig)
                        restartIndex = i + 1
                    }
                    else -> {
                        newList.add(prevDig)
                        newList.add(operator)
                    }
                }

            }
            if(i>restartIndex)
                newList.add(passedList[i])
        }
        return newList
    }

    private fun digitOp ():MutableList<Any> {

        var list = mutableListOf<Any>()
        var currentDig = ""
        for(char in att.text){
            if(char.isDigit() || char == '.')
                currentDig += char
            else 
            {
                list.add(currentDig.toFloat())
                currentDig = ""
                list.add(char)

            }
        }

        if(currentDig != "")
            list.add(currentDig.toFloat())

        return list
    }

}
