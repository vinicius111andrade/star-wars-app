package com.vdemelo.starwarswiki.ui.details

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.vdemelo.starwarswiki.R
import com.vdemelo.starwarswiki.ui.theme.ComposeStarWarsTheme

@Composable
fun SpeciesDetailsScreen(
    navController: NavController,
    imageUrl: String?,
    homeWorld: String?,
    language: String?,
    classification: String?
) {
    DetailsScreen(
        title = stringResource(id = R.string.details_screen_title_species),
        imageUrl = imageUrl,
        firstField = homeWorld,
        secondField = language,
        thirdField = classification
    )
}

@Composable
fun PlanetDetailsScreen(
    imageUrl: String?,
//    homeWorld: String?,
//    language: String?,
//    classification: String?
) {
    DetailsScreen(
        title = stringResource(id = R.string.details_screen_title_planet),
        imageUrl = imageUrl,
        firstField = null,
        secondField = null,
        thirdField = null
    )
}

@Composable
fun DetailsScreen(
    title: String,
    imageUrl: String?,
    firstField: String?,
    secondField: String?,
    thirdField: String?
) {

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 32.dp)
        ) {
            Spacer(modifier = Modifier.height(32.dp))
            Text(
                modifier = Modifier.fillMaxWidth(),
                textAlign = TextAlign.Center,
                text = title,
                color = Color.Black,
                fontSize = 24.sp
            )
            Spacer(modifier = Modifier.height(32.dp))
            LoadImageOrShowDefault(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.CenterHorizontally)
                    .heightIn(min = 0.dp, 200.dp),
                url = imageUrl,
                contentDescription = stringResource(
                    id = R.string.details_screen_image_content_description
                )
            )
            ShowTextContentIfNotNull(text = firstField)
            ShowTextContentIfNotNull(text = secondField)
            ShowTextContentIfNotNull(text = thirdField)
            Spacer(modifier = Modifier.height(32.dp))
        }
    }

}

@Composable
fun LoadImageOrShowDefault(
    modifier: Modifier = Modifier,
    url: String?,
    contentDescription: String
) {
    if (url != null) {
        AsyncImage(
            modifier = modifier,
            model = url,
            contentDescription = contentDescription
        )
    } else {
        Image(
            modifier = modifier,
            painter = painterResource(id = R.drawable.image_placeholder),
            contentDescription = contentDescription
        )
    }
}

@Composable
fun ShowTextContentIfNotNull(text: String?) {
    text?.run {
        Spacer(modifier = Modifier.height(32.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Center,
            text = text,
            color = Color.Black,
            fontSize = 16.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun DetailsScreenPreview(
    title: String = "Titulo dos Detalhes",
//    imageUrl: String? = null,
    imageUrl: String? = "https://starwars-visualguide.com/assets/img/species/1.jpg",
    firstField: String? = "Coisa muito boa boa boa boa",
    secondField: String? = "Coisa muito boa boa boa boa",
    thirdField: String? = "Coisa muito boa boa boa boa"
) {
    ComposeStarWarsTheme {
        DetailsScreen(title, imageUrl, firstField, secondField, thirdField)
    }
}
