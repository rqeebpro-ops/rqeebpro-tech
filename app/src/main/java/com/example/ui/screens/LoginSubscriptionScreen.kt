package com.example.ui.screens

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCopy
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PhoneAndroid
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Smartphone
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material.icons.filled.WarningAmber
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
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
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.model.PaymentChannel
import com.example.model.PaymentCurrency
import com.example.model.PhoneServiceDetail
import com.example.model.ServicePricing
import com.example.model.UserProfile
import com.example.ui.theme.AccentBlue
import com.example.ui.theme.AccentBlueContainer
import com.example.ui.theme.AccentTeal
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldLight
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorder
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.ImmersiveSurface
import com.example.ui.theme.ImmersiveSurfaceCard
import com.example.ui.theme.ImmersiveSurfaceVariant
import com.example.ui.theme.StatusError
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite
import com.example.util.AppIntentUtils
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun LoginSubscriptionScreen(
    userProfile: UserProfile,
    services: List<PhoneServiceDetail>,
    isGateMode: Boolean = false,
    onSaveSubscription: (
        name: String,
        phone: String,
        phoneModel: String,
        problemType: String,
        serviceId: String,
        serviceName: String,
        currency: PaymentCurrency,
        priceAmount: Int,
        paymentAccount: String,
        transactionRef: String
    ) -> Unit,
    onRedeemActivationCode: (
        code: String,
        name: String,
        phone: String,
        phoneModel: String,
        problemType: String,
        serviceId: String,
        serviceName: String,
        onResult: (Boolean, String, Int?) -> Unit
    ) -> Unit = { _, _, _, _, _, _, _, cb -> cb(false, "غير متاح", null) },
    onLogout: () -> Unit,
    onProceedToServices: () -> Unit,
    onLogoClick: (() -> Unit)? = null
) {
    val context = LocalContext.current

    var fullName by remember { mutableStateOf(userProfile.fullName) }
    var phoneNumber by remember { mutableStateOf(userProfile.phoneNumber) }
    var phoneModel by remember { mutableStateOf(userProfile.phoneModel) }
    var problemType by remember { mutableStateOf(userProfile.problemType) }

    var selectedService by remember {
        mutableStateOf(
            services.firstOrNull { it.id == userProfile.selectedServiceId } ?: services.firstOrNull()
        )
    }
    var selectedCurrency by remember { mutableStateOf(userProfile.currency) }

    val paymentChannels = remember {
        listOf(
            PaymentChannel(
                id = "kuraimi",
                name = "بنك الكريمي",
                accountOrPhone = AppIntentUtils.KURAMI_ACCOUNT,
                typeLabel = "رقم الحساب",
                badge = "حساب بنكي",
                instruction = "التحويل عبر تطبيق الكريمي جوال أو أقرب وكيل كريمي إكسبرس"
            ),
            PaymentChannel(
                id = "jeep",
                name = "محفظة جيب",
                accountOrPhone = AppIntentUtils.JEEP_WALLET_PHONE,
                typeLabel = "رقم المحفظة",
                badge = "كاك بنك",
                instruction = "التحويل المباشر من محفظة جيب إلى الرقم 785502919"
            ),
            PaymentChannel(
                id = "one_cash",
                name = "ون كاش",
                accountOrPhone = AppIntentUtils.ONE_CASH_PHONE,
                typeLabel = "رقم المحفظة",
                badge = "محفظة إلكترونية",
                instruction = "التحويل الفوري من تطبيق ون كاش إلى الرقم 785502919"
            ),
            PaymentChannel(
                id = "jawwali",
                name = "محفظة جوالي",
                accountOrPhone = AppIntentUtils.JAWWALI_PHONE,
                typeLabel = "رقم المحفظة",
                badge = "اليمن والكويت",
                instruction = "التحويل المباشر من تطبيق جوالي إلى الرقم 785502919"
            )
        )
    }

    var selectedPaymentChannel by remember {
        mutableStateOf(
            paymentChannels.firstOrNull { userProfile.paymentAccountUsed.contains(it.name) }
                ?: paymentChannels.first()
        )
    }
    var transactionRef by remember { mutableStateOf(userProfile.paymentTransactionRef) }

    var currentStep by remember { mutableIntStateOf(1) }
    var validationError by remember { mutableStateOf<String?>(null) }
    var hasSentNotificationToOwner by remember { mutableStateOf(false) }
    var isEditingExisting by remember { mutableStateOf(!userProfile.isSubscribed) }
    var showCodeActivationBox by remember { mutableStateOf(false) }
    var activationCodeInput by remember { mutableStateOf("") }
    var codeActivationError by remember { mutableStateOf<String?>(null) }
    var isRedeemingCode by remember { mutableStateOf(false) }

    val currentPricing = selectedService?.pricing ?: ServicePricing(yer = 2000, sar = 15, usd = 4)
    val currentAmount = currentPricing.getAmount(selectedCurrency)

    fun buildOwnerNotificationMessage(): String {
        val dateFormat = SimpleDateFormat("yyyy/MM/dd - hh:mm a", Locale("ar"))
        val nowFormatted = dateFormat.format(Date())
        val serviceTitle = selectedService?.title ?: "خدمة برمجية وتقنية"
        return """
            🔔 إشعار طلب اشتراك ودخول للتطبيق 🔔
            مرسل إلى: مالك تطبيق مركز رقيب (773557771)
            ━━━━━━━━━━━━━━━━━━━━
            👤 الاسم واللقب: ${fullName.trim()}
            📱 رقم الهاتف: ${phoneNumber.trim()}
            📲 نوع الهاتف: ${phoneModel.trim()}
            ⚠️ نوع المشكلة: ${problemType.trim()}
            ━━━━━━━━━━━━━━━━━━━━
            🛠️ الخدمة المختارة: $serviceTitle
            💰 قيمة الاشتراك: $currentAmount ${selectedCurrency.code} (${selectedCurrency.symbolAr})
            💵 العملة المحددة: ${selectedCurrency.code}
            💳 الحساب المحول إليه: ${selectedPaymentChannel.name} (${selectedPaymentChannel.accountOrPhone})
            ${if (transactionRef.isNotBlank()) "🔖 رقم السند / العملية: ${transactionRef.trim()}" else "🔖 حالة السداد: إشعار تحويل مباشر"}
            ⏰ الوقت والتاريخ: $nowFormatted
            ━━━━━━━━━━━━━━━━━━━━
            أرجو تأكيد اشتراكي للدخول إلى خدمات التطبيق.
        """.trimIndent()
    }

    fun validateStep1(): Boolean {
        return when {
            fullName.isBlank() -> {
                validationError = "يرجى إدخال الاسم واللقب"
                false
            }
            phoneNumber.trim().length < 6 -> {
                validationError = "يرجى إدخال رقم هاتف صحيح للتواصل"
                false
            }
            phoneModel.isBlank() -> {
                validationError = "يرجى تحديد نوع وموديل الهاتف"
                false
            }
            problemType.isBlank() -> {
                validationError = "يرجى اختيار أو كتابة نوع المشكلة"
                false
            }
            else -> {
                validationError = null
                true
            }
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(ImmersiveBg),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .widthIn(max = 560.dp)
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp, vertical = 16.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Clean, Calm Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(54.dp)
                        .clip(RoundedCornerShape(16.dp))
                        .background(ImmersiveSurfaceCard)
                        .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), RoundedCornerShape(16.dp))
                        .let { mod ->
                            if (onLogoClick != null) {
                                mod.clickable(
                                    interactionSource = remember { MutableInteractionSource() },
                                    indication = null,
                                    onClick = onLogoClick
                                )
                            } else mod
                        }
                        .testTag("login_app_logo"),
                    contentAlignment = Alignment.Center
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.raqeeb_app_logo),
                        contentDescription = "شعار رقيب",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (userProfile.isSubscribed && !isEditingExisting) {
                            "ملف الاشتراك والحساب"
                        } else if (isGateMode) {
                            "مرحباً بك في رقيب"
                        } else {
                            "تسجيل اشتراك جديد"
                        },
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = if (userProfile.isSubscribed && !isEditingExisting) {
                            "بيانات اشتراكك المفعّل وخدمتك المختارة"
                        } else {
                            "أكمل البيانات في 3 خطوات بسيطة للوصول للخدمات"
                        },
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSlate
                    )
                }
            }

            if (userProfile.isSubscribed && !isEditingExisting) {
                ActiveMembershipCard(
                    userProfile = userProfile,
                    selectedServiceTitle = selectedService?.title ?: userProfile.selectedServiceName,
                    currentAmount = currentAmount,
                    selectedCurrency = selectedCurrency,
                    paymentChannelName = selectedPaymentChannel.name,
                    onProceedToServices = onProceedToServices,
                    onEditSubscription = {
                        isEditingExisting = true
                        currentStep = 1
                        hasSentNotificationToOwner = false
                    },
                    onLogout = {
                        onLogout()
                        fullName = ""
                        phoneNumber = ""
                        phoneModel = ""
                        problemType = ""
                        transactionRef = ""
                        hasSentNotificationToOwner = false
                        isEditingExisting = true
                        currentStep = 1
                    }
                )
            } else {
                // Clean 3-Step Progress Header
                LoginStepperHeader(
                    currentStep = currentStep,
                    onStepClick = { target ->
                        if (target < currentStep) {
                            validationError = null
                            currentStep = target
                        } else if (target == 2 && currentStep == 1 && validateStep1()) {
                            currentStep = 2
                        } else if (target == 3 && validateStep1()) {
                            currentStep = 3
                        }
                    }
                )

                // Validation Error Banner
                if (validationError != null) {
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = StatusError.copy(alpha = 0.12f),
                        border = BorderStroke(1.dp, StatusError.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = StatusError,
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = validationError ?: "",
                                style = MaterialTheme.typography.bodySmall,
                                fontWeight = FontWeight.SemiBold,
                                color = Color(0xFFFCA5A5)
                            )
                        }
                    }
                }

                // Animated Step Content
                AnimatedContent(
                    targetState = currentStep,
                    transitionSpec = { fadeIn() togetherWith fadeOut() },
                    label = "login_step_transition"
                ) { step ->
                    when (step) {
                        1 -> StepPersonalDetails(
                            fullName = fullName,
                            onFullNameChange = { fullName = it; validationError = null },
                            phoneNumber = phoneNumber,
                            onPhoneNumberChange = { phoneNumber = it; validationError = null },
                            phoneModel = phoneModel,
                            onPhoneModelChange = { phoneModel = it; validationError = null },
                            problemType = problemType,
                            onProblemTypeChange = { problemType = it; validationError = null },
                            onNext = {
                                if (validateStep1()) {
                                    currentStep = 2
                                }
                            }
                        )

                        2 -> StepServiceSelection(
                            services = services,
                            selectedService = selectedService,
                            selectedCurrency = selectedCurrency,
                            onSelectService = { selectedService = it },
                            onSelectCurrency = { selectedCurrency = it },
                            onBack = { currentStep = 1 },
                            onNext = { currentStep = 3 }
                        )

                        else -> StepPaymentAndConfirm(
                            fullName = fullName,
                            serviceTitle = selectedService?.title ?: "خدمة برمجية",
                            amount = currentAmount,
                            currency = selectedCurrency,
                            paymentChannels = paymentChannels,
                            selectedChannel = selectedPaymentChannel,
                            onSelectChannel = { selectedPaymentChannel = it },
                            transactionRef = transactionRef,
                            onTransactionRefChange = { transactionRef = it },
                            hasSentNotificationToOwner = hasSentNotificationToOwner,
                            onSendWhatsAppNotification = {
                                if (validateStep1()) {
                                    AppIntentUtils.openWhatsAppToOwner(context, buildOwnerNotificationMessage())
                                    hasSentNotificationToOwner = true
                                }
                            },
                            onCopyNotification = {
                                AppIntentUtils.copyToClipboard(
                                    context,
                                    "إشعار اشتراك رقيب",
                                    buildOwnerNotificationMessage()
                                )
                            },
                            onConfirmAndEnter = {
                                if (!hasSentNotificationToOwner) {
                                    validationError = "يرجى إرسال إشعار الاشتراك عبر واتساب المالك (773557771) أولاً"
                                    return@StepPaymentAndConfirm
                                }
                                if (!validateStep1()) return@StepPaymentAndConfirm

                                val service = selectedService ?: services.first()
                                val pricing = service.pricing
                                val amount = pricing.getAmount(selectedCurrency)
                                onSaveSubscription(
                                    fullName.trim(),
                                    phoneNumber.trim(),
                                    phoneModel.trim(),
                                    problemType.trim(),
                                    service.id,
                                    service.title,
                                    selectedCurrency,
                                    amount,
                                    "${selectedPaymentChannel.name} (${selectedPaymentChannel.accountOrPhone})",
                                    transactionRef.trim()
                                )
                                isEditingExisting = false
                                validationError = null
                                onProceedToServices()
                            },
                            onBack = { currentStep = 2 }
                        )
                    }
                }

                if (userProfile.isSubscribed && isEditingExisting) {
                    TextButton(
                        onClick = { isEditingExisting = false },
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    ) {
                        Text("إلغاء التعديل والعودة لحسابي", color = TextSlate)
                    }
                }

                // Direct Activation Code Redemption (zero hints)
                Card(
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
                    border = BorderStroke(1.dp, ImmersiveBorder),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable { showCodeActivationBox = !showCodeActivationBox },
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VerifiedUser,
                                    contentDescription = null,
                                    tint = EmeraldPrimary,
                                    modifier = Modifier.size(20.dp)
                                )
                                Text(
                                    text = "التفعيل المباشر عبر كود الاشتراك",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite
                                )
                            }
                            Text(
                                text = if (showCodeActivationBox) "إخفاء" else "إدخال الكود",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }

                        if (showCodeActivationBox) {
                            OutlinedTextField(
                                value = activationCodeInput,
                                onValueChange = {
                                    activationCodeInput = it
                                    codeActivationError = null
                                },
                                label = { Text("كود التفعيل") },
                                placeholder = { Text("أدخل كود التفعيل") },
                                singleLine = true,
                                shape = RoundedCornerShape(14.dp),
                                colors = OutlinedTextFieldDefaults.colors(
                                    focusedBorderColor = EmeraldPrimary,
                                    unfocusedBorderColor = ImmersiveBorder,
                                    focusedLabelColor = EmeraldPrimary,
                                    unfocusedLabelColor = TextSlate,
                                    focusedTextColor = TextWhite,
                                    unfocusedTextColor = TextWhite,
                                    cursorColor = EmeraldPrimary
                                ),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("login_activation_code_input")
                            )

                            if (codeActivationError != null) {
                                Text(
                                    text = codeActivationError ?: "",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = StatusError
                                )
                            }

                            Button(
                                onClick = {
                                    if (activationCodeInput.isBlank()) {
                                        codeActivationError = "يرجى إدخال كود التفعيل"
                                        return@Button
                                    }
                                    isRedeemingCode = true
                                    val service = selectedService ?: services.firstOrNull()
                                    onRedeemActivationCode(
                                        activationCodeInput,
                                        fullName.ifBlank { "مشترك" },
                                        phoneNumber,
                                        phoneModel,
                                        problemType,
                                        service?.id ?: "app_access",
                                        service?.title ?: "اشتراك عبر الكود"
                                    ) { success, message, _ ->
                                        isRedeemingCode = false
                                        if (success) {
                                            activationCodeInput = ""
                                            codeActivationError = null
                                            isEditingExisting = false
                                            onProceedToServices()
                                        } else {
                                            codeActivationError = message
                                        }
                                    }
                                },
                                enabled = !isRedeemingCode,
                                shape = RoundedCornerShape(14.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = EmeraldPrimary,
                                    contentColor = ImmersiveBg
                                ),
                                contentPadding = PaddingValues(vertical = 13.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .minimumInteractiveComponentSize()
                                    .testTag("login_redeem_code_button")
                            ) {
                                Text(
                                    text = if (isRedeemingCode) "جاري التحقق..." else "تفعيل الآن",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
private fun LoginStepperHeader(
    currentStep: Int,
    onStepClick: (Int) -> Unit
) {
    val steps = listOf(
        1 to "بياناتك",
        2 to "الخدمة",
        3 to "السداد"
    )

    Surface(
        shape = RoundedCornerShape(16.dp),
        color = ImmersiveSurfaceCard,
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 12.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            steps.forEachIndexed { index, (stepNum, label) ->
                val isCurrent = currentStep == stepNum
                val isCompleted = currentStep > stepNum

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier
                        .clip(RoundedCornerShape(10.dp))
                        .clickable { onStepClick(stepNum) }
                        .padding(horizontal = 6.dp, vertical = 4.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(
                                when {
                                    isCurrent -> EmeraldPrimary
                                    isCompleted -> EmeraldContainer
                                    else -> DarkButtonBg
                                }
                            )
                            .border(
                                width = 1.dp,
                                color = when {
                                    isCurrent || isCompleted -> EmeraldPrimary
                                    else -> DarkButtonBorder
                                },
                                shape = CircleShape
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        if (isCompleted) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = null,
                                tint = EmeraldPrimary,
                                modifier = Modifier.size(14.dp)
                            )
                        } else {
                            Text(
                                text = stepNum.toString(),
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isCurrent) ImmersiveBg else TextSlate
                            )
                        }
                    }

                    Text(
                        text = label,
                        fontSize = 13.sp,
                        fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Medium,
                        color = when {
                            isCurrent -> TextWhite
                            isCompleted -> EmeraldLight
                            else -> TextMuted
                        }
                    )
                }

                if (index < steps.lastIndex) {
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .padding(horizontal = 4.dp)
                            .height(1.dp)
                            .background(if (isCompleted) EmeraldPrimary.copy(alpha = 0.5f) else ImmersiveBorder)
                    )
                }
            }
        }
    }
}

