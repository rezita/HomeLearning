function getFormattedDate(){
  const date =new Date(Date.now());
  return date.getFullYear() + "-" + (date.getMonth()+1) + "-" + date.getDate() + " " + date.getHours()+":"+date.getMinutes()+ ":"+date.getSeconds()+":"+date.getMilliseconds();
}


function getErikSpellingCategories(){
  //return erikSpellingCategoryRules.map(m => m[0]);
  return erikSpellingCategories;
}

function getMarkSpellingCategories(){
  //return markSpellingCategoryRules.map(m => m[0]);
  return markSpellingCategories;
}


function getDataSheet(sheetName){
  /*parameters: 
    - required: spreadSheetId, sheetName
  */
  
  const ss = SpreadsheetApp.openById(spreadSheetID);
  return ss.getSheetByName(sheetName);
}

function getOrCreateDataSheet(sheetName){
  /*parameters: 
    - required: spreadSheetId, sheetName
  */
  const ss = SpreadsheetApp.openById(spreadSheetID);
  let sheet = ss.getSheetByName(sheetName);
  if (sheet == null){
    ss.insertSheet(sheetName);
    sheet = ss.getSheetByName(sheetName);
  }
  return sheet;
}

function setUserName(name){
    /*parameters: 
    - optional: userName or null
  */
    name? userName = name: userName = "";
}

function getAllDataWoHeader(dataSheet){
  //getRange(topRow,LeftColumn, numRows, numColumns) 
  return dataSheet.getRange(2,1, dataSheet.getLastRow() - 1, dataSheet.getLastColumn()).getValues();
}

function getRandomNumber(minNr, maxNr) {
    //returns a random number between the given numbers
    return Math.floor(Math.random() * (maxNr - minNr + 1) + minNr);
}

function createJSONResponse(response){
  return ContentService.createTextOutput(JSON.stringify(response)).setMimeType(ContentService.MimeType.JSON);
}

function createTextResponse(message){
  return ContentService.createTextOutput(message).setMimeType(ContentService.MimeType.TEXT);
}

function getIndexForValue(dataSheet, value){
  const range = dataSheet.getRange('A2:A').getDisplayValues().map(r => r[0]);
  const index = range.indexOf(value);
  //if the word not in reange -> rerun -1, else index+2 (range starts with the 2nd row)
  return (index == -1) ? index : index + 2;
}

/*
function resetCol(nrOfRowWontChange, sheetName, colNr){
  const spellingSheet = getDataSheet(sheetName);
  const rowsWillChange = spellingSheet.getLastRow() - nrOfRowWontChange; 
  //repeatCol
  spellingSheet.getRange(1 + nrOfRowWontChange, colNr, rowsWillChange).setValue(0);
}
*/

function calculateWeight(nrOfAattempts, nrOfIncorrect){
  const random =  (Math.random() * (nrOfIncorrect + 1)) / (nrOfAattempts + 1);
  return random
}

function findMinWeightIndex(weightedWords, weightIndex){
  let minIndex = 0;
  for (let i = 1; i < weightedWords.length; i++){
    if (weightedWords[i][weightIndex] < weightedWords[minIndex][weightIndex]){
      minIndex = i;
    }
  } 
 return minIndex;
}

function convertArrayToMap(dataArray, colNames){
  const result = [];
  dataArray.forEach(function(row) {
    const currentData = {};
    Object.entries(colNames).forEach((entry) => currentData[entry[0]] = getValueAccordingTypeRule(row[entry[1][0]], entry[1][1]));
    result.push(currentData);
  });
  return result;
}

function getValueAccordingTypeRule(value, colTypeValue) {
  if (colTypeValue == isNumber) {
    return (!isNaN(value)) ? value : 0;
  } 
  return value;
}

//using for updateSpellinWords and updateIrregularVerbs
function getRepeatIncorrectValue(result){
  return (result == correctResult) ? 0 : 1;
}

function getAttrForWord(word){
  const dataSheet = getDataSheet(sheets.spellingErik);
  const indexOfWord = getIndexForValue(dataSheet,  word);
  if (indexOfWord != -1) {
    return dataSheet.getRange(indexOfWord, spellingIdxs.category[0] + 1).getValue();
  }
  return "";
}

function shuffleArray(source){
  //using Fisher-Yates algorithm
  for (var index in source)
  {
    const index2 = getRandomNumber(index, source.length - 1);
    const value = source[index];
    source[index] = source[index2];
    source[index2] = value; 
  }
}