package com.github.rinotc.bank.adaptor

import akka.actor.typed.Behavior
import akka.persistence.typed.PersistenceId
import akka.persistence.typed.scaladsl.{Effect, EventSourcedBehavior}
import com.github.rinotc.bank.domain.model.BankAccount.BankAccountError
import com.github.rinotc.bank.domain.model.{BankAccount, BankAccountId, BankAccountName}
import com.github.rinotc.bank.domain.support.Money

object BankAccountAggregate {

  def name(id: BankAccountId): String = id.value.toString

  final val AggregateName = "BankAccount"

  sealed trait Command
  final case class OpenBankAccount(bankAccountId: BankAccountId, bankAccountName: BankAccountName) extends Command

  sealed trait Event
  final case class BankAccountOpenSucceeded(bankAccountId: BankAccountId, bankAccountName: BankAccountName)
      extends Event
  final case class OpenBankAccountFailed(bankAccountId: BankAccountId, error: BankAccountError) extends Event
  final case class State()

  def apply(): Behavior[Command] = EventSourcedBehavior[Command, Event, State](
    persistenceId = PersistenceId.ofUniqueId("abc"),
    emptyState = State(),
    commandHandler = (state, cmd) => throw new NotImplementedError("TODO: process the command & return an Effect"),
    eventHandler = (state, cmd) => throw new NotImplementedError("TODO: process the event return the next state")
  )

  val commandHandler: (State, Command) => Effect[Event, State] = { (state, command) =>
    command match {
      case OpenBankAccount(bankAccountId, bankAccountName) =>
        Effect.persist(BankAccountOpenSucceeded(bankAccountId, bankAccountName))
    }
  }

  object Protocol {

    sealed trait BankAccountCommandRequest {
      val bankAccountId: BankAccountId
    }

    sealed trait BankAccountCommandResponse {
      val bankAccountId: BankAccountId
    }

    //--

    case class OpenBankAccountRequest(bankAccountId: BankAccountId, name: BankAccountName)
        extends BankAccountCommandRequest

    sealed trait OpenBankAccountResponse {
      val bankAccountId: BankAccountId
    }

    case class OpenBankAccountSucceeded(bankAccountId: BankAccountId) extends OpenBankAccountResponse

    case class OpenBankAccountFailed(bankAccountId: BankAccountId, error: BankAccountError)
        extends OpenBankAccountResponse

    //--

    case class UpdateBankAccountRequest(bankAccountId: BankAccountId, name: BankAccountName)
        extends BankAccountCommandRequest

    sealed trait UpdateBankAccountResponse {
      val bankAccountId: BankAccountId
    }

    case class UpdateBankAccountSucceeded(bankAccountId: BankAccountId) extends OpenBankAccountResponse

    case class UpdateBankAccountFailed(bankAccountId: BankAccountId, error: BankAccountError)
        extends OpenBankAccountResponse

    //--

    case class CloseBankAccountRequest(bankAccountId: BankAccountId) extends BankAccountCommandRequest

    sealed trait CloseBankAccountResponse {
      val bankAccountId: BankAccountId
    }

    case class CloseBankAccountSucceeded(bankAccountId: BankAccountId) extends CloseBankAccountResponse

    case class CloseBankAccountFailed(bankAccountId: BankAccountId, error: BankAccountError)
        extends CloseBankAccountResponse

    //--

    case class GetBalanceRequest(bankAccountId: BankAccountId) extends BankAccountCommandRequest

    case class GetBalanceResponse(bankAccountId: BankAccountId, balance: Money) extends BankAccountCommandRequest

    //--

    case class DepositRequest(bankAccountId: BankAccountId, deposit: Money) extends BankAccountCommandRequest

    sealed trait DepositResponse extends BankAccountCommandResponse

    case class DepositSucceeded(bankAccountId: BankAccountId) extends DepositResponse

    case class DepositFailed(bankAccountId: BankAccountId, error: BankAccountError) extends DepositResponse

    //--

    case class WithdrawRequest(bankAccountId: BankAccountId, withdraw: Money) extends BankAccountCommandRequest

    sealed trait WithdrawResponse extends BankAccountCommandResponse

    case class WithdrawSucceeded(bankAccountId: BankAccountId) extends WithdrawResponse

    case class WithdrawFailed(bankAccountId: BankAccountId, error: BankAccountError) extends WithdrawResponse
  }

  implicit class EitherOps(val self: Either[BankAccountError, BankAccount]) {
    def toSomeOrThrow: Option[BankAccount] = self.fold(error => throw new IllegalStateException(error.message), Some(_))
  }
}
