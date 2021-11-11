package com.github.rinotc.bank.domain.support

import java.util.Currency
import scala.math.BigDecimal.RoundingMode.RoundingMode

class Money(val amount: BigDecimal, val currency: Currency) extends Ordered[Money] with Equals {

  override def canEqual(that: Any): Boolean = that.isInstanceOf[Money]

  override def equals(obj: Any): Boolean = obj match {
    case that: Money => amount == that.amount && currency == that.currency
    case _           => false
  }

  override def hashCode(): Int = (amount, currency).##

  override def compare(that: Money): Int = {
    require(currency == that.currency)
    amount compare that.amount
  }

  override def toString: String = s"${currency.getSymbol} $amount"

  def +(other: Money): Money = {
    require(currency == other.currency)
    plus(other)
  }

  def -(other: Money): Money = {
    require(currency == other.currency)
    minus(other)
  }

  def plus(other: Money): Money = {
    checkHasSameCurrencyAs(other)
    Money.adjustBy(amount + other.amount, currency)
  }

  def minus(other: Money): Money = plus(other.negated)

  def negated: Money = Money(BigDecimal(amount.bigDecimal.negate), currency)

  private def hasSameCurrencyAs(arg: Money): Boolean =
    currency.equals(arg.currency) || arg.amount.equals(BigDecimal(0)) || amount.equals(BigDecimal(0))

  private def checkHasSameCurrencyAs(aMoney: Money): Unit = {
    if (!hasSameCurrencyAs(aMoney)) {
      throw new ClassCastException(s"$aMoney is not same currency as $this")
    }
  }

  lazy val isNegative: Boolean = amount < BigDecimal(0)

  lazy val isZero: Boolean = equals(Money.adjustBy(0.0, currency))
}

object Money {

  private val DefaultRoundingMode = BigDecimal.RoundingMode.HALF_EVEN

  def apply(amount: BigDecimal, currency: Currency): Money = new Money(amount, currency)

  def adjustBy(rawAmount: BigDecimal, currency: Currency, roundingMode: RoundingMode): Money = {
    val amount = rawAmount.setScale(currency.getDefaultFractionDigits, roundingMode)
    new Money(amount, currency)
  }

  def adjustBy(amount: BigDecimal, currency: Currency): Money =
    adjustBy(amount, currency, BigDecimal.RoundingMode.UNNECESSARY)

  def adjustBy(dblAmount: Double, currency: Currency): Money = adjustBy(dblAmount, currency, DefaultRoundingMode)
}
