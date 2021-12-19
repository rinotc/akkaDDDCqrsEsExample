package com.github.rinotc.bank.adaptor

import scala.concurrent.duration.Duration

case class BankAccountAggregateConfig(receiveTimeout: Duration, numOfEventsToSnapshot: Int)
