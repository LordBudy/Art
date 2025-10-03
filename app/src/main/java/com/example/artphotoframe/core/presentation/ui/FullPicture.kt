import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.PreviewLightDark
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.artphotoframe.R
import com.example.artphotoframe.core.data.models.Picture
import com.example.artphotoframe.core.presentation.ui.theme.ArtPhotoFrameTheme

@Composable
fun FullPicture(
    picture: Picture
) {
    Row(
        verticalAlignment = Alignment.Top,
        modifier = Modifier
            .padding(start = 10.dp, end = 10.dp)
            . fillMaxSize()
            .fillMaxWidth()
            .background(
                color = MaterialTheme.colorScheme.background
            )
    ) {

        AsyncImage(
            model = picture.imageURL,
            contentDescription = "Picture image",
            contentScale = ContentScale.FillHeight,// Сохраняем пропорции
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)// Соотношение 16:9 или .aspectRatio(1f) // Соотношение сторон 1:1
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(10.dp)
                ),
            placeholder = painterResource(id = R.drawable.media),
            error = painterResource(id = R.drawable.media)
        )
    }
}

@PreviewLightDark
@Composable
// давай всегда одинаково называть превью функции. Название UI компонента + Preview
fun FullPicturePreview() {
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
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(color = MaterialTheme.colorScheme.surface)
        ) {
            FullPicture(
                picture = pic
            )
        }
    }
}