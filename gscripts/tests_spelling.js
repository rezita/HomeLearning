function test_getAllSpellingWords_Erik() {
  spreadSheetID = test_sheet_id;
  const result = getAllSpellingWords(sheets.spellingErik);
  Logger.log(result['items']);
  Logger.log(result["items"].length);
}

function test_getAllSpellingWords_Mark() {
  spreadSheetID = test_sheet_id;
  const result = getAllSpellingWords(sheets.spellingMark);
  Logger.log(result['items']);
  Logger.log(result["items"].length);
}

function test_getSpellingWords() {
  spreadSheetID = test_sheet_id;
  //const param = { parameter: {ssId: test_sheet_id, action: actions.getSpellingWords}};
  const result = getSpellingWords(sheets.spellingErik, erikSpellingCategoryRules);
  //Logger.log(result["items"]);
  Logger.log(result['items']);
  Logger.log(result["items"].length);
}

function test_instertSpellingWords() {
  spreadSheetID = test_sheet_id;
  const words = [{ word: "newWord2", category: "home", comment: "comment", ssId: test_sheet_id }];
  Logger.log(getDataSheetLength(sheets.spellingErik));
  //already exists
  insertSpellingWords(words, sheets.spellingErik, sheets.spellingErik_logs);
  Logger.log(getDataSheetLength(sheets.spellingErik));
  insertSpellingWords(words, sheets.spellingErik, sheets.spellingErik_logs);
  Logger.log(getDataSheetLength(sheets.spellingErik));
  //new word
  const words2 = [{ word: "word" + Date.now().toString(), category: "home", comment: "comment", ssId: test_sheet_id }];
  insertSpellingWords(words2, sheets.spellingErik, sheets.spellingErik_logs);
  Logger.log(getDataSheetLength(sheets.spellingErik));
}


function test_getCategories() {
  Logger.log(getErikSpellingCategories());
}

//helper function
function getDataSheetLength(sheetName) {
  const dataSheet = getDataSheet(sheetName);
  const rows = dataSheet.getRange(2, 1, dataSheet.getLastRow() - 1, dataSheet.getLastColumn()).getValues();
  return rows.length;
}

function test00_modifySpellingWordValues() {
  Logger.log("Testing Case: modify word values")
  test1_modifySpellingWordValues();
  test2_modifySpellingWordValues();
  test3_modifySpellingWordValues();
  test4_modifySpellingWordValues();
  test5_modifySpellingWordValues();
  test6_modifySpellingWordValues();
}

function testHelper_insertSpellingWord() {
  const word = "kacsa" + Date.now().toString()
  const wordParam = { word: word, category: "home", comment: "test Case" };
  const sheetName = sheets.spellingErik;
  const logSheetName = sheets.spellingErik_logs;
  insertSpellingWord(wordParam, sheetName, logSheetName);
  return word;

}

function test1_modifySpellingWordValues() {
  //insert word and modify
  //expected result: instert the word, then success: repeat ==0, attempt == 1, nrOfIncorrect ==0
  Logger.log("Testing Case: modify word values - 1x correct");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  //Logger.log(index);

  updateSpellingWordValues(dataSheet, index, correctResult);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 0, 1, 0];
  //Logger.log(modifiedWordOnSheet);
  //Logger.log(modifiedWord);
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test2_modifySpellingWordValues() {
  //insert word and modify
  //expected result: instert the word, then success: repeat ==1 attempt == 1, nrOfIncorrect =1
  Logger.log("Testing Case: modify word values - 1x incorrect");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  updateSpellingWordValues(dataSheet, index, inCorrectResult);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 1, 1, 1];
  //Logger.log(modifiedWordOnSheet);
  //Logger.log(modifiedWord);
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test3_modifySpellingWordValues() {
  //insert word, then set incorrect then correct
  //insert: 0,0 -> after incorrect: 1, 1,1 -> after correct: 0, 2, 1
  //expected result: instert the word, then success: rate ==-1, repeat ==1
  Logger.log("Testing Case: modify word values - incorrect -> correct");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = inCorrectResult;
  updateSpellingWordValues(dataSheet, index, wordResult1);

  const wordResult2 = correctResult;
  updateSpellingWordValues(dataSheet, index, wordResult2);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 0, 2, 1];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test4_modifySpellingWordValues() {
  //insert word, then set correct then incorrect
  //inset: 0,0 -> after correct: 0, 1, 0 -> after incorrect: 1, 2, 1
  //expected result: instert the word, then success: rate ==-1, repeat ==1
  Logger.log("Testing Case: modify word values - correct -> incorrect");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = correctResult;
  updateSpellingWordValues(dataSheet, index, wordResult1);

  const wordResult2 = inCorrectResult;
  updateSpellingWordValues(dataSheet, index, wordResult2);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 1, 2, 1];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test5_modifySpellingWordValues() {
  //insert word, then set correct then correct again
  //inset: 0,0 -> after correct: 0, 1, 0 -> after correct: 0, 2, 0
  //expected result: instert the word, then success: rate ==-1, repeat ==1
  Logger.log("Testing Case: modify word values - correct -> correct");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = correctResult;
  updateSpellingWordValues(dataSheet, index, wordResult1);

  const wordResult2 = correctResult;
  updateSpellingWordValues(dataSheet, index, wordResult2);

  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 0, 2, 0];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

