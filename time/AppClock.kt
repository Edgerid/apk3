package com.personalfitnessos.core.time

import java.time.Clock
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId

/**
 * Every "what day is it" and "what time is it" call in the app goes through this instead of
 * `LocalDate.now()` / `Instant.now()` directly, for two reasons (spec §36):
 *
 * 1. Testability — unit tests can inject a fixed [Clock] to test midnight/day-boundary
 *    behavior deterministically instead of depending on the real wall clock.
 * 2. Correctness — [today] always resolves in the device's *current* local timezone at the
 *    call site, and the resulting [LocalDate] is meant to be stored and never re-derived
 *    from an [Instant] later, so a meal logged at 23:59 keeps its calendar day even if the
 *    timezone changes afterward.
 */
interface AppClock {
    fun now(): Instant
    fun today(): LocalDate
    fun zone(): ZoneId
}

class SystemAppClock(private val clock: Clock = Clock.systemDefaultZone()) : AppClock {
    override fun now(): Instant = clock.instant()
    override fun today(): LocalDate = LocalDate.now(clock)
    override fun zone(): ZoneId = clock.zone
}
