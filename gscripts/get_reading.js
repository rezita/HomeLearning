function getReadingWords(sheetName){
  const dataSheet = getDataSheet(sheetName);
  const rows = getAllDataWoHeader(dataSheet);
  const readWords = rows.filter((row) => row[readingIdx.isReadable[0]].toString().toLowerCase() == "x");
  shuffleArray(readWords);
  
  const result = convertArrayToMap(readWords, readingIdx);
  return {items: result}
}