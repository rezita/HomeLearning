package com.github.rezita.homelearning.ui.screens.spelling

import android.speech.tts.TextToSpeech
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Done
import androidx.compose.material.icons.rounded.RecordVoiceOver
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedIconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.rezita.homelearning.R
import com.github.rezita.homelearning.model.SpellingWord
import com.github.rezita.homelearning.model.WordStatus
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayInColumn
import com.github.rezita.homelearning.ui.screens.common.ErrorDisplayWithContent
import com.github.rezita.homelearning.ui.screens.common.ErrorText
import com.github.rezita.homelearning.ui.screens.common.LoadingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.LoadingProgressBar
import com.github.rezita.homelearning.ui.screens.common.SavingErrorSnackbar
import com.github.rezita.homelearning.ui.screens.common.SavingSuccessSnackbar
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.theme.sentence_correct
import com.github.rezita.homelearning.ui.theme.sentence_incorrect
import com.github.rezita.homelearning.ui.viewmodels.MAX_WORD_LENGTH
import com.github.rezita.homelearning.utils.toDp
import kotlinx.coroutines.CoroutineScope
import java.util.Locale

@Composable
fun SpellingContent(
    state: SpellingUiState,
    onItemValueChange: (Int, WordStatus) -> Unit,
    onItemReset: (Int) -> Unit,
    onItemEdit: (Int) -> Unit,
    onEditItemValueChange: (String) -> Unit,
    onLoadCallback: () -> Unit,
    onSaveCallback: () -> Unit,
    onEditCancelCallback: () -> Unit,
    onEditSubmitCallback: () -> Unit,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    rbContentType: RadioButtonContentType,
    modifier: Modifier = Modifier
) {
    val tts = rememberTextToSpeech()
    when (state) {
        is SpellingUiState.Loading -> {
            LoadingProgressBar(modifier = modifier)
        }

        is SpellingUiState.Loaded -> {
            SpellingItems(
                words = state.words,
                rbContentType = rbContentType,
                textToSpeech = tts.value,
                modifier = modifier,
                onValueChange = onItemValueChange,
                onItemReset = onItemReset,
                onItemEdit = onItemEdit
            )
        }

        is SpellingUiState.LoadingError -> {
            LoadingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayInColumn(
                message = stringResource(id = state.errorMessage),
                callback = onLoadCallback,
                modifier = modifier
            )
        }

        is SpellingUiState.Saved -> {
            SavingSuccessSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            SpellingItems(
                words = state.words,
                rbContentType = rbContentType,
                textToSpeech = tts.value,
                modifier = modifier,
                isEnabled = false
            )
        }

        is SpellingUiState.SavingError -> {
            SavingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayWithContent(
                message = stringResource(id = state.errorMessage),
                callback = onSaveCallback,
                content = {
                    SpellingItems(
                        words = state.words,
                        rbContentType = rbContentType,
                        textToSpeech = tts.value,
                        isEnabled = false,
                    )
                },
                modifier = modifier
            )
        }

        is SpellingUiState.Editing -> {
            EditSpellingItems(
                words = state.words,
                rbContentType = rbContentType,
                editedIndex = state.editState.index!!,
                modifiedWord = state.editState.wordModified,
                onValueChange = onEditItemValueChange,
                onEditCancelCallback = onEditCancelCallback,
                onEditSubmitCallback = onEditSubmitCallback,
                modifier = modifier,
                isEditing = state.isEditing
            )
        }

        is SpellingUiState.EditError -> {
            EditSpellingItems(
                words = state.words,
                rbContentType = rbContentType,
                editedIndex = state.editState.index!!,
                modifiedWord = state.editState.wordModified,
                onValueChange = onEditItemValueChange,
                onEditCancelCallback = onEditCancelCallback,
                onEditSubmitCallback = onEditSubmitCallback,
                modifier = modifier,
                isEditing = true,
                errorMsg = state.errorMessage
            )
        }
    }
}