@Composable
private fun StepPersonalDetails(
    fullName: String,
    onFullNameChange: (String) -> Unit,
    phoneNumber: String,
    onPhoneNumberChange: (String) -> Unit,
    phoneModel: String,
    onPhoneModelChange: (String) -> Unit,
    problemType: String,
    onProblemTypeChange: (String) -> Unit,
    onNext: () -> Unit
) {
    val quickBrands = remember {
        listOf("سامسونج", "شاومي", "آيفون", "هواوي", "ريلمي", "أوبو", "تكنو")
    }
    val quickProblems = remember {
        listOf(
            "فك رمز القفل",
            "تجاوز حساب جوجل FRP",
            "تفعيل 3G / 4G",
            "استعادة بيانات محذوفة",
            "تعريب النظام",
            "تفليش وإصلاح النظام",
            "تسويق رقمي"
        )
    }

    val fieldColors = OutlinedTextFieldDefaults.colors(
        focusedBorderColor = EmeraldPrimary,
        unfocusedBorderColor = ImmersiveBorder,
        focusedLabelColor = EmeraldPrimary,
        unfocusedLabelColor = TextSlate,
        focusedTextColor = TextWhite,
        unfocusedTextColor = TextWhite,
        focusedPlaceholderColor = TextMuted,
        unfocusedPlaceholderColor = TextMuted,
        cursorColor = EmeraldPrimary,
        focusedContainerColor = ImmersiveSurfaceVariant,
        unfocusedContainerColor = ImmersiveSurfaceVariant
    )

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "بياناتك الشخصية والجهاز",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            OutlinedTextField(
                value = fullName,
                onValueChange = onFullNameChange,
                label = { Text("الاسم واللقب") },
                placeholder = { Text("مثال: احمد عبلان") },
                leadingIcon = { Icon(Icons.Default.Person, contentDescription = null, tint = EmeraldPrimary) },
                singleLine = true,
                colors = fieldColors,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_fullname_input")
            )

            OutlinedTextField(
                value = phoneNumber,
                onValueChange = onPhoneNumberChange,
                label = { Text("رقم الهاتف (واتساب)") },
                placeholder = { Text("مثال: 785502919") },
                leadingIcon = { Icon(Icons.Default.PhoneAndroid, contentDescription = null, tint = EmeraldPrimary) },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                singleLine = true,
                colors = fieldColors,
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("login_phone_input")
            )

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = phoneModel,
                    onValueChange = onPhoneModelChange,
                    label = { Text("نوع وموديل الهاتف") },
                    placeholder = { Text("مثال: سامسونج A54") },
                    leadingIcon = { Icon(Icons.Default.Smartphone, contentDescription = null, tint = AccentTeal) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_device_input")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickBrands) { brand ->
                        val selected = phoneModel.contains(brand)
                        QuickSelectChip(
                            text = brand,
                            selected = selected,
                            onClick = {
                                onPhoneModelChange(
                                    if (phoneModel.isBlank() || quickBrands.any { phoneModel == it }) brand
                                    else "$brand $phoneModel"
                                )
                            }
                        )
                    }
                }
            }

            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedTextField(
                    value = problemType,
                    onValueChange = onProblemTypeChange,
                    label = { Text("نوع المشكلة أو الطلب") },
                    placeholder = { Text("مثال: نسيت رمز القفل") },
                    leadingIcon = { Icon(Icons.Default.WarningAmber, contentDescription = null, tint = AccentTeal) },
                    singleLine = true,
                    colors = fieldColors,
                    shape = RoundedCornerShape(14.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("login_problem_input")
                )

                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(quickProblems) { item ->
                        QuickSelectChip(
                            text = item,
                            selected = problemType == item,
                            onClick = { onProblemTypeChange(item) }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(4.dp))

            Button(
                onClick = onNext,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary,
                    contentColor = ImmersiveBg
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("login_step1_next")
            ) {
                Text("التالي: اختيار الخدمة", fontWeight = FontWeight.Bold, fontSize = 15.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(18.dp))
            }
        }
    }
}

