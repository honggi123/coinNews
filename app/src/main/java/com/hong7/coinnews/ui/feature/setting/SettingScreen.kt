package com.hong7.coinnews.ui.feature.setting

import android.app.NotificationManager
import android.content.Context
import android.content.Intent
import android.os.Build
import android.provider.Settings
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Divider
import androidx.compose.material.RadioButton
import androidx.compose.material.Switch
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavController
import com.gun0912.tedpermission.PermissionListener
import com.gun0912.tedpermission.normal.TedPermission
import com.hong7.coinnews.BuildConfig
import com.hong7.coinnews.ui.theme.Grey100
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
                        text = "알림 설정",
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
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            checkAlertPermission(
                                context,
                                { viewModel.toggleVolumeAlertEnabled(enabled) }
                            )
                        } else {
                            viewModel.toggleVolumeAlertEnabled(enabled)
                        }
                    }
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
                            text = "+${rate}%",
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
                    text = "가격 급등 포착 알림",
                    color = Grey500,
                    style = coinNewsTypography.bodyLarge.copy(
                        fontWeight = FontWeight.Bold,
                    ),
                )
                Switch(
                    checked = priceAlertEnabled.value,
                    onCheckedChange = { enabled ->
                        if (enabled) {
                            checkAlertPermission(
                                context,
                                { viewModel.togglePriceAlertEnabled(enabled) }
                            )
                        } else {
                            viewModel.togglePriceAlertEnabled(enabled)
                        }
                    }
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
                            text = "+${rate}%",
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

private fun checkAlertPermission(context: Context, onGranted: () -> Unit) {
    if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU) {
        if (isNotificationEnabled(context)) {
            onGranted()
        } else {
            val intent = Intent(Settings.ACTION_APP_NOTIFICATION_SETTINGS).apply {
                putExtra(Settings.EXTRA_APP_PACKAGE, context.packageName)
            }
            context.startActivity(intent)
        }
    } else {
        TedPermission.create()
            .setPermissionListener(object : PermissionListener {
                override fun onPermissionGranted() {
                    onGranted()
                    // TODO: 권한이 허용된 후 추가 작업
                }

                override fun onPermissionDenied(deniedPermissions: List<String?>?) {
                    // TODO: 권한이 거부된 경우 처리
                }
            })
            .setDeniedMessage("권한을 허용해 주셔야\n사용 가능합니다.")
            .setPermissions(
                android.Manifest.permission.POST_NOTIFICATIONS,
            )
            .check()
    }
}

private fun isNotificationEnabled(context: Context): Boolean {
    val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
    return notificationManager.areNotificationsEnabled()
}
