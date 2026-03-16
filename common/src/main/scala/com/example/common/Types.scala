package com.example.common

opaque type Id = String

object Id:
  def apply(value: String): Id = value
  extension (id: Id) def value: String = id

type Result[A] = Either[String, A]
