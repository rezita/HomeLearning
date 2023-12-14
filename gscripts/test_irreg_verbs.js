function run_all_irregVerb_tests(){
  test1_get_irregVerbs();
  test2_getAllUniqueHomophones();
  test3_getAllUniqueHomophonesWithResults();
  test4_get_homophones_with_response();
  test5_updateHomophones();
  test6_updateHomophones();
}

function test1_get_irregVerbs(){
  Logger.log("Testing Case1: get Irregular Verb Sentences");
  spreadSheetID = test_sheet_id;
  Logger.log(getSentences(sheets.irregVerbs, nrOfIrregVerbExercises));
}

function test2_getAllUniqueHomophones(){
  Logger.log("Testing Case2: get All Unique Irregular Verbs");
  spreadSheetID = test_sheet_id;
  const dataSheet = getDataSheet(sheets.irregVerbs);
  const rows = getAllDataWoHeader(dataSheet);
  const verbs = getAllUniqueSuggestions(rows) 
  Logger.log(verbs);
  Logger.log(verbs.length);
}

function test3_getAllUniqueHomophonesWithResults(){
  Logger.log("Testing Case3: get All Unique Irregular Verbs with Results");
  spreadSheetID = test_sheet_id;
  const result = getAllUniqueSuggestionsWithResults(getAllDataWoHeader(getDataSheet(sheets.irregVerbs)));
  Logger.log(result);
  Logger.log(result.length);
}

function test4_get_homophones_with_response(){
  Logger.log("Testing Case4: get Irregular Verb Sentences As RESPONSE");
  const param = {parameter: {action: actions.getIrregularVerbs, ssId: test_sheet_id}};
  const response = doGet(param);
  Logger.log(JSON.stringify(response));
  }

function test5_updateHomophones(){
  Logger.log("Testing Case5: update irregularVerbs");
  spreadSheetID = test_sheet_id;
  const sentence1 = "Kate $£ angry yesterday."
  const sentence2 = "The band $£ popular in the 1990s."
  const sentence3 = "We $£ to the theatre early."

  const verbs=JSON.stringify([{"sentence":`${sentence1}`,"result":1},
            {"sentence":`${sentence2}`,"result":1},
            {"sentence":`${sentence3}`,"result":-1}]);
  
  const param = {parameter: {sentences:verbs, action:"updateIrregularVerbs"}};
  updateSentences(param, sheets.irregVerbs, sheets.irregular_logs);
}

function test6_updateHomophones(){
  Logger.log("Testing Case6: update irregularVerbs");
  spreadSheetID = test_sheet_id;
  const sentence1 = "Kate $£ angry yesterday."
  const sentence2 = "The band $£ popular in the 1990s."
  const sentence3 = "We $£ to the theatre early."

  const verbs=JSON.stringify([{"sentence":`${sentence1}`,"result":1},
            {"sentence":`${sentence2}`,"result":1},
            {"sentence":`${sentence3}`,"result":-1}]);
  
  const param = {parameter: {sentences:verbs, action:"updateIrregularVerbs", ssId: spreadSheetID}};
  doPost(param);
}