@Composable
private fun SpellingItems(
    words: List<SpellingWord>,
    rbContentType: RadioButtonContentType,
    textToSpeech: TextToSpeech?,
    modifier: Modifier = Modifier,
    onValueChange: (Int, WordStatus) -> Unit = { _, _ -> run {} },
    onItemReset: (Int) -> Unit = {},
    onItemEdit: (Int) -> Unit = {},
    isEnabled: Boolean = true,
) {

    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = getScores(words),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            textAlign = TextAlign.Right,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(words) { index, item ->
                SpellingItem(
                    index = index,
                    word = item.word,
                    wordStatus = item.status,
                    onItemSelected = { status -> onValueChange(index, status) },
                    onItemReset = { onItemReset(index) },
                    onItemEdit = { onItemEdit(index) },
                    rbContentType = rbContentType,
                    isEnabled = isEnabled,
                    showSpeaker = textToSpeech != null,
                    isSpeakingCallback = { setSpeaker(item.word, textToSpeech) },
                )
                HorizontalDivider(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}

@Composable
private fun EditSpellingItems(
    words: List<SpellingWord>,
    rbContentType: RadioButtonContentType,
    editedIndex: Int,
    modifiedWord: String,
    onValueChange: (String) -> Unit,
    onEditCancelCallback: () -> Unit,
    onEditSubmitCallback: () -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = true,
    errorMsg: String? = null
) {
    Column(modifier = modifier.fillMaxSize()) {
        Text(
            text = getScores(words),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            textAlign = TextAlign.Right,
        )

        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(words) { index, item ->
                if (index == editedIndex) {
                    SpellingEditedItem(
                        index = index,
                        word = modifiedWord,
                        onValueChange = onValueChange,
                        onEditCancelCallback = onEditCancelCallback,
                        onEditSubmitCallback = onEditSubmitCallback,
                        isEditing = isEditing,
                        errorMsg = errorMsg
                    )

                } else {
                    SpellingItem(
                        index = index,
                        word = item.word,
                        wordStatus = item.status,
                        onItemSelected = { _ -> run {} },
                        onItemReset = {},
                        onItemEdit = {},
                        rbContentType = rbContentType,
                        isEnabled = false,
                        showSpeaker = false
                    )
                }
                HorizontalDivider(
                    modifier = Modifier
                        .height(1.dp)
                        .fillMaxHeight()
                        .fillMaxWidth(),
                    color = MaterialTheme.colorScheme.surfaceVariant
                )
            }
        }
    }
}


@Composable
private fun SpellingItem(
    index: Int,
    word: String,
    wordStatus: WordStatus,
    onItemSelected: (WordStatus) -> Unit,
    onItemReset: () -> Unit,
    onItemEdit: () -> Unit,
    rbContentType: RadioButtonContentType,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    showSpeaker: Boolean = true,
    isSpeakingCallback: () -> Unit = {}

) {
    if (rbContentType == RadioButtonContentType.BUTTONS_SECOND_LINE) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_medium)
                ),
            horizontalAlignment = Alignment.Start,
            verticalArrangement = Arrangement.Center
        ) {
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                if (showSpeaker) {
                    SpeakerIconButton { isSpeakingCallback() }
                }
                SpellingTextWithNumber(
                    index = index,
                    word = word,
                    onItemReset = onItemReset,
                    onItemEdit = onItemEdit,
                    textModifier = Modifier.weight(1f),
                    isEnabled = isEnabled
                )
            }
            Row(
                modifier = modifier
                    .fillMaxWidth()
                    .padding(horizontal = dimensionResource(id = R.dimen.padding_extra_big)),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Start
            ) {
                SpellingRadioGroup(
                    selected = wordStatus,
                    setSelected = onItemSelected,
                    rbContentType = rbContentType,
                    isEnabled = isEnabled,
                )
            }
        }
    } else {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(
                    start = dimensionResource(id = R.dimen.padding_small),
                    end = dimensionResource(id = R.dimen.padding_medium)
                ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            if (showSpeaker) {
                SpeakerIconButton { isSpeakingCallback() }
            }
            SpellingTextWithNumber(
                index = index,
                word = word,
                onItemReset = onItemReset,
                onItemEdit = onItemEdit,
                textModifier = Modifier.weight(1f),
                isEnabled = isEnabled,
            )
            SpellingRadioGroup(
                selected = wordStatus,
                setSelected = onItemSelected,
                rbContentType = rbContentType,
                isEnabled = isEnabled,
            )
        }
    }
}

