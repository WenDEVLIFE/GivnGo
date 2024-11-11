package com.go.givngo

import android.content.Context
import android.content.SharedPreferences
import android.os.Build
import android.provider.Settings

object SharedPreferences {
    
    private const val PREFERENCES_FILE = "MyAppPrefs"
    
    private const val KEY_EMAIL = "user_email"
    private const val KEY_PROFILE_IMAGE_URI = "user_profileimage"
    private const val KEY_ORG_NAME = "user_firstname"
    private const val KEY_FIRSTNAME = "user_firstname"
    private const val KEY_LASTNAME = "user_lastname"
    private const val KEY_BANK_RESULT_PRIVACY = "bank_result_privacy"
    private const val KEY_ALLOW_NOTIFICATIONS_ONE = "allownotifications1"
    private const val KEY_ALLOW_VIBRATIONS = "allowvibrations"
    private const val KEY_SAVED_SCAN_SOUND = "Scan_sound"
    private const val KEY_IS_LOGGED_IN = "101"
    private const val KEY_TUTORIAL_ONE = "000"
    private const val KEY_USER_AGREE = "status_useragreed"
    private const val KEY_IS_SUBSCRIBED = "billing_check"
    private const val KEY_BILLING_YEARLY = "billing_check_yearly"
    private const val KEY_BILLING_WEEKLY = "billing_check_weekly"
    private const val KEY_BG_THEME = "backgroundtheme"
    private const val KEY_GUEST_ID = "GUEST_ID"
    private const val KEY_GUEST_STATUS = "GUEST_STATUS"
    private const val KEY_SUBSCRIPTION_TIME = "User_subscription_time"
    private const val KEY_PASSWORDHASH = "PASSWORDHASH"
    private const val KEY_REGIS_STATUS = "USER_REGISTRATION_TYPE"
    private const val KEY_ACCOUNTBASIC = "ACCOUNTBASIC"
private const val KEY_PRIMARY_ADDRESS = "ACCOUNTADDRESS"


    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREFERENCES_FILE, Context.MODE_PRIVATE)
    }
    
    // Logged In methods
    fun getIsLoggedIn(context: Context): Boolean {
    return getSharedPreferences(context).getBoolean(KEY_IS_LOGGED_IN, false)
    }

    fun setIsLoggedIn(context: Context, value: Boolean) {
    getSharedPreferences(context).edit().putBoolean(KEY_IS_LOGGED_IN, value).apply()
    }
    
    //address
    fun saveAdd(context: Context, emailId: String) {
        getSharedPreferences(context).edit().putString(KEY_PRIMARY_ADDRESS, emailId).apply()
    }

    fun getAdd(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_PRIMARY_ADDRESS, null)
    }

    fun clearAdd(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_PRIMARY_ADDRESS).apply()
    }

    // Email methods
    fun saveEmail(context: Context, emailId: String) {
        getSharedPreferences(context).edit().putString(KEY_EMAIL, emailId).apply()
    }

    fun getEmail(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_EMAIL, null)
    }

    fun clearEmail(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_EMAIL).apply()
    }
    
      // Password verification
    fun saveHashPass(context: Context, emailId: String) {
        getSharedPreferences(context).edit().putString(KEY_PASSWORDHASH, emailId).apply()
    }

    fun getHashPass(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_PASSWORDHASH, null)
    }

    fun clearHashPass(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_PASSWORDHASH).apply()
    }
    
     // Status Current User methods
    fun saveStatus(context: Context, emailId: String) {
        getSharedPreferences(context).edit().putString(KEY_REGIS_STATUS, emailId).apply()
    }

    fun getStatusType(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_REGIS_STATUS, null)
    }

    fun clearStatusType(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_REGIS_STATUS).apply()
    }
    
    //Organization Name
    fun saveOrgName(context: Context, firstName: String) {
        getSharedPreferences(context).edit().putString(KEY_ORG_NAME, firstName).apply()
    }

    fun getOrgName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_ORG_NAME, null)
    }

    fun clearOrgName(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_ORG_NAME).apply()
    }
    
    //AccountTyoe
    fun saveBasicType(context: Context, firstName: String) {
        getSharedPreferences(context).edit().putString(KEY_ACCOUNTBASIC, firstName).apply()
    }

    fun getBasicType(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_ACCOUNTBASIC, null)
    }

    fun clearBasicType(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_ACCOUNTBASIC).apply()
    }


    // First Name methods
    fun saveFirstName(context: Context, firstName: String) {
        getSharedPreferences(context).edit().putString(KEY_FIRSTNAME, firstName).apply()
    }

    fun getFirstName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_FIRSTNAME, null)
    }

    fun clearFirstName(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_FIRSTNAME).apply()
    }
    
    fun saveLastName(context: Context, firstName: String) {
        getSharedPreferences(context).edit().putString(KEY_LASTNAME, firstName).apply()
    }

    fun getLastName(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_LASTNAME, null)
    }

    fun clearLastName(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_LASTNAME).apply()
    }

    // Profile Image URI methods
    fun saveProfileImageUri(context: Context, imageUri: String) {
        getSharedPreferences(context).edit().putString(KEY_PROFILE_IMAGE_URI, imageUri).apply()
    }

    fun getProfileImageUri(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_PROFILE_IMAGE_URI, null)
    }

    fun clearProfileImageUri(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_PROFILE_IMAGE_URI).apply()
    }

    

   /* // Bank Result Privacy methods
    fun setBankResultPrivacy(context: Context, isEnabled: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_BANK_RESULT_PRIVACY, isEnabled).apply()
    }

    fun isBankResultPrivacyEnabled(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_BANK_RESULT_PRIVACY, false)
    }

    fun clearBankResultPrivacy(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_BANK_RESULT_PRIVACY).apply()
    } */

    // Allow Notifications methods
    fun setAllowNotifications(context: Context, isEnabled: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_ALLOW_NOTIFICATIONS_ONE, isEnabled).apply()
    }

    fun isAllowNotificationsEnabled(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_ALLOW_NOTIFICATIONS_ONE, false)
    }

    fun clearAllowNotifications(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_ALLOW_NOTIFICATIONS_ONE).apply()
    }

    // Allow Vibrations methods
    fun setAllowVibe(context: Context, isEnabled: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_ALLOW_VIBRATIONS, isEnabled).apply()
    }

    fun isAllowVibe(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_ALLOW_VIBRATIONS, false)
    }

    fun clearAllowVibe(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_ALLOW_VIBRATIONS).apply()
    }

    // Scan Sound methods
  /*  fun saveScanSound(context: Context, sound: String) {
        getSharedPreferences(context).edit().putString(KEY_SAVED_SCAN_SOUND, sound).apply()
    }

    fun getScanSound(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_SAVED_SCAN_SOUND, null)
    }

    fun clearScanSound(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_SAVED_SCAN_SOUND).apply()
    } */

    // Background Theme methods
    fun saveBackgroundTheme(context: Context, theme: String) {
        getSharedPreferences(context).edit().putString(KEY_BG_THEME, theme).apply()
    }

    fun getBackgroundTheme(context: Context): String? {
        return getSharedPreferences(context).getString(KEY_BG_THEME, null)
    }

    fun clearBackgroundTheme(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_BG_THEME).apply()
    }

    // Subscription methods
    fun notifySubscriptionStatus(context: Context, isSubscribed: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_IS_SUBSCRIBED, isSubscribed).apply()
    }

    fun isSubscribed(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_IS_SUBSCRIBED, false)
    }

   /* 
   USE FEATURE SOON BE CAREFULL WITH IT.
   
   // Guest ID methods
    fun getOrCreateGuestId(context: Context): String {
        val prefs = getSharedPreferences(context)
        var guestId = prefs.getString(KEY_GUEST_ID, null)

        if (guestId == null) {
            val deviceId = Settings.Secure.getString(context.contentResolver, Settings.Secure.ANDROID_ID)
            val brand = Build.BRAND
            val model = Build.MODEL
            guestId = "guest_$brand_$model_$deviceId"
            prefs.edit().putString(KEY_GUEST_ID, guestId).apply()
        }

        return guestId
    }

    // Guest Status methods
    fun getGuestAccountStatus(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_GUEST_STATUS, false)
    }

    fun clearGuestAccountStatus(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_GUEST_STATUS).apply()
    }

    fun notifyGuestAccountStatus(context: Context, status: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_GUEST_STATUS, status).apply()
    } */

    // Subscription Time methods
    fun getSubscriptionTime(context: Context): Long {
        return getSharedPreferences(context).getLong(KEY_SUBSCRIPTION_TIME, -1)
    }

    fun clearSubscriptionTime(context: Context) {
        getSharedPreferences(context).edit().remove(KEY_SUBSCRIPTION_TIME).apply()
    }

    fun notifySubscriptionTime(context: Context, time: Long) {
        getSharedPreferences(context).edit().putLong(KEY_SUBSCRIPTION_TIME, time).apply()
    }

    // Tutorial Status methods
    fun isOkayTutorial(context: Context): Boolean {
        return getSharedPreferences(context).getBoolean(KEY_TUTORIAL_ONE, false)
    }

    fun setOkayTutorial(context: Context, isOkay: Boolean) {
        getSharedPreferences(context).edit().putBoolean(KEY_TUTORIAL_ONE, isOkay).apply()
    }
}

/* 

// Save an email
SharedPreferencesManager.saveEmail(context, "user@example.com")

// Retrieve an email
val email = SharedPreferencesManager.getEmail(context)

// Check if logged in
val isLoggedIn = SharedPreferencesManager.isLoggedIn

// Set logged in status
SharedPreferencesManager.isLoggedIn = true

*/