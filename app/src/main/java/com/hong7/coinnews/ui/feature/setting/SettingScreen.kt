package com.hong7.coinnews.ui.feature.setting

import android.content.Intent
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.R
import com.hong7.coinnews.ui.theme.Grey
import com.hong7.coinnews.ui.theme.Grey100
import com.hong7.coinnews.ui.theme.Grey200
import com.hong7.coinnews.ui.theme.Grey400
import com.hong7.coinnews.ui.theme.Grey500
import com.hong7.coinnews.ui.theme.Grey700
import com.hong7.coinnews.ui.theme.coinNewsTypography

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: SettingViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val priceAlertEnabled = viewModel.priceAlertEnabled.collectAsStateWithLifecycle()
    val volumeAlertEnabled = viewModel.volumeAlertEnabled.collectAsStateWithLifecycle()
    val selectedVolumeRate = viewModel.selectedVolumeRate.collectAsStateWithLifecycle()
    val selectedPriceRate = viewModel.selectedPriceRate.collectAsStateWithLifecycle()

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "설정",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = Grey700
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                ),
            )
        },
        modifier = modifier
    ) {
        Column(
            modifier = modifier
                .fillMaxWidth()
                .padding(it)
                .padding(24.dp),
            horizontalAlignment = Alignment.Start,
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "거래량 급등 포착 알림",
                    color = Grey500,
                    style = coinNewsTypography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Switch(
                    checked = volumeAlertEnabled.value,
                    onCheckedChange = { viewModel.toggleVolumeAlertEnabled(it) }
                )
            }
            Row(
                horizontalArrangement = Arrangement.spacedBy(10.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                viewModel.volumeRatePercentages.forEach { rate ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (rate == selectedVolumeRate.value),
                            onClick = {
                                viewModel.selectVolumeRate(rate)
                            }
                        )
                        Text(
                            text = "${rate}%",
                            color = Grey500,
                            style = coinNewsTypography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Grey100, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "급등 포착 알림",
                    color = Grey500,
                    style = coinNewsTypography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Switch(
                    checked = priceAlertEnabled.value,
                    onCheckedChange = { viewModel.togglePriceAlertEnabled(it) }
                )
            }
            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                viewModel.priceRatePercentages.forEach { rate ->
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                    ) {
                        RadioButton(
                            selected = (rate == selectedPriceRate.value),
                            onClick = {
                                viewModel.selectPriceRate(rate)
                            }
                        )
                        Text(
                            text = "${rate}%",
                            color = Grey500,
                            style = coinNewsTypography.bodySmall.copy(
                                fontWeight = FontWeight.Medium,
                            ),
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Divider(color = Grey100, thickness = 1.dp)
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "문의하기",
                color = Grey500,
                style = coinNewsTypography.bodyLarge.copy(
                    fontWeight = FontWeight.Bold,
                ),
                modifier = Modifier.clickable {
                    val intent = Intent(Intent.ACTION_SEND).apply {
                        type = "message/rfc822"
                        putExtra(Intent.EXTRA_EMAIL, arrayOf("ghdrl7526@gmail.com"))
                        putExtra(Intent.EXTRA_SUBJECT, "[코인왓치] 문의 사항")
                    }
                    context.startActivity(Intent.createChooser(intent, "이메일 앱을 선택하세요"))
                }
            )
            Spacer(modifier = Modifier.height(16.dp))
            Column(
                verticalArrangement = Arrangement.spacedBy(2.dp)
            ) {
                Text(
                    text = "버전 : ${BuildConfig.VERSION_NAME}",
                    color = Grey400,
                    style = coinNewsTypography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                    )
                )
                Text(
                    text = "개발자 이메일 : ghdrl7526@gmail.com",
                    color = Grey400,
                    style = coinNewsTypography.labelSmall.copy(
                        fontWeight = FontWeight.Medium,
                    )
                )
            }
        }
    }
}