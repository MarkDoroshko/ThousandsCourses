package com.example.presentation.screen.login

import android.content.Intent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.draw.clip
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.core.net.toUri
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import com.example.presentation.R
import com.example.presentation.component.AppButton
import com.example.presentation.component.AppErrorSnackbar
import com.example.presentation.component.AppText
import com.example.presentation.component.AppTextField
import com.example.presentation.component.AppTitle
import com.example.presentation.theme.Blue
import com.example.presentation.theme.Orange100
import com.example.presentation.theme.Orange200
import com.example.presentation.theme.White
import com.example.presentation.util.Constants

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    viewModel: LoginViewModel = hiltViewModel(),
    onSubmit: () -> Unit
) {
    val state by viewModel.state.collectAsState()
    val context = LocalContext.current

    Box(
        modifier = modifier
            .fillMaxSize()
            .imePadding()
    ) {
        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            AppTitle(
                modifier = Modifier.fillMaxWidth(),
                text = stringResource(R.string.title_sign_in),
                textAlign = TextAlign.Left
            )

            Spacer(modifier = Modifier.height(28.dp))

            Column(
                modifier = Modifier.fillMaxWidth()
            ) {
                AppTextField(
                    title = "Email",
                    value = state.email,
                    onValueChange = { viewModel.processIntent(LoginIntent.InputEmail(it)) },
                    placeholderText = stringResource(R.string.email_placeholder),
                    height = 40.dp,
                    paddingStart = 16.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    radius = 30.dp
                )

                AnimatedVisibility(visible = state.emailError != null) {
                    Text(
                        text = state.emailError ?: "",
                        color = MaterialTheme.colorScheme.error,
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 6.dp, start = 4.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                AppTextField(
                    title = "Пароль",
                    value = state.password,
                    onValueChange = { viewModel.processIntent(LoginIntent.InputPassword(it)) },
                    placeholderText = stringResource(R.string.password_placeholder),
                    height = 40.dp,
                    paddingStart = 16.dp,
                    color = MaterialTheme.colorScheme.surfaceVariant,
                    radius = 30.dp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            AppButton(
                text = "Вход",
                onClick = {
                    viewModel.processIntent(LoginIntent.Submit)
                    onSubmit()
                },
                enabled = state.isSubmitButtonEnabled,
                isLoading = state.isLoading
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(
                horizontalArrangement = Arrangement.Center
            ) {
                AppText(text = stringResource(R.string.dont_have_account))

                Spacer(modifier = Modifier.width(2.dp))

                AppText(
                    text = stringResource(R.string.register),
                    isClickable = true,
                    onClick = {}
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            AppText(
                text = stringResource(R.string.forgot_password),
                isClickable = true,
                onClick = {}
            )

            Spacer(modifier = Modifier.height(32.dp))

            HorizontalDivider(
                thickness = 1.dp,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    modifier = Modifier.weight(1f).height(40.dp),
                    onClick = {
                        context.startActivity(Intent(Intent.ACTION_VIEW, Constants.VK_URL.toUri()))
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue,
                        contentColor = White
                    ),
                    shape = RoundedCornerShape(30.dp),
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_vk),
                        contentDescription = null
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .height(40.dp)
                        .clip(RoundedCornerShape(30.dp))
                        .background(
                            brush = Brush.verticalGradient(colors = listOf(Orange100, Orange200))
                        )
                        .clickable {
                            context.startActivity(Intent(Intent.ACTION_VIEW, Constants.OK_URL.toUri()))
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        painter = painterResource(R.drawable.ic_ok),
                        contentDescription = null,
                        tint = White
                    )
                }
            }
        }

        AppErrorSnackbar(
            modifier = Modifier.align(Alignment.BottomCenter),
            errorMessage = state.error,
            onDismiss = { viewModel.processIntent(LoginIntent.DismissError) }
        )
    }
}
