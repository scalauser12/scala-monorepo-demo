package com.example.core

import com.example.common.Id

final case class User(id: Id, name: String, email: String)

final case class CreateUserRequest(name: String, email: String)
