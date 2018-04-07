package com.ternaryop.utils.text

import java.util.regex.Pattern

/**
 * Surround all pattern strings found on text with a ** (bold) tag
 * @param pattern the pattern to surround
 * @param text the whole string
 * @return the string with highlighted patterns
 ** */
fun String.htmlHighlightPattern(pattern: String): String {
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
 * @param string html string to strip
 * @return stripped string
 */
fun String.stripHtmlTags(tags: String): String {
    return Pattern.compile("""</?($tags).*?>""", Pattern.CASE_INSENSITIVE)
        .matcher(this).replaceAll("")
}