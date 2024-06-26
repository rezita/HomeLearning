# About
<img src="https://github.com/rezita/HomeLearning/assets/43845243/ecda5ad4-6261-4ae2-b7de-54383443c0ab" width="100">

Home Learning is a simple app to practice spelling, irregular verbs and reading for children.
In other words: Home Learning App is the 'front end' part of our family learning project. (The 'back end' is written in google app script and the data is stored in spreadsheets.)

# Short Story
When my older son was in year 2 in school, he had to learn around 6-10 words a week to spell (these were his spelling words of the week.) The lists followed the National Curriculum spelling list recommendations.
Each week he successfully learned to write (spell) the words, but we noticed that after a few weeks he forgot the words he had already learned.

We tried many different methods and then started the "daily spelling dictation at home". Every day he got a few words from the previous spelling lists to write down.
Initially, the words were stored in a spreadsheet and random words were selected using the RANDBETWEEN() function somehow. It worked, but based on the results of the daily practice, we wanted to monitor which words he could write without any problems and which ones still needed practice. Handling this required more time than we expected. 

That was time when the idea came to automate the selection of words and the maintenance of the daily results.
Then this app was born, which we now use not only for daily spelling, but also has other functions. (see section App Screenshots.)  

# App Screenshots
## Main
<img src="https://github.com/rezita/HomeLearning/assets/43845243/a7ec4f2b-6b4a-4dab-9557-6e2a324b12ca" width="200">
<img src="https://github.com/rezita/HomeLearning/assets/43845243/50db6b26-e917-4cfb-8874-10a667028b15" width="200">

## Spelling
<img src="https://github.com/rezita/HomeLearning/assets/43845243/150d506a-db44-4d9b-9461-18d84944d217" width="200">

## Reading
The appearance of the words follows the Monster Phonics' colour code.

<img src="https://github.com/rezita/HomeLearning/assets/43845243/af12675b-cd71-41d5-8667-4c59965a9d7e" height="200">
<img src="https://github.com/rezita/HomeLearning/assets/43845243/298404f2-a30f-41dc-878b-6b53b41c67f8" height="200">
<img src="https://github.com/rezita/HomeLearning/assets/43845243/b7dfab03-bdd0-45e2-a89b-911451c984c3" height="200">
<img src="https://github.com/rezita/HomeLearning/assets/43845243/853f563f-4f73-4ff5-bb8b-e444e5c373a0" height="200">

## Irregular Verbs
<img src="https://github.com/rezita/HomeLearning/assets/43845243/583a6bb6-3423-47c5-826a-3ee25d79c260" width="200">
<img src="https://github.com/rezita/HomeLearning/assets/43845243/6fca9f92-33e4-4f79-abf8-a7ec00143dc1" width="200">

## Homophones
<img src="https://github.com/rezita/HomeLearning/assets/43845243/4c865e3a-67c2-43c6-8d6a-4ecb1cb91fc0" width="200">
<img src="https://github.com/rezita/HomeLearning/assets/43845243/7bdde463-9a04-48be-8274-5e7253433962" width="200">

## Upload New Words
<img src="https://github.com/rezita/HomeLearning/assets/43845243/abd991e9-0678-4a0e-bc79-4ae17339ad9c" width="200">
<img src="https://github.com/rezita/HomeLearning/assets/43845243/ec1f7ed5-ee0d-468e-a307-38f8b2b24282" width="200">

# Getting Started
For using the app, you need to set some variables. 
## App scriptID and sheet ID
The Google Apps Script files can be found under the `gscript` folder. Feel free to create your own Apps Script Project using them.

Insert the following lines into `local.properties` of Home Learning App:
```
scriptID="YOUR_GOOGLE_SCRIPT_ID"
testSheetID="YOUR_GOOGLE_SHEET_ID"
sheetID="YOUR_GOOGLE_SHEET_ID"
```
As you can see, there are two sheetIDs you need to set. One for the debug and one for the release version of the app. (See `build.gradle` `buildTypes` section.)

## For Signed Release
If you would like to generate a signed apk, you also need to set some variable in the `gradle.properties` file:
```
storePassword=YOUR_PASSWORD
keyPassword=YOUR_PASSWORD
keyAlias=YOUR_KEY_ALIAS
storeFile=YOUR_FILE_PATH
```
Also add the followings to the `gradle.properties` file:
```
android.useAndroidX=true
```

NOTE: Do not forget Sync Project with gradle files (File/ Sync Project with gradle files)
# Google Scripts Projecet and Spreadheets
The Home Learning App and the Google Apps Script communicate each outher using Http(s) protocoll. 

According to the Http(s)'s `action` parameter of the request, the Google App Script calls different functions and sends back different kind of data. 
The following table summarise the available requests:

