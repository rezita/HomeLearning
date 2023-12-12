package com.github.rezita.homelearning.network

enum class SheetAction(val value: String) {
    UPDATE_SPELLING_WORDS("updateSpellingWords"),
    SAVE_WORDS("insertSpellingWords"),
    READ_SPELLING_WORDS("getSpellingWords"),
    READ_SPELLING_CATEGORIES("getSpellingCategories"),
    READ_READING_WORDS("getReadingWords"),
    READ_IRREGULAR_VERBS("getIrregularVerbs"),
    RESTORE_SPELLING_FROM_LOG("restoreSpellingFromLogs"),
    UPDATE_IRREGULAR_VERBS( "updateIrregularVerbs"),
    READ_READING_CEW("getReadingCEW"),
    READ_HOMOPHONES("getHomophones"),
    UPDATE_HOMOPHONES("updateHomophones");

    companion object {
        fun forValue(value: String) = values().find{ it.value == value }
    }
}