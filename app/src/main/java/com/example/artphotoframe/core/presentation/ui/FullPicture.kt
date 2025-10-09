import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
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
fun FullPicture(picture: Picture) {
    val imageUrl = picture.imageURL.firstOrNull()
    if (imageUrl != null) {
        // Если URL есть, используем AsyncImage для загрузки из сети
        AsyncImage(
            model = imageUrl,
            contentDescription = "Picture image",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(10.dp)
                ),
            placeholder = painterResource(id = R.drawable.media),
            error = painterResource(id = R.drawable.media)
        )
    } else {
        // Если URL нет, используем Image с Painter (ресурсом)
        Image(
            painter = painterResource(id = R.drawable.media),
            contentDescription = "Picture image",
            contentScale = ContentScale.FillHeight,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(16f / 9f)
                .clip(RoundedCornerShape(10.dp))
                .border(
                    width = 1.5.dp,
                    color = MaterialTheme.colorScheme.outline,
                    shape = RoundedCornerShape(10.dp)
                )
        )
    }
}


//@PreviewLightDark
//@Composable
//fun PreviewFullPicture() {
//    val pic = Picture(
//        id = "1",
//        title = "Die Malkunst",
//        imageURL = "https://api.europeana.eu/thumbnail/v2/url.json?uri=http%3A%2F%2Fimageapi.khm.at%2Fimages%2F2574%2FGG_9128_Web.jpg&type=IMAGE",
//        description = "Daten nach Texteingabe migriert, Beschriftung:"
//    )
//    ArtPhotoFrameTheme {
//        Box(
//            modifier = Modifier
//                .fillMaxSize()
//                .background(color = MaterialTheme.colorScheme.onSurface)
//        ) {
//            FullPicture(
//                picture = pic
//            )
//        }
//    }
//}