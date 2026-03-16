package com.example.common

class TypesSuite extends munit.FunSuite:

  test("Id round-trips through apply and value") {
    val id = Id("abc-123")
    assertEquals(id.value, "abc-123")
  }

  test("Result Right case") {
    val r: Result[Int] = Right(42)
    assertEquals(r, Right(42))
  }

  test("Result Left case") {
    val r: Result[Int] = Left("error")
    assertEquals(r, Left("error"))
  }
