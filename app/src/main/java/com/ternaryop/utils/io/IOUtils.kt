package com.ternaryop.utils.io

import java.io.File
import java.io.FileNotFoundException
import java.io.IOException
import java.util.regex.Pattern

private const val BUFFER_SIZE = 100 * 1024

@Throws(IOException::class)
fun File.moveTo(targetFile: File) {
    if (!exists()) {
        throw FileNotFoundException("Source '$this' does not exist")
    }

    if (isDirectory) {
        throw IOException("Source '$this' is a directory")
    }

    if (targetFile.exists()) {
        throw IOException("Destination '$targetFile' already exists")
    }

    if (targetFile.isDirectory) {
        throw IOException("Destination '$targetFile' is a directory")
    }
    if (!renameTo(targetFile)) {
        copyTo(targetFile, true, BUFFER_SIZE)
        if (!delete()) {
            targetFile.delete()
            throw IOException("Failed to delete original file '$this' after copy to '$targetFile'")
        }
    }
}

fun File.generateUniqueFileName(): File {
    var file = this
    val parentFile = file.parentFile
    var nameWithExt = file.name
    val patternCount = Pattern.compile("""(.*) \((\d+)\)""")

    while (file.exists()) {
        var name: String
        val ext: String
        val extPos = nameWithExt.lastIndexOf('.')
        if (extPos < 0) {
            name = nameWithExt
            ext = ""
        } else {
            name = nameWithExt.substring(0, extPos)
            // contains dot
            ext = nameWithExt.substring(extPos)
        }
        val matcherCount = patternCount.matcher(name)
        var count = 1
        if (matcherCount.matches()) {
            name = matcherCount.group(1)
            count = Integer.parseInt(matcherCount.group(2)) + 1
        }
        nameWithExt = "$name ($count)$ext"
        file = File(parentFile, nameWithExt)
    }
    return file
}
