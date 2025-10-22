/******INSERT (LIST OF) SPANISH WORDS **** */
function insertSpanishWords(words, sheetName, logSheetName) {
  //const words = JSON.parse(e.parameter.words);
  Logger.log(words);
  response = "";
  //Logger.log(words);
  words.forEach(function (word) {
    response += insertSpanishWord(word, sheetName, logSheetName) + ", ";
  })
  return response;
}

function insertSpanishWord(word, sheetName, logSheetName) {
  //empty or null en (English) or sp (Spanish) values
  if (word.en == null || word.en == "") {
    return `${responseMessages.insertFailed}: ${responseMessages.enWordMissing}`;
  }

  if (word.sp == null || word.sp == "") {
    return `${responseMessages.insertFailed}: ${responseMessages.spWordMissing}`;
  }
  
  const dataSheet = getDataSheet(sheetName);
  const indexEn = getIndexForValue(dataSheet, word.en, 'B2:B');
  const indexSp = getIndexForValue(dataSheet, word.es, 'C2:C');

  //if new word
  if (indexEn == -1 && indexSp == -1) {
    //en, sp, comment, isWeekWord, repeat, attempt, inCorrect
    dataSheet.appendRow(["", word.en, word.sp, word.comment, 0, 0, 0, 0]);
    insertLog(logSheetName, spanishLogAction.insertSpanishWord, word);
    return `${word.en}:${responseMessages.success}`;
  } else {
    //it the word (the English or Spanish version) is exists on the sheet:
    //return the exists response
    return `${word.en}:${responseMessages.wordAlreadyExist}`;
  }
}

/******RESET WEEK OF SPANISH WORDS **** */
function resetWeekSpanisWords(sheetName, logSheetName) {
  const dataSheet = getDataSheet(sheetName);
  const lastRow = dataSheet.getLastRow();

  if (lastRow > 1) { // make sure there are rows
    dataSheet.getRange(2, spanishIdxs.isWeekWord[0] + 1, lastRow - 1).setValue(0);
  }

  insertLog(logSheetName, "Update spanish words", spanishLogAction.removeWeekTags);
  return `${responseMessages.success}`;
}


/******SET (LIST OF) WEEK OF SPANISH WORDS **** */
function setWeekSpanishWords(words, sheetName, logSheetName) {
  resetWeekSpanisWords(sheetName, logSheetName)
  const dataSheet = getDataSheet(sheetName);
  words.forEach(function (word) {
    const indexEn = getIndexForValue(dataSheet, word);
    const indexSp = getIndexForValue(dataSheet, word, 'B2:B');

    const index = (indexEn == -1) ? indexSp : indexEn;
    if (index == -1) {
      return `${responseMessages.updateFailed}: ${responseMessages.mordMissing}`;
    }

    dataSheet.getRange(index, spanishIdxs.isWeekWord[0] + 1).setValue(1);
    insertLog(logSheetName, spanishLogAction.setWeekTag, word);
    return `${word.word}:${responseMessages.wordAlreadyExist}`;
  })
}


/***** MODIFY SPANISH WORD - EN / SP VALUE **** */
function modifySpanishWord(sheetName, logSheetName, oldVersion, newVersion) {
  if (oldVersion == null || oldVersion == "") {
    return `${responseMessages.modifyWordFailed}: ${responseMessages.wordMissing}`;
  }

  if (newVersion == null || newVersion == "") {
    return `${responseMessages.modifyWordFailed}: ${responseMessages.wordMissing}`;
  }

  const dataSheet = getDataSheet(sheetName);

  const indexSp = getIndexForValue(dataSheet, oldVersion, 'B2:B');
  //modify Spanish version
  if (indexSp != -1) {
    const response = modifySpanishWord(dataSheet, indexSp, newVersion, 'sp');
    insertLog(logSheetName, spanishLogAction.modifySpanishWord, { oldWord: oldVersion, newWord: newVersion });
    return response
  } else {
    //modify english version
    const indexEn = getIndexForValue(dataSheet, oldVersion);
    if (indexEn != -1) {
      const response = modifySpanishWord(dataSheet, indexEn, newVersion, 'en');
      insertLog(logSheetName, spanishLogAction.insertSpanishWord, { oldWord: oldVersion, newWord: newVersion });
      return response
    } else {
      //the oldWord wasn't in the sheet
      return `${responseMessages.modifyWordFailed}: ${responseMessages.wordNotFound}`;
    }
  }
}

