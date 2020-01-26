package ru.wtrn.budgetanalyzer.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus
import java.lang.RuntimeException

@ResponseStatus(HttpStatus.FORBIDDEN)
class UnknownSmsHookSecretException : RuntimeException("Unknown sms hook secret")
