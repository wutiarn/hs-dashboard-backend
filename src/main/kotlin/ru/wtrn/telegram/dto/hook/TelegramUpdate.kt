package ru.wtrn.telegram.dto.hook

data class TelegramUpdate(
    val message: Message?
) {
    data class Message(
        val text: String,
        val chat: Chat,
        val from: From
    )

    data class Chat(
        val id: Int
    )

    data class From(
        val id: Int,
        val username: String?
    )
}
