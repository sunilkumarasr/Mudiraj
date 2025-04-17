package com.mudiraj.mudirajfoundation.Fragments

import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Configuration
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.RadioButton
import android.widget.RadioGroup
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.mudiraj.mudirajfoundation.Activitys.AboutUsActivity
import com.mudiraj.mudirajfoundation.Activitys.CartActivity
import com.mudiraj.mudirajfoundation.Activitys.DashBoardActivity
import com.mudiraj.mudirajfoundation.Activitys.DeleteAccountActivity
import com.mudiraj.mudirajfoundation.Activitys.EditProfileActivity
import com.mudiraj.mudirajfoundation.Activitys.PrivacyPolicyActivity
import com.mudiraj.mudirajfoundation.Activitys.TermsAndConditionsActivity
import com.mudiraj.mudirajfoundation.Config.Preferences
import com.mudiraj.mudirajfoundation.Config.ViewController
import com.mudiraj.mudirajfoundation.Logins.LoginActivity
import com.mudiraj.mudirajfoundation.R
import com.mudiraj.mudirajfoundation.databinding.FragmentMenuBinding
import java.util.Locale

class MenuFragment : Fragment() ,View.OnClickListener{

    private lateinit var binding: FragmentMenuBinding

    var userId: String = "";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMenuBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        init()

    }

    private fun init() {

        if (!ViewController.noInterNetConnectivity(requireActivity())) {
            ViewController.showToast(requireActivity(), "Please check your connection ")
            return
        } else {

        }

        val packageName = "com.mudiraj.mudirajfoundation"
        try {
            val packageInfo = requireActivity().packageManager.getPackageInfo(packageName, 0)
            val versionName = packageInfo.versionName
            binding.txtVersion.text = "v$versionName"
        } catch (e: PackageManager.NameNotFoundException) {
            e.printStackTrace()
        }


        binding.linearProfile.setOnClickListener(this)
        binding.linearShare.setOnClickListener(this)
        binding.linearAbout.setOnClickListener(this)
        binding.linearTermsAndConditions.setOnClickListener(this)
        binding.linearPrivacyPolicy.setOnClickListener(this)
        binding.linearDeleteAccount.setOnClickListener(this)
        binding.linearLogout.setOnClickListener(this)
    }

    override fun onClick(view: View?) {
        when (view?.id) {
            R.id.linearProfile -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                if (userId.equals("")){
                    val intent = Intent(requireActivity(), LoginActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
                }else{
                    val intent = Intent(requireActivity(), EditProfileActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
                }
            }
            R.id.linearCart -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), CartActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }

            R.id.linearShare -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                shareApp()
            }

            R.id.linearAbout -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), AboutUsActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearTermsAndConditions -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), TermsAndConditionsActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }
            R.id.linearPrivacyPolicy -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                val intent = Intent(requireActivity(), PrivacyPolicyActivity::class.java)
                startActivity(intent)
                requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
            }

            R.id.linearDeleteAccount -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                deleteAccountDialog()
            }
            R.id.linearLogout -> {
                val animations = ViewController.animation()
                view.startAnimation(animations)
                LogoutDialog()
            }
        }
    }

    private fun shareApp() {
        // Replace with your app's package name
        val appPackageName = requireContext().packageName
        val appLink = "https://play.google.com/store/apps/details?id=$appPackageName"
        // Create the intent
        val shareIntent = Intent(Intent.ACTION_SEND).apply {
            type = "text/plain"
            putExtra(Intent.EXTRA_SUBJECT, "Check out this app!")
            putExtra(Intent.EXTRA_TEXT, "Hey, check out this app: $appLink")
        }
        // Launch the share chooser
        startActivity(Intent.createChooser(shareIntent, "Share via"))
    }


    private fun LogoutDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_logout, null)
        bottomSheetDialog.setContentView(view)

        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
            Preferences.deleteSharedPreferences(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finishAffinity()
        }
        bottomSheetDialog.show()

    }

    private fun deleteAccountDialog() {
        val bottomSheetDialog = BottomSheetDialog(requireActivity(), R.style.AppBottomSheetDialogTheme)
        val view = layoutInflater.inflate(R.layout.bottom_sheet_delete_account, null)
        bottomSheetDialog.setContentView(view)

        val buttonCancel = view.findViewById<Button>(R.id.buttonCancel)
        val buttonOk = view.findViewById<Button>(R.id.buttonOk)
        buttonCancel.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
        }
        buttonOk.setOnClickListener {
            val animations = ViewController.animation()
            view.startAnimation(animations)
            bottomSheetDialog.dismiss()
            val intent = Intent(requireActivity(), DeleteAccountActivity::class.java)
            startActivity(intent)
            requireActivity().overridePendingTransition(R.anim.from_right, R.anim.to_left)
        }
        bottomSheetDialog.show()

    }

}