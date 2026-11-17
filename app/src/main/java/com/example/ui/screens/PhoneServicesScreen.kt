package com.example.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.CellTower
import androidx.compose.material.icons.filled.FiberManualRecord
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.ManageAccounts
import androidx.compose.material.icons.filled.NetworkCell
import androidx.compose.material.icons.filled.PhonelinkLock
import androidx.compose.material.icons.filled.RestorePage
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.model.PhoneProblemType
import com.example.model.PhoneServiceDetail
import com.example.model.ServiceSection
import com.example.ui.dialogs.AccountRecoveryWizardDialog
import com.example.ui.dialogs.ArabizationToolDialog
import com.example.ui.dialogs.CarrierUnlockToolDialog
import com.example.ui.dialogs.PhoneDetailDialog
import com.example.ui.dialogs.RequestServiceDialog
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeContainer
import com.example.ui.theme.AccentPink
import com.example.ui.theme.AccentPinkContainer
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.AccentTealContainer
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldDark
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.TealGradientDark
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite

@Composable
fun PhoneServicesScreen(
    services: List<PhoneServiceDetail>,
    selectedFilter: PhoneProblemType?,
    onFilterChange: (PhoneProblemType?) -> Unit,
    onNavigateSection: (ServiceSection) -> Unit,
    onSubmitRequest: (title: String, category: String, subCategory: String, device: String, phone: String, notes: String) -> Unit
) {
    var selectedDetailService by remember { mutableStateOf<PhoneServiceDetail?>(null) }
    var serviceToRequest by remember { mutableStateOf<PhoneServiceDetail?>(null) }
    var showCarrierUnlockTool by remember { mutableStateOf(false) }
    var showAccountRecoveryTool by remember { mutableStateOf(false) }
    var showArabizationTool by remember { mutableStateOf(false) }

    val filteredServices = remember(services, selectedFilter) {
        if (selectedFilter == null) services else services.filter { it.type == selectedFilter }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg),
        contentAlignment = Alignment.TopCenter
    ) {
        LazyColumn(
            modifier = Modifier
                .widthIn(max = 640.dp)
                .fillMaxSize()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(top = 14.dp, bottom = 96.dp)
        ) {
            // Clean Hero Banner
            item {
                Card(
                    shape = RoundedCornerShape(24.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.Transparent),
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.25f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(
                                Brush.linearGradient(
                                    colors = listOf(EmeraldDark, TealGradientDark)
                                )
                            )
                            .padding(20.dp)
                    ) {
                        Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                modifier = Modifier
                                    .clip(RoundedCornerShape(50.dp))
                                    .background(Color(0x2BFFFFFF))
                                    .padding(horizontal = 10.dp, vertical = 4.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.FiberManualRecord,
                                    contentDescription = null,
                                    tint = EmeraldLight,
                                    modifier = Modifier.size(8.dp)
                                )
                                Text(
                                    text = "أدوات الصيانة جاهزة",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.White
                                )
                            }

                            Text(
                                text = "صيانة وبرمجة الهواتف الذكية",
                                style = MaterialTheme.typography.headlineSmall,
                                fontWeight = FontWeight.Bold,
                                color = Color.White
                            )

                            Text(
                                text = "حلول متكاملة لفك الأقفال والشفرات، استرجاع الحسابات والبيانات، التعريب، وضبط شبكة 4G.",
                                style = MaterialTheme.typography.bodyMedium,
                                color = EmeraldLight.copy(alpha = 0.92f)
                            )

                            Button(
                                onClick = { onNavigateSection(ServiceSection.LOCK_RECOVERY_SIMULATOR) },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color.White,
                                    contentColor = EmeraldDark
                                ),
                                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 10.dp),
                                modifier = Modifier
                                    .padding(top = 4.dp)
                                    .minimumInteractiveComponentSize()
                                    .testTag("hero_lock_simulator_button")
                            ) {
                                Icon(Icons.Default.Key, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("تشغيل محاكي فك القفل", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }

            // Quick Tools Strip (3 Airy Cards)
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    QuickToolTile(
                        title = "محاكي القفل",
                        subtitle = "تذكير الرمز",
                        icon = Icons.Default.Key,
                        accentColor = AccentOrange,
                        accentBg = AccentOrangeContainer,
                        onClick = { onNavigateSection(ServiceSection.LOCK_RECOVERY_SIMULATOR) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickToolTile(
                        title = "الاسترداد",
                        subtitle = "صور وملفات",
                        icon = Icons.Default.RestorePage,
                        accentColor = AccentPink,
                        accentBg = AccentPinkContainer,
                        onClick = { onNavigateSection(ServiceSection.DATA_RECOVERY_SCANNER) },
                        modifier = Modifier.weight(1f)
                    )
                    QuickToolTile(
                        title = "شبكة 4G",
                        subtitle = "ضبط APN",
                        icon = Icons.Default.CellTower,
                        accentColor = AccentTeal,
                        accentBg = AccentTealContainer,
                        onClick = { onNavigateSection(ServiceSection.APN_SETTINGS) },
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            // Filter Chips Row
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text(
                        text = "الخدمات والحلول",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        item {
                            FilterChip(
                                selected = selectedFilter == null,
                                onClick = { onFilterChange(null) },
                                label = { Text("الكل", fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(50.dp),
                                border = BorderStroke(1.dp, if (selectedFilter == null) EmeraldPrimary else ImmersiveBorder),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = ImmersiveSurfaceCard,
                                    labelColor = TextSlate,
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = ImmersiveBg
                                )
                            )
                        }
                        items(PhoneProblemType.values()) { type ->
                            val isSelected = selectedFilter == type
                            FilterChip(
                                selected = isSelected,
                                onClick = { onFilterChange(if (isSelected) null else type) },
                                label = { Text(type.titleAr, fontSize = 12.sp, fontWeight = FontWeight.SemiBold) },
                                shape = RoundedCornerShape(50.dp),
                                border = BorderStroke(1.dp, if (isSelected) EmeraldPrimary else ImmersiveBorder),
                                colors = FilterChipDefaults.filterChipColors(
                                    containerColor = ImmersiveSurfaceCard,
                                    labelColor = TextSlate,
                                    selectedContainerColor = EmeraldPrimary,
                                    selectedLabelColor = ImmersiveBg
                                )
                            )
                        }
                    }
                }
            }

            // Streamlined Service Cards
            items(filteredServices, key = { it.id }) { service ->
                PhoneServiceCard(
                    service = service,
                    onViewDetails = { selectedDetailService = service },
                    onLaunchInteractiveTool = {
                        when (service.type) {
                            PhoneProblemType.LOCK_BYPASS,
                            PhoneProblemType.FAILURE_LOCK_SIMULATOR -> onNavigateSection(ServiceSection.LOCK_RECOVERY_SIMULATOR)
                            PhoneProblemType.CARRIER_UNLOCK -> showCarrierUnlockTool = true
                            PhoneProblemType.ACCOUNT_RECOVERY -> showAccountRecoveryTool = true
                            PhoneProblemType.DATA_RECOVERY,
                            PhoneProblemType.DOCUMENTS_RECOVERY -> onNavigateSection(ServiceSection.DATA_RECOVERY_SCANNER)
                            PhoneProblemType.ARABIZATION -> showArabizationTool = true
                            PhoneProblemType.DATA_ACTIVATION_3G_4G -> onNavigateSection(ServiceSection.APN_SETTINGS)
                        }
                    }
                )
            }
        }
    }

    selectedDetailService?.let { service ->
        PhoneDetailDialog(
            service = service,
            onDismiss = { selectedDetailService = null },
            onRequestAssistance = {
                selectedDetailService = null
                serviceToRequest = service
            }
        )
    }

    serviceToRequest?.let { service ->
        RequestServiceDialog(
            initialCategory = "صيانة وبرمجة هواتف",
            initialSubCategory = service.title,
            onDismiss = { serviceToRequest = null },
            onSubmit = { title, category, subCategory, device, phone, notes ->
                onSubmitRequest(title, category, subCategory, device, phone, notes)
            }
        )
    }

    if (showCarrierUnlockTool) {
        CarrierUnlockToolDialog(
            onDismiss = { showCarrierUnlockTool = false },
            onRequestDirectHelp = {
                showCarrierUnlockTool = false
                serviceToRequest = services.firstOrNull { it.type == PhoneProblemType.CARRIER_UNLOCK }
            }
        )
    }

    if (showAccountRecoveryTool) {
        AccountRecoveryWizardDialog(
            onDismiss = { showAccountRecoveryTool = false },
            onRequestDirectHelp = {
                showAccountRecoveryTool = false
                serviceToRequest = services.firstOrNull { it.type == PhoneProblemType.ACCOUNT_RECOVERY }
            }
        )
    }

    if (showArabizationTool) {
        ArabizationToolDialog(
            onDismiss = { showArabizationTool = false },
            onRequestDirectHelp = {
                showArabizationTool = false
                serviceToRequest = services.firstOrNull { it.type == PhoneProblemType.ARABIZATION }
            }
        )
    }
}

@Composable
private fun QuickToolTile(
    title: String,
    subtitle: String,
    icon: ImageVector,
    accentColor: Color,
    accentBg: Color,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    Card(
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = modifier
            .clip(RoundedCornerShape(18.dp))
            .clickable(onClick = onClick)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(10.dp))
                    .background(accentBg),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = accentColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Column {
                Text(
                    text = title,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = subtitle,
                    fontSize = 11.sp,
                    color = TextSlate,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )
            }
        }
    }
}

