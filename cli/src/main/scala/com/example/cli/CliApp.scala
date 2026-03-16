package com.example.cli

import com.example.core.{CreateUserRequest, UserService}

@main def cliMain(args: String*): Unit =
  val service = UserService()

  println("=== CLI Demo ===")
  println("Creating users...")

  val names = if args.isEmpty then Seq("Default User") else args

  names.foreach: name =>
    service.createUser(CreateUserRequest(name, "user@example.com")) match
      case Right(user) => println(s"  [OK] Created: ${user.name} (${user.id.value})")
      case Left(err)   => println(s"  [FAIL] $err")

  println(s"\nTotal users: ${service.listUsers.size}")
