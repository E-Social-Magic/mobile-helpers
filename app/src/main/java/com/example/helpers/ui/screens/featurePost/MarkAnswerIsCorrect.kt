package com.example.helpers.ui.screens.featurePost

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.helpers.ui.components.posts.CustomDropdownMenuItem

@Composable
fun MarkAnswerIsCorrect(action: () -> Unit) {
    var expanded by remember { mutableStateOf(false) }
    val (showDialog, setShowDialog) = remember { mutableStateOf(false) }
    Box(modifier = Modifier.wrapContentSize(Alignment.TopStart)) {
        IconButton(onClick = { expanded = true }) {
            Icon(
                imageVector = Icons.Default.MoreVert,
                tint = Color.Gray,
                contentDescription = "more action"
            )
        }
        DropdownMenu(
            modifier = Modifier
                .wrapContentSize()
                .width(200.dp),
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            CustomDropdownMenuItem(
                icon = Icons.Outlined.Done,
                text = "Đánh dấu là đáp án chính xác"
            ) {
                expanded = false
                setShowDialog.invoke(true)
            }
        }
    }
    DialogDemo(
        title = "Xác nhận",
        "Bạn có chắc đây là đáp án đúng?",
        showDialog,
        setShowDialog = setShowDialog,
        action
    )
}

@Composable
fun DialogDemo(
    title: String,
    content: String,
    showDialog: Boolean,
    setShowDialog: (Boolean) -> Unit,
    action: () -> Unit
) {
    if (showDialog) {
        AlertDialog(
            onDismissRequest = {
            },
            title = {
                Text(title)
            },
            confirmButton = {
                Button(
                    onClick = {
                        action.invoke()
                        setShowDialog(false)
                    },
                ) {
                    Text("Xác nhận")
                }
            },
            dismissButton = {
                Button(
                    onClick = {
                        setShowDialog(false)
                    },
                ) {
                    Text("Hủy")
                }
            },
            text = {
                Text(content)
            },
        )
    }
}