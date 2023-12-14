/******INSERT (LIST OF) SPELLING WORDS **** */
function insertSpellingWords(e, sheetName, logSheetName){
  const words = JSON.parse(e.parameter.words);
  response = "";
  words.forEach(function(word){
    response += insertSpellingWord(word, sheetName, logSheetName) + ", ";
  })
  return response;
}

function insertSpellingWord(word, sheetName, logSheetName){
  if (word.word == null || word.word == "") {
    return `${responseMessages.insertFailed}: ${responseMessages.wordMissing}`;
  }
  if (word.category == null || word.category == "") {
    return `${responseMessages.insertFailed}: ${responseMessages.categoryMissing}`;
  }

  const dataSheet = getDataSheet(sheetName);
  
  const index = getIndexForValue(dataSheet, word.word);
  if(index == -1) {
      dataSheet.appendRow([word.word, word.category, word.comment, 0, 0, 0]);
      insertLog(logSheetName, logAction.insertSpellingWord, word);
      return `${word.word}:${responseMessages.success}`;
    }
  return `${word.word}:${responseMessages.wordAlreadyExist}`;
}

/******UPDATE (LIST OF) SPELLING WORDS **** */
function updateSpellingWords(e, sheetName, logSheetName){
  const words = JSON.parse(e.parameter.words);
  response = "";
  words.forEach(function(word){
    let modifySuccess = modifySpellingWord(sheetName, word.word, word.result);
    //word.category = getCategoryForWord(word.word);
    response += `${word} ${modifySuccess}, `
    insertUpdateLog(logSheetName, word, modifySuccess);
  });
  return response;
}


function insertUpdateLog(logSheetName, updatedWord, responseMessage){
  const clogAction = (responseMessage == responseMessages.success) ? logAction.updateSpellingWord : logAction.wordNotFound;
  insertLog(logSheetName, clogAction, updatedWord);
}

function modifySpellingWord(sheetName, word, result){  
  if (word == null) {
    return `${responseMessages.updateFailed} ${responseMessages.wordMissing}` 
  }
  if (result == null) {
    return `${responseMessages.updateFailed} ${responseMessages.resultMissing}` 
  }

  const dataSheet = getDataSheet(sheetName);
  const indexOfWord = getIndexForValue(dataSheet, word);
  if (indexOfWord != -1) {
    modifySpellingWordValues(dataSheet, indexOfWord, result);
    return `${responseMessages.success}`
  } else {
    return `${responseMessages.wordNotFound}`
  }
}


function modifySpellingWordValues(dataSheet, indexOfWord, result){
  // words - category - comment  - repeat - attempt -- nrOfIncorrect
  var row = dataSheet.getRange(indexOfWord, 1, 1, 6);
  const rowValues = row.getValues()[0]; 

  
  //attempt value -> increase by 1
  const attempt = getValueAccordingTypeRule(rowValues[spellingIdxs.attempt[0]], spellingIdxs.attempt[1]);
  dataSheet.getRange(indexOfWord, spellingIdxs.attempt[0] + 1).setValue([attempt + 1]);

  const resultIncrement = getRepeatIncorrectValue(result);

  //nrOfIncorrect -> increase by 1 if incorrect, else by 0
  const nrOfIncorrect = getValueAccordingTypeRule(rowValues[spellingIdxs.nrOfIncorrect[0]], spellingIdxs.nrOfIncorrect[1]);
  dataSheet.getRange(indexOfWord, spellingIdxs.nrOfIncorrect[0] + 1).setValue([nrOfIncorrect + resultIncrement]);

  //repeat -> set 1 if incorrect, otherwise 0
  dataSheet.getRange(indexOfWord, spellingIdxs.repeat[0] + 1).setValue(resultIncrement);
}

/**
function modifyOrInsertSpellingWord(word, result, category){
  if (word == null) {
    return `${responseMessages.updateFailed} ${responseMessages.wordMissing}` 
  }
  if (result == null) {
    return `${responseMessages.updateFailed} ${responseMessages.resultMissing}` 
  }

  const dataSheet = getDataSheet(sheets.spelling);
  const indexOfWord = getIndexForValue(dataSheet, word);
  if (indexOfWord != -1) {
    modifySpellingWordValues(dataSheet, indexOfWord, result);
    return `${responseMessages.success}`
  } else {
    dataSheet.appendRow([word, category, "", 0, 0, 0]);
    const indexOfNewWord = getIndexForValue(dataSheet, word);
    modifySpellingWordValues(dataSheet, indexOfNewWord, result);
    return `${responseMessages.success}`
  }
}
*/

function insertSpellingWordFromLog(word){
  const dataSheet = getDataSheet(sheets.spellingErik);
  
  const index = getIndexForValue(dataSheet, word.word);
  Logger.log(index);
  if(index == -1) {
      dataSheet.appendRow([word.word, word.category, word.comment, 0, 0, 0]);
      return `${word.word}:${responseMessages.success}`;
    }
  return `${word.word}:${responseMessages.wordAlreadyExist}`;
}
