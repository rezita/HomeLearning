# About
<img src="https://github.com/rezita/HomeLearning/assets/43845243/ecda5ad4-6261-4ae2-b7de-54383443c0ab" width="100">

Home Learning is a simple app to practice spelling, irregular verbs and reading for children.
In other words: Home Learning App is the 'front end' part of out our family learning project. (The 'back end' is written in google app script and the data is stred in spreadheets.)

# Short Story
When my older son was in year 2 in school, he had to learn around 6-10 words a week to spell (these were his spelling words of the week.) The lists followed the National Curriculum spelling list recommendations.
Each week he successfully learned to write (spell) the words, but we noticed that after a few weeks he forgot the words he had already learned.

We tried many different methods and then started the "daily spelling dictation at home". Every day he got a few words from the previous spelling lists to write down.
Initially, the words were stored in a spreadsheet and random words were selected using the RANDBETWEEN() function somehow. It worked, but based on the results of the daily practice, we wanted to monitor which words he could write without any problems and which ones still needed practice. Handling this required more time than we expected. 

That was time when the idea came to automate the selection of words and the maintenance of the daily results.
Then this app was born, which we now use not only for daily spelling, but also has other functions. (see section App Screenshots.)  

# App Screenshots
## Main
<img src="https://github.com/rezita/HomeLearning/assets/43845243/42eba095-3c5e-43ff-8c1f-ff2edf56446b" width="200">

<img src="https://github.com/rezita/HomeLearning/assets/43845243/fdd6294d-1898-4cbb-bc8f-55641f3724ad" width="200">

## Spelling

## Reading

## Irregular Verbs

## Homophones

## Upload New Words

# Getting Started
Set the sheetId and ScriptId
github ID - for partiallyEditableText (see LINK!!!!)

# TODO
- restore from log: timeout after 6 mins -> using a variable to check the process (how many rows were processed)
- add: Mark log restore from log 
- dialog sheetAction differently
- uploadWord Adapter sheet action differently 
- negative response: handle differently (eg. remove progressbar, inform the user)
- dialog: check edittexts when the focus leaves (remove error message when the word is correct, or show when it is not correct)
