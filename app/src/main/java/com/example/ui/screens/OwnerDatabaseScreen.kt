package com.example.ui.screens

import android.content.Context
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Key
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.LockOpen
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Pin
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Storage
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.RaqeebRepository
import com.example.data.local.ActivationCodeEntity
import com.example.data.local.ClientSubscriptionEntity
import com.example.ui.OwnerAuthStage
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentOrange
import com.example.ui.theme.AccentOrangeContainer
import com.example.ui.theme.AccentPurple
import com.example.ui.theme.AccentPurpleContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.AccentTealContainer
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.StatusError
import com.example.ui.theme.StatusSuccess
import com.example.ui.theme.StatusWarning
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun OwnerDatabaseScreen(
    authStage: OwnerAuthStage,
    onVerifyPassword: (String) -> Boolean,
    onVerifyPin: (String) -> Boolean,
    onBackToPasswordStep: () -> Unit,
    subscriptions: List<ClientSubscriptionEntity>,
    subscribersCount: Int,
    revenueYER: Int,
    revenueSAR: Int,
    revenueUSD: Int,
    activationCodes: List<ActivationCodeEntity>,
    totalCodesCount: Int,
    availableCodesCount: Int,
    usedCodesCount: Int,
    onGenerateCodes: (price: Int, count: Int, onDone: (Int) -> Unit) -> Unit,
    onResetCode: (Long) -> Unit,
    onDeleteCode: (Long) -> Unit,
    onUpdateStatus: (id: Long, status: String, approved: Boolean) -> Unit,
    onUpdateNotes: (id: Long, notes: String) -> Unit,
    onDeleteClient: (id: Long) -> Unit,
    onAddManualClient: (
        fullName: String,
        phone: String,
        phoneModel: String,
        problemType: String,
        serviceName: String,
        currency: String,
        priceAmount: Int,
        paymentMethod: String,
        transactionRef: String,
        status: String,
        ownerNotes: String
    ) -> Unit,
    onLockDashboard: () -> Unit
) {
    val context = LocalContext.current

    if (authStage != OwnerAuthStage.AUTHENTICATED) {
        OwnerAuthGate(
            authStage = authStage,
            onVerifyPassword = onVerifyPassword,
            onVerifyPin = onVerifyPin,
            onBackToPasswordStep = onBackToPasswordStep
        )
    } else {
        OwnerDatabaseDashboard(
            context = context,
            subscriptions = subscriptions,
            subscribersCount = subscribersCount,
            revenueYER = revenueYER,
            revenueSAR = revenueSAR,
            revenueUSD = revenueUSD,
            activationCodes = activationCodes,
            totalCodesCount = totalCodesCount,
            availableCodesCount = availableCodesCount,
            usedCodesCount = usedCodesCount,
            onGenerateCodes = onGenerateCodes,
            onResetCode = onResetCode,
            onDeleteCode = onDeleteCode,
            onUpdateStatus = onUpdateStatus,
            onUpdateNotes = onUpdateNotes,
            onDeleteClient = onDeleteClient,
            onAddManualClient = onAddManualClient,
            onLockDashboard = onLockDashboard
        )
    }
}

