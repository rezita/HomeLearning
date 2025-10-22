/******INSERT (LIST OF) SPELLING WORDS **** */
function insertSpellingWords(words, sheetName, logSheetName) {
  //const words = JSON.parse(e.parameter.words);
  response = "";
  //Logger.log(words);
  words.forEach(function (word) {
    response += insertSpellingWord(word, sheetName, logSheetName) + ", ";
  })
  return response;
}

function insertSpellingWord(word, sheetName, logSheetName) {
  if (word.word == null || word.word == "") {
    return `${responseMessages.insertFailed}: ${responseMessages.wordMissing}`;
  }
  if (word.category == null || word.category == "") {
    return `${responseMessages.insertFailed}: ${responseMessages.categoryMissing}`;
  }

  const dataSheet = getDataSheet(sheetName);

  const index = getIndexForValue(dataSheet, word.word);
  if (index == -1) {
    dataSheet.appendRow([word.word, word.category, word.comment, 0, 0, 0]);
    insertLog(logSheetName, logAction.insertSpellingWord, word);
    return `${word.word}:${responseMessages.success}`;
  } else {
    //it the word is exists on the sheet:
    //set the repeat to 1, write into the log and return the exists response
    dataSheet.getRange(index, spellingIdxs.repeat[0] + 1).setValue(1);
    insertLog(logSheetName, logAction.repeatSpellingWord, word.word);
    return `${word.word}:${responseMessages.wordAlreadyExist}`;

  }
}

/******UPDATE (LIST OF) SPELLING WORDS **** */
function updateSpellingWords(words, sheetName, logSheetName) {
  //const words = JSON.parse(e.parameter.words);
  response = "";
  words.forEach(function (word) {
    let modifySuccess = updateSpellingWord(sheetName, word.word, word.result);
    response += `${word.word} ${modifySuccess}, `
    insertUpdateLog(logSheetName, word, modifySuccess);
  });
  return response;
}


function insertUpdateLog(logSheetName, updatedWord, responseMessage) {
  const clogAction = (responseMessage == responseMessages.success) ? logAction.updateSpellingWord : logAction.wordNotFound;
  insertLog(logSheetName, clogAction, updatedWord);
}

function updateSpellingWord(sheetName, word, result) {
  if (word == null) {
    return `${responseMessages.updateFailed} ${responseMessages.wordMissing}`;
  }
  if (result == null) {
    return `${responseMessages.updateFailed} ${responseMessages.resultMissing}`;
  }

  const dataSheet = getDataSheet(sheetName);
  const indexOfWord = getIndexForValue(dataSheet, word);
  if (indexOfWord != -1) {
    updateSpellingWordValues(dataSheet, indexOfWord, result);
    return `${responseMessages.success}`
  } else {
    return `${responseMessages.wordNotFound}`;
  }
}


function updateSpellingWordValues(dataSheet, indexOfWord, result) {
  const resultIncrement = getRepeatIncorrectValue(result);
  modifySpellingWordValues(dataSheet, indexOfWord, 1, resultIncrement, resultIncrement, false)
}

function modifySpellingWordValues(dataSheet, indexOfWord, attemptMod, nrOfIncorrMod, repeatMod, isMerge) {
  // words - category - comment  - repeat - attempt -- nrOfIncorrect
  var row = dataSheet.getRange(indexOfWord, 1, 1, 6);
  const rowValues = row.getValues()[0];

  //get AttemptValue
  const attempt = getValueAccordingTypeRule(rowValues[spellingIdxs.attempt[0]], spellingIdxs.attempt[1]);
  //new AttemptValue
  dataSheet.getRange(indexOfWord, spellingIdxs.attempt[0] + 1).setValue([attempt + attemptMod]);

  //get NrOfIncorrectValue
  const nrOfIncorrect = getValueAccordingTypeRule(rowValues[spellingIdxs.nrOfIncorrect[0]], spellingIdxs.nrOfIncorrect[1]);
  //new NrOfIncorrectValue
  dataSheet.getRange(indexOfWord, spellingIdxs.nrOfIncorrect[0] + 1).setValue([nrOfIncorrect + nrOfIncorrMod]);

  var repeatNewValue = repeatMod;
  //get repeatValue
  if (isMerge) {
    const repeatOld = getValueAccordingTypeRule(rowValues[spellingIdxs.repeat[0]], spellingIdxs.repeat[1]);
    repeatNewValue = (repeatMod + repeatOld == 0) ? 0 : 1;
  }
  dataSheet.getRange(indexOfWord, spellingIdxs.repeat[0] + 1).setValue(repeatNewValue);
}

function insertSpellingWordFromLog(word) {
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word.word);
  Logger.log(index);
  if (index == -1) {
    dataSheet.appendRow([word.word, word.category, word.comment, 0, 0, 0]);
    return `${word.word}:${responseMessages.success}`;
  }
  return `${word.word}:${responseMessages.wordAlreadyExist}`;
}


/****** MODIFY SPELLING WORD *****/
function modifySpellingWord(sheetName, logSheetName, oldVersion, newVersion) {
  if (oldVersion == null || oldVersion == "") {
    return `${responseMessages.modifyWordFailed}: ${responseMessages.wordMissing}`;
  }

  const dataSheet = getDataSheet(sheetName);

  const index = getIndexForValue(dataSheet, oldVersion);

  if (index == -1) {
    return `${responseMessages.modifyWordFailed}: ${responseMessages.wordNotFound}`;
  }
  else {
    //modify word
    const response = modifySpellingWordWord(dataSheet, index, newVersion);
    insertLog(logSheetName, logAction.modifySpellingWord, { oldWord: oldVersion, newWord: newVersion });
    return response
  }
}

function modifySpellingWordWord(dataSheet, indexOfWord, word) {
  // words - category - comment  - repeat - attempt -- nrOfIncorrect
  const indexOfNewWord = getIndexForValue(dataSheet, word);

  var row = dataSheet.getRange(indexOfWord, 1, 1, 6);
  const rowValues = row.getValues()[0];


  //if the new word has not existed yet
  if (indexOfNewWord == -1 || indexOfNewWord == indexOfWord) {
    //update the word's word
    dataSheet.getRange(indexOfWord, spellingIdxs.word[0] + 1).setValue([word]);
    return `${word}:${responseMessages.success}`;
  } else {
    //The word is already in the sheet -> merge the results (repeat, attmept, nrOfIncorrect)
    const attempt = getValueAccordingTypeRule(rowValues[spellingIdxs.attempt[0]], spellingIdxs.attempt[1]);
    const nrOfIncorrect = getValueAccordingTypeRule(rowValues[spellingIdxs.nrOfIncorrect[0]], spellingIdxs.nrOfIncorrect[1]);
    const repeat = getValueAccordingTypeRule(rowValues[spellingIdxs.repeat[0]], spellingIdxs.repeat[1]);
    modifySpellingWordValues(dataSheet, indexOfNewWord, attempt, nrOfIncorrect, repeat, true);
    //delete the old word from the sheet
    dataSheet.deleteRow(indexOfWord);
    return `${word}:${responseMessages.success}`;
  }
}