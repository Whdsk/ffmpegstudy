package com.alibaba.ailab.ffmpegso

import android.Manifest
import androidx.fragment.app.FragmentActivity
import com.permissionx.guolindev.PermissionX

/**
 * @Author gaohangbo
 * @Date 2021 12/23/21 8:04 下午
 * @Describe
 */
object PermissionManager {
    fun requestPermission(activity: FragmentActivity?, onPermissionListener: OnPermissionListener) {
        PermissionX.init(activity)
            .permissions(
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
            )
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "请重新开启权限",
                    "允许",
                    "取消"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "请在设置中手动开启以下权限",
                    "允许",
                    "取消"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    onPermissionListener.onResultSuccess()
                } else {
                    Toasty.showToast("以下权限被拒绝：$deniedList 可能影响系统使用")
                }
            }
    }

    fun requestLocationPermission(
        activity: FragmentActivity?,
        onPermissionListener: OnPermissionListener
    ) {
        PermissionX.init(activity) //多个permission会回调多次
            .permissions(Manifest.permission.ACCESS_FINE_LOCATION)
            .explainReasonBeforeRequest()
            .onExplainRequestReason { scope, deniedList, beforeRequest ->
                scope.showRequestReasonDialog(
                    deniedList,
                    "请重新开启权限",
                    "允许",
                    "取消"
                )
            }
            .onForwardToSettings { scope, deniedList ->
                scope.showForwardToSettingsDialog(
                    deniedList,
                    "请在设置中手动开启以下权限",
                    "允许",
                    "取消"
                )
            }
            .request { allGranted, grantedList, deniedList ->
                if (allGranted) {
                    onPermissionListener.onResultSuccess()
                } else {
                    Toasty.showToast("以下权限被拒绝：$deniedList 可能影响系统使用")
                }
            }
    }

    interface OnPermissionListener {
        fun onResultSuccess()
    }
}