@Composable
private fun SpellingEditedItem(
    index: Int,
    word: String,
    onValueChange: (String) -> Unit,
    onEditCancelCallback: () -> Unit,
    onEditSubmitCallback: () -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = true,
    errorMsg: String? = null
) {
    val fontScale = LocalDensity.current.fontScale
    val ordinalNumberWidth = (28.sp * fontScale)
    Column(
        modifier = modifier
            .fillMaxWidth(),
        horizontalAlignment = Alignment.Start,
        verticalArrangement = Arrangement.Center
    ) {
        Row(
            modifier = modifier
                .fillMaxWidth()
                .padding(horizontal = dimensionResource(id = R.dimen.padding_medium)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center
        ) {
            //index
            Text(
                text = getIndexPrefix(index),
                modifier = Modifier
                    .width(ordinalNumberWidth.toDp())
            )
            //Word
            OutlinedTextField(
                value = word,
                enabled = isEditing,
                onValueChange = {
                    if (it.length <= MAX_WORD_LENGTH) {
                        onValueChange(it)
                    }
                },
                isError = errorMsg != null,
                modifier = Modifier.weight(1f)

            )
            //buttons
            OutlinedIconButton(
                onClick = { onEditSubmitCallback() },
                shape = MaterialTheme.shapes.small,
                enabled = isEditing
            ) {
                Icon(
                    imageVector = Icons.Outlined.Done,
                    contentDescription = stringResource(id = R.string.spelling_save_edited_word),
                    tint = sentence_correct,

                    )
            }
            OutlinedIconButton(
                onClick = { onEditCancelCallback() },
                shape = MaterialTheme.shapes.small,
                enabled = isEditing
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = stringResource(id = R.string.spelling_cancel_editing),
                    tint = sentence_incorrect
                )
            }
        }
        //ErrorMessage
        if (errorMsg != null) {
            ErrorText(errorMsg)
        }
    }
}


@Composable
fun SpellingTextWithNumber(
    index: Int,
    word: String,
    onItemReset: () -> Unit,
    onItemEdit: () -> Unit,
    textModifier: Modifier = Modifier,
    isEnabled: Boolean = true,
) {
    val alpha = if (isEnabled) 1f else 0.38f
    val fontScale = LocalDensity.current.fontScale
    val ordinalNumberWidth = (28.sp * fontScale)
    Text(
        text = getIndexPrefix(index),
        modifier = Modifier
            .width(ordinalNumberWidth.toDp())
            .alpha(alpha),
    )
    Text(
        text = word,
        modifier = textModifier
            .padding(end = dimensionResource(id = R.dimen.padding_small))
            .pointerInput(Unit) {
                if (isEnabled) {
                    detectTapGestures(
                        onDoubleTap = { onItemReset() },
                        onLongPress = { onItemEdit() }
                    )
                }
            }
            .alpha(alpha),
    )
}

private fun getIndexPrefix(index: Int): String {
    return "${index + 1}".padStart(1, ' ') + "."
}

@Composable
private fun getScores(words: List<SpellingWord>): String {
    val nrOfQuestions = words.filter { it.status != WordStatus.UNCHECKED }.size
    val nrOfCorrect = words.filter { it.status == WordStatus.CORRECT }.size
    val ratio = if (nrOfQuestions == 0) 0 else nrOfCorrect * 100 / nrOfQuestions
    return stringResource(
        R.string.spelling_result,
        nrOfCorrect,
        nrOfQuestions,
        ratio
    )
}


