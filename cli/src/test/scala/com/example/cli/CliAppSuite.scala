package com.example.cli

import com.example.core.{CreateUserRequest, UserService}

class CliAppSuite extends munit.FunSuite:

  test("cliMain runs without throwing") {
    cliMain("TestUser")
  }

  test("createUser returns Right with valid name") {
    val service = UserService()
    val result = service.createUser(CreateUserRequest("Alice", "alice@example.com"))
    assert(result.isRight)
    assertEquals(result.map(_.name), Right("Alice"))
  }

  test("createUser rejects blank name") {
    val service = UserService()
    val result = service.createUser(CreateUserRequest("   ", "blank@example.com"))
    assert(result.isLeft)
  }

  test("multiple users can be created and listed") {
    val service = UserService()
    service.createUser(CreateUserRequest("Alice", "alice@example.com"))
    service.createUser(CreateUserRequest("Bob", "bob@example.com"))
    assertEquals(service.listUsers.size, 2)
  }
