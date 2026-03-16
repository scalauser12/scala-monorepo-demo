package com.example.core

import com.example.common.{Id, Result, StringUtils}

class UserService:
  private var users: Map[Id, User] = Map.empty

  def createUser(req: CreateUserRequest): Result[User] =
    val normalizedName = StringUtils.normalizeWhitespace(req.name)
    if normalizedName.isEmpty then Left("Name must not be blank")
    else
      val id   = Id(java.util.UUID.randomUUID().toString)
      val user = User(id, normalizedName, req.email.trim)
      users = users + (id -> user)
      Right(user)

  def getUser(id: Id): Result[User] =
    users.get(id).toRight(s"User not found: ${id.value}")

  def listUsers: Seq[User] = users.values.toSeq
