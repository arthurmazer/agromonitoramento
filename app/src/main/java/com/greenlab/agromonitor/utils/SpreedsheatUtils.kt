package com.greenlab.agromonitor.utils

import com.greenlab.agromonitor.entity.Product
import com.greenlab.agromonitor.entity.SpreadsheetValues
import java.util.*
import kotlin.collections.HashMap

object SpreedsheatUtils {

    @JvmStatic fun getPTArray(myList: ArrayList<Any>): ArrayList<Float>{
        var flagPT = false
        var arrayResult = ArrayList<Float>()
        myList.forEach {
            if ( it is Product){
                //categoria
                flagPT = it.product.toLowerCase(Locale.getDefault()) == "pt"
            }else if (it is SpreadsheetValues){
                if (flagPT) {
                   arrayResult.add(it.value)
                }
            }
        }
        return arrayResult
    }


    @JvmStatic fun getSubArrays(myList: ArrayList<Any>): HashMap<Int, ArrayList<Float>>{
        var index = 0
        var flagPT = false
        var arrayResult = HashMap<Int, ArrayList<Float>>()
        myList.forEach {
            if ( it is Product){
                //categoria
                flagPT = it.product.toLowerCase(Locale.getDefault()) == "pt"
                index = 0
            }else if (it is SpreadsheetValues){
                if (!flagPT) {
                    //valores da planilha
                    var currentArray = arrayResult[index]
                    if (currentArray != null) {
                        currentArray.add(it.value)
                        arrayResult[index] = currentArray
                        index++
                    } else {
                        currentArray = ArrayList()
                        currentArray.add(it.value)
                        arrayResult[index] = currentArray
                        index++
                    }
                }
            }
        }
        return arrayResult
    }
}