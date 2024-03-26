package com.lqp.permission

import android.Manifest
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.lqp.base.permission.EasyPermissionUtil
import com.lqp.permission.ui.theme.LQPPermissionTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            LQPPermissionTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Root { it ->
                        val text: StringBuilder = StringBuilder();
                        var index = 0;
                        it.forEach {
                            if (index == 0) {
                                text.append(it)
                            } else {
                                text.append("\n$it")
                            }
                            index++
                        }
                        val data = MyEasyPermissionData(this, false, *it)
                            .setIconRes(R.mipmap.ic_launcher)
                            .setTitle("获取权限")
                            .setMsg("app需要申请以下权限：${text}")
                            .setSettingTitle("设置权限")
                            .setSettingMsg("app需要以下权限才可运行：${text}\n是否去设置")
                        EasyPermissionUtil.reqPermission(
                            data,
                            PermissionDialogCreator(),
                            EasyPermissionCallbackKT(denied = {
                                Toast.makeText(
                                    this,
                                    "授权失败：${it.name}\n${text}",
                                    Toast.LENGTH_LONG
                                )
                                    .show()
                            }, granted = {
                                Toast.makeText(this, "授权成功${text}", Toast.LENGTH_LONG).show()
                            })
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun Root(
    modifier: Modifier = Modifier,
    model: PermissionViewModel = PermissionViewModel(),
    callback: PermissionClick
) {
    var permissions = model.permissions;

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        permissions.forEach {
            Button(onClick = { callback.onPermissionClick(it) }) {
                val text: StringBuilder = StringBuilder();
                var index = 0;
                it.forEach {
                    if (index == 0) {
                        text.append(it)
                    } else {
                        text.append("\n$it")
                    }
                    index++
                }
                Text(text = text.toString())
            }
        }
    }
}

@Preview(
    showBackground = true,
    showSystemUi = true
)
@Composable
fun RootPreview() {
    LQPPermissionTheme {
        Root {}
    }
}

class PermissionViewModel : ViewModel() {
    var permissions = arrayOf(
        arrayOf(Manifest.permission.SEND_SMS),
        arrayOf(Manifest.permission.CAMERA),
        arrayOf(Manifest.permission.ACCESS_FINE_LOCATION),
        arrayOf(Manifest.permission.READ_CONTACTS),
        arrayOf(
            Manifest.permission.WRITE_CALENDAR,
            Manifest.permission.READ_CALENDAR,
            Manifest.permission.READ_CALL_LOG
        )
    )
}

fun interface PermissionClick {
    fun onPermissionClick(permissions: Array<String>)
}