package com.github.rezita.homelearning.ui.util

import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.SemanticsProperties
import androidx.compose.ui.test.SemanticsMatcher
import androidx.compose.ui.test.hasAnyChild
import androidx.compose.ui.test.hasClickAction
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasText
import androidx.compose.ui.test.isEnabled

fun withRole(role: Role) = SemanticsMatcher.expectValue(
    SemanticsProperties.Role, role
)

fun properButton(title: String) =
    hasText(title).and(withRole(Role.Button)).and(isEnabled()).and(hasClickAction())

fun buttonWithImageAndDescription(description: String) =
    withRole(Role.Button).and(hasAnyChild(withRole(Role.Image) and hasContentDescription(description)))


