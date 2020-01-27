package ru.wtrn.telegram.exception

import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.ResponseStatus

@ResponseStatus(HttpStatus.BAD_REQUEST)
class IncorrectWebhookKeyException : RuntimeException("Incorrect webhook key provided")
