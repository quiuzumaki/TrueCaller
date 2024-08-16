package com.example.truecaller.ui.theme.icons

/*
 * Copyright 2024 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.materialIcon
import androidx.compose.material.icons.materialPath
import androidx.compose.ui.graphics.vector.ImageVector

public val Icons.Rounded.SpeakerPhone: ImageVector
    get() {
        if (_speakerPhone != null) {
            return _speakerPhone!!
        }
        _speakerPhone = materialIcon(name = "Rounded.SpeakerPhone") {
            materialPath {
                moveTo(7.76f, 7.83f)
                lineToRelative(0.02f, 0.02f)
                curveToRelative(0.35f, 0.35f, 0.89f, 0.38f, 1.3f, 0.09f)
                curveTo(9.91f, 7.37f, 10.92f, 7.02f, 12.0f, 7.02f)
                reflectiveCurveToRelative(2.09f, 0.35f, 2.92f, 0.93f)
                curveToRelative(0.4f, 0.29f, 0.95f, 0.26f, 1.3f, -0.09f)
                lineToRelative(0.02f, -0.02f)
                curveToRelative(0.42f, -0.42f, 0.39f, -1.14f, -0.09f, -1.49f)
                curveTo(14.98f, 5.5f, 13.55f, 5.0f, 12.0f, 5.0f)
                reflectiveCurveTo(9.02f, 5.5f, 7.86f, 6.34f)
                curveTo(7.37f, 6.69f, 7.34f, 7.41f, 7.76f, 7.83f)
                close()
            }
            materialPath {
                moveTo(12.0f, 1.0f)
                curveTo(9.38f, 1.0f, 6.97f, 1.93f, 5.08f, 3.47f)
                curveTo(4.62f, 3.84f, 4.57f, 4.53f, 5.0f, 4.96f)
                lineToRelative(0.0f, 0.0f)
                curveToRelative(0.36f, 0.36f, 0.93f, 0.39f, 1.32f, 0.07f)
                curveTo(7.86f, 3.76f, 9.85f, 3.0f, 12.0f, 3.0f)
                reflectiveCurveToRelative(4.14f, 0.76f, 5.69f, 2.03f)
                curveToRelative(0.39f, 0.32f, 0.96f, 0.29f, 1.32f, -0.07f)
                lineToRelative(0.0f, 0.0f)
                curveToRelative(0.42f, -0.42f, 0.38f, -1.11f, -0.08f, -1.49f)
                curveTo(17.03f, 1.93f, 14.62f, 1.0f, 12.0f, 1.0f)
                close()
            }
            materialPath {
                moveTo(15.0f, 10.0f)
                lineToRelative(-6.0f, 0.0f)
                curveToRelative(-0.55f, 0.0f, -1.0f, 0.45f, -1.0f, 1.0f)
                verticalLineToRelative(10.0f)
                curveToRelative(0.0f, 0.55f, 0.45f, 1.0f, 1.0f, 1.0f)
                horizontalLineToRelative(5.99f)
                curveToRelative(0.55f, 0.0f, 1.0f, -0.45f, 1.0f, -1.0f)
                lineTo(16.0f, 11.0f)
                curveTo(16.0f, 10.45f, 15.55f, 10.0f, 15.0f, 10.0f)
                close()
                moveTo(15.0f, 20.0f)
                horizontalLineTo(9.0f)
                verticalLineToRelative(-8.0f)
                horizontalLineToRelative(6.0f)
                verticalLineTo(20.0f)
                close()
            }
        }
        return _speakerPhone!!
    }

private var _speakerPhone: ImageVector? = null
