package com.example.data.local.converters

import org.junit.jupiter.api.Assertions.*
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.DisplayName
import org.junit.jupiter.api.Test
import java.time.LocalDate

@DisplayName("LocalDateConverter")
class LocalDateConverterTest {
    private lateinit var converter: LocalDateConverter

    @BeforeEach
    fun setUp() {
        converter = LocalDateConverter()
    }

    @Test
    @DisplayName("fromLocalDate() конвертирует LocalDate в строку ISO формата")
    fun fromLocalDateConvertsToString() {
        val date = LocalDate.of(2025, 1, 15)

        val result = converter.fromLocalDate(date)

        assertEquals(result, "2025-01-15")
    }

    @Test
    @DisplayName("toLocalDate() парсит строку ISO формата в LocalDate")
    fun toLocalDateParsesFromString() {
        val result = converter.toLocalDate("2025-01-15")

        assertEquals(result, LocalDate.of(2025, 1, 15))
    }

    @Test
    @DisplayName("Round-trip: fromLocalDate(toLocalDate(string)) возвращает исходную строку")
    fun roundTripStringToDateAndBack() {
        val original = "2025-06-30"

        val result = converter.fromLocalDate(converter.toLocalDate(original))

        assertEquals(result, original)
    }

    @Test
    @DisplayName("Round-trip: toLocalDate(fromLocalDate(date)) возвращает исходную дату")
    fun roundTripDateToStringAndBack() {
        val original = LocalDate.of(2025, 6, 30)

        val result = converter.toLocalDate(converter.fromLocalDate(original))

        assertEquals(result, original)
    }
}
