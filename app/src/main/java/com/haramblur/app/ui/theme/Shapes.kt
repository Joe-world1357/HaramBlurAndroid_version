package com.haramblur.app.ui.theme

import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Shapes
import androidx.compose.ui.unit.dp

val AppShapes = Shapes(
    // Small components like buttons, chips
    small = RoundedCornerShape(4.dp),
    
    // Medium components like cards, dialog
    medium = RoundedCornerShape(12.dp),
    
    // Large components like bottom sheets, modal dialogs
    large = RoundedCornerShape(16.dp)
)
