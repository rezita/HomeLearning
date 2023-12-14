const logAction = {
      insertSpellingWord: "Insert Spelling word", 
      updateSpellingWord: "Update spelling word", 
      wordNotFound: "Spelling word not found", 
      recreateFromLogs: "Recreate From Logs",
      updateSentence: "Update sentence", 
      sentenceNotFound: "Sentence not found", 
      }

const logSpellingResult = {correct: 1, incorrect: -1, unchecked: 0}

/*
function insertLog(logSheetName, args){
  const loggerSheet = getOrCreateDataSheet(logSheetName)
  if (loggerSheet== null){
    spreadSheetID.instertSheet()
  }
  const content = [getFormattedDate(), userName].concat(args)
  loggerSheet.appendRow(content);
}
*/

function insertLog(logSheetName, logAction, logObject){
  const loggerSheet = getOrCreateDataSheet(logSheetName)
  if (loggerSheet== null){
    spreadSheetID.instertSheet()
  }
  const content = [getFormattedDate(), userName, logAction, JSON.stringify(logObject)]
  loggerSheet.appendRow(content);
}


function getSpellingResultFromLog(logResult){
  return logSpellingResult[logResult];
}

function getLogResultForUpdateResult(result){
  for (let [key, value] of Object.entries(logSpellingResult)) {
    if (value === result)
      return key;
  }
  return getLogResultForUpdateResult(0);
}




//**RESTORE SPELLING FROM LOGS */
function restoreSpellingFromLogs(){
  //reset all values (attempt, repeat, nrOfIncorrect)
  const spellingDataSheet = getDataSheet(sheets.spellingErik);
  resetAllValues(spellingDataSheet);
  insertSpellingHeader(spellingDataSheet);
  
  processLogs();
  insertLog(sheets.spellingErik_logs, logAction.recreateFromLogs, responseMessages.success);
/*
  //update values (attempt, repeat, nrOfIncorrect)
  let unsuccessUpdate = updateLogWordValues(logs);

  if (unsuccessUpdate.length == 0) {
    insertLog(sheets.spelling_logs, [logAction.recreateFromLogs, responseMessages.success]);
    return `${responseMessages.success}`
  }else {
    insertLog(sheets.spelling_logs, [logAction.recreateFromLogs, unsuccessUpdate]);
    return `${responseMessages.updateFailed}`
  }
  */
}

function resetAllValues(dataSheet){
  //clears all data
  dataSheet.clear();
}

function insertSpellingHeader(dataSheet){
  dataSheet.appendRow(["word",	"category",	"comment",	"repeat",	"attempt",	"inCorrect"]);
}

function processLogs(){
  let result = [];
  let i = 0;
  const logDataSheet = getDataSheet(sheets.spellingErik_logs);
  const logs = getAllDataWoHeader(logDataSheet);
  logs.forEach(function(log){
    if(log[2] == logAction.insertSpellingWord) {
      //Logger.log(log[3]);
      let jsonObj = JSON.parse(log[3]);
      let response = insertSpellingWordFromLog(jsonObj);
      //Logger.log(response);

    } else if(log[2] == logAction.updateSpellingWord) {
      i++;
      let jsonObj = JSON.parse(log[3]);
      let response = modifySpellingWord(jsonObj.word, jsonObj.result);
      Logger.log(`${i}, ${jsonObj.word}`);
    }
  });
  return result;
}


/**
function updateLogWordValues(logs){
  let result = []
  logs.forEach(function(log){
    let modifySuccess = modifyOrInsertSpellingWord(log[3], getSpellingResultFromLog(log[4]), log[5]);
    if (modifySuccess != responseMessages.success) {
      result.concat([log[3], log[4]]);
    }
  });
  return result; 
}
*/



/**
function logWrapper(func){
  if(typeof(func)==='function'){
       
       function loggingFunc(args){              
           func(args);
       }

       return loggingFunc
   }
   else{
       return func
   }  
}

function getLogContent(args){
  return `${args} inserted.`
}
*/

/** DO NOT USE!!!!! - use only for convert old logs to new format!!!!*/
function createInitialLog(logSheetName){
  insertLogHeader(logSheetName);

  const dataSheet = getDataSheet(sheets.spellingErik);
  const rows = getAllDataWoHeader(dataSheet);
  const wordsMap = convertArrayToMap(rows, spellingIdxs);
  wordsMap.forEach(row => insertIntoLog(logSheetName, row))
}

function insertLogHeader(logSheetName){
  const logDataSheet =  getDataSheet(logSheetName);
  logDataSheet.appendRow(["date",	"user",	"action",	"parameter"]);
}

function insertIntoLog(logDataSheetName, word){
  insertLog(logDataSheetName, logAction.insertSpellingWord, word);
}

function convertUpdateWordLogs(sourceSheetName, destinationSheetName){
  const usefulLogs = getUsefulLogs(sourceSheetName, logAction.updateSpellingWord);
  usefulLogs.forEach(row => convertAndInsertLog(destinationSheetName, row))
}

function getUsefulLogs(logSheetName, logAction) {
  const logs = getAllDataWoHeader(getDataSheet(logSheetName));
  const usefulLogs = logs.filter(log => log[2] == logAction);
  return usefulLogs;  
}

function convertAndInsertLog(destinationSheetName, row){
  const category = getAttrForWord(row[3], spellingIdxs.category[0]);
  const comment = getAttrForWord(row[3], spellingIdxs.comment[0]);
  const word = {word: row[3], category: category, comment: comment, result: getSpellingResultFromLog(row[4])};
  insertLog(destinationSheetName, logAction.updateSpellingWord, word);
}

function getAttrForWord(word, attrIndex){
  result = "";
  const indexOfWord = getIndexForValue(dataSheet, word);
  if (indexOfWord != -1) {
    const row = dataSheet.getRange(indexOfWord, 1, 1, 6);
    const rowValues = row.getValues()[0];
    result = rowValues[attrIndex];
  } 
  return result;
}

