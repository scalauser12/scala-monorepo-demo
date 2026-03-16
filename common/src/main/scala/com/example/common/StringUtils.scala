package com.example.common

object StringUtils:

  def truncate(s: String, maxLen: Int): String =
    if s.length <= maxLen then s else s.take(maxLen) + "..."

  def normalizeWhitespace(s: String): String =
    s.trim.replaceAll("\\s+", " ")
