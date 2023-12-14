function test_minIndex() {
  Logger.log("Test: find min index");
  const soureceArray = [["dig", 0.0, 0.0, 0.9859704242736076], ["bear", 0.0, 0.0, 0.9905550270417782], ["burst", 0.0, 0.0, 0.9836927180821262], ["swell", 0.0, 0.0, 0.9863568081650251], ["have", 0.0, 0.0, 0.5179913794241551]];
  const minindex = findMinWeightIndex(soureceArray, suggestionWithResultsCols.weightCol);
  Logger.log(`Min index: ${minindex}, Result: ${minindex == 4}`);
}

function test_getCategoryForWord(){
  test1_getCategoryForWord()
  test2_getCategoryForWord()
  test3_getCategoryForWord()
}

function test1_getCategoryForWord(){
  Logger.log("Test1: getCategory for word");
  spreadSheetID = test_sheet_id;
  const word = "kacsa" + Date.now().toString()
  const wordParam = {word: word, category: "home", comment: "test Case"}; 
  insertSpellingWord(wordParam);
  const category = getAttrForWord(word);
  Logger.log(category == "home");
}


function test2_getCategoryForWord(){
  Logger.log("Test2: getCategory for word");
  spreadSheetID = test_sheet_id;
  const word = "kacsa" + Date.now().toString()
  const wordParam = {word: word, category: "school", comment: "test Case"}; 
  insertSpellingWord(wordParam);
  const category = getAttrForWord(word);
  Logger.log(category == "school");
}

function test3_getCategoryForWord(){
  Logger.log("Test3: getCategory for word");
  spreadSheetID = test_sheet_id;
  const category = getAttrForWord("kacsa" + Date.now().toString());
  Logger.log(category == "");
}