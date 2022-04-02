package com.example.namessearchapp.namessearch

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Button
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.namessearchapp.data.Error
import com.example.namessearchapp.data.Success
import kotlinx.coroutines.ExperimentalCoroutinesApi

@Composable
@ExperimentalCoroutinesApi
fun NamesSearchScreen() {
    Column {
        SearchBar()
        NamesList(modifier = Modifier.weight(1f))
        EmulateErrorButton()
    }
}

@Composable
fun SearchBar(
    namesSearchViewModel: NamesSearchViewModel = viewModel()
){
    Surface(
        shape = RectangleShape,
        elevation = 3.dp,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 10.dp, horizontal = 5.dp)
        ) {
            TextField(
                value = namesSearchViewModel.namePattern,
                onValueChange = { namesSearchViewModel.onNamePatternChanged(it) },
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}

@Composable
@ExperimentalCoroutinesApi
fun NamesList(
    modifier: Modifier = Modifier,
    namesSearchViewModel: NamesSearchViewModel = viewModel()
){
    val namesListState = namesSearchViewModel.namesList.observeAsState()

    when(val result = namesListState.value){
        is Success -> {
            LazyColumn(
                modifier = modifier,
                contentPadding = PaddingValues(top = 8.dp)
            ) {
                items(
                    items = result.names
                ) { name ->
                    Row(modifier = Modifier.padding(5.dp)) {
                        Text(name)
                    }
                }
            }
        }
        Error -> {
            Column(modifier = modifier) {
                Row(modifier = Modifier.padding(5.dp)) {
                    Text("Something went wrong", color = Color.Red)
                }
            }
        }
        else -> {}
    }
}

@Composable
fun EmulateErrorButton(
    viewModel: NamesSearchViewModel = viewModel()
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp, horizontal = 5.dp)
    ) {
        Button(onClick = viewModel::onEmulateError) {
            Text("Emulate Error")
        }
    }
}

@Preview
@Composable
@ExperimentalCoroutinesApi
fun PreviewNamesSearchScreen(){
    NamesSearchScreen()
}