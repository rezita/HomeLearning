package com.github.rezita.homelearning.tts

import android.content.Context
import android.speech.tts.TextToSpeech
import java.util.Locale


class HLTextToSpeech(private val context: Context) {
    private val utteranceManager = HLUtteranceProgressListener()
    private lateinit var textToSpeech: TextToSpeech
    private var ttsStatus: TtsStatus = TtsStatus.Init

    init {
        textToSpeech = TextToSpeech(context) { status ->
            if (status == TextToSpeech.SUCCESS) {
                ttsStatus = TtsStatus.Success
                val locale = availableLocale()
                if (locale != null) {
                    textToSpeech.setLanguage(locale)
                    textToSpeech.setOnUtteranceProgressListener(utteranceManager)
                } else {
                    ttsStatus = TtsStatus.Error("There is no suitable locale")
                }
            } else {
                ttsStatus = TtsStatus.Error("Error while init")
            }
        }
    }

    fun speak(text: String, queueMode: Int) {
        val utterance = utteranceManager.create(text, queueMode)
        speak(textToSpeech, utterance)

    }

    private fun speak(tts: TextToSpeech, utterance: Utterance) {
        tts.speak(utterance.text, utterance.queueMode, null, utterance.id)
    }

    fun onDestroy() {
        textToSpeech.stop()
        textToSpeech.shutdown()
    }

    private fun availableLocale(): Locale? {
        val availableLocales = textToSpeech.availableLanguages
        if (Locale.UK in availableLocales) {
            return Locale.UK
        } else if (Locale.US in availableLocales) {
            return Locale.US
        }
        return null
    }
}

//will use before speak to check the status
sealed interface TtsStatus {
    data object Init : TtsStatus
    data object Success : TtsStatus
    data class Error(val msg: String) : TtsStatus

}