package com.ternaryop.utils.text

import java.util.regex.Pattern

/**
 * Surround all pattern strings found on text with a ** (bold) tag
 * @param pattern the pattern to surround
 * @return the string with highlighted patterns
 ** */
fun String.htmlHighlightPattern(pattern: String): String {
    if (pattern.isBlank()) {
        return this
    }
    val sb = StringBuffer()
    val m = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE).matcher(this)

    while (m.find()) {
        // get substring to preserve case
        m.appendReplacement(sb, "<b>" + substring(m.start(), m.end()) + "</b>")
    }
    m.appendTail(sb)

    return sb.toString()
}

/**
 * Strip all specified HTML tags contained into string
 * @param tags tags separated by pipe (eg "a|br|img")
 * @return stripped string
 */
fun String.stripHtmlTags(tags: String): String {
    return if (tags.isBlank()) {
        this
    } else {
        Pattern.compile("""</?($tags).*?>""", Pattern.CASE_INSENSITIVE)
            .matcher(this).replaceAll("")
    }
}
