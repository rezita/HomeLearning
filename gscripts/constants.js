//test sheet;
const test_sheet_id = "1LOXUdBWMVqLFaKVrJeLZHdMEabTtS0UzXEpcnVkeJ7c";


//ezt be kellene allitani mindig, a post meg a GET hivaskor.
//sheets
const sheets = {
  spellingErik: "spellingErik",
  spellingErik_logs: "logs_Erik_spelling",
  spellingMark: "spellingMark",
  spellingMark_logs: "logs_Mark_spelling",
  read: "readWords",
  cew: "readCEW",
  irregVerbs: "irregVerbs",
  irregular_logs: "logs_irregular",
  homophones: "homophones",
  homophones_logs: "logs_homophones",
  spanishZita: "spanishZita",
  spanishZita_logs: "logs_spanishZita",
  error_logs: "logs_error"
}

//actions
const actions = {
  //insertSpellingWords: "insertSpellingWords", 
  //              updateSpellingWords: "updateSpellingWords", 
  //              getSpellingWords: "getSpellingWords", 
  //              getSpellingCategories: "getSpellingCategories",

  insertErikSpellingWords: "insertErikSpellingWords",
  updateErikSpellingWords: "updateErikSpellingWords",
  getErikSpellingWords: "getErikSpellingWords",
  getErikSpellingCategories: "getErikSpellingCategories",
  getAllErikSpellingWords: "getAllSpellingWords",
  modifyErikSpellingWord: "modifyErikSpellingWord",

  insertMarkSpellingWords: "insertMarkSpellingWords",
  updateMarkSpellingWords: "updateMarkSpellingWords",
  getMarkSpellingWords: "getMarkSpellingWords",
  getMarkSpellingCategories: "getMarkSpellingCategories",
  getAllMarkSpellingWords: "getAllSpellingWords",
  modifyMarkSpellingWord: "modifyMarkSpellingWord",

  getReadingWords: "getReadingWords",
  getReadingCEW: "getReadingCEW",
  getSounds: "getSounds",
  restoreErikSpellingFromLogs: "restoreErikSpellingFromLogs",
  getIrregularVerbs: "getIrregularVerbs",
  updateIrregularVerbs: "updateIrregularVerbs",
  getHomophones: "getHomophones",
  updateHomophones: "updateHomophones",
  getAllSpellingWords: "getAllSpellingWords",

  getSpanishWordsZita: "getSpanishWordsZita",
  getWeekSpanishWords: "getWeekSpanishWords",

  insertSpanishWordsZita: "insertSpanishWordsZita",
  setWeekSpanishWords: "setWeekSpanishWords",
  updateSpanishWords: "updateSpanishWordsZita",
  modifySpanishWord: "modifySpanishWordZita",
}


//error messages
const responseMessages = {
  success: "Success",
  actionMissing: "Action is missing.",
  spreadShIdMissing: "Spread sheet Id is missing.",
  wordMissing: "Word is missing",
  categoryMissing: "Category is missing",
  resultMissing: "Result is missing",
  insertFailed: "Word insert is failed.",
  updateFailed: "Update failed.",
  wrongAction: "Wrong action provided.",
  wordNotFound: "The given word did not find.",
  wordAlreadyExist: "Exists",
  sentenceMissing: "Sentence is missing.",
  sentenceNotFound: "The given sentence did not find.",
  severError: "There's a problem on the server side.",
  modifyWordFailed: "Modifying spelling word failed.",
  enWordMissing: "English word is missing",
  spWordMissing: "Spanish word is missing",
}

//const spellingCategoryRules = [["school", 18], ["home", 7]];
//Change in Dec.2023 - don't chach teh categories for Erik
const erikSpellingCategoryRules = [["", 25]];
const erikSpellingCategories = ["school", "home"];

const markSpellingCategoryRules = [["CEW", 7], ["home", 10]];
const markSpellingCategories = ["CEW", "home"];


const nrOfIrregVerbExercises = 5;
const nrOfHomophonesExercises = 3;
const nrOfSpanishWords = 5;
const isNumber = 0;
const notNumber = 1;

const spellingIdxs = {
  word: [0, notNumber],
  category: [1, notNumber],
  comment: [2, notNumber],
  repeat: [3, isNumber],
  attempt: [4, isNumber],
  nrOfIncorrect: [5, isNumber],
  weight: [6, isNumber],
};

const readingIdx = {
  isReadable: [0, notNumber],
  word: [1, notNumber],
  category: [2, notNumber],
  comment: [3, notNumber],
  rule: [4, notNumber]
};

const sentencesIdx = {
  sentence: [0, notNumber],
  suggestion: [1, notNumber],
  tense: [2, notNumber],
  solution: [3, notNumber],
  attempt: [4, isNumber],
  nrOfIncorrect: [5, isNumber]
};

const suggestionWithResultsCols = {
  suggestionCol: 0,
  nrOfAttemptCol: 1,
  nrOfIncorrectCol: 2,
  weightCol: 3
};

const spanishIdxs = {
  en: [0, notNumber],
  sp: [1, notNumber],
  isWeekWord: [2, notNumber],
  repeat: [3, isNumber],
  attempt: [4, isNumber],
  nrOfIncorrect: [5, isNumber],
  weight: [6, isNumber],
};



//spellingWordResultValues
const correctResult = 1;
const inCorrectResult = -1;

