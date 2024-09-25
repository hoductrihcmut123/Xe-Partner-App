package com.example.xepartnerapp.screen

import android.annotation.SuppressLint
import android.content.Intent
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Button
import androidx.compose.material.ButtonDefaults
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import com.example.xepartnerapp.HomeDriverActivity
import com.example.xepartnerapp.signup_login.login.LoginActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.signup_login.signup.SignupCsoActivity
import com.example.xepartnerapp.signup_login.signup.SignupDriverActivity


@Composable
fun LoginAndSignUpScreen() {
    val context = LocalContext.current
    val screenHeight = LocalConfiguration.current.screenHeightDp.dp
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White),
    ) {
        Column(modifier = Modifier.fillMaxSize()) {
            Spacer(modifier = Modifier.height(if (screenHeight < 725.dp) 50.sdp else 150.sdp) )
            Image(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = if (screenHeight < 725.dp) 50.sdp else 75.sdp),
                painter = painterResource(id = R.drawable.login_and_signup),
                contentDescription = "Login_and_Signup Image"
            )
            Text(
                buildAnnotatedString {
                    withStyle(style = SpanStyle(
                        color = Color(62,73,88),
                        fontSize = MaterialTheme.typography.h5.fontSize,
                    )) {
                        append(stringResource(id = R.string.WelcomeTitle))
                    }
                    withStyle(style = SpanStyle(
                        color = Color(17,82,253),
                        fontSize = MaterialTheme.typography.h4.fontSize,
                    )) {
                        append(stringResource(id = R.string.Xe))
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.sdp),
                fontWeight = FontWeight.Bold,
                textAlign = TextAlign.Center
            )
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.sdp)
                    .padding(top = if (screenHeight < 725.dp) 50.sdp else 75.sdp, bottom = 25.sdp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .height(60.sdp)
                        .fillMaxWidth(),
                    onClick = {
                        context.startActivity(Intent(context, HomeDriverActivity::class.java))
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(0.sdp, 0.sdp)
                ) {
                    Text(
                        text = stringResource(id = R.string.CreateAccountDriver),
                        fontSize = 19.ssp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.sdp)
                    .padding(bottom = 25.sdp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .height(60.sdp)
                        .fillMaxWidth(),
                    onClick = {
                        context.startActivity(Intent(context, SignupCsoActivity::class.java))
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color.White
                    ),
                    elevation = ButtonDefaults.elevation(0.sdp, 0.sdp)
                ) {
                    Text(
                        text = stringResource(id = R.string.CreateAccountBusiness),
                        fontSize = 19.ssp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 40.sdp),
                verticalAlignment = Alignment.Top,
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier
                        .height(60.sdp)
                        .fillMaxWidth(),
                    onClick = {
                        context.startActivity(Intent(context, LoginActivity::class.java))
                    },
                    colors = ButtonDefaults.buttonColors(
                        contentColor = Color(17,82,253),
                        backgroundColor = Color.White
                    ),
                    border = BorderStroke(2.sdp, Color(17,82,253)),
                    elevation = ButtonDefaults.elevation(0.sdp, 0.sdp)
                ) {
                    Text(
                        text = stringResource(id = R.string.Login),
                        fontSize = 19.ssp,
                        fontWeight = FontWeight.Bold,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}

val Int.sdp: Dp
    @Composable
    get() = this.sdpGet()

val Int.ssp: TextUnit
    @Composable get() = this.textSdp(density = LocalDensity.current)

@Composable
private fun Int.textSdp(density: Density): TextUnit = with(density) {
    this@textSdp.sdp.toSp()
}

@Composable
private fun Int.sdpGet(): Dp {

    val id = when (this) {
        in 1..600 -> "_${this}sdp"
        in (-60..-1) -> "_minus${this}sdp"
        else -> return this.dp
    }

    val resourceField = getFieldId(id)
    return if (resourceField != 0) dimensionResource(id = resourceField) else this.dp

}

@SuppressLint("DiscouragedApi")
@Composable
private fun getFieldId(id: String): Int {
    val context = LocalContext.current
    return context.resources.getIdentifier(id, "dimen", context.packageName)

}

@Composable
@Preview(showBackground = true)
fun LoginAndSignUpScreenPreview() {
    LoginAndSignUpScreen()
}
