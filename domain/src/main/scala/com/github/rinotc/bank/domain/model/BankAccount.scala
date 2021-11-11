package com.github.rinotc.bank.domain.model

import com.github.rinotc.bank.domain.model.BankAccount.BankAccountError
import com.github.rinotc.bank.domain.support.Money

import java.time.Instant

trait BankAccount {

  def id: BankAccountId

  def name: BankAccountName

  def isClosed: Boolean

  def balance: Money

  def createdAt: Instant

  def updatedAt: Instant

  def close(occurredAt: Instant = Instant.now): Either[BankAccountError, BankAccount]

  def withName(name: BankAccountName, occurredAt: Instant = Instant.now): Either[BankAccountError, BankAccount]

  def deposit(money: Money, occurredAt: Instant = Instant.now): Either[BankAccountError, BankAccount]

  def withdraw(money: Money, occurredAt: Instant = Instant.now): Either[BankAccountError, BankAccount]
}

object BankAccount {

  sealed abstract class BankAccountError(val message: String)

  case class InvalidStateError(id: Option[BankAccountId] = None)
      extends BankAccountError(s"Invalid state${id.fold("")(id => s":id = ${id.value}")}")

  case class AlreadyClosedStateError(id: BankAccountId)
      extends BankAccountError(s"State is already closed: id = ${id.value}")

  case class DepositZeroError(id: BankAccountId, money: Money)
      extends BankAccountError(s"A deposited money amount 0 is illegal: id = ${id.value}, money = $money")

  case class NegativeBalanceError(id: BankAccountId, money: Money)
      extends BankAccountError(s"Forbidden that deposit amount to negative: id = ${id.value}, money = $money")

  private case class BankAccountImpl(
      id: BankAccountId,
      name: BankAccountName,
      isClosed: Boolean,
      balance: Money,
      createdAt: Instant,
      updatedAt: Instant
  ) extends BankAccount {

    override def close(occurredAt: Instant): Either[BankAccountError, BankAccount] = {
      if (isClosed) Left(AlreadyClosedStateError(id))
      else Right(copy(isClosed = true, updatedAt = occurredAt))
    }

    override def withName(name: BankAccountName, occurredAt: Instant): Either[BankAccountError, BankAccount] = {
      if (isClosed) Left(AlreadyClosedStateError(id))
      else Right(copy(name = name, updatedAt = occurredAt))
    }

    override def deposit(money: Money, occurredAt: Instant): Either[BankAccountError, BankAccount] = {
      if (isClosed) Left(AlreadyClosedStateError(id))
      else {
        money match {
          case d if d.isZero                 => Left(DepositZeroError(id, money))
          case d if (balance + d).isNegative => Left(NegativeBalanceError(id, money))
          case _                             => Right(copy(balance = balance + money, updatedAt = occurredAt))
        }
      }
    }

    override def withdraw(money: Money, occurredAt: Instant): Either[BankAccountError, BankAccount] = {
      if (isClosed) Left(AlreadyClosedStateError(id))
      else
        money match {
          case d if d.isZero                 => Left(DepositZeroError(id, money))
          case d if (balance - d).isNegative => Left(NegativeBalanceError(id, money))
          case _                             => Right(copy(balance = balance + money, updatedAt = occurredAt))
        }
    }
  }
}
