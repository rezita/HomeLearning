function getAllSpellingWords(sheetName){
  const dataSheet = getDataSheet(sheetName);
  const rows = getAllDataWoHeader(dataSheet);
  const result = convertArrayToMap(rows, spellingIdxs);
  return {items: result}
}

function getSpellingWords(sheetName, rules){
  const dataSheet = getDataSheet(sheetName);
  const rows = getAllDataWoHeader(dataSheet);
  const selectedWords = getSelectedSpellingWords(rows, rules); 
  const result = convertArrayToMap(selectedWords, spellingIdxs);
  return {items: result}
}

function getSelectedSpellingWords(allWords, rules){
  var selectedWords = [];
  rules.forEach(function(rule){
    const categoryWords = getSelectedSpellingWordsByCategory(allWords, rule);
    selectedWords = selectedWords.concat(categoryWords);
  })
  return selectedWords
}

function getSelectedSpellingWordsByCategory(allWords, rule){
  var result = [];
  
  const repeatedWordsByCategory = (rule[0] == "") ? allWords.filter(word => word[spellingIdxs.repeat[0]] == 1) : allWords.filter(word => word[spellingIdxs.category[0]] == rule[0] && word[spellingIdxs.repeat[0]] == 1)
  
  result = result.concat(repeatedWordsByCategory);

  const nrOfRemained = rule[1] - repeatedWordsByCategory.length;
  
  //if the repeated words less than the required nr of words
  if (nrOfRemained > 0){
    const notRepeatedWordsByCategory = (rule[0] == "") ? allWords.filter(word => word[spellingIdxs.repeat[0]] == 0) : allWords.filter(word => word[spellingIdxs.category[0]] == rule[0] && word[spellingIdxs.repeat[0]] == 0);

    const randomWords = getRandomWordsByWeight(notRepeatedWordsByCategory, nrOfRemained);
    result = result.concat(randomWords);
  }
   
  return result;
}

function getRandomWordsByWeight(words, nrOfWords) {
  if (nrOfWords <=0) {
    return [];
  }

  if (words.length <= nrOfWords) {
    return words;
  } 

  let resultWords = [];
  let minWeightIndex = 0;
  
  for (let i = 0; i < words.length; i++){
    const wordWeight = calculateWeight(words[i][spellingIdxs.attempt[0]], words[i][spellingIdxs.nrOfIncorrect[0]]);
    words[i][spellingIdxs.weight[0]] = wordWeight;
    if (i < nrOfWords) {
      resultWords.push(words[i]);
      if (wordWeight < resultWords[minWeightIndex][spellingIdxs.weight[0]]){
        minWeightIndex = i;
      }
    } else if (wordWeight > resultWords[minWeightIndex][spellingIdxs.weight[0]]) {
      resultWords[minWeightIndex] = words[i];
      minWeightIndex = findMinWeightIndex(resultWords, spellingIdxs.weight[0]);
    }
  }
  return resultWords; 
}