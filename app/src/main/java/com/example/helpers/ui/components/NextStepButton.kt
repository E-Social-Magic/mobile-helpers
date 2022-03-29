package com.example.helpers.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.helpers.ui.theme.Green400

@Composable
fun NextStepButton(enabled:Boolean,onButtonClick:()->Unit,textButton:@Composable() ()->Unit){
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = 20.dp)
    ) {
        Button(
            enabled =  enabled,
            onClick = onButtonClick,
            colors = ButtonDefaults.buttonColors(Green400),
            modifier =Modifier
                .height(45.dp)
                .align(alignment = Alignment.CenterEnd),
            shape = RoundedCornerShape(10.dp)
        ) {
            textButton.invoke()
        }
    }
}