@Composable
private fun StepServiceSelection(
    services: List<PhoneServiceDetail>,
    selectedService: PhoneServiceDetail?,
    selectedCurrency: PaymentCurrency,
    onSelectService: (PhoneServiceDetail) -> Unit,
    onSelectCurrency: (PaymentCurrency) -> Unit,
    onBack: () -> Unit,
    onNext: () -> Unit
) {
    val activePricing = selectedService?.pricing ?: ServicePricing(2000, 15, 4)

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Text(
                    text = "اختر الخدمة والعملة",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = TextWhite
                )
                Text(
                    text = "كل خدمة لها اشتراك منفصل، اختر العملة المناسبة لك:",
                    style = MaterialTheme.typography.bodySmall,
                    color = TextSlate
                )
            }

            // Clean 3-Segment Currency Toggle
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
                    horizontalArrangement = Arrangement.spacedBy(4.dp)
                ) {
                    PaymentCurrency.values().forEach { currency ->
                        val isSelected = selectedCurrency == currency
                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isSelected) EmeraldPrimary else Color.Transparent,
                            modifier = Modifier
                                .weight(1f)
                                .clip(RoundedCornerShape(10.dp))
                                .clickable { onSelectCurrency(currency) }
                                .minimumInteractiveComponentSize()
                        ) {
                            Box(
                                contentAlignment = Alignment.Center,
                                modifier = Modifier.padding(vertical = 8.dp)
                            ) {
                                Text(
                                    text = "${currency.code} (${currency.symbolAr})",
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) ImmersiveBg else TextSlate,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }
                        }
                    }
                }
            }

            // Clean List of Services
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                services.forEach { service ->
                    val isSelected = selectedService?.id == service.id
                    val price = service.pricing.getAmount(selectedCurrency)
                    Surface(
                        shape = RoundedCornerShape(14.dp),
                        color = if (isSelected) EmeraldContainer else ImmersiveSurfaceVariant,
                        border = BorderStroke(1.dp, if (isSelected) EmeraldPrimary else ImmersiveBorderSubtle),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { onSelectService(service) }
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
                                    .size(22.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) EmeraldPrimary else DarkButtonBg)
                                    .border(1.dp, if (isSelected) EmeraldPrimary else DarkButtonBorder, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Icon(
                                        imageVector = Icons.Default.Check,
                                        contentDescription = null,
                                        tint = ImmersiveBg,
                                        modifier = Modifier.size(14.dp)
                                    )
                                }
                            }

                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = service.title,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold,
                                    color = TextWhite,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                Text(
                                    text = service.type.titleAr,
                                    style = MaterialTheme.typography.labelSmall,
                                    color = TextSlate,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isSelected) EmeraldPrimary else DarkButtonBg
                            ) {
                                Text(
                                    text = "$price ${selectedCurrency.code}",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isSelected) ImmersiveBg else EmeraldPrimary,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Selected Price Equivalent Summary
            Text(
                text = "المعادل: ${activePricing.yer} YER • ${activePricing.sar} SAR • ${activePricing.usd} USD",
                style = MaterialTheme.typography.labelSmall,
                color = TextSlate,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth()
            )

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onBack,
                    shape = RoundedCornerShape(14.dp),
                    border = BorderStroke(1.dp, DarkButtonBorder),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = DarkButtonBg,
                        contentColor = TextWhite
                    ),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    modifier = Modifier
                        .weight(0.4f)
                        .minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("السابق", fontWeight = FontWeight.SemiBold)
                }

                Button(
                    onClick = onNext,
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = EmeraldPrimary,
                        contentColor = ImmersiveBg
                    ),
                    contentPadding = PaddingValues(vertical = 14.dp),
                    modifier = Modifier
                        .weight(0.6f)
                        .minimumInteractiveComponentSize()
                        .testTag("login_step2_next")
                ) {
                    Text("التالي: السداد", fontWeight = FontWeight.Bold)
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(16.dp))
                }
            }
        }
    }
}

