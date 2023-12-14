function test_getSpellingWords(){
  spreadSheetID = test_sheet_id;
  const param = { parameter: {ssId: test_sheet_id, action: actions.getSpellingWords}};
  const result = getSpellingWords(sheets.spellingErik);
  //Logger.log(result["items"]);
  Logger.log(result['items']);
  Logger.log(result["items"].length);
}

function test_instertSpellingWord(){
  const param = { parameter: { word: "newWord2", category: "home", comment: "comment", ssId: test_sheet_id}};
  Logger.log(getDataSheetLength(sheets.spellingErik));
  //already exists
  insertSpellingWords(param);
  Logger.log(getDataSheetLength(sheets.spellingErik));
  //new word
  const param2 = { parameter: { word: "word" + Date.now().toString(), category: "home", comment: "comment", ssId: test_sheet_id}};
  insertSpellingWords(param2);
  Logger.log(getDataSheetLength(sheets.spellingErik));
}


function test_getCategories(){
  Logger.log(getSpellingCategories());
}

function getDataSheetLength(sheetName){
  const dataSheet = getDataSheet(sheetName);
  const rows = dataSheet.getRange(2,1, dataSheet.getLastRow() - 1, dataSheet.getLastColumn()).getValues();
  return rows.length;
}

function test1_setUserName(){
  Logger.log(userName);
  const param = { parameter: {userName: "kacsa"}};
  setUserName(param);
  Logger.log(userName);
}

function test2_setUserName(){
  Logger.log(userName);
  const param = { parameter: {something: "kacsa"}};
  setUserName(param);
  Logger.log(userName);
}

function test1_modifySpellingWord(){
  Logger.log("Testing Case: modify spelling word word - 1x correct");
  spreadSheetID = test_sheet_id;
  const word = "kacsa" + Date.now().toString()
  const wordParam = {word: word, category: "home", comment: "test Case"}; 
  insertSpellingWord(wordParam);

  //const param = { word: word, result: 1};

  Logger.log(modifySentence(word, 1));

  const dataSheet = getDataSheet(sheets.spellingErik);
  const index = getIndexForValue(dataSheet, word);
  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 0, 1, 0];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test2_modifySpellingWord(){
  Logger.log("Testing Case: modify spelling word word - 1x incorrect");
  spreadSheetID = test_sheet_id;
  const word = "kacsa" + Date.now().toString()
  const wordParam = {word: word, category: "home", comment: "test Case"}; 
  insertSpellingWord(wordParam);

  Logger.log(modifySentence(word, -1));

  const dataSheet = getDataSheet(sheets.spellingErik);
  const index = getIndexForValue(dataSheet, word);
  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 1, 1, 1];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test00_modifySpellingWordValues(){
  Logger.log("Testing Case: modify word values")
  test1_modifySpellingWordValues();
  test2_modifySpellingWordValues();
  test3_modifySpellingWordValues();
  test4_modifySpellingWordValues();
  test5_modifySpellingWordValues();
  test6_modifySpellingWordValues();
}

function testHelper_insertSpellingWord(){
  const word = "kacsa" + Date.now().toString()
  const wordParam = {word: word, category: "home", comment: "test Case"}; 
  insertSpellingWord(wordParam);
  return word;

}

