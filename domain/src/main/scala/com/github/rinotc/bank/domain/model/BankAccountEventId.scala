package com.github.rinotc.bank.domain.model

case class BankAccountEventId (value: Long) {
  require(value > 0)
}
