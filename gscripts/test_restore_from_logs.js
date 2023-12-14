function test_initialLog() {
  spreadSheetID = test_sheet_id; 
  Logger.log("Test: initialLog");
  createInitialLog("logs_test");
}


function test_recreateFromLogs(){
  Logger.log("Test: recreate from logs");
  spreadSheetID = test_sheet_id;
  restoreSpellingFromLogs();
}


function test_processLogs(){
  Logger.log("Test: recreate from logs");
  spreadSheetID = test_sheet_id;
  processLogs();
}


function test_clearDataSheet(){
  Logger.log("Test: clear Data seheet");
  spreadSheetID = test_sheet_id;
  resetAllValues();
}


function test_convertUpdateWordLogs(){
  Logger.log("Test: convert logs");
  spreadSheetID = test_sheet_id;
  convertUpdateWordLogs("logs_spelling", "logs_test");
}