@Composable
private fun StepPaymentAndConfirm(
    fullName: String,
    serviceTitle: String,
    amount: Int,
    currency: PaymentCurrency,
    paymentChannels: List<PaymentChannel>,
    selectedChannel: PaymentChannel,
    onSelectChannel: (PaymentChannel) -> Unit,
    transactionRef: String,
    onTransactionRefChange: (String) -> Unit,
    hasSentNotificationToOwner: Boolean,
    onSendWhatsAppNotification: () -> Unit,
    onCopyNotification: () -> Unit,
    onConfirmAndEnter: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current

    fun getWalletIcon(channelId: String): ImageVector = when (channelId) {
        "kuraimi" -> Icons.Default.AccountBalance
        "jeep" -> Icons.Default.AccountBalanceWallet
        "one_cash" -> Icons.Default.CreditCard
        else -> Icons.Default.PhoneAndroid
    }

    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, ImmersiveBorder),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Order Summary Pill
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ImmersiveSurfaceVariant,
                border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = serviceTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = TextWhite,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = "المشترك: $fullName",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextSlate
                        )
                    }
                    Surface(
                        shape = RoundedCornerShape(10.dp),
                        color = EmeraldContainer
                    ) {
                        Text(
                            text = "$amount ${currency.code}",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = EmeraldPrimary,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                }
            }

            Text(
                text = "اختر وسيلة السداد المناسبة",
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = TextWhite
            )

            // Clean 2x2 Wallet Grid (no duplicate dropdown!)
            Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                paymentChannels.chunked(2).forEach { rowChannels ->
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        rowChannels.forEach { channel ->
                            val isSelected = selectedChannel.id == channel.id
                            Surface(
                                shape = RoundedCornerShape(14.dp),
                                color = if (isSelected) AccentBlueContainer else ImmersiveSurfaceVariant,
                                border = BorderStroke(1.dp, if (isSelected) AccentBlue else ImmersiveBorderSubtle),
                                modifier = Modifier
                                    .weight(1f)
                                    .clip(RoundedCornerShape(14.dp))
                                    .clickable { onSelectChannel(channel) }
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    Icon(
                                        imageVector = getWalletIcon(channel.id),
                                        contentDescription = null,
                                        tint = if (isSelected) AccentBlue else TextSlate,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Column {
                                        Text(
                                            text = channel.name,
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = TextWhite,
                                            maxLines = 1,
                                            overflow = TextOverflow.Ellipsis
                                        )
                                        Text(
                                            text = channel.badge,
                                            fontSize = 10.sp,
                                            color = TextSlate,
                                            maxLines = 1
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
            }

            // Selected Account Number Display
            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ImmersiveSurface,
                border = BorderStroke(1.dp, AccentBlue.copy(alpha = 0.4f)),
                modifier = Modifier.fillMaxWidth()
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
                        Column {
                            Text(
                                text = "${selectedChannel.typeLabel} (${selectedChannel.name})",
                                style = MaterialTheme.typography.labelSmall,
                                color = TextSlate
                            )
                            Text(
                                text = selectedChannel.accountOrPhone,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = TextWhite,
                                letterSpacing = 1.sp
                            )
                        }
                        OutlinedButton(
                            onClick = {
                                AppIntentUtils.copyToClipboard(
                                    context,
                                    selectedChannel.name,
                                    selectedChannel.accountOrPhone
                                )
                            },
                            shape = RoundedCornerShape(10.dp),
                            border = BorderStroke(1.dp, DarkButtonBorder),
                            colors = ButtonDefaults.outlinedButtonColors(
                                containerColor = DarkButtonBg,
                                contentColor = TextWhite
                            ),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.minimumInteractiveComponentSize()
                        ) {
                            Icon(Icons.Default.ContentCopy, contentDescription = null, modifier = Modifier.size(15.dp))
                            Spacer(modifier = Modifier.width(6.dp))
                            Text("نسخ", fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }

                    Text(
                        text = selectedChannel.instruction,
                        style = MaterialTheme.typography.bodySmall,
                        color = TextSlate
                    )
                }
            }

            OutlinedTextField(
                value = transactionRef,
                onValueChange = onTransactionRefChange,
                label = { Text("رقم السند أو الحوالة (اختياري)") },
                placeholder = { Text("مثال: 1048593") },
                leadingIcon = { Icon(Icons.Default.ReceiptLong, contentDescription = null, tint = AccentBlue) },
                singleLine = true,
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = AccentBlue,
                    unfocusedBorderColor = ImmersiveBorder,
                    focusedLabelColor = AccentBlue,
                    unfocusedLabelColor = TextSlate,
                    focusedTextColor = TextWhite,
                    unfocusedTextColor = TextWhite,
                    focusedPlaceholderColor = TextMuted,
                    unfocusedPlaceholderColor = TextMuted,
                    cursorColor = AccentBlue,
                    focusedContainerColor = ImmersiveSurfaceVariant,
                    unfocusedContainerColor = ImmersiveSurfaceVariant
                ),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier.fillMaxWidth()
            )

            HorizontalDivider(color = ImmersiveBorderSubtle)

            if (hasSentNotificationToOwner) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = EmeraldContainer,
                    border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.6f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier.padding(14.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Icon(Icons.Default.CheckCircle, contentDescription = null, tint = EmeraldPrimary, modifier = Modifier.size(20.dp))
                            Text(
                                text = "تم توجيه إشعار الاشتراك لواتساب المالك",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = EmeraldPrimary
                            )
                        }
                        Text(
                            text = "تأكد من إرسال الرسالة في واتساب للرقم 773557771، ثم اضغط على زر التأكيد أدناه.",
                            style = MaterialTheme.typography.bodySmall,
                            color = TextWhite
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                            TextButton(onClick = onSendWhatsAppNotification, contentPadding = PaddingValues(0.dp)) {
                                Text("إعادة فتح واتساب", fontSize = 12.sp, color = EmeraldLight)
                            }
                            TextButton(onClick = onCopyNotification, contentPadding = PaddingValues(0.dp)) {
                                Text("نسخ نص الإشعار", fontSize = 12.sp, color = TextSlate)
                            }
                        }
                    }
                }
            }

            // Primary Action: Send WhatsApp Notification to Owner
            Button(
                onClick = onSendWhatsAppNotification,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (hasSentNotificationToOwner) DarkButtonBg else EmeraldPrimary,
                    contentColor = if (hasSentNotificationToOwner) EmeraldPrimary else ImmersiveBg
                ),
                border = if (hasSentNotificationToOwner) BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)) else null,
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("login_send_whatsapp_button")
            ) {
                Icon(Icons.AutoMirrored.Filled.Send, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasSentNotificationToOwner) "إعادة إرسال إشعار الواتساب (773557771)" else "إرسال إشعار الاشتراك للمالك (773557771)",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            // Confirmation & Enter Button
            Button(
                onClick = onConfirmAndEnter,
                enabled = hasSentNotificationToOwner,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary,
                    contentColor = ImmersiveBg,
                    disabledContainerColor = DarkButtonBg,
                    disabledContentColor = TextMuted
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("login_confirm_enter_button")
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, modifier = Modifier.size(18.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (hasSentNotificationToOwner) "تأكيد والدخول لخدمات التطبيق" else "الدخول متاح بعد إرسال الإشعار",
                    fontWeight = FontWeight.Bold,
                    fontSize = 14.sp
                )
            }

            TextButton(
                onClick = onBack,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .minimumInteractiveComponentSize()
            ) {
                Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = null, modifier = Modifier.size(16.dp), tint = TextSlate)
                Spacer(modifier = Modifier.width(4.dp))
                Text("العودة لاختيار الخدمة", color = TextSlate)
            }
        }
    }
}

