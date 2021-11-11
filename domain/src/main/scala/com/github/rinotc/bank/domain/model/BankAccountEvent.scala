package com.github.rinotc.bank.domain.model

import com.github.rinotc.bank.domain.support.Money

import java.time.Instant

sealed trait BankAccountEvent {
  val bankAccountId: BankAccountId
  val occurredAt: Instant
}

object BankAccountEvent {
  case class BankAccountOpened(bankAccountId: BankAccountId, name: BankAccountName, occurredAt: Instant = Instant.now())
      extends BankAccountEvent

  case class BankAccountEventUpdated(bankAccountId: BankAccountId, name: BankAccountName, occurredAt: Instant)
      extends BankAccountEvent

  case class BankAccountDeposited(bankAccountId: BankAccountId, deposit: Money, occurredAt: Instant)
      extends BankAccountEvent

  case class BankAccountWithdraw(bankAccountId: BankAccountId, withdraw: Money, occurredAt: Instant)
      extends BankAccountEvent

  case class BankAccountClosed(bankAccountId: BankAccountId, occurredAt: Instant) extends BankAccountEvent
}
