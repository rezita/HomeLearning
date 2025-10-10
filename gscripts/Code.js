var userName = "";
var spreadSheetID = null;

//Query PArameters: https://developers.google.com/apps-script/guides/web


function doGet(e) {
  /*parameters: 
    - required: ssId (sheetId), action 
    - optional: userName
  */
  var items = []
  try {
    const action = e.parameter.action;
    spreadSheetID = e.parameter.ssId;

    if (action == null) {
      insertLog(sheets.error_logs, responseMessages.actionMissing, "");
      return createJSONResponse(
        { message: `${responseMessages.actionMissing}`, items: [] }
      );
    }

    if (spreadSheetID == null) {
      insertLog(sheets.error_logs, responseMessages.spreadShIdMissing, "");
      return createJSONResponse(
        { message: `${responseMessages.spreadShIdMissing}`, items: [] }
      );
    }

    setUserName(e.parameter.userName);
    switch (action) {
      case actions.getErikSpellingWords:
        const spellingErikWords = getSpellingWords(sheets.spellingErik, erikSpellingCategoryRules);
        return createJSONResponse({ items: spellingErikWords.items, message: "" });

      case actions.getAllErikSpellingWords:
        const allSpellingErikWords = getAllSpellingWords(sheets.spellingErik);
        return createJSONResponse({ items: allSpellingErikWords.items, message: "" });


      case actions.getMarkSpellingWords:
        const spellingMarkWords = getSpellingWords(sheets.spellingMark, markSpellingCategoryRules);
        return createJSONResponse({ items: spellingMarkWords.items, message: "" });

      case actions.getAllMarkSpellingWords:
        const allSpellingMarkWords = getAllSpellingWords(sheets.spellingMark);
        return createJSONResponse({ items: allSpellingMarkWords.items, message: "" });

      case actions.getErikSpellingCategories:
        return createJSONResponse({ categories: getErikSpellingCategories(), message: "" });

      case actions.getMarkSpellingCategories:
        return createJSONResponse({ categories: getMarkSpellingCategories(), message: "" });

      case actions.getReadingWords:
        const readingWords = getReadingWords(sheets.read);
        return createJSONResponse({ items: readingWords.items, message: "" });

      case actions.getReadingCEW:
        const readingCEW = getReadingWords(sheets.cew);
        return createJSONResponse({ items: readingCEW.items, message: "" });

      case actions.getIrregularVerbs:
        const sentences = getSentences(sheets.irregVerbs, nrOfIrregVerbExercises);
        return createJSONResponse({ items: sentences.items, message: "" });

      case actions.getHomophones:
        const homophones = getSentences(sheets.homophones, nrOfHomophonesExercises);
        return createJSONResponse({ items: homophones.items, message: "" });

      case actions.getSpanishWordsZita:
        const words = getSpanishWords(sheets.spanishZita, nrOfSpanishWords);
        return createJSONResponse({ items: words.items, message: "" });

      case actions.getWeekSpanishWords:
        const weekWords = getWeekSpanishWords(sheets.spanishZita);
        return createJSONResponse({ items: weekWords.items, message: "" });

      default:
        insertLog(sheets.error_logs, `${responseMessages.wrongAction}: ${action}`, "");
        return createJSONResponse(
          { message: `${responseMessages.wrongAction}: ${action}`, items: [] }
        );
    }
  } catch (e) {
    insertLog(sheets.error_logs, e.toString(), "");
    throw new Error(e.toString())
    /*return createJSONResponse(
      { message: `${responseMessages.severError}: ${action}`, items: [] }
    );*/
  }
}


