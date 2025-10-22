/******UPDATE (LIST OF) SENTENCES (IRREGULAR VERBS and HOMOPHONES) **** */
function updateSentences(sentences, sheetName, logSheetName){
  //const sentences = JSON.parse(e.parameter.sentences);
  response = "";
  sentences.forEach(function(sentence){
    let modifySuccess = modifySentence(sheetName, sentence.sentence, sentence.result);
    response += `${sentence.sentence} ${modifySuccess}, `
    insertUpdateSentenceLog(logSheetName, sentence, modifySuccess);
  });
  return response;
}


function modifySentence(sheetName, sentence, result){  
  if (sentence == null) {
    return `${responseMessages.updateFailed} ${responseMessages.sentenceMissing}` 
  }
  if (result == null) {
    return `${responseMessages.updateFailed} ${responseMessages.resultMissing}` 
  }

  const dataSheet = getDataSheet(sheetName);
  const indexOfSentence = getIndexForValue(dataSheet, sentence, getColumnRangeFromIndex(sentencesIdx.sentence[0]));

  if (indexOfSentence != -1) {
    Logger.log(`Index ${indexOfSentence}`);
    modifySentenceValues(dataSheet, indexOfSentence, result);
    return `${responseMessages.success}`
  } else {
    return `${responseMessages.sentenceNotFound}`
  }
}


function modifySentenceValues(dataSheet, indexOfSentence, result){
  // words - category - comment  - repeat - attempt -- nrOfIncorrect
  var row = dataSheet.getRange(indexOfSentence, 1, 1, 6);
  const rowValues = row.getValues()[0]; 
  
  //attempt value -> increase by 1
  const attempt = getValueAccordingTypeRule(rowValues[sentencesIdx.attempt[0]], sentencesIdx.attempt[1]);
  Logger.log(`Attempt: ${attempt}`);
  dataSheet.getRange(indexOfSentence, sentencesIdx.attempt[0] + 1).setValue([attempt + 1]);

  if (result == inCorrectResult) {
    const resultIncrement = getRepeatIncorrectValue(result);

    //nrOfIncorrect -> increase by 1 if incorrect, else by 0
    const nrOfIncorrect = getValueAccordingTypeRule(rowValues[sentencesIdx.nrOfIncorrect[0]], sentencesIdx.nrOfIncorrect[1]);
        Logger.log(`Incorrect ${nrOfIncorrect}`);
    dataSheet.getRange(indexOfSentence, sentencesIdx.nrOfIncorrect[0] + 1).setValue([nrOfIncorrect + resultIncrement]);
  }
}


function insertUpdateSentenceLog(logSheetName, updatedSentence, responseMessage){
  const clogAction = (responseMessage == responseMessages.success) ? logAction.updateSentence : logAction.sentenceNotFound;
  insertLog(logSheetName, clogAction, updatedSentence);
}
