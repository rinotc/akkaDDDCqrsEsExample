package com.github.rinotc.bank.domain.model

case class BankAccountName (value: String) {
  require(value.length < 255)
}
