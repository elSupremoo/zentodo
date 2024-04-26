package com.thesupreme.ZenToDo

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

@Composable
fun AddNoteDialog(
    state: NoteState,
    onEvent: (NoteEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        modifier = modifier,
        onDismissRequest = {
            onEvent(NoteEvent.HideDialog)
        },
        confirmButton = {
            Button(onClick = { 
                onEvent(NoteEvent.SaveNote)
            }) {
                Text(text = "Save")
            }
        },
        title = { Text(text = "Add New To-Do")},
        text = {
            Column {
                TextField(
                    value = state.title,
                    onValueChange = {
                        onEvent(NoteEvent.setTitle(it))
                    },
                    placeholder = {
                        Text(text = "New Task")
                    },
                    singleLine = true,
                )
            }
        },
        dismissButton = {
            Button(onClick = { onEvent(NoteEvent.HideDialog)}) {
                Text(text = "Cancel")
            }
        }

    )
}