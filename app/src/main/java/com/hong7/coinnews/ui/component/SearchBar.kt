package com.hong7.coinnews.ui.component

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.theme.Grey400
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.Salmon600

@Composable
fun SearchBar(
    query: String,
    onValueChange: (String) -> Unit,
    onBackClick: () -> Unit,
    onClearQueryClick: () -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "코인 검색",
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .height(68.dp)
            .padding(horizontal = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_arrow_back),
            contentDescription = "SearchBar_ArrowLeft",
            modifier = Modifier
                .size(36.dp)
                .padding(8.dp)
                .clickable(onClick = onBackClick),
            tint = Grey400,
        )

        TextField(
            value = query,
            onValueChange = onValueChange,
            modifier = Modifier.weight(1F),
            textStyle = MaterialTheme.typography.body1.copy(
                color = Grey700,
                fontWeight = FontWeight.Medium,
            ),
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search,
                keyboardType = KeyboardType.Text,
                capitalization = KeyboardCapitalization.Words,
                autoCorrect = true
            ),
            keyboardActions = KeyboardActions(
                onSearch = {

                }
            ),
            singleLine = true,
            placeholder = {
                Text(
                    text = placeholderText,
                    modifier = Modifier.weight(1F),
                    color = Grey400,
                    maxLines = 1,
                    style = MaterialTheme.typography.body1.copy(
                        fontWeight = FontWeight.Medium,
                    ),
                )
            },
            trailingIcon = {
                if (query.isNotEmpty()) {
                    Row {
                        Spacer(modifier = Modifier.width(6.dp))

                        Icon(
                            painter = painterResource(id = R.drawable.ic_circle_cancel),
                            contentDescription = "SearchBar_CircleX",
                            modifier = Modifier
                                .size(36.dp)
                                .padding(8.dp)
                                .clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onClearQueryClick,
                                ),
                            tint = Color.Unspecified,
                        )
                    }
                }
            },
            colors = TextFieldDefaults.textFieldColors(
                backgroundColor = Color.White,
                cursorColor = Salmon600,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
            ),
        )
    }
}

@Preview
@Composable
fun SearchBarPreview(){

    var query by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(White)
    ) {
        SearchBar(
            query = query,
            onValueChange = { query = it },
            onBackClick = {},
            onClearQueryClick = {},
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
        )
    }
}