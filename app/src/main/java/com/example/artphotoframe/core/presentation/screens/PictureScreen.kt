package com.example.artphotoframe.core.presentation.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.PreviewLightDark
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.FullPictureInfo
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun PictureScreen() {
    val pic = Picture(
        id = 1,
        title = "Die Malkunst",
        imageURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fimageapi.khm.at%2Fimages%2F2574%2FGG_9128_Web.jpg&type=IMAGE",
        body = "Daten nach Texteingabe migriert, Beschriftung: Signatur: Bez. auf " +
                "dem unteren Einfassungsstreifen der Landkarte: I Ver-Meer, Beschriftung:" +
                " Beschriftung: Bez. auf dem oberen Einfassungstreifen der Landkarte: NOVA XVII " +
                "PROV[IN]CIARUM [GERMAINIAE INF]ERI[O]RIS DESCRIPTIO / ET ACCURATA EARUNDEM " +
                "... DE NO[VO] EM[EN]D[ATA] ... REC[TISS]IME EDIT[A P]ER NICOLAUM PISCATOREM, " +
                "Label: inscription: Bez. on the top strip of the map: NOVA XVII PROV [IN] CIARUM " +
                "[GERMAINIAE INF] ERI [O] RIS DESCRIPTIO/ET ACCURATA EARUNDEM... DE NO [VO] EM " +
                "[EN] D [ATA]... REC [TISS] IME EDIT [A P] ER NICOLAUM PISCATOREM"
    )
    ArtPhotoFrameTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.background)
        ) {
            FullPictureInfo(
                picture = pic
            )

        }
    }
}

@PreviewLightDark
@Composable
fun PrevPicScreen() {
    ArtPhotoFrameTheme {
        PictureScreen()
    }
}