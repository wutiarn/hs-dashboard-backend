package ru.wtrn.telegram.dto.hook

data class TelegramUpdate(
    val message: Message?
) {
    data class Message(
        val text: String,
        val chat: Chat
    )

    data class Chat(
        val id: Int
    )
}
