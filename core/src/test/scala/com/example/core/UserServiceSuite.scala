package com.example.core

import com.example.common.Id

class UserServiceSuite extends munit.FunSuite:

  test("createUser returns Right with valid request") {
    val service = UserService()
    val result = service.createUser(CreateUserRequest("Alice", "alice@example.com"))
    assert(result.isRight)
    assertEquals(result.toOption.get.name, "Alice")
  }

  test("createUser rejects blank name") {
    val service = UserService()
    val result = service.createUser(CreateUserRequest("   ", "blank@example.com"))
    assert(result.isLeft)
  }

  test("createUser generates unique IDs") {
    val service = UserService()
    val u1 = service.createUser(CreateUserRequest("Alice", "a@example.com")).toOption.get
    val u2 = service.createUser(CreateUserRequest("Bob", "b@example.com")).toOption.get
    assertNotEquals(u1.id.value, u2.id.value)
  }

  test("getUser returns Right for existing user") {
    val service = UserService()
    val user = service.createUser(CreateUserRequest("Alice", "a@example.com")).toOption.get
    assertEquals(service.getUser(user.id), Right(user))
  }

  test("getUser returns Left for missing user") {
    val service = UserService()
    assert(service.getUser(Id("nonexistent")).isLeft)
  }

  test("listUsers returns all created users") {
    val service = UserService()
    service.createUser(CreateUserRequest("Alice", "a@example.com"))
    service.createUser(CreateUserRequest("Bob", "b@example.com"))
    assertEquals(service.listUsers.size, 2)
  }
