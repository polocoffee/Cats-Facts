package com.banklannister.catsfact

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.CutCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.banklannister.catsfact.model.CatsModel
import com.banklannister.catsfact.network.RetrofitInstance
import com.banklannister.catsfact.ui.theme.CatsFactTheme
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.io.IOException

@OptIn(DelicateCoroutinesApi::class)
class MainActivity : ComponentActivity() {

    private val cats = mutableStateOf(CatsModel())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CatsFactTheme {

                MainScreen(cat = cats)
            }
        }

    }

    private fun sendRequest() {
        GlobalScope.launch(Dispatchers.IO) {
            val response = try {
                RetrofitInstance.api.getCatsFacts()
            } catch (e: HttpException) {
                Toast.makeText(this@MainActivity, "Http Error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            } catch (e: IOException) {
                Toast.makeText(this@MainActivity, "IO Error ${e.message}", Toast.LENGTH_SHORT)
                    .show()
                return@launch
            }

            if (response.isSuccessful && response.body() != null) {
                withContext(Dispatchers.Main) {
                    cats.value = response.body()!!
                }
            }
        }
    }


    @Composable
    fun MainScreen(cat: MutableState<CatsModel>, modifier: Modifier = Modifier) {
        Box(
            modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {

            Card(
                modifier
                    .fillMaxSize()
                    .padding(10.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(Color.White)
            ){
                Text(
                    text = "Cats Facts",
                    modifier.padding(bottom = 25.dp, top = 15.dp),
                    fontWeight = FontWeight.Bold,
                    fontSize = 28.sp,
                    textAlign = TextAlign.Center
                )

                Image(
                    painter = painterResource(
                        id = R.drawable.cats_button
                    ),
                    contentDescription = "cats image",
                    modifier
                        .fillMaxWidth()
                        .clip(CutCornerShape(topStart = 5.dp, bottomEnd = 5.dp))
                        .padding(top = 20.dp)
                        .clickable { sendRequest() },
                    alignment = Alignment.Center
                )

                Text(
                    text = cat.value.fact,
                    modifier.padding(top = 35.dp),
                    fontSize = 24.sp,
                    maxLines = 6,
                    lineHeight = 40.sp,
                    textAlign = TextAlign.Center
                )
            }

        }
    }
}


