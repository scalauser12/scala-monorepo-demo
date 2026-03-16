package com.example.api

import com.example.core.UserService

@main def apiMain(): Unit =
  val service = UserService()
  val route   = UserRoute(service)

  println("=== API Demo ===")

  route.handleCreateUser("Alice Johnson", "alice@example.com") match
    case Right(user) => println(s"Created user: ${user.id.value} - ${user.name}")
    case Left(err)   => println(s"Error: $err")

  route.handleCreateUser("Bob Smith", "bob@example.com") match
    case Right(user) => println(s"Created user: ${user.id.value} - ${user.name}")
    case Left(err)   => println(s"Error: $err")

  println(s"All users: ${route.handleListUsers.mkString(", ")}")
