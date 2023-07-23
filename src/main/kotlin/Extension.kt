package com.example

import com.example.xiao.piglet.tool.MessageDigestUtil

//将字符串sha256
fun String.sha256(): String = MessageDigestUtil.sha256(this)

//字符串集合用换行符拼接
fun List<String>.toTextWithLine(): String{
    val stringBuilder = StringBuilder()
    this.forEach {
        stringBuilder.appendLine(it)
    }
    return stringBuilder.toString()
}