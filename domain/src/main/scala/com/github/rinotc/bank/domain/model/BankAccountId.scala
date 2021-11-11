package com.github.rinotc.bank.domain.model

case class BankAccountId (value: Long) {
  require(value > 0)
}