function test1_modifySpellingWordValues(){
  //insert word and modify
  //expected result: instert the word, then success: repeat ==0, attempt == 1, nrOfIncorrect ==0
  Logger.log("Testing Case: modify word values - 1x correct");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
 
  modifySentenceValues(dataSheet, index, correctResult);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 0, 1, 0];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test2_modifySpellingWordValues(){
  //insert word and modify
  //expected result: instert the word, then success: repeat ==1 attempt == 1, nrOfIncorrect =1
  Logger.log("Testing Case: modify word values - 1x incorrect");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult = inCorrectResult;
  modifySentenceValues(dataSheet, index, wordResult);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 1, 1, 1];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test3_modifySpellingWordValues(){
  //insert word, then set incorrect then correct
  //insert: 0,0 -> after incorrect: 1, 1,1 -> after correct: 0, 2, 1
  //expected result: instert the word, then success: rate ==-1, repeat ==1
  Logger.log("Testing Case: modify word values - incorrect -> correct");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = inCorrectResult;
  modifySentenceValues(dataSheet, index, wordResult1);

  const wordResult2 = correctResult;
  modifySentenceValues(dataSheet, index, wordResult2);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 0, 2, 1];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test4_modifySpellingWordValues(){
  //insert word, then set correct then incorrect
  //inset: 0,0 -> after correct: 0, 1, 0 -> after incorrect: 1, 2, 1
  //expected result: instert the word, then success: rate ==-1, repeat ==1
  Logger.log("Testing Case: modify word values - correct -> incorrect");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = correctResult;
  modifySentenceValues(dataSheet, index, wordResult1);

  const wordResult2 = inCorrectResult;
  modifySentenceValues(dataSheet, index, wordResult2);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 1, 2, 1];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test5_modifySpellingWordValues(){
  //insert word, then set correct then correct again
  //inset: 0,0 -> after correct: 0, 1, 0 -> after correct: 0, 2, 0
  //expected result: instert the word, then success: rate ==-1, repeat ==1
  Logger.log("Testing Case: modify word values - correct -> correct");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = correctResult;
  modifySentenceValues(dataSheet, index, wordResult1);

  const wordResult2 = correctResult;
  modifySentenceValues(dataSheet, index, wordResult2);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 0, 2, 0];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test6_modifySpellingWordValues(){
  //insert word, then set incorrect then incorrect again
  //inset: 0,0 -> after incorrect: 1, 1, 1 -> after incorrect: 1, 2, 2
  //expected result: instert the word, then success: rate ==-1, repeat ==1
   Logger.log("Testing Case: modify word values - incorrect -> incorrect");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = inCorrectResult;
  modifySentenceValues(dataSheet, index, wordResult1);

  const wordResult2 = inCorrectResult;
  modifySentenceValues(dataSheet, index, wordResult2);


  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 1, 2, 2];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test00_modifySpellingWordValues(){
  Logger.log("Testing Case: modify word values")
  test1_modifySpellingWordValues();
  test2_modifySpellingWordValues();
  test3_modifySpellingWordValues();
  test4_modifySpellingWordValues();
  test5_modifySpellingWordValues();
  test6_modifySpellingWordValues();
}

function test_setUserName(){
  setUserName(null);
  Logger.log("Username: " + userName);
  setUserName("kacsa");
  Logger.log("Username: " + userName);
  
}

function getWordFromSheet(sheet,index){
  var row = sheet.getRange(index, 1, 1, 6);
  return [row.getValues()[0][0], row.getValues()[0][1], row.getValues()[0][2], row.getValues()[0][3], row.getValues()[0][4], row.getValues()[0][5]];
  }

function compareWords(word1, word2){
   return JSON.stringify(word1) === JSON.stringify(word2);
}

function test1_updateSpellingWords(){
  spreadSheetID = test_sheet_id;
  const word1 = testHelper_insertSpellingWord();
  const word2 = testHelper_insertSpellingWord();
  const word3 = testHelper_insertSpellingWord();

  const words=JSON.stringify([{"word":`${word1}`,"result":1},
            {"word":`${word2}`,"result":1},
            {"word":`${word3}`,"result":-1}]);
  
  const param = {parameter: {words:words, action:"updateSpellingWords"}};
  updateSpellingWords(param)
}

function test_getLogResultFromSpelling(){
  Logger.log(getLogResultForUpdateResult(1) == "correct");
  Logger.log(getLogResultForUpdateResult(-1) == "incorrect");
  Logger.log(getLogResultForUpdateResult(0) == "unchecked");
  Logger.log(getLogResultForUpdateResult(25) == "unchecked");
}
