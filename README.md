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
In `local.properties` insert the following lines:
```
scriptID="YOUR_GOOGLE_SCRIPT_ID"
testSheetID="YOUR_GOOGLE_SHEET_ID"
sheetID="YOUR_GOOGLE_SHEET_ID"
```
As you can see, there are two sheetIDs you need to set. One for the debug and one for the release version of the app. (See `build.gradle` `buildTypes` section.)

## github access key
For using the partiallyeditabletext library, you need to set the github access token and username in the `gradle.properties` file:
```
user = "YOUR_USERNAME"
acckey = "YOUR_GITHUB_TOKEN"
```
(For more information, see the partiableEdditableText description: https://github.com/rezita/PartiallyEditableText)

Or you can edit the `settings.gradle` file directly:
```
dependencyResolutionManagement {
    ...
    repositories {
        ...
        maven {
            url = uri("https://maven.pkg.github.com/rezita/PartiallyEditableText")
            credentials {
                username = YOUR_USERNAME
                password = YOUR_PASSWORD
            }
        }
    }
```

## For Signed Release
If you would like to generate a signed apk, you also need to set some variable in the `gradle.properties` file:
```
storePassword=YOUR_PASSWORD
keyPassword=YOUR_PASSWORD
keyAlias=YOUR_KEY_ALIAS
storeFile=YOUR_FILE_PATH
```

# Next Steps
- restore from log: timeout after 6 mins -> using a variable to check the process (how many rows were processed)
- add: Mark log restore from log 
- dialog sheetAction differently
- uploadWord Adapter sheet action differently 
- negative response: handle differently (eg. remove progressbar, inform the user)
- dialog: check edittexts when the focus leaves (remove error message when the word is correct, or show when it is not correct)

# License
 [Apache 2.0]