function doPost(e) {
  /*parameters: 
    - required: ssId (sheetId), action, 
    - required for words: word, category, comment, result 
    - optional: userName
  */

  const parameter = JSON.parse(e.postData.contents)
  const action = parameter.action;
  //return createTextResponse(`${JSON.stringify(action)}`);
  Logger.log(`items: ${parameter.items}`);

  spreadSheetID = parameter.ssId;

  if (action == null) {
    //return createTextResponse(`${responseMessages.actionMissing}`);
    return createJSONResponse({ result: "", message: `${responseMessages.actionMissing}` });
  }
  if (spreadSheetID == null) {
    //return createTextResponse(`${responseMessages.spreadShIdMissing}`);
    return createJSONResponse({ result: "", message: `${responseMessages.spreadShIdMissing}` });
  }

  setUserName(parameter.userName);
  switch (action) {
    case actions.updateErikSpellingWords:
      //return createTextResponse(updateSpellingWords(parameter.items, sheets.spellingErik, sheets.spellingErik_logs));
      return createJSONResponse({ result: updateSpellingWords(parameter.items, sheets.spellingErik, sheets.spellingErik_logs), message: "" });

    case actions.updateMarkSpellingWords:
      //return createTextResponse(updateSpellingWords(parameter.items, sheets.spellingMark, sheets.spellingMark_logs));
      return createJSONResponse({ result: updateSpellingWords(parameter.items, sheets.spellingMark, sheets.spellingMark_logs), message: "" });

    case actions.insertErikSpellingWords:
      //return createTextResponse(insertSpellingWords(parameter.items, sheets.spellingErik, sheets.spellingErik_logs));
      return createJSONResponse({ result: insertSpellingWords(parameter.items, sheets.spellingErik, sheets.spellingErik_logs), message: "" });

    case actions.insertMarkSpellingWords:
      //return createTextResponse(insertSpellingWords(parameter.items, sheets.spellingMark, sheets.spellingMark_logs));
      return createJSONResponse({ result: insertSpellingWords(parameter.items, sheets.spellingMark, sheets.spellingMark_logs), message: "" });

    case actions.updateIrregularVerbs:
      //return createTextResponse(updateSentences(parameter.items, sheets.irregVerbs, sheets.irregular_logs));
      return createJSONResponse({ result: updateSentences(parameter.items, sheets.irregVerbs, sheets.irregular_logs), message: "" });

    case actions.updateHomophones:
      //return createTextResponse(updateSentences(parameter.items, sheets.homophones, sheets.homophones_logs));
      return createJSONResponse({ result: updateSentences(parameter.items, sheets.homophones, sheets.homophones_logs), message: "" });

    case actions.restoreErikSpellingFromLogs:
      //return createTextResponse(restoreSpellingFromLogs());
      return createJSONResponse({ result: restoreSpellingFromLogs(), message: "" });

    case actions.modifyErikSpellingWord:
      return createJSONResponse({ result: modifySpellingWord(sheets.spellingErik, sheets.spellingErik_logs, parameter.items[0], parameter.items[1]), message: "" });

    case actions.modifyMarkSpellingWord:
      return createJSONResponse({ result: modifySpellingWord(sheets.spellingMark, sheets.spellingMark_logs, parameter.items[0], parameter.items[1]), message: "" });

    case actions.insertSpanishWordsZita:
      return createJSONResponse({ result: insertSpanishWord(parameter.items, sheets.spanishZita, sheets.spanishZita_logs), message: "" });

    case actions.setWeekSpanishWords:
      return createJSONResponse({ result: setWeekSpanishWords(parameter.items, sheets.spanishZita, sheets.spanishZita_logs), message: "" });

    case actions.updateSpanishWords:
      return createJSONResponse({ result: updateSpanishWords(parameter.items, sheets.spanishZita, sheets.spanishZita_logs), message: "" });

    case actions.modifySpanishWord:
      return createJSONResponse({ result: modifySpanishWord(sheets.spanishZita, sheets.spanishZita, parameter.items[0], parameter.items[1]), message: "" });

    default:
      //return createTextResponse(`${responseMessages.wrongAction}: ${action}, ${spreadSheetID} `);
      return createJSONResponse({ result: "", message: `${responseMessages.wrongAction}: ${action}` });
  }
}
