function run_all_irregVerb_tests(){
  test1_get_homophones();
  test2_getAllUniqueHomophones();
  test3_getAllUniqueHomophonesWithResults();
  test4_get_homophones_with_response();
  test5_updateHomophones();
  test6_updateHomophones();
}

function test1_get_homophones(){
  Logger.log("Testing Case: get Irregular Verb Sentences");
  spreadSheetID = test_sheet_id;
  Logger.log(getSentences(sheets.homophones, nrOfHomophonesExercises));
}



function test2_getAllUniqueHomophones(){
  Logger.log("Testing Case2: get All Unique Homophone pairs");
  spreadSheetID = test_sheet_id;
  const dataSheet = getDataSheet(sheets.homophones);
  const rows = getAllDataWoHeader(dataSheet);
  const homophones = getAllUniqueSuggestions(rows) 
  Logger.log(homophones);
  Logger.log(homophones.length);
}

function test3_getAllUniqueHomophonesWithResults(){
  Logger.log("Testing Case3: get All Unique Homophone pairs with Results");
  spreadSheetID = test_sheet_id;
  const result = getAllUniqueSuggestionsWithResults(getAllDataWoHeader(getDataSheet(sheets.homophones)));
  Logger.log(result);
  Logger.log(result.length);
}

function test4_get_homophones_with_response(){
  Logger.log("Testing Case4: get Homophone Sentences As RESPONSE");
  const param = {parameter: {action: actions.getHomophones, ssId: test_sheet_id}};
  const response = doGet(param);
  Logger.log(JSON.stringify(response));
  }

function test5_updateHomophones(){
  Logger.log("Testing Case5: update Homophones");
  spreadSheetID = test_sheet_id;
  const sentence1 = "The police refused to $£ her version of the story.";
  const sentence2 = "Do you $£ credit cards?";
  const sentence3 = "Can I give you a piece of $£?";

  const sentences=JSON.stringify([{"sentence":`${sentence1}`,"result":1},
            {"sentence":`${sentence2}`,"result":1},
            {"sentence":`${sentence3}`,"result":-1}]);
  
  const param = {parameter: {sentences:sentences, action:"updateHomophones"}};
  updateSentences(param, sheets.homophones, sheets.homophones_logs);
}

function test6_updateHomophones(){
  Logger.log("Testing Case6: update Homophones");
  spreadSheetID = test_sheet_id;
  const sentence1 = "The police refused to $£ her version of the story.";
  const sentence2 = "Do you $£ credit cards?";
  const sentence3 = "Can I give you a piece of $£?";

  const sentences=JSON.stringify([{"sentence":`${sentence1}`,"result":1},
            {"sentence":`${sentence2}`,"result":1},
            {"sentence":`${sentence3}`,"result":-1}]);
  
  const param = {parameter: {sentences:sentences, action:"updateHomophones", ssId: spreadSheetID}};
  doPost(param);
}