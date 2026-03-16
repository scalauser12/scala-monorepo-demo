package com.example.api

import com.example.core.UserService

class UserRouteSuite extends munit.FunSuite:

  test("handleCreateUser returns Right on success") {
    val route = UserRoute(UserService())
    val result = route.handleCreateUser("Alice", "alice@example.com")
    assert(result.isRight)
    assertEquals(result.toOption.get.name, "Alice")
  }

  test("handleGetUser returns Left for unknown ID") {
    val route = UserRoute(UserService())
    assert(route.handleGetUser("nonexistent").isLeft)
  }

  test("handleGetUser returns Right for existing user") {
    val route = UserRoute(UserService())
    val user = route.handleCreateUser("Alice", "alice@example.com").toOption.get
    assertEquals(route.handleGetUser(user.id.value), Right(user))
  }

  test("handleListUsers returns formatted strings") {
    val route = UserRoute(UserService())
    route.handleCreateUser("Alice", "alice@example.com")
    val list = route.handleListUsers
    assertEquals(list.size, 1)
    assert(list.head.contains("Alice"))
    assert(list.head.contains("<alice@example.com>"))
  }
