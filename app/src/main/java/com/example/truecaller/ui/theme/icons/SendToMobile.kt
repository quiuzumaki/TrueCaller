package com.example.truecaller.ui.theme.icons

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector
import kotlin.Deprecated

@Deprecated(
    "Use the AutoMirrored version at Icons.AutoMirrored.Rounded.SendToMobile",
    ReplaceWith( "Icons.AutoMirrored.Rounded.SendToMobile",
        "androidx.compose.material.icons.automirrored.rounded.SendToMobile"),
)
public val Icons.Rounded.SendToMobile: ImageVector
    get() {
        if (_sendToMobile != null) {
            return _sendToMobile!!
        }
        _sendToMobile = materialIcon(name = "Rounded.SendToMobile") {
            materialPath {
                moveTo(17.0f, 18.0f)
                horizontalLineTo(7.0f)
                verticalLineTo(6.0f)
                horizontalLineToRelative(10.0f)
                verticalLineToRelative(0.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(0.0f)
                curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                verticalLineTo(3.0f)
                curveToRelative(0.0f, -1.1f, -0.9f, -2.0f, -2.0f, -2.0f)
                lineTo(7.0f, 1.01f)
                curveTo(5.9f, 1.01f, 5.0f, 1.9f, 5.0f, 3.0f)
                verticalLineToRelative(18.0f)
                curveToRelative(0.0f, 1.1f, 0.9f, 2.0f, 2.0f, 2.0f)
                horizontalLineToRelative(10.0f)
                curveToRelative(1.1f, 0.0f, 2.0f, -0.9f, 2.0f, -2.0f)
                verticalLineToRelative(-3.0f)
                curveToRelative(0.0f, -0.55f, -0.45f, -1.0f, -1.0f, -1.0f)
                horizontalLineToRelative(0.0f)
                curveTo(17.45f, 17.0f, 17.0f, 17.45f, 17.0f, 18.0f)
                lineTo(17.0f, 18.0f)
                close()
            }
            materialPath {
                moveTo(21.65f, 11.65f)
                lineToRelative(-2.79f, -2.79f)
                curveTo(18.54f, 8.54f, 18.0f, 8.76f, 18.0f, 9.21f)
                verticalLineTo(11.0f)
                horizontalLineToRelative(-4.0f)
                curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                verticalLineToRelative(0.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(4.0f)
                verticalLineToRelative(1.79f)
                curveToRelative(0.0f, 0.45f, 0.54f, 0.67f, 0.85f, 0.35f)
                lineToRelative(2.79f, -2.79f)
                curveTo(21.84f, 12.16f, 21.84f, 11.84f, 21.65f, 11.65f)
                close()
            }
        }
        return _sendToMobile!!
    }

private var _sendToMobile: ImageVector? = null