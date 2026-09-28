function getZitaSpanishWords(sheetName, nrOfWords) {
  const dataSheet = getDataSheet(sheetName);
  const rows = getAllDataWoHeader(dataSheet);
  const selectedWords = getSelectedSpanishWords(rows, nrOfWords);
  const result = convertArrayToMap(selectedWords, spanishIdxs);
  return { items: result }
}

function getWeekSpanishWords(sheetName) {
  const dataSheet = getDataSheet(sheetName);
  const rows = getAllDataWoHeader(dataSheet);
  const markedRows = rows.filter(word => word[spanisIdxs.isWeekWord[0]] == 1);
  const result = convertArrayToMap(markedRows, spanishIdxs);
  return { items: result }
}

function getReadSpanishWords(sheetName) {
  const dataSheet = getDataSheet(sheetName);
  const rows = getAllDataWoHeader(dataSheet);
  const readWords = rows.filter(row => row[spanishIdxs.isReadable[0]].toString().toLowerCase() == "x");
  shuffleArray(readWords);
  const result = convertArrayToMap(readWords, spanishIdxs);
  return { items: result }
}

function getSelectedSpanishWords(allWords, nrOfWords) {
  var result = [];
  const repeatedWords = allWords.filter(word => word[spanishIdxs.repeat[0]] == 1);
  const nrOfRemained = nrOfWords - repeatedWords.length;
  result = result.concat(repeatedWords);

  if (nrOfRemained > 0) {
    const notRepeatedWords = allWords.filter(word => word[spanishIdxs.repeat[0]] == 0);
    const randomWords = getRandomWordsByWeight(notRepeatedWords, nrOfRemained);
    result = result.concat(randomWords)
  }
  return result;
}