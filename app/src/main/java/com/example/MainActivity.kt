package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Build
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.model.ServiceSection
import com.example.ui.MainViewModel
import com.example.ui.components.MoreToolsBottomSheet
import com.example.ui.components.RaqeebBottomBar
import com.example.ui.components.RaqeebTopBar
import com.example.ui.dialogs.RequestServiceDialog
import com.example.ui.screens.ApnConfigScreen
import com.example.ui.screens.ContactUsScreen
import com.example.ui.screens.DataRecoveryScreen
import com.example.ui.screens.LockSimulatorScreen
import com.example.ui.screens.LoginSubscriptionScreen
import com.example.ui.screens.MarketingScreen
import com.example.ui.screens.MyRequestsScreen
import com.example.ui.screens.OwnerDatabaseScreen
import com.example.ui.screens.PhoneServicesScreen
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.RaqeebTheme
import com.example.util.AppIntentUtils

class MainActivity : ComponentActivity() {

    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            RaqeebTheme {
                CompositionLocalProvider(LocalLayoutDirection provides LayoutDirection.Rtl) {
                    RaqeebMainApp(viewModel = viewModel)
                }
            }
        }
    }
}

@Composable
fun RaqeebMainApp(viewModel: MainViewModel) {
    val context = LocalContext.current
    val currentSection by viewModel.currentSection.collectAsStateWithLifecycle()
    val selectedFilter by viewModel.selectedProblemFilter.collectAsStateWithLifecycle()
    val requests by viewModel.userRequests.collectAsStateWithLifecycle()
    val isScanning by viewModel.isScanning.collectAsStateWithLifecycle()
    val scanProgress by viewModel.scanProgress.collectAsStateWithLifecycle()
    val scannedMedia by viewModel.scannedMedia.collectAsStateWithLifecycle()
    val selectedMediaIds by viewModel.selectedMediaIds.collectAsStateWithLifecycle()
    val restoredMediaIds by viewModel.restoredMediaIds.collectAsStateWithLifecycle()
    val isDiagnosing by viewModel.isDiagnosingNetwork.collectAsStateWithLifecycle()
    val diagnosticResult by viewModel.networkDiagnosticResult.collectAsStateWithLifecycle()
    val marketingBudget by viewModel.marketingBudget.collectAsStateWithLifecycle()
    val userProfile by viewModel.userProfile.collectAsStateWithLifecycle()

    // Owner State Flows
    val ownerAuthStage by viewModel.ownerAuthStage.collectAsStateWithLifecycle()
    val ownerSubscriptions by viewModel.allClientSubscriptions.collectAsStateWithLifecycle()
    val subscribersCount by viewModel.subscribersCount.collectAsStateWithLifecycle()
    val revenueYER by viewModel.revenueYER.collectAsStateWithLifecycle()
    val revenueSAR by viewModel.revenueSAR.collectAsStateWithLifecycle()
    val revenueUSD by viewModel.revenueUSD.collectAsStateWithLifecycle()
    val activationCodes by viewModel.allActivationCodes.collectAsStateWithLifecycle()
    val totalCodesCount by viewModel.totalCodesCount.collectAsStateWithLifecycle()
    val availableCodesCount by viewModel.availableCodesCount.collectAsStateWithLifecycle()
    val usedCodesCount by viewModel.usedCodesCount.collectAsStateWithLifecycle()

    var showQuickRequestDialog by remember { mutableStateOf(false) }
    var showMoreMenuSheet by remember { mutableStateOf(false) }

    // Gate Mode: Unsubscribed user sees the calm, focused 3-step Login & Subscription screen
    if (!userProfile.isSubscribed && currentSection != ServiceSection.OWNER_DATABASE) {
        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = ImmersiveBg,
            topBar = {
                RaqeebTopBar(
                    title = "مركز رقيب",
                    subtitle = "تسجيل الدخول والاشتراك",
                    onLogoClick = { viewModel.onAppLogoTapped() },
                    onContactWhatsApp = {
                        AppIntentUtils.openWhatsAppToOwner(
                            context,
                            "مرحباً مركز رقيب، أود الاستفسار عن الاشتراك والدخول للتطبيق."
                        )
                    },
                    onOpenProfile = null
                )
            }
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                LoginSubscriptionScreen(
                    userProfile = userProfile,
                    services = viewModel.phoneServices,
                    isGateMode = true,
                    onSaveSubscription = { name, phone, phoneModel, problemType, serviceId, serviceName, currency, priceAmount, paymentAccount, transactionRef ->
                        viewModel.saveLoginAndSubscription(
                            name = name,
                            phone = phone,
                            phoneModel = phoneModel,
                            problemType = problemType,
                            serviceId = serviceId,
                            serviceName = serviceName,
                            currency = currency,
                            priceAmount = priceAmount,
                            paymentAccount = paymentAccount,
                            transactionRef = transactionRef,
                            markAsPaid = true
                        )
                    },
                    onRedeemActivationCode = { code, name, phone, phoneModel, problemType, serviceId, serviceName, onResult ->
                        viewModel.redeemActivationCodeAndSubscribe(
                            rawCode = code,
                            name = name,
                            phone = phone,
                            phoneModel = phoneModel,
                            problemType = problemType,
                            serviceId = serviceId,
                            serviceName = serviceName,
                            onResult = onResult
                        )
                    },
                    onLogout = {
                        viewModel.logoutOrClearSubscription()
                    },
                    onProceedToServices = {
                        viewModel.setSection(ServiceSection.PHONE_SOLUTIONS)
                    },
                    onLogoClick = {
                        viewModel.onAppLogoTapped()
                    }
                )
            }
        }
        return
    }

    // Handle system Back button smoothly across secondary sections
    val isSecondarySection = currentSection != ServiceSection.PHONE_SOLUTIONS
    BackHandler(enabled = isSecondarySection) {
        viewModel.setSection(ServiceSection.PHONE_SOLUTIONS)
    }

    val sectionTitle = when (currentSection) {
        ServiceSection.PHONE_SOLUTIONS -> "حلول وبرمجة الهاتف"
        ServiceSection.LOCK_RECOVERY_SIMULATOR -> "محاكي فك القفل"
        ServiceSection.MARKETING -> "التسويق الرقمي"
        ServiceSection.DATA_RECOVERY_SCANNER -> "فاحص استرداد البيانات"
        ServiceSection.APN_SETTINGS -> "تفعيل بيانات 3G/4G"
        ServiceSection.LOGIN_SUBSCRIPTION -> "حسابي واشتراكي"
        ServiceSection.MY_REQUESTS -> "طلباتي واستشاراتي"
        ServiceSection.CONTACT_US -> "مركز المساعدة"
        ServiceSection.OWNER_DATABASE -> "لوحة تحكم المالك"
    }

    val showBackArrow = currentSection == ServiceSection.LOCK_RECOVERY_SIMULATOR ||
        currentSection == ServiceSection.DATA_RECOVERY_SCANNER ||
        currentSection == ServiceSection.APN_SETTINGS ||
        currentSection == ServiceSection.CONTACT_US ||
        currentSection == ServiceSection.OWNER_DATABASE

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = ImmersiveBg,
        topBar = {
            RaqeebTopBar(
                title = sectionTitle,
                subtitle = if (!showBackArrow) "مركز رقيب للبرمجة والتسويق" else null,
                onNavigateBack = if (showBackArrow) {
                    { viewModel.setSection(ServiceSection.PHONE_SOLUTIONS) }
                } else null,
                onLogoClick = { viewModel.onAppLogoTapped() },
                onContactWhatsApp = {
                    AppIntentUtils.openWhatsAppToOwner(
                        context,
                        "مرحباً مركز رقيب، أود الاستفسار عن خدماتكم."
                    )
                },
                onOpenProfile = if (!showBackArrow && currentSection != ServiceSection.LOGIN_SUBSCRIPTION) {
                    { viewModel.setSection(ServiceSection.LOGIN_SUBSCRIPTION) }
                } else null
            )
        },
        bottomBar = {
            if (userProfile.isSubscribed) {
                RaqeebBottomBar(
                    currentSection = currentSection,
                    requestsCount = requests.size,
                    onSectionSelected = { viewModel.setSection(it) },
                    onOpenMoreMenu = { showMoreMenuSheet = true }
                )
            }
        },
        floatingActionButton = {
            if (currentSection == ServiceSection.PHONE_SOLUTIONS || currentSection == ServiceSection.MARKETING) {
                ExtendedFloatingActionButton(
                    onClick = { showQuickRequestDialog = true },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("fab_quick_request")
                ) {
                    Icon(Icons.Default.Build, contentDescription = null)
                    Text(
                        text = "طلب خدمة",
                        modifier = Modifier.padding(start = 6.dp),
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            when (currentSection) {
                ServiceSection.PHONE_SOLUTIONS -> {
                    PhoneServicesScreen(
                        services = viewModel.phoneServices,
                        selectedFilter = selectedFilter,
                        onFilterChange = { viewModel.setProblemFilter(it) },
                        onNavigateSection = { viewModel.setSection(it) },
                        onSubmitRequest = { title, category, subCategory, device, phone, notes ->
                            viewModel.submitServiceRequest(title, category, subCategory, device, phone, notes) {}
                        }
                    )
                }

                ServiceSection.LOCK_RECOVERY_SIMULATOR -> {
                    LockSimulatorScreen(
                        onBackToPhoneServices = { viewModel.setSection(ServiceSection.PHONE_SOLUTIONS) }
                    )
                }

                ServiceSection.MARKETING -> {
                    MarketingScreen(
                        packages = viewModel.marketingPackages,
                        marketingBudget = marketingBudget,
                        onBudgetChange = { viewModel.setMarketingBudget(it) },
                        onSubmitRequest = { title, category, subCategory, device, phone, notes ->
                            viewModel.submitServiceRequest(title, category, subCategory, device, phone, notes) {}
                        }
                    )
                }

                ServiceSection.DATA_RECOVERY_SCANNER -> {
                    DataRecoveryScreen(
                        isScanning = isScanning,
                        scanProgress = scanProgress,
                        scannedMedia = scannedMedia,
                        selectedIds = selectedMediaIds,
                        restoredIds = restoredMediaIds,
                        onStartScan = { viewModel.startMediaScan() },
                        onToggleSelect = { viewModel.toggleMediaSelection(it) },
                        onRestoreSelected = { viewModel.restoreSelectedMedia() },
                        onSubmitLabRequest = { title, category, subCategory, device, phone, notes ->
                            viewModel.submitServiceRequest(title, category, subCategory, device, phone, notes) {}
                        }
                    )
                }

                ServiceSection.APN_SETTINGS -> {
                    ApnConfigScreen(
                        profiles = viewModel.apnProfiles,
                        isDiagnosing = isDiagnosing,
                        diagnosticResult = diagnosticResult,
                        onRunDiagnostic = { viewModel.runNetworkDiagnostic(it) }
                    )
                }

                ServiceSection.LOGIN_SUBSCRIPTION -> {
                    LoginSubscriptionScreen(
                        userProfile = userProfile,
                        services = viewModel.phoneServices,
                        isGateMode = false,
                        onSaveSubscription = { name, phone, phoneModel, problemType, serviceId, serviceName, currency, priceAmount, paymentAccount, transactionRef ->
                            viewModel.saveLoginAndSubscription(
                                name = name,
                                phone = phone,
                                phoneModel = phoneModel,
                                problemType = problemType,
                                serviceId = serviceId,
                                serviceName = serviceName,
                                currency = currency,
                                priceAmount = priceAmount,
                                paymentAccount = paymentAccount,
                                transactionRef = transactionRef,
                                markAsPaid = true
                            )
                        },
                        onRedeemActivationCode = { code, name, phone, phoneModel, problemType, serviceId, serviceName, onResult ->
                            viewModel.redeemActivationCodeAndSubscribe(
                                rawCode = code,
                                name = name,
                                phone = phone,
                                phoneModel = phoneModel,
                                problemType = problemType,
                                serviceId = serviceId,
                                serviceName = serviceName,
                                onResult = onResult
                            )
                        },
                        onLogout = {
                            viewModel.logoutOrClearSubscription()
                        },
                        onProceedToServices = {
                            viewModel.setSection(ServiceSection.PHONE_SOLUTIONS)
                        },
                        onLogoClick = {
                            viewModel.onAppLogoTapped()
                        }
                    )
                }

                ServiceSection.MY_REQUESTS -> {
                    MyRequestsScreen(
                        requests = requests,
                        onDeleteRequest = { viewModel.deleteRequest(it) }
                    )
                }

                ServiceSection.CONTACT_US -> {
                    ContactUsScreen(
                        onLogoClick = { viewModel.onAppLogoTapped() }
                    )
                }

                ServiceSection.OWNER_DATABASE -> {
                    OwnerDatabaseScreen(
                        authStage = ownerAuthStage,
                        onVerifyPassword = { viewModel.verifyOwnerPassword(it) },
                        onVerifyPin = { viewModel.verifyOwnerPin(it) },
                        onBackToPasswordStep = { viewModel.resetOwnerAuthToPasswordStep() },
                        subscriptions = ownerSubscriptions,
                        subscribersCount = subscribersCount,
                        revenueYER = revenueYER,
                        revenueSAR = revenueSAR,
                        revenueUSD = revenueUSD,
                        activationCodes = activationCodes,
                        totalCodesCount = totalCodesCount,
                        availableCodesCount = availableCodesCount,
                        usedCodesCount = usedCodesCount,
                        onGenerateCodes = { price, count, onDone ->
                            viewModel.generateActivationCodes(price, count, onDone)
                        },
                        onResetCode = { viewModel.resetActivationCode(it) },
                        onDeleteCode = { viewModel.deleteActivationCode(it) },
                        onUpdateStatus = { id, status, approved ->
                            viewModel.updateClientSubscriptionStatus(id, status, approved)
                        },
                        onUpdateNotes = { id, notes ->
                            viewModel.updateClientSubscriptionNotes(id, notes)
                        },
                        onDeleteClient = { id ->
                            viewModel.deleteClientSubscription(id)
                        },
                        onAddManualClient = { name, phone, model, problem, service, currency, amount, payment, ref, status, notes ->
                            viewModel.addManualClientSubscription(name, phone, model, problem, service, currency, amount, payment, ref, status, notes)
                        },
                        onLockDashboard = {
                            viewModel.lockOwnerDashboard()
                        }
                    )
                }
            }
        }
    }

    if (showMoreMenuSheet) {
        MoreToolsBottomSheet(
            currentSection = currentSection,
            onDismiss = { showMoreMenuSheet = false },
            onSelectSection = { viewModel.setSection(it) }
        )
    }

    if (showQuickRequestDialog) {
        val initialCategory = if (currentSection == ServiceSection.MARKETING) "تسويق إلكتروني" else "صيانة وبرمجة هواتف"
        val initialSubCategory = if (currentSection == ServiceSection.MARKETING) "حملة إعلانية ممولة" else "فك شفرة أو إقفال هاتف"

        RequestServiceDialog(
            initialCategory = initialCategory,
            initialSubCategory = initialSubCategory,
            onDismiss = { showQuickRequestDialog = false },
            onSubmit = { title, category, subCategory, device, phone, notes ->
                viewModel.submitServiceRequest(title, category, subCategory, device, phone, notes) {}
            }
        )
    }
}

// Greeting kept for test compatibility
@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(text = "Hello $name!", modifier = modifier)
}
