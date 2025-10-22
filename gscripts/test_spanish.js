function test_getSpanishWords() {
    spreadSheetID = test_sheet_id;
    const result = getSpanishWords(sheets.spanishZita, 10);
    Logger.log(result['items']);
    Logger.log(result["items"].length);
  }
  
  
  function test_getWeekSpanishWords() {
    spreadSheetID = test_sheet_id;
    const result = getWeekSpanishWords(sheets.spanishZita);
    Logger.log(result['items']);
    Logger.log(result["items"].length);
  }
  
  function test_resetWeekSpanisWords(){
    spreadSheetID = test_sheet_id;
    const response = resetWeekSpanisWords(sheets.spanishZita, sheets.spanishZita_logs);
    Logger.log(response);
  }
  
  function test_get_week_words_as_response(){
    Logger.log("Testing Case: get week words As RESPONSE");
    const param = {parameter: {action: "getWeekSpanishWords", ssId: test_sheet_id}};
    const response = doGet(param);
    Logger.log(JSON.stringify(response));
  }
  
  function test_get_week_words_as_response_as_sheetAction(){
    Logger.log("Testing Case: get week words As RESPONSE");
    const param = {parameter: {action: actions.getErikSpellingWords, ssId: test_sheet_id}};
    const response = doGet(param);
    Logger.log(JSON.stringify(response));
  }