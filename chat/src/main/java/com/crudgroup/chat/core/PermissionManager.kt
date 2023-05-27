package com.crudgroup.chat.core

import androidx.activity.result.ActivityResultRegistry
import androidx.activity.result.contract.ActivityResultContracts
import androidx.lifecycle.LifecycleOwner

class PermissionManager(
    activityResultRegistry: ActivityResultRegistry,
    lifecycleOwner: LifecycleOwner,
    onPermissionResult: (Boolean) -> Unit
) {

    val requestMultiplePermissions = activityResultRegistry.register(
        "permission",
        lifecycleOwner,
        ActivityResultContracts.RequestMultiplePermissions()
    ) { permissions ->
        permissions.values.forEach {
            if (!it) {
                onPermissionResult(it)
                return@forEach
            }
        }

        onPermissionResult(true)
    }
}