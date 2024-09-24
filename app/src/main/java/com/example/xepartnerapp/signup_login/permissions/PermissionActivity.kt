package com.example.xepartnerapp.signup_login.permissions

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.text.Html
import android.text.Html.FROM_HTML_MODE_LEGACY
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.example.xepartnerapp.HomeCsoActivity
import com.example.xepartnerapp.HomeDriverActivity
import com.example.xepartnerapp.R
import com.example.xepartnerapp.databinding.ActivityPermissionBinding

const val REQUEST_CODE = 300

class PermissionActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPermissionBinding
    private var isDriver: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPermissionBinding.inflate(layoutInflater)
        setContentView(binding.root)

        isDriver = intent.getBooleanExtra("isDriver", true)

        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED &&
            ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_COARSE_LOCATION
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            val intent = Intent(
                this@PermissionActivity,
                if (isDriver) HomeDriverActivity::class.java else HomeCsoActivity::class.java
            )
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
            startActivity(intent)
        }

        val guideContent1 = getColoredSpanned(
            getString(R.string.GuideContent1),
            "#979EB6"
        )
        val guideContent2 = getColoredSpanned(getString(R.string.GuideContent2), "#1152FD")
        val guideContent3 = getColoredSpanned(getString(R.string.GuideContent3), "#979EB6")
        val guideContent4 = getColoredSpanned(getString(R.string.GuideContent4), "#1152FD")
        val guideContent5 = getColoredSpanned(getString(R.string.GuideContent5), "#979EB6")
        binding.guideContent.text = Html.fromHtml(
            guideContent1 + guideContent2 + guideContent3 + guideContent4 + guideContent5,
            FROM_HTML_MODE_LEGACY
        )

        binding.permissionButton.setOnClickListener {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(
                    android.Manifest.permission.ACCESS_FINE_LOCATION,
                    android.Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                REQUEST_CODE
            )
        }

    }

    private fun getColoredSpanned(text: String, color: String): String {
        return "<font color=$color>$text</font>"
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(
                        this@PermissionActivity,
                        getString(R.string.WelcomeXePartner),
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(
                        this@PermissionActivity,
                        if (isDriver) HomeDriverActivity::class.java else HomeCsoActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                } else {
                    Toast.makeText(
                        this@PermissionActivity,
                        getString(R.string.PleaseGrantLocation),
                        Toast.LENGTH_LONG
                    ).show()
                    val intent = Intent(
                        this@PermissionActivity,
                        if (isDriver) HomeDriverActivity::class.java else HomeCsoActivity::class.java
                    )
                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK)
                    startActivity(intent)
                }
            }
        }
    }

    @Deprecated("Deprecated in Java")
    override fun onBackPressed() {
        AlertDialog.Builder(this)
            .setTitle(getString(R.string.ExitTheApp))
            .setMessage(getString(R.string.AreYouExit))
            .setNegativeButton(getString(R.string.Exit)) { _, _ ->
                super.onBackPressed() // Call the default back button action
            }
            .setPositiveButton(getString(R.string.Return), null)
            .show()
    }
}
