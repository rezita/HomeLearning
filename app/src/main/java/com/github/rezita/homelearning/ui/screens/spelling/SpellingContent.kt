package com.github.rezita.homelearning.ui.screens.spelling

import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.PreviewFontScale
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.DpSize
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
import com.github.rezita.homelearning.ui.size.HomeLearningWindowSizeClass
import com.github.rezita.homelearning.ui.theme.HomeLearningTheme
import com.github.rezita.homelearning.ui.theme.sentence_correct
import com.github.rezita.homelearning.ui.theme.sentence_incorrect
import com.github.rezita.homelearning.ui.viewmodels.MAX_WORD_LENGTH
import com.github.rezita.homelearning.utils.toDp
import kotlinx.coroutines.CoroutineScope

@Composable
fun SpellingContent(
    state: SpellingUiState,
    rbContentType: RadioButtonContentType,
    scope: CoroutineScope,
    snackBarHostState: SnackbarHostState,
    onUserEvent: (SpellingUserEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    when (state) {
        is SpellingUiState.Loading -> {
            LoadingProgressBar(modifier = modifier)
        }

        is SpellingUiState.Loaded -> {
            SpellingItems(
                words = state.words,
                rbContentType = rbContentType,
                onUserEvent = onUserEvent,
                modifier = modifier,
            )
        }

        is SpellingUiState.LoadingError -> {
            LoadingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayInColumn(
                message = stringResource(id = state.errorMessage),
                callback = { onUserEvent(SpellingUserEvent.OnLoad) },
                modifier = modifier
            )
        }

        is SpellingUiState.Saved -> {
            SavingSuccessSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            SpellingItems(
                words = state.words,
                rbContentType = rbContentType,
                onUserEvent = onUserEvent,
                modifier = modifier,
                isEnabled = false
            )
        }

        is SpellingUiState.SavingError -> {
            SavingErrorSnackbar(scope = scope, snackbarHostState = snackBarHostState)
            ErrorDisplayWithContent(
                message = stringResource(id = state.errorMessage),
                callback = { onUserEvent(SpellingUserEvent.OnSave) },
                content = {
                    SpellingItems(
                        words = state.words,
                        rbContentType = rbContentType,
                        onUserEvent = onUserEvent,
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
                onUserEvent = onUserEvent,
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
                onUserEvent = onUserEvent,
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
    onUserEvent: (SpellingUserEvent) -> Unit,
    modifier: Modifier = Modifier,
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
                    isRepeatedWord = item.repeated,
                    rbContentType = rbContentType,
                    onUserEvent = onUserEvent,
                    isEnabled = isEnabled,
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
    onUserEvent: (SpellingUserEvent) -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = true,
    errorMsg: String? = null
) {
    val listState = rememberLazyListState(editedIndex)

    Column(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Text(
            text = getScores(words),
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(id = R.dimen.padding_medium)),
            textAlign = TextAlign.Right,
        )

        LazyColumn(
            state = listState,
            modifier = Modifier.fillMaxSize()
        ) {
            itemsIndexed(words) { index, item ->
                if (index == editedIndex) {
                    SpellingEditedItem(
                        index = index,
                        word = modifiedWord,
                        onUserEvent = onUserEvent,
                        isEditing = isEditing,
                        errorMsg = errorMsg
                    )

                } else {
                    SpellingItem(
                        index = index,
                        word = item.word,
                        wordStatus = item.status,
                        isRepeatedWord = item.repeated,
                        rbContentType = rbContentType,
                        onUserEvent = onUserEvent,
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
    isRepeatedWord: Boolean,
    rbContentType: RadioButtonContentType,
    onUserEvent: (SpellingUserEvent) -> Unit,
    modifier: Modifier = Modifier,
    isEnabled: Boolean = true,
    showSpeaker: Boolean = true,
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
                    SpeakerIconButton { onUserEvent(SpellingUserEvent.OnSpeakerClicked(word)) }
                }
                SpellingTextWithNumber(
                    index = index,
                    word = word,
                    isRepeated = isRepeatedWord,
                    onUserEvent = onUserEvent,
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
                    setSelected = { status ->
                        onUserEvent(
                            SpellingUserEvent.OnItemStatusChange(
                                index,
                                status
                            )
                        )
                    },
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
                SpeakerIconButton { onUserEvent(SpellingUserEvent.OnSpeakerClicked(word)) }
            }
            SpellingTextWithNumber(
                index = index,
                word = word,
                isRepeated = isRepeatedWord,
                onUserEvent = onUserEvent,
                textModifier = Modifier.weight(1f),
                isEnabled = isEnabled,
            )
            SpellingRadioGroup(
                selected = wordStatus,
                setSelected = { status ->
                    onUserEvent(
                        SpellingUserEvent.OnItemStatusChange(
                            index,
                            status
                        )
                    )
                },
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
    onUserEvent: (SpellingUserEvent) -> Unit,
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
            AutoFocusingSpellingText(
                word = word,
                isEditing = isEditing,
                onValueChange = { value -> onUserEvent(SpellingUserEvent.OnEditItemChange(value)) },
                errorMsg = errorMsg,
                modifier = Modifier.weight(1f)

            )
            //buttons
            OutlinedIconButton(
                onClick = { onUserEvent(SpellingUserEvent.OnSaveEditing) },
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
                onClick = { onUserEvent(SpellingUserEvent.OnDiscardEditing) },
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
fun AutoFocusingSpellingText(
    word: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    isEditing: Boolean = true,
    errorMsg: String? = null
) {
    //var value by mutableStateOf("Enter Text")
    val focusRequester = remember { FocusRequester() }
    OutlinedTextField(
        value = word,
        enabled = isEditing,
        onValueChange = {
            if (it.length <= MAX_WORD_LENGTH) {
                onValueChange(it)
            }
        },
        isError = errorMsg != null,
        // modifier = modifier.weight(1f)
        modifier = modifier.focusRequester(focusRequester)
    )

    LaunchedEffect(Unit) {
        focusRequester.requestFocus()
    }
}


@Composable
fun SpellingTextWithNumber(
    index: Int,
    word: String,
    isRepeated: Boolean,
    onUserEvent: (SpellingUserEvent) -> Unit,
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
                        onDoubleTap = { onUserEvent(SpellingUserEvent.OnValueReset(index)) },
                        onLongPress = { onUserEvent(SpellingUserEvent.OnPrepareForEditing(index)) }
                    )
                }
            }
            .alpha(alpha),
        color = if (isRepeated) MaterialTheme.colorScheme.primary else Color.Unspecified
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
                    isRepeatedWord = true,
                    rbContentType = rbContentType,
                    onUserEvent = {},
                    modifier = Modifier.padding(it),
                    showSpeaker = true
                )
            }
        }
    }
}

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
                    onUserEvent = {},
                    modifier = Modifier.padding(it)
                )
            }
        }
    }
}

@PreviewLightDark
@Composable
private fun SpellingItemRepeatedPreview(
    @PreviewParameter(BooleanPreviewProvider::class) isEnabled: Boolean
) {
    HomeLearningTheme {
        Surface {
            SpellingItem(
                index = 0,
                word = "successfully",
                wordStatus = WordStatus.CORRECT,
                isRepeatedWord = true,
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                onUserEvent = {},
                isEnabled = isEnabled,
                showSpeaker = true,
            )
        }
    }
}


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
                isRepeatedWord = false,
                rbContentType = RadioButtonContentType.BUTTONS_AND_LONG,
                onUserEvent = {},
                isEnabled = isEnabled,
                showSpeaker = true,
            )
        }
    }
}


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
                onUserEvent = {},
                modifier = Modifier.padding(it),
                isEnabled = isEnabled
            )
        }
    }
}