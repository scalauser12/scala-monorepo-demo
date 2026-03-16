package com.example.common

class StringUtilsSuite extends munit.FunSuite:

  test("truncate returns original when under limit") {
    assertEquals(StringUtils.truncate("hello", 10), "hello")
  }

  test("truncate adds ellipsis when over limit") {
    assertEquals(StringUtils.truncate("hello world", 5), "hello...")
  }

  test("truncate handles empty string") {
    assertEquals(StringUtils.truncate("", 5), "")
  }

  test("normalizeWhitespace collapses multiple spaces") {
    assertEquals(StringUtils.normalizeWhitespace("hello   world"), "hello world")
  }

  test("normalizeWhitespace trims leading and trailing") {
    assertEquals(StringUtils.normalizeWhitespace("  hello  "), "hello")
  }
