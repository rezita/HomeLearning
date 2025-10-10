package com.github.rezita.homelearning.network

enum class SheetAction(val value: String) {
    //Erik spelling
    UPDATE_ERIK_SPELLING_WORDS("updateErikSpellingWords"),
    SAVE_ERIK_WORDS("insertErikSpellingWords"),
    READ_ERIK_SPELLING_WORDS("getErikSpellingWords"),
    READ_ERIK_SPELLING_CATEGORIES("getErikSpellingCategories"),
    MODIFY_ERIK_SPELLING_WORD("modifyErikSpellingWord"),

    //Mark reading
    READ_READING_WORDS("getReadingWords"),
    READ_READING_CEW("getReadingCEW"),

    //Erik sentences - irregular verbs, homophones
    READ_IRREGULAR_VERBS("getIrregularVerbs"),
    UPDATE_IRREGULAR_VERBS("updateIrregularVerbs"),
    READ_HOMOPHONES("getHomophones"),
    UPDATE_HOMOPHONES("updateHomophones"),


    //Mark spelling
    UPDATE_MARK_SPELLING_WORDS("updateMarkSpellingWords"),
    SAVE_MARK_WORDS("insertMarkSpellingWords"),
    READ_MARK_SPELLING_WORDS("getMarkSpellingWords"),
    READ_MARK_SPELLING_CATEGORIES("getMarkSpellingCategories"),
    MODIFY_MARK_SPELLING_WORD("modifyMarkSpellingWord"),

    //Spanish
    READ_ZITA_SPANISH_WORDS("getSpanishWordsZita"),
    READ_WEEK_SPANISH_WORDS("getWeekSpanishWords"),
    SAVE_ZITA_SPANISH_WORDS("insertSpanishWordsZita"),
    SET_WEEK_SPANISH_WORDS("setWeekSpanishWords"),
    UPDATE_ZITA_SPANISH_WORDS("updateSpanishWordsZita"),
    MODIFY_ZITA_SPANISH_WORD("modifySpanishWordZita");

    //TODO
    //RESTORE_ERIK_SPELLING_FROM_LOG("restoreErikSpellingFromLogs"),
    //RESTORE_MARK_SPELLING_FROM_LOG("restoreSpellingFromLogs"),

    companion object {
        fun forValue(value: String) = values().find { it.value == value }
    }
}