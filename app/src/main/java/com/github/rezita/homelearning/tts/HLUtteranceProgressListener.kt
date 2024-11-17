package com.github.rezita.homelearning.tts

import android.speech.tts.TextToSpeech
import android.speech.tts.UtteranceProgressListener

class HLUtteranceProgressListener : UtteranceProgressListener() {
    private val utterances = HashMap<String, Utterance>()
    private var nextUtteranceId = 0L

    @Synchronized
    fun create(text: String, queueMode: Int): Utterance {
        val utteranceId = generateUtteranceId()
        val utterance = Utterance(utteranceId, text, queueMode)
        utterances.put(utteranceId, utterance)
        return utterance

    }

    override fun onStart(utteranceId: String) {
        val started = utterances.remove(utteranceId) as Utterance
        if (started.queueMode == TextToSpeech.QUEUE_FLUSH) {
            utterances.clear()
        }
        utterances.put(utteranceId, started)
    }

    override fun onDone(utteranceId: String?) {
        utterances.remove(utteranceId) as Utterance
    }

    override fun onError(utteranceId: String?) {
        //should do something in tts, will do it later
        utterances.remove(utteranceId) as Utterance
    }

    private fun generateUtteranceId(): String {
        val utteranceId = nextUtteranceId++
        return utteranceId.toString()
    }
}