package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.AdminPanelSettings
import androidx.compose.material.icons.filled.Assignment
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Campaign
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.GridView
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.RestorePage
import androidx.compose.material.icons.filled.SupportAgent
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.ServiceSection
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeContainer
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentPinkContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.AccentTealContainer
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite

@Composable
fun RaqeebBottomBar(
    currentSection: ServiceSection,
    requestsCount: Int = 0,
    onSectionSelected: (ServiceSection) -> Unit,
    onOpenMoreMenu: () -> Unit
) {
    NavigationBar(
        containerColor = ImmersiveSurface,
        tonalElevation = 0.dp,
        modifier = Modifier
            .fillMaxWidth()
            .drawBehind {
                drawLine(
                    color = ImmersiveBorder,
                    start = Offset(0f, 0f),
                    end = Offset(size.width, 0f),
                    strokeWidth = 1.dp.toPx()
                )
            }
    ) {
        val navItemColors = NavigationBarItemDefaults.colors(
            selectedIconColor = EmeraldPrimary,
            selectedTextColor = EmeraldPrimary,
            indicatorColor = EmeraldContainer,
            unselectedIconColor = TextMuted,
            unselectedTextColor = TextMuted
        )

        val isPhoneSelected = currentSection == ServiceSection.PHONE_SOLUTIONS ||
            currentSection == ServiceSection.LOCK_RECOVERY_SIMULATOR
        NavigationBarItem(
            selected = isPhoneSelected,
            onClick = { onSectionSelected(ServiceSection.PHONE_SOLUTIONS) },
            icon = { Icon(Icons.Default.Build, contentDescription = "البرمجة") },
            label = {
                Text(
                    text = "البرمجة",
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (isPhoneSelected) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = navItemColors,
            modifier = Modifier.testTag("nav_phone_solutions")
        )

        val isMarketingSelected = currentSection == ServiceSection.MARKETING
        NavigationBarItem(
            selected = isMarketingSelected,
            onClick = { onSectionSelected(ServiceSection.MARKETING) },
            icon = { Icon(Icons.Default.Campaign, contentDescription = "التسويق") },
            label = {
                Text(
                    text = "التسويق",
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (isMarketingSelected) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = navItemColors,
            modifier = Modifier.testTag("nav_marketing")
        )

        val isRequestsSelected = currentSection == ServiceSection.MY_REQUESTS
        NavigationBarItem(
            selected = isRequestsSelected,
            onClick = { onSectionSelected(ServiceSection.MY_REQUESTS) },
            icon = {
                if (requestsCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(
                                containerColor = EmeraldPrimary,
                                contentColor = ImmersiveBg
                            ) {
                                Text(text = requestsCount.toString(), fontSize = 10.sp)
                            }
                        }
                    ) {
                        Icon(Icons.Default.Assignment, contentDescription = "طلباتي")
                    }
                } else {
                    Icon(Icons.Default.Assignment, contentDescription = "طلباتي")
                }
            },
            label = {
                Text(
                    text = "طلباتي",
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (isRequestsSelected) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = navItemColors,
            modifier = Modifier.testTag("nav_requests")
        )

        val isAccountSelected = currentSection == ServiceSection.LOGIN_SUBSCRIPTION
        NavigationBarItem(
            selected = isAccountSelected,
            onClick = { onSectionSelected(ServiceSection.LOGIN_SUBSCRIPTION) },
            icon = { Icon(Icons.Default.AccountCircle, contentDescription = "حسابي") },
            label = {
                Text(
                    text = "حسابي",
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (isAccountSelected) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = navItemColors,
            modifier = Modifier.testTag("nav_account")
        )

        val isMoreSelected = currentSection == ServiceSection.DATA_RECOVERY_SCANNER ||
            currentSection == ServiceSection.APN_SETTINGS ||
            currentSection == ServiceSection.CONTACT_US
        NavigationBarItem(
            selected = isMoreSelected,
            onClick = onOpenMoreMenu,
            icon = { Icon(Icons.Default.GridView, contentDescription = "الأدوات والمزيد") },
            label = {
                Text(
                    text = "المزيد",
                    fontSize = 11.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = if (isMoreSelected) FontWeight.Bold else FontWeight.Medium
                )
            },
            colors = navItemColors,
            modifier = Modifier.testTag("nav_more")
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MoreToolsBottomSheet(
    currentSection: ServiceSection,
    onDismiss: () -> Unit,
    onSelectSection: (ServiceSection) -> Unit
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    ModalBottomSheet(
        onDismissRequest = onDismiss,
        sheetState = sheetState,
        containerColor = ImmersiveSurface,
        contentColor = TextWhite,
        contentWindowInsets = { WindowInsets(0, 0, 0, 0) }
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .navigationBarsPadding()
                .padding(horizontal = 20.dp)
                .padding(bottom = 24.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Text(
                text = "الأدوات الذكية والإعدادات",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite,
                modifier = Modifier.padding(bottom = 4.dp)
            )

            MoreMenuItem(
                title = "محاكي فك وتذكير القفل",
                subtitle = "اختبار وتذكير رمز القفل عبر إثبات الملكية",
                icon = Icons.Default.Key,
                accentColor = AccentOrange,
                accentBg = AccentOrangeContainer,
                isActive = currentSection == ServiceSection.LOCK_RECOVERY_SIMULATOR,
                testTag = "more_lock_simulator",
                onClick = {
                    onDismiss()
                    onSelectSection(ServiceSection.LOCK_RECOVERY_SIMULATOR)
                }
            )

            MoreMenuItem(
                title = "فاحص استرداد البيانات",
                subtitle = "البحث عن الصور والملفات والمستندات المحذوفة",
                icon = Icons.Default.RestorePage,
                accentColor = AccentPink,
                accentBg = AccentPinkContainer,
                isActive = currentSection == ServiceSection.DATA_RECOVERY_SCANNER,
                testTag = "more_data_recovery",
                onClick = {
                    onDismiss()
                    onSelectSection(ServiceSection.DATA_RECOVERY_SCANNER)
                }
            )

            MoreMenuItem(
                title = "إعدادات وتفعيل 3G / 4G",
                subtitle = "ضبط نقاط الوصول (APN) وفحص سرعة الشبكة",
                icon = Icons.Default.CellTower,
                accentColor = AccentTeal,
                accentBg = AccentTealContainer,
                isActive = currentSection == ServiceSection.APN_SETTINGS,
                testTag = "more_apn_settings",
                onClick = {
                    onDismiss()
                    onSelectSection(ServiceSection.APN_SETTINGS)
                }
            )

            MoreMenuItem(
                title = "مركز المساعدة والدعم",
                subtitle = "التواصل المباشر عبر واتساب والحسابات المعتمدة",
                icon = Icons.Default.SupportAgent,
                accentColor = AccentBlue,
                accentBg = AccentBlueContainer,
                isActive = currentSection == ServiceSection.CONTACT_US,
                testTag = "more_contact_us",
                onClick = {
                    onDismiss()
                    onSelectSection(ServiceSection.CONTACT_US)
                }
            )
        }
    }
}

@Composable
private fun MoreMenuItem(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    accentBg: Color,
    isActive: Boolean,
    testTag: String,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(16.dp),
        color = if (isActive) EmeraldContainer else ImmersiveSurfaceCard,
        border = BorderStroke(1.dp, if (isActive) EmeraldPrimary else ImmersiveBorder),
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .clickable(onClick = onClick)
            .testTag(testTag)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(accentBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = accentColor,
                    modifier = Modifier.size(22.dp)
                )
            }

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = subtitle,
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSlate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Icon(
                imageVector = Icons.AutoMirrored.Filled.KeyboardArrowLeft,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
