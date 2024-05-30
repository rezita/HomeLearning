package com.github.rezita.homelearning.ui.size


/** window width SpellingScreen and UploadSpellingScreen
 * It was important to show at least 16 chars long words without breaking them into two lines with any FontScales
 * There are longer words, but not as frequently used as the shorter ones
 * (average English word length is 4.79 chars)*/
const val wxS_min_type1 = 0
const val wS_min_type1 = 320
const val wM_min_type1 = 365
const val wL_min_type1 = 390
const val wxL_min_type1 = 480

/**window width for ReadingScreen
 * The width of the screen is optimised for the readingScreen
 * The aim is to show "children" with the preset max font size
 * "children" is one of the longest word in Rec Class the children need to be able to read
 * (average English word length is 4.79 chars)*/
const val wxS_min_type2 = 0
const val wS_min_type2 = 530
const val wM_min_type2 = 730
const val wL_min_type2 = 850
const val wxL_min_type2 = 900

const val height = 600