@Composable
private fun OwnerAuthGate(
    authStage: OwnerAuthStage,
    onVerifyPassword: (String) -> Boolean,
    onVerifyPin: (String) -> Boolean,
    onBackToPasswordStep: () -> Unit
) {
    var passwordInput by remember { mutableStateOf("") }
    var pinInput by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var pinVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg)
            .padding(20.dp),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .widthIn(max = 460.dp)
                .fillMaxWidth(),
            colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
            border = BorderStroke(1.dp, ImmersiveBorder),
            shape = RoundedCornerShape(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(64.dp)
                        .clip(CircleShape)
                        .background(EmeraldContainer)
                        .border(1.dp, EmeraldPrimary.copy(alpha = 0.5f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = if (authStage == OwnerAuthStage.PIN_STEP) Icons.Default.Pin else Icons.Default.Security,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(32.dp)
                    )
                }

                // Two-stage indicator pill (no credential hints)
                Surface(
                    shape = RoundedCornerShape(50.dp),
                    color = ImmersiveSurfaceVariant,
                    border = BorderStroke(1.dp, ImmersiveBorderSubtle)
                ) {
                    Text(
                        text = if (authStage == OwnerAuthStage.PASSWORD_STEP) {
                            "المرحلة 1 من 2 • كلمة السر"
                        } else {
                            "المرحلة 2 من 2 • رمز PIN"
                        },
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = EmeraldPrimary,
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                    )
                }

                Text(
                    text = "بوابة التحقق الثنائي للمالك",
                    style = MaterialTheme.typography.titleLarge,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite,
                    textAlign = TextAlign.Center
                )

                AnimatedContent(
                    targetState = authStage,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "owner_auth_stage"
                ) { stage ->
                    when (stage) {
                        OwnerAuthStage.PASSWORD_STEP -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text(
                                    text = "أدخل كلمة السر للانتقال إلى مرحلة التحقق الثانية.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSlate,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = passwordInput,
                                    onValueChange = {
                                        passwordInput = it
                                        errorMessage = null
                                    },
                                    label = { Text("كلمة السر") },
                                    singleLine = true,
                                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                                    trailingIcon = {
                                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                                            Icon(
                                                imageVector = if (passwordVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = if (passwordVisible) "إخفاء" else "إظهار",
                                                tint = TextSlate
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("owner_password_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EmeraldPrimary,
                                        unfocusedBorderColor = ImmersiveBorder,
                                        focusedLabelColor = EmeraldPrimary,
                                        unfocusedLabelColor = TextSlate,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        cursorColor = EmeraldPrimary
                                    )
                                )

                                if (errorMessage != null) {
                                    Text(
                                        text = errorMessage ?: "",
                                        color = StatusError,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Button(
                                    onClick = {
                                        if (passwordInput.isBlank()) {
                                            errorMessage = "يرجى إدخال كلمة السر"
                                            return@Button
                                        }
                                        val valid = onVerifyPassword(passwordInput)
                                        if (valid) {
                                            passwordInput = ""
                                            pinInput = ""
                                            errorMessage = null
                                        } else {
                                            errorMessage = "كلمة السر غير صحيحة"
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .minimumInteractiveComponentSize()
                                        .testTag("owner_password_submit"),
                                    shape = RoundedCornerShape(14.dp),
                                    contentPadding = PaddingValues(vertical = 14.dp),
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = EmeraldPrimary,
                                        contentColor = ImmersiveBg
                                    )
                                ) {
                                    Icon(Icons.Default.LockOpen, contentDescription = null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text("متابعة للمرحلة الثانية", fontWeight = FontWeight.Bold)
                                }
                            }
                        }

                        OwnerAuthStage.PIN_STEP, OwnerAuthStage.AUTHENTICATED -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                verticalArrangement = Arrangement.spacedBy(14.dp)
                            ) {
                                Text(
                                    text = "تم التحقق من كلمة السر. أدخل رمز PIN لإكمال الدخول.",
                                    style = MaterialTheme.typography.bodyMedium,
                                    color = TextSlate,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.fillMaxWidth()
                                )

                                OutlinedTextField(
                                    value = pinInput,
                                    onValueChange = {
                                        pinInput = it
                                        errorMessage = null
                                    },
                                    label = { Text("رمز PIN") },
                                    singleLine = true,
                                    visualTransformation = if (pinVisible) VisualTransformation.None else PasswordVisualTransformation(),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.NumberPassword),
                                    trailingIcon = {
                                        IconButton(onClick = { pinVisible = !pinVisible }) {
                                            Icon(
                                                imageVector = if (pinVisible) Icons.Default.VisibilityOff else Icons.Default.Visibility,
                                                contentDescription = if (pinVisible) "إخفاء" else "إظهار",
                                                tint = TextSlate
                                            )
                                        }
                                    },
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("owner_pin_input"),
                                    colors = OutlinedTextFieldDefaults.colors(
                                        focusedBorderColor = EmeraldPrimary,
                                        unfocusedBorderColor = ImmersiveBorder,
                                        focusedLabelColor = EmeraldPrimary,
                                        unfocusedLabelColor = TextSlate,
                                        focusedTextColor = TextWhite,
                                        unfocusedTextColor = TextWhite,
                                        cursorColor = EmeraldPrimary
                                    )
                                )

                                if (errorMessage != null) {
                                    Text(
                                        text = errorMessage ?: "",
                                        color = StatusError,
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        textAlign = TextAlign.Center,
                                        modifier = Modifier.fillMaxWidth()
                                    )
                                }

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedButton(
                                        onClick = {
                                            pinInput = ""
                                            errorMessage = null
                                            onBackToPasswordStep()
                                        },
                                        modifier = Modifier
                                            .weight(0.4f)
                                            .minimumInteractiveComponentSize(),
                                        shape = RoundedCornerShape(14.dp),
                                        border = BorderStroke(1.dp, DarkButtonBorder),
                                        colors = ButtonDefaults.outlinedButtonColors(
                                            containerColor = DarkButtonBg,
                                            contentColor = TextWhite
                                        ),
                                        contentPadding = PaddingValues(vertical = 14.dp)
                                    ) {
                                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Text("رجوع")
                                    }

                                    Button(
                                        onClick = {
                                            if (pinInput.isBlank()) {
                                                errorMessage = "يرجى إدخال رمز PIN"
                                                return@Button
                                            }
                                            val valid = onVerifyPin(pinInput)
                                            if (valid) {
                                                pinInput = ""
                                                errorMessage = null
                                            } else {
                                                errorMessage = "رمز PIN غير صحيح"
                                            }
                                        },
                                        modifier = Modifier
                                            .weight(0.6f)
                                            .minimumInteractiveComponentSize()
                                            .testTag("owner_pin_submit"),
                                        shape = RoundedCornerShape(14.dp),
                                        contentPadding = PaddingValues(vertical = 14.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = EmeraldPrimary,
                                            contentColor = ImmersiveBg
                                        )
                                    ) {
                                        Icon(Icons.Default.LockOpen, contentDescription = null, modifier = Modifier.size(18.dp))
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text("فتح اللوحة", fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun OwnerDatabaseDashboard(
    context: Context,
    subscriptions: List<ClientSubscriptionEntity>,
    subscribersCount: Int,
    revenueYER: Int,
    revenueSAR: Int,
    revenueUSD: Int,
    activationCodes: List<ActivationCodeEntity>,
    totalCodesCount: Int,
    availableCodesCount: Int,
    usedCodesCount: Int,
    onGenerateCodes: (price: Int, count: Int, onDone: (Int) -> Unit) -> Unit,
    onResetCode: (Long) -> Unit,
    onDeleteCode: (Long) -> Unit,
    onUpdateStatus: (id: Long, status: String, approved: Boolean) -> Unit,
    onUpdateNotes: (id: Long, notes: String) -> Unit,
    onDeleteClient: (id: Long) -> Unit,
    onAddManualClient: (
        fullName: String,
        phone: String,
        phoneModel: String,
        problemType: String,
        serviceName: String,
        currency: String,
        priceAmount: Int,
        paymentMethod: String,
        transactionRef: String,
        status: String,
        ownerNotes: String
    ) -> Unit,
    onLockDashboard: () -> Unit
) {
    // 0 = Subscribers & Revenue, 1 = Activation Codes
    var selectedDashboardTab by remember { mutableIntStateOf(0) }

    var searchQuery by remember { mutableStateOf("") }
    var selectedStatusFilter by remember { mutableStateOf("الكل") }
    var selectedCurrencyFilter by remember { mutableStateOf("الكل") }
    var showAddClientDialog by remember { mutableStateOf(false) }
    var clientToEditNotes by remember { mutableStateOf<ClientSubscriptionEntity?>(null) }
    var clientToDelete by remember { mutableStateOf<ClientSubscriptionEntity?>(null) }

    // Activation codes state
    var codeSearchQuery by remember { mutableStateOf("") }
    var codeStatusFilter by remember { mutableStateOf("الكل") } // الكل, متاح, مستخدم
    var codePriceFilter by remember { mutableStateOf<Int?>(null) }
    var showGenerateCodesDialog by remember { mutableStateOf(false) }

    val filteredSubscriptions = remember(subscriptions, searchQuery, selectedStatusFilter, selectedCurrencyFilter) {
        subscriptions.filter { sub ->
            val matchesQuery = searchQuery.isBlank() ||
                sub.fullName.contains(searchQuery, ignoreCase = true) ||
                sub.phoneNumber.contains(searchQuery, ignoreCase = true) ||
                sub.phoneModel.contains(searchQuery, ignoreCase = true) ||
                sub.serviceName.contains(searchQuery, ignoreCase = true) ||
                sub.transactionRef.contains(searchQuery, ignoreCase = true)

            val matchesStatus = when (selectedStatusFilter) {
                "الكل" -> true
                else -> sub.status == selectedStatusFilter
            }

            val matchesCurrency = selectedCurrencyFilter == "الكل" || sub.currency == selectedCurrencyFilter

            matchesQuery && matchesStatus && matchesCurrency
        }
    }

    val filteredCodes = remember(activationCodes, codeSearchQuery, codeStatusFilter, codePriceFilter) {
        activationCodes.filter { item ->
            val matchesQuery = codeSearchQuery.isBlank() ||
                item.code.contains(codeSearchQuery, ignoreCase = true) ||
                item.price.toString().contains(codeSearchQuery) ||
                item.usedByName.contains(codeSearchQuery, ignoreCase = true) ||
                item.usedByPhone.contains(codeSearchQuery, ignoreCase = true)

            val matchesStatus = when (codeStatusFilter) {
                "متاح" -> !item.isUsed
                "مستخدم" -> item.isUsed
                else -> true
            }

            val matchesPrice = codePriceFilter == null || item.price == codePriceFilter

            matchesQuery && matchesStatus && matchesPrice
        }
    }

    val pendingCount = subscriptions.count { it.status == "قيد المراجعة" }
    val approvedCount = subscriptions.count { it.status == "تم التحقق والسداد" || it.status == "مكتمل" }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Top Owner Header Card
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.4f)),
                shape = RoundedCornerShape(20.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(40.dp)
                                    .clip(CircleShape)
                                    .background(EmeraldContainer),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    Icons.Default.Storage,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                            Column {
                                Text(
                                    text = "لوحة إدارة المالك",
                                    style = MaterialTheme.typography.titleMedium,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                                Text(
                                    text = "المشتركين، الإيرادات، وأكواد التفعيل",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = EmeraldPrimary,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        IconButton(
                            onClick = onLockDashboard,
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(DarkButtonBg)
                                .border(1.dp, DarkButtonBorder, CircleShape)
                        ) {
                            Icon(
                                Icons.Default.Lock,
                                contentDescription = "قفل اللوحة",
                                tint = AccentOrange,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                    }

                    // Primary 2-Tab Switcher
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = ImmersiveSurfaceVariant,
                        border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            val tabs = listOf(
                                0 to "المشتركين ($subscribersCount)",
                                1 to "أكواد التفعيل ($totalCodesCount)"
                            )
                            tabs.forEach { (tabIdx, label) ->
                                val selected = selectedDashboardTab == tabIdx
                                Surface(
                                    shape = RoundedCornerShape(10.dp),
                                    color = if (selected) EmeraldPrimary else Color.Transparent,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .clickable { selectedDashboardTab = tabIdx }
                                        .minimumInteractiveComponentSize()
                                ) {
                                    Box(
                                        contentAlignment = Alignment.Center,
                                        modifier = Modifier.padding(vertical = 8.dp)
                                    ) {
                                        Text(
                                            text = label,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (selected) ImmersiveBg else TextSlate
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }

        if (selectedDashboardTab == 1) {
            // ================= TAB 1: ACTIVATION CODES =================
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("إجمالي الأكواد", fontSize = 11.sp, color = TextSlate)
                                Text("$totalCodesCount", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = TextWhite)
                            }
                            Column {
                                Text("المتاحة", fontSize = 11.sp, color = TextSlate)
                                Text("$availableCodesCount", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
                            }
                            Column {
                                Text("المستخدمة", fontSize = 11.sp, color = TextSlate)
                                Text("$usedCodesCount", fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = AccentOrange)
                            }
                        }

                        HorizontalDivider(color = ImmersiveBorderSubtle)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showGenerateCodesDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldPrimary,
                                    contentColor = ImmersiveBg
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("توليد أكواد جديدة", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val availableBatch = filteredCodes.filter { !it.isUsed }.take(50)
                                    if (availableBatch.isEmpty()) {
                                        Toast.makeText(context, "لا توجد أكواد متاحة مطابقة للفلتر", Toast.LENGTH_SHORT).show()
                                    } else {
                                        val text = availableBatch.joinToString("\n") { "${it.code} (${it.price} YER)" }
                                        AppIntentUtils.copyToClipboard(context, "أكواد تفعيل رقيب", text)
                                    }
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, DarkButtonBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = DarkButtonBg,
                                    contentColor = TextWhite
                                )
                            ) {
                                Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("نسخ المتاحة (50)", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Search & Filter for Codes
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = codeSearchQuery,
                        onValueChange = { codeSearchQuery = it },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
                        },
                        trailingIcon = {
                            if (codeSearchQuery.isNotEmpty()) {
                                IconButton(onClick = { codeSearchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "مسح", tint = TextSlate)
                                }
                            }
                        },
                        placeholder = { Text("بحث بالكود أو السعر أو المستخدم...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = ImmersiveBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            cursorColor = EmeraldPrimary,
                            focusedContainerColor = ImmersiveSurfaceCard,
                            unfocusedContainerColor = ImmersiveSurfaceCard
                        ),
                        singleLine = true
                    )

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        val statuses = listOf("الكل", "متاح", "مستخدم")
                        items(statuses) { st ->
                            FilterChip(
                                selected = codeStatusFilter == st,
                                onClick = { codeStatusFilter = st },
                                label = { Text(st, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldContainer,
                                    selectedLabelColor = EmeraldPrimary,
                                    containerColor = DarkButtonBg,
                                    labelColor = TextSlate
                                )
                            )
                        }
                    }

                    LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                        item {
                            FilterChip(
                                selected = codePriceFilter == null,
                                onClick = { codePriceFilter = null },
                                label = { Text("كل الأسعار", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentBlueContainer,
                                    selectedLabelColor = AccentBlue,
                                    containerColor = DarkButtonBg,
                                    labelColor = TextSlate
                                )
                            )
                        }
                        items(RaqeebRepository.ALLOWED_CODE_PRICES) { price ->
                            FilterChip(
                                selected = codePriceFilter == price,
                                onClick = { codePriceFilter = if (codePriceFilter == price) null else price },
                                label = { Text("$price", fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentBlueContainer,
                                    selectedLabelColor = AccentBlue,
                                    containerColor = DarkButtonBg,
                                    labelColor = TextSlate
                                )
                            )
                        }
                    }

                    Text(
                        text = "النتائج المطابقة: ${filteredCodes.size} كود",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                }
            }

            if (filteredCodes.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                        border = BorderStroke(1.dp, ImmersiveBorder),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.Key, contentDescription = null, tint = TextMuted, modifier = Modifier.size(38.dp))
                            Text("لا توجد أكواد مطابقة للبحث", color = TextSlate, fontSize = 13.sp)
                        }
                    }
                }
            } else {
                items(filteredCodes, key = { it.id }) { codeEntity ->
                    ActivationCodeItemCard(
                        codeEntity = codeEntity,
                        context = context,
                        onReset = { onResetCode(codeEntity.id) },
                        onDelete = { onDeleteCode(codeEntity.id) }
                    )
                }
            }
        } else {
            // ================= TAB 0: SUBSCRIBERS & REVENUE =================
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    shape = RoundedCornerShape(18.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Column {
                                Text("إجمالي المشتركين", fontSize = 11.sp, color = TextSlate)
                                Text("$subscribersCount مشترك", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = TextWhite)
                            }
                            Column {
                                Text("قيد المراجعة", fontSize = 11.sp, color = TextSlate)
                                Text("$pendingCount طلب", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StatusWarning)
                            }
                            Column {
                                Text("المعتمدون", fontSize = 11.sp, color = TextSlate)
                                Text("$approvedCount مشترك", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = StatusSuccess)
                            }
                            Column {
                                Text("قاعدة البيانات", fontSize = 11.sp, color = TextSlate)
                                Text("نشطة", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = AccentTeal)
                            }
                        }

                        HorizontalDivider(color = ImmersiveBorderSubtle)

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Button(
                                onClick = { showAddClientDialog = true },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldPrimary,
                                    contentColor = ImmersiveBg
                                )
                            ) {
                                Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("إضافة عميل يدوياً", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                            }

                            OutlinedButton(
                                onClick = {
                                    val report = buildString {
                                        appendLine("📊 تقرير قاعدة بيانات المشتركين - مركز رقيب")
                                        appendLine("• إجمالي المشتركين: $subscribersCount")
                                        appendLine("• الإيرادات باليمني: $revenueYER YER")
                                        appendLine("• الإيرادات بالسعودي: $revenueSAR SAR")
                                        appendLine("• الإيرادات بالدولار: $revenueUSD USD")
                                        appendLine("-----------------------------")
                                        subscriptions.forEachIndexed { i, sub ->
                                            appendLine("${i + 1}. ${sub.fullName} (${sub.phoneNumber})")
                                            appendLine("   الجهاز: ${sub.phoneModel} | الخدمة: ${sub.serviceName}")
                                            appendLine("   المبلغ: ${sub.priceAmount} ${sub.currency} | الحالة: ${sub.status}")
                                            appendLine("   الحوالة/الكود: ${sub.transactionRef}")
                                        }
                                    }
                                    AppIntentUtils.copyToClipboard(context, "تقرير المشتركين", report)
                                },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp),
                                border = BorderStroke(1.dp, DarkButtonBorder),
                                colors = ButtonDefaults.outlinedButtonColors(
                                    containerColor = DarkButtonBg,
                                    contentColor = TextWhite
                                )
                            ) {
                                Icon(Icons.Default.Share, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(4.dp))
                                Text("نسخ التقرير", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Financial KPI Cards (YER, SAR, USD)
            item {
                Text(
                    text = "إجمالي الإيرادات المسجلة:",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
                        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("الريال اليمني", fontSize = 10.sp, color = TextSlate)
                            Text("$revenueYER", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = EmeraldPrimary)
                            Text("YER", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSlate)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
                        border = BorderStroke(1.dp, AccentBlue.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("الريال السعودي", fontSize = 10.sp, color = TextSlate)
                            Text("$revenueSAR", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = AccentBlue)
                            Text("SAR", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSlate)
                        }
                    }

                    Card(
                        modifier = Modifier.weight(1f),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceVariant),
                        border = BorderStroke(1.dp, AccentPurple.copy(alpha = 0.3f)),
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Column(
                            modifier = Modifier.padding(10.dp),
                            verticalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text("الدولار الأمريكي", fontSize = 10.sp, color = TextSlate)
                            Text("$revenueUSD", fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = AccentPurple)
                            Text("USD", fontSize = 11.sp, fontWeight = FontWeight.Bold, color = TextSlate)
                        }
                    }
                }
            }

            // Search & Filter Bar
            item {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        leadingIcon = {
                            Icon(Icons.Default.Search, contentDescription = null, tint = EmeraldPrimary)
                        },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Close, contentDescription = "مسح", tint = TextSlate)
                                }
                            }
                        },
                        placeholder = { Text("بحث باسم العميل، الهاتف، الجهاز، أو الحوالة...") },
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(14.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = EmeraldPrimary,
                            unfocusedBorderColor = ImmersiveBorder,
                            focusedTextColor = TextWhite,
                            unfocusedTextColor = TextWhite,
                            cursorColor = EmeraldPrimary,
                            focusedContainerColor = ImmersiveSurfaceCard,
                            unfocusedContainerColor = ImmersiveSurfaceCard
                        ),
                        singleLine = true
                    )

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val statuses = listOf("الكل", "قيد المراجعة", "تم التحقق والسداد", "مكتمل", "ملغي")
                        items(statuses) { status ->
                            FilterChip(
                                selected = selectedStatusFilter == status,
                                onClick = { selectedStatusFilter = status },
                                label = { Text(status, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = EmeraldContainer,
                                    selectedLabelColor = EmeraldPrimary,
                                    containerColor = DarkButtonBg,
                                    labelColor = TextSlate
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = selectedStatusFilter == status,
                                    borderColor = DarkButtonBorder,
                                    selectedBorderColor = EmeraldPrimary
                                )
                            )
                        }
                    }

                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        val currencies = listOf("الكل", "YER", "SAR", "USD")
                        items(currencies) { curr ->
                            FilterChip(
                                selected = selectedCurrencyFilter == curr,
                                onClick = { selectedCurrencyFilter = curr },
                                label = { Text(if (curr == "الكل") "كل العملات" else curr, fontSize = 11.sp) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = AccentBlueContainer,
                                    selectedLabelColor = AccentBlue,
                                    containerColor = DarkButtonBg,
                                    labelColor = TextSlate
                                )
                            )
                        }
                    }
                }
            }

            item {
                Text(
                    text = "سجل المشتركين (${filteredSubscriptions.size}):",
                    style = MaterialTheme.typography.titleSmall,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
            }

            if (filteredSubscriptions.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                        border = BorderStroke(1.dp, ImmersiveBorder),
                        shape = RoundedCornerShape(16.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(28.dp),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(
                                Icons.Default.Storage,
                                contentDescription = null,
                                tint = TextMuted,
                                modifier = Modifier.size(42.dp)
                            )
                            Text(
                                text = "لا توجد سجلات مطابقة للبحث أو الفلتر حالياً",
                                color = TextSlate,
                                fontSize = 13.sp,
                                textAlign = TextAlign.Center
                            )
                            Button(
                                onClick = { showAddClientDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = ImmersiveBg),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("إضافة عميل يدوياً")
                            }
                        }
                    }
                }
            } else {
                items(filteredSubscriptions, key = { it.id }) { client ->
                    ClientSubscriptionItemCard(
                        client = client,
                        context = context,
                        onApprove = {
                            onUpdateStatus(client.id, "تم التحقق والسداد", true)
                        },
                        onComplete = {
                            onUpdateStatus(client.id, "مكتمل", true)
                        },
                        onEditNotes = {
                            clientToEditNotes = client
                        },
                        onDelete = {
                            clientToDelete = client
                        }
                    )
                }
            }
        }
    }

    if (showGenerateCodesDialog) {
        GenerateCodesDialog(
            onDismiss = { showGenerateCodesDialog = false },
            onGenerate = { price, count ->
                onGenerateCodes(price, count) { created ->
                    Toast.makeText(context, "تم توليد $created كود بنجاح لفئة $price", Toast.LENGTH_SHORT).show()
                }
                showGenerateCodesDialog = false
            }
        )
    }

    // Add Client Dialog
    if (showAddClientDialog) {
        AddManualClientDialog(
            onDismiss = { showAddClientDialog = false },
            onSubmit = { name, phone, model, problem, service, currency, amount, payment, ref, status, notes ->
                onAddManualClient(name, phone, model, problem, service, currency, amount, payment, ref, status, notes)
                showAddClientDialog = false
            }
        )
    }

    // Edit Notes Dialog
    clientToEditNotes?.let { client ->
        EditClientNotesDialog(
            client = client,
            onDismiss = { clientToEditNotes = null },
            onSave = { newNotes, newStatus ->
                onUpdateNotes(client.id, newNotes)
                onUpdateStatus(client.id, newStatus, newStatus == "تم التحقق والسداد" || newStatus == "مكتمل")
                clientToEditNotes = null
            }
        )
    }

    // Delete Confirmation Dialog
    clientToDelete?.let { client ->
        AlertDialog(
            onDismissRequest = { clientToDelete = null },
            title = { Text("تأكيد حذف السجل", fontWeight = FontWeight.Bold, color = TextWhite) },
            text = {
                Text(
                    "هل أنت متأكد من حذف بيانات العميل: ${client.fullName} (${client.phoneNumber}) نهائياً؟",
                    color = TextSlate
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        onDeleteClient(client.id)
                        clientToDelete = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = StatusError)
                ) {
                    Text("نعم، حذف", color = TextWhite)
                }
            },
            dismissButton = {
                TextButton(onClick = { clientToDelete = null }) {
                    Text("إلغاء", color = TextSlate)
                }
            },
            containerColor = ImmersiveSurfaceCard
        )
    }
}

