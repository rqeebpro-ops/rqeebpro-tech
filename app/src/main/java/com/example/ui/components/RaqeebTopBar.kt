package com.example.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Chat
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.minimumInteractiveComponentSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.R
import com.example.ui.theme.DarkButtonBg
import com.example.ui.theme.DarkButtonBorder
import com.example.ui.theme.EmeraldContainer
import com.example.ui.theme.EmeraldPrimary
import com.example.ui.theme.ImmersiveBg
import com.example.ui.theme.ImmersiveBorderSubtle
import com.example.ui.theme.TextSlate
import com.example.ui.theme.TextWhite

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RaqeebTopBar(
    title: String,
    subtitle: String? = null,
    onNavigateBack: (() -> Unit)? = null,
    onLogoClick: (() -> Unit)? = null,
    onContactWhatsApp: () -> Unit,
    onOpenProfile: (() -> Unit)? = null
) {
    TopAppBar(
        modifier = Modifier.drawBehind {
            drawLine(
                color = ImmersiveBorderSubtle,
                start = Offset(0f, size.height),
                end = Offset(size.width, size.height),
                strokeWidth = 1.dp.toPx()
            )
        },
        navigationIcon = {
            if (onNavigateBack != null) {
                IconButton(
                    onClick = onNavigateBack,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .testTag("topbar_back_button")
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "رجوع",
                        tint = TextWhite
                    )
                }
            }
        },
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                if (onNavigateBack == null) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(DarkButtonBg)
                            .border(1.dp, DarkButtonBorder, RoundedCornerShape(12.dp))
                            .let { mod ->
                                if (onLogoClick != null) {
                                    mod.clickable(
                                        interactionSource = remember { MutableInteractionSource() },
                                        indication = null,
                                        onClick = onLogoClick
                                    )
                                } else mod
                            }
                            .testTag("topbar_app_logo"),
                        contentAlignment = Alignment.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.raqeeb_app_logo),
                            contentDescription = "شعار رقيب",
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }
                }
                Column {
                    Text(
                        text = title,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = TextWhite,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    if (!subtitle.isNullOrBlank()) {
                        Text(
                            text = subtitle,
                            style = MaterialTheme.typography.labelSmall,
                            color = TextSlate,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                    }
                }
            }
        },
        actions = {
            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.padding(end = 8.dp)
            ) {
                if (onOpenProfile != null) {
                    IconButton(
                        onClick = onOpenProfile,
                        modifier = Modifier
                            .minimumInteractiveComponentSize()
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(DarkButtonBg)
                            .border(1.dp, DarkButtonBorder, CircleShape)
                            .testTag("topbar_profile_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccountCircle,
                            contentDescription = "حسابي واشتراكي",
                            tint = TextWhite,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                IconButton(
                    onClick = onContactWhatsApp,
                    modifier = Modifier
                        .minimumInteractiveComponentSize()
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(EmeraldContainer)
                        .border(1.dp, EmeraldPrimary.copy(alpha = 0.4f), CircleShape)
                        .testTag("topbar_whatsapp_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Chat,
                        contentDescription = "الدعم عبر واتساب",
                        tint = EmeraldPrimary,
                        modifier = Modifier.size(18.dp)
                    )
                }
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = ImmersiveBg,
            titleContentColor = TextWhite
        )
    )
}
