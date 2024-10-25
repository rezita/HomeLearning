function getSentences(sheetName, nrOfSentences) {
  const dataSheet = getDataSheet(sheetName);

  const rows = getAllDataWoHeader(dataSheet);
  const suggestionsWithResults = getAllUniqueSuggestionsWithResults(rows);
  
  const selectedSuggestions = getSelectedSuggestions(suggestionsWithResults, nrOfSentences);
  const selectedSentences = [];
  selectedSuggestions.forEach((suggestion) => selectedSentences.push(getRandomSentenceForSuggestion(rows, suggestion[suggestionWithResultsCols.suggestionCol])));
  
  const result = convertArrayToMap(selectedSentences, sentencesIdx);
  return {items: result}
}

function getAllUniqueSuggestions(data){
  //data: rows from sheet in multi dim array
  //get suggestions (aka irreg.verbs or homophone pairs) as a set: get values->map -> set -> array
//  const allVerbs = dataSheet.getRange("B2:B").getValues().map(r => r[0])
  const allSuggestions = data.map(r => r[sentencesIdx.suggestion[0]]);
  const uniqueSuggestions = new Set(allSuggestions);
  return Array.from(uniqueSuggestions);
}

function getAllUniqueSuggestionsWithResults(rows){
  const suggestions = getAllUniqueSuggestions(rows);
  let result = [];
  suggestions.forEach(suggestion => result.push(getResultsBySuggestion(rows, suggestion)));
  return result;
}

function getResultsBySuggestion(rows, suggestion){
  //result: suggestion, nrOfAttempt, nrOfIcorrect, weight
  let result = [suggestion, 0, 0, 0];
  rows.filter(row => row[sentencesIdx.suggestion[0]] == suggestion).reduce(sumOfResults, result);
  result[suggestionWithResultsCols.weightCol] = calculateWeight(result[suggestionWithResultsCols.nrOfAttemptCol], result[suggestionWithResultsCols.nrOfIncorrectCol]);
  return result;
}

function sumOfResults(previous, current){
    previous[suggestionWithResultsCols.nrOfAttemptCol] += current[sentencesIdx.attempt[0]];
    previous[suggestionWithResultsCols.nrOfIncorrectCol] += current[sentencesIdx.nrOfIncorrect[0]];
    return previous;
}

function getSelectedSuggestions(suggestionsWithResults, nrOfSentences){
  if (suggestionsWithResults.length < nrOfSentences){
    return suggestionsWithResults.map(v => v[0]);
  }

  let resultSuggestions = [];
  let minWeightIndex = 0;

  for (let i = 0; i< suggestionsWithResults.length; i++){
    const currentSuggestion = suggestionsWithResults[i];
    const suggestionWeight = currentSuggestion[suggestionWithResultsCols.weightCol];
    
    if (i < nrOfSentences) {
      resultSuggestions.push(currentSuggestion);
      if (suggestionWeight < resultSuggestions[minWeightIndex][suggestionWithResultsCols.weightCol]){
        minWeightIndex = i;
      }
    } else if (suggestionWeight > resultSuggestions[minWeightIndex][suggestionWithResultsCols.weightCol]) {
      resultSuggestions[minWeightIndex] = suggestionsWithResults[i];
      minWeightIndex = findMinWeightIndex(resultSuggestions, suggestionWithResultsCols.weightCol);
    }
  }
  return resultSuggestions; 
}

function getRandomSentenceForSuggestion(allData, suggestion){
  const rowsForSuggestion = allData.filter(row => row[sentencesIdx.suggestion[0]] == suggestion);
  const sentenceIndex = getRandomNumber(0, rowsForSuggestion.length - 1);
  return rowsForSuggestion[sentenceIndex];
}