@Composable
private fun ActiveMembershipCard(
    userProfile: UserProfile,
    selectedServiceTitle: String,
    currentAmount: Int,
    selectedCurrency: PaymentCurrency,
    paymentChannelName: String,
    onProceedToServices: () -> Unit,
    onEditSubscription: () -> Unit,
    onLogout: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = ImmersiveSurfaceCard),
        border = BorderStroke(1.dp, EmeraldPrimary.copy(alpha = 0.5f)),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(22.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(46.dp)
                        .clip(CircleShape)
                        .background(EmeraldContainer),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.VerifiedUser,
                        contentDescription = null,
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(24.dp)
                    )
                }
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = userProfile.fullName.ifBlank { "عضو مركز رقيب" },
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite
                    )
                    Text(
                        text = "اشتراك مفعّل",
                        style = MaterialTheme.typography.labelMedium,
                        color = EmeraldPrimary
                    )
                }
            }

            Surface(
                shape = RoundedCornerShape(14.dp),
                color = ImmersiveSurfaceVariant,
                border = BorderStroke(1.dp, ImmersiveBorderSubtle),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(
                    modifier = Modifier.padding(14.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    ProfileDetailRow("رقم الهاتف", userProfile.phoneNumber)
                    ProfileDetailRow("نوع الهاتف", userProfile.phoneModel)
                    if (userProfile.problemType.isNotBlank()) {
                        ProfileDetailRow("نوع المشكلة", userProfile.problemType)
                    }
                    HorizontalDivider(color = ImmersiveBorderSubtle, modifier = Modifier.padding(vertical = 2.dp))
                    ProfileDetailRow("الخدمة المختارة", selectedServiceTitle)
                    ProfileDetailRow("قيمة الاشتراك", "$currentAmount ${selectedCurrency.code}", valueColor = EmeraldPrimary)
                    ProfileDetailRow("وسيلة السداد", paymentChannelName)
                }
            }

            Button(
                onClick = onProceedToServices,
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = EmeraldPrimary,
                    contentColor = ImmersiveBg
                ),
                contentPadding = PaddingValues(vertical = 14.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .minimumInteractiveComponentSize()
                    .testTag("active_proceed_services_button")
            ) {
                Text("الانتقال إلى خدمات التطبيق", fontWeight = FontWeight.Bold, fontSize = 15.sp)
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onEditSubscription,
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
                    Icon(Icons.Default.Edit, contentDescription = null, modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("تحديث / خدمة جديدة", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }

                OutlinedButton(
                    onClick = onLogout,
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, StatusError.copy(alpha = 0.4f)),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = StatusError.copy(alpha = 0.08f),
                        contentColor = StatusError
                    ),
                    modifier = Modifier.minimumInteractiveComponentSize()
                ) {
                    Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "تسجيل الخروج", modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("خروج", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                }
            }
        }
    }
}

@Composable
private fun ProfileDetailRow(
    label: String,
    value: String,
    valueColor: Color = TextWhite
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = TextSlate
        )
        Text(
            text = value.ifBlank { "—" },
            style = MaterialTheme.typography.bodySmall,
            fontWeight = FontWeight.Bold,
            color = valueColor,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
            modifier = Modifier.widthIn(max = 220.dp),
            textAlign = TextAlign.End
        )
    }
}

@Composable
private fun QuickSelectChip(
    text: String,
    selected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(50.dp),
        color = if (selected) EmeraldContainer else DarkButtonBg,
        border = BorderStroke(1.dp, if (selected) EmeraldPrimary else DarkButtonBorder),
        modifier = Modifier
            .clip(RoundedCornerShape(50.dp))
            .clickable(onClick = onClick)
    ) {
        Text(
            text = text,
            fontSize = 12.sp,
            fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
            color = if (selected) EmeraldPrimary else TextSlate,
            modifier = Modifier.padding(horizontal = 12.dp, vertical = 7.dp)
        )
    }
}