|Action Name | GET/PUT | Sheet Name | Description|
|--- |--- |--- |---------- |
|getErikSpellingWords | GET | spellingErik | Returns words from the `spellingErik` sheet according to the `erikSpellingCategoryRules`. !!!!!!!!! |
|getErikSpellingCategories | GET |- | Returns the predefined `erikSpellingCategories`|
|insertErikSpellingWords | PUT | spellingErik, logs_Erik_spelling | Inserts new spelling words into the `spellingErik` sheet and makes a log entry of the insertation. |
|updateErikSpellingWords | PUT | spellingErik, logs_Erik_spelling | Updates spelling words in `spellingErik` sheet according to the value of the `result` and makes a log entry of the update. |
|getMarkSpellingWords | GET | spellingMark | Returns words from the `spellingMark` sheet according to the `markSpellingCategoryRules`. !!!!!!!!! |
|getMarkSpellingCategories | GET | -  |Returns the predefined `markSpellingCategories` |
|insertMarkSpellingWords | PUT |spellingMark, logs_Mark_spelling  | Inserts new spelling words into the `spellingMark` sheet and makes a log entry of the insertation.|
|updateMarkSpellingWords | PUT |spellingMark, logs_Mark_spelling  |Updates spelling words in `spellingMark` sheet according to the value of the `result` and makes a log entry of the update. |
|getReadingWords | GET | readWords | Returns the marked words from the `readWords` sheet. |
|getReadingCEW | GET | readCEW | Returns the marked words from the `readCEW` sheet. (CEW means Common Exception Word)|
|getIrregularVerbs | GET | irregVerbs | Returns predefined number of irrgular verbs entries from the `irregVerbs` sheet.|
|updateIrregularVerbs | PUT | irregVerbs, logs_irregular | Updates irregular verbs entries in `irregVerbs` sheet according to the value of the `result` and makes a log entry of the update. |
|getHomophones | GET | homophones | Returns predefined number of homophones entries from the `homophones` sheet.|
|updateHomophones | PUT | homophones, logs_homophones | Updates homophones entries in `homophones` sheet according to the value of the `result` and makes a log entry of the update. |
|restoreErikSpellingFromLogs | PUT | spellingErik, logs_Erik_spelling | Rebuilds the spellingErik sheet from strach according to the log entries.|

# Sheets
The App uses the following sheets:
## spellingErik and spellingMark
These are the main sheets of the spelling. The following table describes the columns of the sheets:

|Column Name | Type | Description|
|--- |--- |---------- |
|word | String | This is the spelling `word` needs to be practise. |
|category | String | This is the category of the word. (The categories are defined in `erikSpellingCategories` and `markSpellingCategories` constants.)|
|comment | String | Any comment for the word (e.g. CEW, plural etc)|
|repeat | Number (0/1) | It can be 0 or 1. It 1 the word will be selected next time for practising. (*)|
|attempt | Number | It shows how many times the word was selected before.|
|inCorrect | Number | It shows the number of the incorrect attempts.|

(*) Few words about the word selection. The words can be in the selected list in two ways:
- If the `repeat` value is 1. The repeat value is 1 if the word was marked to 'incorrect' the last time. 
- If the word is selected via the selection procedure. Every word gets a `weight` value which is calculated by multiplying a random number by the `attempt/incorrect` ratio. 
  This means higher the attempt/incorrect ratio, the lower chance of selectiong the word, but not impossible (The random number weight can influence the final weight of the word).

Exmaple:

<img src="https://github.com/rezita/HomeLearning/assets/43845243/757a9670-2822-41dd-9519-1b7f77ca039e" width="600">

## logs_Erik_spelling and logs_Mark_spelling
These are the logging sheets for spelling. Every changes (insert, update, restore) of `spellingErik` and `spellingMark` sheets has an entri in these sheets. The following table describes the columns of the sheets:

|Column Name | Type | Description|
|--- |--- |---------- |
|date | Date | Date of the action. |
|user| String | The name of the user made the action.|
|action | String | Action description: update, insert or restore from log.|
|parameter | String | Parameters of the action.|

Example:

<img src="https://github.com/rezita/HomeLearning/assets/43845243/29154fc4-2e70-4fd0-a33f-8ccd9bedb2ce" width="600">

## irregVerbs and homophones
|Column Name | Type | Description|
|--- |--- |---------- |
|Sentences | String | Sentences for the excercise. |
|Suggestions| String | Suggestions for the excercise (eg. the infinite form of the verb, homophen pairs)|
|comment | String | Any description (eg. verb tense)|
|solution | String | The right answer.|
|attempt | Number | It shows how many times the sentence was selected before.|
|NrOfinCorrect | Number | It shows the number of the incorrect attempts.|

As each `Suggestions` has more than one `Sentences`, the selection algorithm is the following: 

- Collect all the unique `suggestions`, then summarise all the `attempts` and `nrOfIncorrect` attempts. 
- Calculate the weight according to the ratio of the attempts and nrOfIncorrect attempts and multiply by a random number. 
- It then selects the first “n” highest weighted `suggestions` and then selects a random sentence for each of them.

Example:

<img src="https://github.com/rezita/HomeLearning/assets/43845243/fe1f5184-d8a7-4b9a-a8ca-4cd1b2677970" width="600">

## logs_irregular and logs_homophones
|Column Name | Type | Description|
|--- |--- |---------- |
|date | Date | Date of the action. |
|user| String | The name of the user made the action.|
|action | String | Action description: update, insert or restore from log.|
|parameter | String | Parameters of the action.|

Example:

<img src="https://github.com/rezita/HomeLearning/assets/43845243/427adb6e-ebc4-4b28-b75c-9a223f760cde" width="600">

## readWords and readCEW
Sheets for reading.
|Column Name | Type | Description|
|--- |--- |---------- |
|isReadable | String | If the value of this cell is "x" or "X". The word will be selected for reading. |
|word| String | Word for reading.|
|category | String | Custom category of the word. |
|comment | String | Custom comment. |
|rule | Number | Semicolon separated rules for the appearance of the word. There can be more than on rule. (Eg. white e; blue oo)|

Selection rule: the rows which `isReadable` cell contains "x" or "X" will be selected.

Example:

<img src="https://github.com/rezita/HomeLearning/assets/43845243/fe064630-0b50-49d2-b2b5-5a18c26e5aa6" width="600">
   
# Next Steps
- restore from log: timeout after 6 mins -> using a variable to check the process (how many rows were processed)
- add: Mark log restore from log 
- dialog sheetAction differently
- uploadWord Adapter sheet action differently 
- negative response: handle differently (eg. remove progressbar, inform the user)
- dialog: check edittexts when the focus leaves (remove error message when the word is correct, or show when it is not correct)

# License
 [Apache 2.0]
