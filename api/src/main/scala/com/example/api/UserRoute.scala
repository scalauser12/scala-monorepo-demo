package com.example.api

import com.example.common.{Id, Result, StringUtils}
import com.example.core.{CreateUserRequest, User, UserService}

class UserRoute(service: UserService):

  def handleCreateUser(name: String, email: String): Result[User] =
    service.createUser(CreateUserRequest(name, email))

  def handleGetUser(rawId: String): Result[User] =
    service.getUser(Id(rawId))

  def handleListUsers: Seq[String] =
    service.listUsers.map: u =>
      s"${StringUtils.truncate(u.name, 20)} <${u.email}>"
