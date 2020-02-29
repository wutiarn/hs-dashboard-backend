package ru.wtrn.hs.dashboard.dto

data class AirMonitorStatusDto(
    val co2: String,
    val temperature: String,
    val rh: String,
    val pm25: String,
    val tvoc: String
)