@Composable
private fun ActivationCodeItemCard(
    codeEntity: ActivationCodeEntity,
    context: Context,
    onReset: () -> Unit,
    onDelete: () -> Unit
) {
    val isUsed = codeEntity.isUsed
    val statusColor = if (isUsed) StatusWarning else EmeraldPrimary
    val statusContainer = if (isUsed) AccentOrangeContainer else EmeraldContainer

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, if (isUsed) ImmersiveBorderSubtle else EmeraldPrimary.copy(alpha = 0.35f)),
        shape = RoundedCornerShape(16.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(statusContainer),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Key,
                            contentDescription = null,
                            tint = statusColor,
                            modifier = Modifier.size(18.dp)
                        )
                    }

                    Column {
                        Text(
                            text = codeEntity.code,
                            fontFamily = FontFamily.Monospace,
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = TextWhite,
                            letterSpacing = 0.8.sp
                        )
                        Text(
                            text = "الفئة: ${codeEntity.price} YER",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = AccentBlue
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusContainer,
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.45f))
                ) {
                    Text(
                        text = if (isUsed) "مستخدم" else "متاح",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            if (isUsed && (codeEntity.usedByName.isNotBlank() || codeEntity.usedByPhone.isNotBlank())) {
                HorizontalDivider(color = ImmersiveBorderSubtle)
                Text(
                    text = "المستخدم: ${codeEntity.usedByName} ${if (codeEntity.usedByPhone.isNotBlank()) "(${codeEntity.usedByPhone})" else ""}",
                    fontSize = 11.sp,
                    color = TextSlate
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = {
                        AppIntentUtils.copyToClipboard(context, "كود التفعيل", codeEntity.code)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkButtonBg,
                        contentColor = TextWhite
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(14.dp), tint = EmeraldPrimary)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("نسخ الكود", fontSize = 11.sp)
                }

                Button(
                    onClick = {
                        val msg = "كود التفعيل الخاص بك في تطبيق رقيب:\n${codeEntity.code}\nقيمة الخدمة: ${codeEntity.price} YER"
                        AppIntentUtils.openWhatsAppForCodeRequest(context, msg)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(14.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("إرسال واتساب", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                if (isUsed) {
                    IconButton(
                        onClick = onReset,
                        modifier = Modifier
                            .size(34.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(DarkButtonBg)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = "إعادة تفعيل", tint = AccentTeal, modifier = Modifier.size(16.dp))
                    }
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkButtonBg)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "حذف الكود", tint = StatusError, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun GenerateCodesDialog(
    onDismiss: () -> Unit,
    onGenerate: (price: Int, count: Int) -> Unit
) {
    var selectedPrice by remember { mutableIntStateOf(2000) }
    var countText by remember { mutableStateOf("10") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("توليد أكواد تفعيل جديدة", fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 16.sp)
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                Text("اختر فئة السعر (YER):", fontSize = 12.sp, color = TextSlate)
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(RaqeebRepository.ALLOWED_CODE_PRICES) { price ->
                        FilterChip(
                            selected = selectedPrice == price,
                            onClick = { selectedPrice = price },
                            label = { Text("$price", fontSize = 11.sp) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = EmeraldContainer,
                                selectedLabelColor = EmeraldPrimary,
                                containerColor = DarkButtonBg,
                                labelColor = TextSlate
                            )
                        )
                    }
                }

                OutlinedTextField(
                    value = countText,
                    onValueChange = { if (it.length <= 3) countText = it.filter { ch -> ch.isDigit() } },
                    label = { Text("عدد الأكواد المطلوب توليدها") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = EmeraldPrimary,
                        unfocusedBorderColor = ImmersiveBorder,
                        focusedLabelColor = EmeraldPrimary,
                        unfocusedLabelColor = TextSlate,
                        focusedTextColor = TextWhite,
                        unfocusedTextColor = TextWhite
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val count = (countText.toIntOrNull() ?: 10).coerceIn(1, 500)
                    onGenerate(selectedPrice, count)
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = ImmersiveBg)
            ) {
                Text("توليد وحفظ", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextSlate)
            }
        },
        containerColor = ImmersiveSurfaceCard
    )
}

@Composable
private fun ClientSubscriptionItemCard(
    client: ClientSubscriptionEntity,
    context: Context,
    onApprove: () -> Unit,
    onComplete: () -> Unit,
    onEditNotes: () -> Unit,
    onDelete: () -> Unit
) {
    val dateString = remember(client.createdAt) {
        try {
            val sdf = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))
            sdf.format(Date(client.createdAt))
        } catch (_: Exception) {
            "الآن"
        }
    }

    val (statusColor, statusContainer) = when (client.status) {
        "تم التحقق والسداد" -> StatusSuccess to EmeraldContainer
        "مكتمل" -> AccentTeal to AccentTealContainer
        "ملغي" -> StatusError to Color(0x26EF4444)
        else -> StatusWarning to AccentOrangeContainer
    }

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        shape = RoundedCornerShape(18.dp)
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(34.dp)
                            .clip(CircleShape)
                            .background(DarkButtonBg),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    }
                    Column {
                        Text(
                            text = client.fullName,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite
                        )
                        Text(
                            text = "جهاز: ${client.phoneModel.ifBlank { "غير محدد" }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlate
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = statusContainer,
                    border = BorderStroke(1.dp, statusColor.copy(alpha = 0.5f))
                ) {
                    Text(
                        text = client.status,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = statusColor,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }
            }

            HorizontalDivider(color = ImmersiveBorderSubtle)

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                DetailRow(label = "الخدمة المطلوبة:", value = client.serviceName, valueColor = AccentTeal)
                if (client.problemType.isNotBlank()) {
                    DetailRow(label = "نوع المشكلة:", value = client.problemType, valueColor = AccentOrange)
                }
                DetailRow(
                    label = "قيمة الاشتراك المنفصل:",
                    value = "${client.priceAmount} ${client.currency}",
                    valueColor = EmeraldPrimary,
                    isBold = true
                )
                if (client.paymentMethod.isNotBlank()) {
                    DetailRow(label = "وسيلة السداد:", value = client.paymentMethod, valueColor = TextWhite)
                }
                if (client.transactionRef.isNotBlank()) {
                    DetailRow(label = "رقم الحوالة/الكود:", value = client.transactionRef, valueColor = AccentBlue)
                }
                DetailRow(label = "تاريخ التسجيل:", value = dateString, valueColor = TextMuted)
                if (client.ownerNotes.isNotBlank()) {
                    DetailRow(label = "ملاحظات المالك:", value = client.ownerNotes, valueColor = StatusWarning)
                }
            }

            HorizontalDivider(color = ImmersiveBorderSubtle)

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        val message = """
                            مرحباً بك أستاذ ${client.fullName}،
                            معك مركز رقيب للبرمجة والتسويق.
                            بخصوص طلبك لخدمة: ${client.serviceName}
                            هاتفك: ${client.phoneModel}
                            نحن على أتم الاستعداد لخدمتك فوراً.
                        """.trimIndent()
                        AppIntentUtils.openWhatsAppWithNumber(context, client.phoneNumber, message)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = ImmersiveBg),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Chat, contentDescription = null, modifier = Modifier.size(15.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("واتساب", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                }

                OutlinedButton(
                    onClick = {
                        AppIntentUtils.dialPhoneNumber(context, client.phoneNumber)
                    },
                    modifier = Modifier.weight(1f),
                    shape = RoundedCornerShape(10.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(containerColor = DarkButtonBg, contentColor = TextWhite),
                    contentPadding = PaddingValues(horizontal = 6.dp, vertical = 6.dp)
                ) {
                    Icon(Icons.Default.Call, contentDescription = null, modifier = Modifier.size(15.dp), tint = AccentBlue)
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("اتصال", fontSize = 11.sp)
                }

                if (client.status == "قيد المراجعة") {
                    IconButton(
                        onClick = onApprove,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(EmeraldContainer)
                    ) {
                        Icon(Icons.Default.CheckCircle, contentDescription = "اعتماد السداد", tint = EmeraldPrimary, modifier = Modifier.size(18.dp))
                    }
                } else if (client.status == "تم التحقق والسداد") {
                    IconButton(
                        onClick = onComplete,
                        modifier = Modifier
                            .size(36.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(AccentTealContainer)
                    ) {
                        Icon(Icons.Default.Check, contentDescription = "إكمال الخدمة", tint = AccentTeal, modifier = Modifier.size(18.dp))
                    }
                }

                IconButton(
                    onClick = onEditNotes,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkButtonBg)
                ) {
                    Icon(Icons.Default.Edit, contentDescription = "تعديل الملاحظات", tint = TextSlate, modifier = Modifier.size(16.dp))
                }

                IconButton(
                    onClick = onDelete,
                    modifier = Modifier
                        .size(36.dp)
                        .clip(RoundedCornerShape(8.dp))
                        .background(DarkButtonBg)
                ) {
                    Icon(Icons.Default.Delete, contentDescription = "حذف السجل", tint = StatusError, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun DetailRow(
    label: String,
    value: String,
    valueColor: Color = TextWhite,
    isBold: Boolean = false
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = label, fontSize = 11.sp, color = TextSlate)
        Text(
            text = value,
            fontSize = 12.sp,
            fontWeight = if (isBold) FontWeight.ExtraBold else FontWeight.Medium,
            color = valueColor,
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun AddManualClientDialog(
    onDismiss: () -> Unit,
    onSubmit: (
        fullName: String,
        phone: String,
        phoneModel: String,
        problemType: String,
        serviceName: String,
        currency: String,
        priceAmount: Int,
        paymentMethod: String,
        transactionRef: String,
        status: String,
        ownerNotes: String
    ) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var phone by remember { mutableStateOf("") }
    var phoneModel by remember { mutableStateOf("") }
    var problemType by remember { mutableStateOf("") }
    var serviceName by remember { mutableStateOf("إسترداد البيانات والملفات") }
    var currency by remember { mutableStateOf("YER") }
    var priceAmount by remember { mutableStateOf("4000") }
    var paymentMethod by remember { mutableStateOf("الكريمي (3001845477)") }
    var transactionRef by remember { mutableStateOf("") }
    var status by remember { mutableStateOf("تم التحقق والسداد") }
    var ownerNotes by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("إضافة عميل جديد", fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 16.sp)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("الاسم واللقب") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phone,
                    onValueChange = { phone = it },
                    label = { Text("رقم الهاتف") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = phoneModel,
                    onValueChange = { phoneModel = it },
                    label = { Text("نوع / موديل الهاتف") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = serviceName,
                    onValueChange = { serviceName = it },
                    label = { Text("الخدمة المطلوبة") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    OutlinedTextField(
                        value = priceAmount,
                        onValueChange = { priceAmount = it },
                        label = { Text("المبلغ") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                    OutlinedTextField(
                        value = currency,
                        onValueChange = { currency = it },
                        label = { Text("العملة (YER/SAR/USD)") },
                        modifier = Modifier.weight(1f),
                        singleLine = true
                    )
                }
                OutlinedTextField(
                    value = transactionRef,
                    onValueChange = { transactionRef = it },
                    label = { Text("رقم الحوالة أو الكود") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
                OutlinedTextField(
                    value = ownerNotes,
                    onValueChange = { ownerNotes = it },
                    label = { Text("ملاحظات المالك") },
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (name.isNotBlank() && phone.isNotBlank()) {
                        val amount = priceAmount.toIntOrNull() ?: 2000
                        onSubmit(name, phone, phoneModel, problemType, serviceName, currency, amount, paymentMethod, transactionRef, status, ownerNotes)
                    }
                },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = ImmersiveBg)
            ) {
                Text("حفظ", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextSlate)
            }
        },
        containerColor = ImmersiveSurfaceCard
    )
}

@Composable
private fun EditClientNotesDialog(
    client: ClientSubscriptionEntity,
    onDismiss: () -> Unit,
    onSave: (notes: String, status: String) -> Unit
) {
    var notes by remember { mutableStateOf(client.ownerNotes) }
    var status by remember { mutableStateOf(client.status) }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text("تعديل حالة وملاحظات: ${client.fullName}", fontWeight = FontWeight.Bold, color = TextWhite, fontSize = 15.sp)
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Text("تغيير حالة الطلب:", fontSize = 12.sp, color = TextSlate)
                val statuses = listOf("قيد المراجعة", "تم التحقق والسداد", "مكتمل", "ملغي")
                LazyRow(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    items(statuses) { s ->
                        FilterChip(
                            selected = status == s,
                            onClick = { status = s },
                            label = { Text(s, fontSize = 11.sp) }
                        )
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("ملاحظات المالك الخاصة") },
                    modifier = Modifier.fillMaxWidth(),
                    minLines = 3
                )
            }
        },
        confirmButton = {
            Button(
                onClick = { onSave(notes, status) },
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldPrimary, contentColor = ImmersiveBg)
            ) {
                Text("حفظ التغييرات", fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("إلغاء", color = TextSlate)
            }
        },
        containerColor = ImmersiveSurfaceCard
    )
}