function test6_modifySpellingWordValues() {
  //insert word, then set incorrect then incorrect again
  //inset: 0,0 -> after incorrect: 1, 1, 1 -> after incorrect: 1, 2, 2
  //expected result: instert the word, then success: rate ==-1, repeat ==1
  Logger.log("Testing Case: modify word values - incorrect -> incorrect");
  spreadSheetID = test_sheet_id;
  const word = testHelper_insertSpellingWord();
  const dataSheet = getDataSheet(sheets.spellingErik);

  const index = getIndexForValue(dataSheet, word);
  const wordResult1 = inCorrectResult;
  updateSpellingWordValues(dataSheet, index, wordResult1);

  const wordResult2 = inCorrectResult;
  updateSpellingWordValues(dataSheet, index, wordResult2);


  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  const modifiedWord = [word, "home", "test Case", 1, 2, 2];
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
}

//helper function
function getWordFromSheet(sheet, index) {
  var row = sheet.getRange(index, 1, 1, 6);
  return [row.getValues()[0][0], row.getValues()[0][1], row.getValues()[0][2], row.getValues()[0][3], row.getValues()[0][4], row.getValues()[0][5]];
}

//helper function
function compareWords(word1, word2) {
  return JSON.stringify(word1) === JSON.stringify(word2);
}

function test1_updateSpellingWords() {
  spreadSheetID = test_sheet_id;
  /*
  const word1 = testHelper_insertSpellingWord();
  const word2 = testHelper_insertSpellingWord();
  const word3 = testHelper_insertSpellingWord();
  const words = JSON.stringify([{ "word": `${word1}`, "result": 1 },
  { "word": `${word2}`, "result": 1 },
  { "word": `${word3}`, "result": -1 }]);
  const param = { parameter: { words: words, action: "updateErikSpellingWords" } };
*/
  const w1 = "kacsa" + Date.now().toString()
  const word1 = { word: w1, comment: "comment", category: "category", result: 1 };
  insertSpellingWord(word1, sheets.spellingErik, sheets.spellingErik_logs);

  const w2 = "mama" + Date.now().toString()
  const word2 = { word: w2, comment: "comment", category: "category", result: 1 };
  insertSpellingWord(word2, sheets.spellingErik, sheets.spellingErik_logs);

  const w3 = "mama" + Date.now().toString()
  const word3 = { word: w3, comment: "comment", category: "category", result: -1 };
  insertSpellingWord(word3, sheets.spellingErik, sheets.spellingErik_logs);


/*
  const words = [{ "word": `${word1}`, "result": 1 },
  { "word": `${word2}`, "result": 1 },
  { "word": `${word3}`, "result": -1 }];
*/

  const result = updateSpellingWords([word1, word2, word3], sheets.spellingErik, sheets.spellingErik_logs);
  Logger.log(result);
}


function test_getLogResultFromSpelling() {
  Logger.log(getLogResultForUpdateResult(1) == "correct");
  Logger.log(getLogResultForUpdateResult(-1) == "incorrect");
  Logger.log(getLogResultForUpdateResult(0) == "unchecked");
  Logger.log(getLogResultForUpdateResult(25) == "unchecked");
}

function test_rewriteSpellingWord() {
  spreadSheetID = test_sheet_id;
  const wordOld = { word: "kacsa" + Date.now().toString(), comment: "comment", category: "category" };
  const dataSheet = getDataSheet(sheets.spellingErik);
  insertSpellingWord(wordOld, sheets.spellingErik, sheets.spellingErik_logs);

  const index = getIndexForValue(dataSheet, wordOld.word)
  const insertedWordOnSheet = getWordFromSheet(dataSheet, index);
  Logger.log(insertedWordOnSheet);
  const wordNew = "mama" + Date.now().toString();
  modifySpellingWord(sheets.spellingErik, sheets.spellingErik_logs, wordOld.word, wordNew);
  //check result
  const modifiedWordOnSheet = getWordFromSheet(dataSheet, index);
  Logger.log(modifiedWordOnSheet);
}


function test_rewriteSpellingWord_alreadyExisted() {
  spreadSheetID = test_sheet_id;
  const dataSheet = getDataSheet(sheets.spellingErik);

  //word1
  const word1word = "kacsa" + Date.now().toString()
  const word1 = { word: word1word, comment: "comment", category: "category" };
  insertSpellingWord(word1, sheets.spellingErik, sheets.spellingErik_logs);
  const index1 = getIndexForValue(dataSheet, word1word);
  modifySpellingWordValues(dataSheet, index1, 3, 1, 0, false);
  //Logger.log(getWordFromSheet(dataSheet, index1));

  //word2
  const word2word = "mama" + Date.now().toString()
  const word2 = { word: word2word, comment: "comment", category: "category" };
  insertSpellingWord(word2, sheets.spellingErik, sheets.spellingErik_logs);
  const index2 = getIndexForValue(dataSheet, word2word);
  modifySpellingWordValues(dataSheet, index2, 4, 2, 1, false);
  //Logger.log(getWordFromSheet(dataSheet, index2))

  //check result  
  const response = modifySpellingWord(sheets.spellingErik, sheets.spellingErik_logs, word1word, word2word);
  //Logger.log(response);

  const newIndex = getIndexForValue(dataSheet, word2word);//because one row was deleted
  const modifiedWordOnSheet = getWordFromSheet(dataSheet, newIndex);

  const modifiedWord = [word2word, "category", "comment", 1, 7, 3];
  Logger.log(modifiedWord);
  Logger.log(modifiedWordOnSheet);
  Logger.log("Compare: " + compareWords(modifiedWord, modifiedWordOnSheet));
  Logger.log("Index is -1: " + (getIndexForValue(dataSheet, word1word) == -1));

}

function test_getSpellingWordsMark() {
  spreadSheetID = test_sheet_id;
  //const param = { parameter: {ssId: test_sheet_id, action: actions.getSpellingWords}};
  const result = getSpellingWords(sheets.spellingMark, markSpellingCategories);
  //Logger.log(result["items"]);
  Logger.log(result['items']);
  Logger.log(result["items"].length);
}

