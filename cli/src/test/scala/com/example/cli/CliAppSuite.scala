package com.example.cli

class CliAppSuite extends munit.FunSuite:

  test("cliMain runs without throwing") {
    cliMain("TestUser")
  }