function modifySpanishWord(dataSheet, indexOfWord, word, version = 'sp') {
  // en - sp - isWeekWord - repeat - attempt -- nrOfIncorrect

  const indexOfNewWord = (version == 'sp') ? getIndexForValue(dataSheet, word, 'B2:B') : getIndexForValue(dataSheet, word);
  const colValue = (version == 'sp') ? spanishIdxs.sp[0] + 1 : spanishIdxs.en[0] + 1

  var row = dataSheet.getRange(indexOfWord, 1, 1, 6);
  const rowValues = row.getValues()[0];

  //if the new word has not existed yet
  if (indexOfNewWord == -1 || indexOfNewWord == indexOfWord) {
    //update the word
    dataSheet.getRange(indexOfWord, colValue).setValue([word]);
    return `${word}:${responseMessages.success}`;
  } else {
    //The new word is already in the sheet -> merge the results (repeat, attmept, nrOfIncorrect)
    //notify that the new word is already exists - can't put in (every word can be only once)
    return `${word}:${responseMessages.wordAlreadyExist}`;
  }
}

/***** UPDATE SPANISH WORDS - RESULTS **** */
function updateSpanishWords(words, sheetName, logSheetName) {
  //const words = JSON.parse(e.parameter.words);
  response = "";
  words.forEach(function (word) {
    let modifySuccess = updateSpanishWord(sheetName, word.en, word.result);
    response += `${word.en} ${modifySuccess}, `
    insertSpanishUpdateLog(logSheetName, word, modifySuccess);
  });
  return response;
}

function insertSpanishUpdateLog(logSheetName, updatedWord, responseMessage) {
  const clogAction = (responseMessage == responseMessages.success) ? spanishLogAction.updateSpanishWord : spanishLogAction.wordNotFound;
  insertLog(logSheetName, clogAction, updatedWord);
}

function updateSpanishWord(sheetName, word, result) {
  if (word == null) {
    return `${responseMessages.updateFailed} ${responseMessages.wordMissing}`;
  }
  if (result == null) {
    return `${responseMessages.updateFailed} ${responseMessages.resultMissing}`;
  }

  const dataSheet = getDataSheet(sheetName);
  const indexOfWord = getIndexForValue(dataSheet, word);
  if (indexOfWord != -1) {
    updateSpanishgWordValues(dataSheet, indexOfWord, result);
    return `${responseMessages.success}`
  } else {
    return `${responseMessages.wordNotFound}`;
  }
}

function updateSpanishgWordValues(dataSheet, indexOfWord, result) {
  const incorrectIncrement = getRepeatIncorrectValue(result); //if correct = 0, if incorrect = 1 -> same for the repeat
  const attemptMod = 1;
 // modifySpanishWordValues(dataSheet, indexOfWord, 1, incorrectIncrement, incorrectIncrement)
  // en - sp - isWeekWord  - repeat - attempt -- nrOfIncorrect
  
  var row = dataSheet.getRange(indexOfWord, 1, 1, 6);
  const rowValues = row.getValues()[0];
  //get AttemptValue
  const attempt = getValueAccordingTypeRule(rowValues[spanishIdxs.attempt[0]], spanishIdxs.attempt[1]);
  //new AttemptValue
  dataSheet.getRange(indexOfWord, spanishIdxs.attempt[0] + 1).setValue([attempt + attemptMod]);

  //get NrOfIncorrectValue
  const nrOfIncorrect = getValueAccordingTypeRule(rowValues[spanishIdxs.nrOfIncorrect[0]], spanishIdxs.nrOfIncorrect[1]);
  //new NrOfIncorrectValue
  dataSheet.getRange(indexOfWord, spanishIdxs.nrOfIncorrect[0] + 1).setValue([nrOfIncorrect + incorrectIncrement]);

  //repeat 
  dataSheet.getRange(indexOfWord, spanishIdxs.repeat[0] + 1).setValue(incorrectIncrement);
}