@Composable
private fun rememberTextToSpeech(): MutableState<TextToSpeech?> {
    val context = LocalContext.current
    val tts = remember { mutableStateOf<TextToSpeech?>(null) }
    DisposableEffect(context) {
        val textToSpeech = TextToSpeech(context) { status ->
            Log.i("Speaking", status.toString())
            if (status == TextToSpeech.SUCCESS) {
                tts.value?.language = Locale.UK
            }
        }
        tts.value = textToSpeech
        //cleaning up the textToSpeech -
        // it calls when the composable leaves the Composition (aka when user leaves SpellingContent)
        onDispose {
            textToSpeech.stop()
            textToSpeech.shutdown()
        }
    }
    return tts
}

@Composable
private fun SpeakerIconButton(
    onClickCallback: () -> Unit
) {
    IconButton(
        onClick = { onClickCallback() }
    ) {
        Icon(
            imageVector = Icons.Rounded.RecordVoiceOver,
            contentDescription = stringResource(id = R.string.spelling_text_to_speech)
        )
    }
}

private fun setSpeaker(
    word: String,
    textToSpeech: TextToSpeech? = null,
) {
    //simply add the word to the queue
    textToSpeech?.speak(word, TextToSpeech.QUEUE_ADD, null, null)
}


/*
@PreviewFontScale
@Composable
private fun SpellingItemSizePreview(
    @PreviewParameter(SpellingScreenSizeParameterProvider::class) screenSize: Pair<Int, Int>
) {
    val size = DpSize(
        screenSize.first.dp,
        screenSize.second.dp
    )

    val rbContentType =
        getRadioButtonType(
            HomeLearningWindowSizeClass.calculateFromSize(
                size
            ).widthClassType1
        )
    HomeLearningTheme {
        Scaffold(
            modifier = Modifier
                .width(screenSize.first.dp)
                .height(screenSize.second.dp)
        ) {
            Column {
                SpellingItem(
                    index = 17,
                    word = "anticlockwise",
                    wordStatus = WordStatus.CORRECT,
                    onItemSelected = {},
                    rbContentType = rbContentType,
                    onItemReset = {},
                    onItemEdit = {},
                    modifier = Modifier.padding(it),
                    showSpeaker = true,
                    isSpeakingCallback = {}
                )
            }
        }
    }
}

 */
/*
@PreviewLightDark
@Composable
private fun SpellingEditItem(
) {
    HomeLearningTheme {
        Scaffold {
            Column {
                SpellingEditedItem(
                    index = 17,
                    word = "anticlockwise",
                    onValueChange = {},
                    onEditCancelCallback = {},
                    onEditSubmitCallback = {},
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}


 */

@PreviewLightDark
@Composable
private fun SpellingItemEnablePreview(
    @PreviewParameter(BooleanPreviewProvider::class) isEnabled: Boolean
) {
    HomeLearningTheme {
        Surface {
            SpellingItem(
                index = 0,
                word = "successfully",
                wordStatus = WordStatus.CORRECT,
                onItemSelected = {},
                onItemReset = {},
                onItemEdit = {},
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                isEnabled = isEnabled,
                showSpeaker = true,
                isSpeakingCallback = {}
            )
        }
    }
}

/*
@PreviewLightDark
@Composable
private fun SpellingItemsPreview(
    @PreviewParameter(BooleanPreviewProvider::class) isEnabled: Boolean
) {
    val spelling1 = SpellingWord(
        word = "successfully",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    val spelling2 = SpellingWord(
        word = "behave",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    val spelling3 = SpellingWord(
        word = "anticlockwise",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.INCORRECT
    )
    val spelling4 = SpellingWord(
        word = "destructive",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.UNCHECKED
    )
    val spelling5 = SpellingWord(
        word = "this",
        category = "school",
        comment = "Y3Y4",
        status = WordStatus.CORRECT
    )
    val words = listOf(
        spelling1,
        spelling2,
        spelling3,
        spelling4,
        spelling5,
        spelling4,
        spelling4,
        spelling4,
        spelling4, spelling4,
        spelling4
    )
    HomeLearningTheme {
        Scaffold {
            SpellingItems(
                words = words,
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                modifier = Modifier.padding(it),
                onItemReset = {},
                isEnabled = isEnabled
            )
        }
    }
}*/