@Composable
fun PhoneServiceCard(
    service: PhoneServiceDetail,
    onViewDetails: () -> Unit,
    onLaunchInteractiveTool: () -> Unit
) {
    val accentColor = getAccentColorForType(service.type)
    val accentContainer = getAccentContainerForType(service.type)

    Card(
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(accentContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = getIconForProblemType(service.type),
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(22.dp)
                    )
                }

                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = service.type.titleAr,
                        style = MaterialTheme.typography.labelSmall,
                        color = accentColor,
                        fontWeight = FontWeight.Bold,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = service.title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = EmeraldContainer
                ) {
                    Text(
                        text = "${service.pricing.yer} YER",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            Text(
                text = service.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSlate,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Button(
                    onClick = onLaunchInteractiveTool,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.Default.Build, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تشغيل الأداة", fontSize = 13.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = onViewDetails,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkButtonBg,
                        contentColor = TextWhite
                    ),
                    modifier = Modifier
                        .weight(1f)
                        .minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextSlate)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("التفاصيل والطلب", fontSize = 13.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

fun getIconForProblemType(type: PhoneProblemType): ImageVector = when (type) {
    PhoneProblemType.CARRIER_UNLOCK -> Icons.Default.LockOpen
    PhoneProblemType.LOCK_BYPASS -> Icons.Default.PhonelinkLock
    PhoneProblemType.FAILURE_LOCK_SIMULATOR -> Icons.Default.Key
    PhoneProblemType.ACCOUNT_RECOVERY -> Icons.Default.ManageAccounts
    PhoneProblemType.DATA_RECOVERY, PhoneProblemType.DOCUMENTS_RECOVERY -> Icons.Default.RestorePage
    PhoneProblemType.ARABIZATION -> Icons.Default.Translate
    PhoneProblemType.DATA_ACTIVATION_3G_4G -> Icons.Default.NetworkCell
}

fun getAccentColorForType(type: PhoneProblemType): Color = when (type) {
    PhoneProblemType.CARRIER_UNLOCK -> AccentBlue
    PhoneProblemType.LOCK_BYPASS, PhoneProblemType.FAILURE_LOCK_SIMULATOR -> AccentOrange
    PhoneProblemType.ACCOUNT_RECOVERY -> AccentPurple
    PhoneProblemType.DATA_RECOVERY -> AccentPink
    PhoneProblemType.DOCUMENTS_RECOVERY, PhoneProblemType.DATA_ACTIVATION_3G_4G -> AccentTeal
    PhoneProblemType.ARABIZATION -> EmeraldPrimary
}

fun getAccentContainerForType(type: PhoneProblemType): Color = when (type) {
    PhoneProblemType.CARRIER_UNLOCK -> AccentBlueContainer
    PhoneProblemType.LOCK_BYPASS, PhoneProblemType.FAILURE_LOCK_SIMULATOR -> AccentOrangeContainer
    PhoneProblemType.ACCOUNT_RECOVERY -> AccentPurpleContainer
    PhoneProblemType.DATA_RECOVERY -> AccentPinkContainer
    PhoneProblemType.DOCUMENTS_RECOVERY, PhoneProblemType.DATA_ACTIVATION_3G_4G -> AccentTealContainer
    PhoneProblemType.ARABIZATION -> EmeraldContainer
}
