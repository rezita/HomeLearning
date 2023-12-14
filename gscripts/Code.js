var userName = "";
var spreadSheetID = null;


function doGet(e) {
  /*parameters: 
    - required: ssId (sheetId), action 
    - optional: userName
  */
  
  const action = e.parameter.action;
  spreadSheetID = e.parameter.ssId;
  
  if (action == null) {
    return createJSONResponse({"error": `${responseMessages.actionMissing}`});
  }
  
  if (spreadSheetID == null) {
    return createJSONResponse({"error": `${responseMessages.spreadShIdMissing}`});
  }

  setUserName(e.parameter.userName);
  switch(action) {
    case actions.getErikSpellingWords:
      const spellingErikWords = getSpellingWords(sheets.spellingErik, erikSpellingCategoryRules);
      return createJSONResponse(spellingErikWords);
    
    case actions.getMarkSpellingWords:
      const spellingMarkWords = getSpellingWords(sheets.spellingMark, markSpellingCategoryRules);
      return createJSONResponse(spellingMarkWords);
    
    case actions.getErikSpellingCategories:
      const erikCategories = {categories: getErikSpellingCategories()};
      return createJSONResponse(erikCategories);
    
    case actions.getMarkSpellingCategories:
      const markCategories = {categories: getMarkSpellingCategories()};
      return createJSONResponse(markCategories);
    
    case actions.getReadingWords:
      const readingWords = getReadingWords(sheets.read);
      return createJSONResponse(readingWords);
    
    case actions.getReadingCEW:
      const readingCEW = getReadingWords(sheets.cew);
      return createJSONResponse(readingCEW);
    
    case actions.getIrregularVerbs:
      const sentences = getSentences(sheets.irregVerbs, nrOfIrregVerbExercises);
      return createJSONResponse(sentences);
    
    case actions.getHomophones:
      const homophones = getSentences(sheets.homophones, nrOfHomophonesExercises);
      return createJSONResponse(homophones);

    //this needs to be removed
    /*case actions.getSpellingWords:
      const spellingWords = getSpellingWords(sheets.spellingErik, erikSpellingCategoryRules);
      return createJSONResponse(spellingWords);
    case actions.getSpellingCategories:
      const categories = {categories: getErikSpellingCategories()};
      return createJSONResponse(categories);
*/
    default:
      return createJSONResponse({"error": `${responseMessages.wrongAction}: ${action}`});
  }
 }


function doPost(e){
  /*parameters: 
    - required: ssId (sheetId), action, 
    - required for words: word, category, comment, result 
    - optional: userName
  */

  const action = e.parameter.action;
  spreadSheetID = e.parameter.ssId;

  if (action == null) {
    return createTextResponse(`${responseMessages.actionMissing}`);
  }
  if (spreadSheetID == null) {
    return createTextResponse(`${responseMessages.spreadShIdMissing}`);
  }

  setUserName(e.parameter.userName);
  switch(action) {
    case actions.updateErikSpellingWords:
      return createTextResponse(updateSpellingWords(e, sheets.spellingErik, sheets.spellingErik_logs)); 
    
    case actions.updateMarkSpellingWords:
      return createTextResponse(updateSpellingWords(e, sheets.spellingMark, sheets.spellingMark_logs));

    case actions.insertErikSpellingWords:
      return createTextResponse(insertSpellingWords(e, sheets.spellingErik, sheets.spellingErik_logs));
    
    case actions.insertMarkSpellingWords:
      return createTextResponse(insertSpellingWords(e, sheets.spellingMark, sheets.spellingMark_logs));
    
    case actions.updateIrregularVerbs:
      return createTextResponse(updateSentences(e, sheets.irregVerbs, sheets.irregular_logs));

    case actions.updateHomophones:
      return createTextResponse(updateSentences(e, sheets.homophones, sheets.homophones_logs));
    
    case actions.restoreErikSpellingFromLogs:
      return createTextResponse(restoreSpellingFromLogs());

    //this needs to be removed
    /*case actions.updateSpellingWords:
      return createTextResponse(updateSpellingWords(e, sheets.spellingErik, sheets.spellingErik_logs));
    case actions.insertSpellingWords:
      return createTextResponse(insertSpellingWords(e, sheets.spellingErik, sheets.spellingErik_logs));

*/
    default:
      return createTextResponse(`${responseMessages.wrongAction}: ${action} `);
 }
}
