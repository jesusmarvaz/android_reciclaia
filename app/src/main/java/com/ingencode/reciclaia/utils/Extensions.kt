package com.ingencode.reciclaia.utils

import android.content.Context
import android.content.pm.PackageInfo
import android.content.pm.PackageManager
import android.graphics.PorterDuff
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.util.TypedValue
import android.widget.ImageView
import androidx.annotation.AttrRes
import androidx.annotation.ColorRes
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.JsonElement
import com.google.gson.JsonParseException
import com.google.gson.JsonParser
import com.ingencode.reciclaia.data.remote.dto.DTO
import com.ingencode.reciclaia.data.remote.dto.ErrorDTO
import retrofit2.HttpException
import java.io.Serializable
import java.security.MessageDigest
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.math.roundToInt

/**
Created with ❤ by jesusmarvaz on 2025-01-12.
 */
/*
fun View.fadeOutScaling() {
    val animationSet = AnimationSet(false)
    val scaleOut = ScaleAnimation(1f, 0.6f, 1f, 0.6f, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5f)
    val fadeOut = AlphaAnimation(1f,0f)
    animationSet.apply{
        addAnimation(scaleOut)
        addAnimation(fadeOut)
        duration = 200L
        interpolator = AccelerateInterpolator(1.5F)
    }
    if(this.visibility == View.VISIBLE) this.startAnimation(animationSet)
    this.visibility = View.INVISIBLE
}

fun View.fadeInScaling() {
    val animationSet = AnimationSet(false)
    val scaleIn = ScaleAnimation(0.6f, 1f, 0.6f, 1f, Animation.RELATIVE_TO_SELF, 0.5F, Animation.RELATIVE_TO_SELF, 0.5f)
    val fadeIn = AlphaAnimation(0f,1f)
    animationSet.apply {
        addAnimation(scaleIn)
        addAnimation(fadeIn)
        duration = 200L
        interpolator = DecelerateInterpolator(1.5F)
        startOffset = 0
    }

    if(this.visibility != View.VISIBLE)
    {
        this.startAnimation(animationSet)
    }

    this.visibility = View.VISIBLE
}

fun AppCompatImageView.loadUrlImage(url: String) {
    Glide.with(context).load(url).into(this)
}



inline fun <reified T: Serializable> Fragment.getSerializable(key: String): T? {
    return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        arguments?.getSerializable(key, T::class.java)
    } else {
        arguments?.getSerializable(key) as T?
    }
}

fun AppCompatActivity.getNavController(navEnum: EnumNavHostFragments): NavController? {
    val id = when(navEnum) {
        EnumNavHostFragments.HOST -> R.id.hostNavFragment
        EnumNavHostFragments.NESTED_INITIAL -> R.id.nestedInitialNavFragment
        EnumNavHostFragments.NESTED_HOME -> R.id.nestedHomeNavFragment
        EnumNavHostFragments.NESTED_ADMIN -> R.id.nestedAdminNavFragment
    }
    return try {
        this.findNavController(id)
    } catch (e: Exception) {
        Log.e(this.nameClass, e.message.toString())
        null
    }
}

fun Context.isAnyNetworkActive(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    val result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

@ColorInt
fun Context.themeColor(@AttrRes attrRes: Int): Int = TypedValue()
    .apply { theme.resolveAttribute (attrRes, this, true) }
    .data

fun <T> Single<T>.configAsync(): Single<T> {
    return this.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}

fun Completable.configAsync(): Completable {
    return this.observeOn(AndroidSchedulers.mainThread()).subscribeOn(Schedulers.io())
}
fun Date.mapToStringDate(c: Context): String {
    val cal = Calendar.getInstance().apply { setCalendarAt0h(this@mapToStringDate) }
    val weekDay = cal.getShortWeekdayString(c)
    val monthDay = cal[Calendar.DAY_OF_MONTH]
    val month = cal.getMonthString(c)
    val year = cal[Calendar.YEAR]
    return "$weekDay $monthDay $month $year"
}

fun Date.mapToISO(): String {
    val pattern = "yyyy-MM-dd'T'HH:mm:ss.000'Z'"
    val tz = TimeZone.getTimeZone("UTC")
    val df = SimpleDateFormat(pattern)
    df.timeZone = tz
    return df.format(this)
}

fun Date.mapToTime(): String {
    val pattern = "%02d:%02d"
    val cal = Calendar.getInstance().apply { time = this@mapToTime }
    return String.format(pattern, cal[Calendar.HOUR_OF_DAY], cal[Calendar.MINUTE])
}

fun Date.mapToShortStringDate(c: Context): String {
    val cal = Calendar.getInstance().apply { setCalendarAt0h(this@mapToShortStringDate) }
    val month = cal.getShortMonthString(c)
    val year = cal[Calendar.YEAR]
    return "$month $year"
}

fun Date.mapToShortStringDate2(c: Context): String {
    val cal = Calendar.getInstance().apply { setCalendarAt0h(this@mapToShortStringDate2) }
    val year = cal[Calendar.YEAR]
    val month = cal[Calendar.MONTH] + 1
    val monthDay = cal[Calendar.DAY_OF_MONTH]
    return "$month/$monthDay/${year.toString().takeLast(2)}"
}

fun Date.mapToDay(): String {
    val cal = Calendar.getInstance().apply { setCalendarAt0h(this@mapToDay) }
    val day = cal.get(Calendar.DAY_OF_MONTH)
    return String.format("%02d", day)
}

fun Calendar.getShortMonthString(c: Context): String {
    return when(this[Calendar.MONTH]){
        Calendar.JANUARY    ->{c.getString(R.string.january_short)}
        Calendar.FEBRUARY   ->{c.getString(R.string.february_short)}
        Calendar.MARCH      ->{c.getString(R.string.march_short)}
        Calendar.APRIL      ->{c.getString(R.string.april_short)}
        Calendar.MAY        ->{c.getString(R.string.may_short)}
        Calendar.JUNE       ->{c.getString(R.string.june_short)}
        Calendar.JULY       ->{c.getString(R.string.july_short)}
        Calendar.AUGUST     ->{c.getString(R.string.august_short)}
        Calendar.SEPTEMBER  ->{c.getString(R.string.september_short)}
        Calendar.OCTOBER    ->{c.getString(R.string.october_short)}
        Calendar.NOVEMBER   ->{c.getString(R.string.november_short)}
        Calendar.DECEMBER   ->{c.getString(R.string.december_short)}
        else ->{""}
    }
}

fun Calendar.getMonthString(c: Context): String {
    return when(this[Calendar.MONTH]){
        Calendar.JANUARY    ->{c.getString(R.string.january)}
        Calendar.FEBRUARY   ->{c.getString(R.string.february)}
        Calendar.MARCH      ->{c.getString(R.string.march)}
        Calendar.APRIL      ->{c.getString(R.string.april)}
        Calendar.MAY        ->{c.getString(R.string.may)}
        Calendar.JUNE       ->{c.getString(R.string.june)}
        Calendar.JULY       ->{c.getString(R.string.july)}
        Calendar.AUGUST     ->{c.getString(R.string.august)}
        Calendar.SEPTEMBER  ->{c.getString(R.string.september)}
        Calendar.OCTOBER    ->{c.getString(R.string.october)}
        Calendar.NOVEMBER   ->{c.getString(R.string.november)}
        Calendar.DECEMBER   ->{c.getString(R.string.december)}
        else ->{""}
    }
}
fun Calendar.getShortWeekdayString(c:Context): String? {
    val wd = this[Calendar.DAY_OF_WEEK]
    val weekdays:Array<String> = arrayOf(
        c.getString(R.string.short_weekday_sunday),
        c.getString(R.string.short_weekday_monday),
        c.getString(R.string.short_weekday_tuesday),
        c.getString(R.string.short_weekday_wednesday),
        c.getString(R.string.short_weekday_thursday),
        c.getString(R.string.short_weekday_friday),
        c.getString(R.string.short_weekday_saturday)
    )
    return if(wd in 1..7) weekdays[wd-1]
    else null
}

fun Calendar.setCalendarAt0h(date: Date) {
    with(this) {
        time = date
        set(Calendar.HOUR_OF_DAY, 0)
        set(Calendar.MINUTE, 0)
        set(Calendar.SECOND, 0)
        set(Calendar.MILLISECOND, 0)
    }
}

fun AppCompatActivity.setDarkStatusBar() {
    val window = this.window
    //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    val decorView: View = window.decorView //set status background black
    decorView.systemUiVisibility = decorView.systemUiVisibility and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv() //set status text  light
    window.statusBarColor = ContextCompat.getColor(this, R.color.bcd_secondary)
}

fun AppCompatActivity.setLightStatusBar() {
    val window = this.window
    //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    getWindow().decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    window.statusBarColor = ContextCompat.getColor(this, R.color.bcd_primary)
}

fun AppCompatActivity.setFullScreenOn() {
    WindowCompat.setDecorFitsSystemWindows(window, false)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        controller.hide(WindowInsetsCompat.Type.systemBars())
        controller.systemBarsBehavior = WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
    }
}

fun AppCompatActivity.setFullScreenOff() {
    WindowCompat.setDecorFitsSystemWindows(window, true)
    WindowInsetsControllerCompat(window, window.decorView).show(WindowInsetsCompat.Type.systemBars())
}


*/

fun Long.toFormattedStringDate(): String {
    val pattern = "yyyy/MM/dd HH:mm:ss"
    val formatter = SimpleDateFormat(pattern, Locale.getDefault())
    return formatter.format(Date(this))
}

fun String.sha256(): String {
    return MessageDigest.getInstance("SHA-256")
        .digest(this.toByteArray())
        .fold("") { str, it -> str + "%02x".format(it) }
}

inline fun <reified T: Serializable> Fragment.getSerializable(key: String): T? {
    return arguments?.getSerializable(key, T::class.java)
}

fun PackageManager.getPackageInfoCompat(packageName: String, flags: Int = 0): PackageInfo =
    getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    /*    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        getPackageInfo(packageName, PackageManager.PackageInfoFlags.of(flags.toLong()))
    } else {
        @Suppress("DEPRECATION") getPackageInfo(packageName, flags)
    }*/

fun Context.dpToPx(dp: Float): Int {
    val d = this.resources.displayMetrics.density
    return (dp * d).roundToInt()
}

fun ImageView.setSizeInDp(dp: Float) {
    val size = this.context.dpToPx(dp)
    this.layoutParams.height = size
    this.layoutParams.width = size
}

fun ImageView.setTint(@ColorRes color: Int) {
    this.setColorFilter(ContextCompat.getColor(context, color), PorterDuff.Mode.SRC_IN);
}

fun Context.getThemeColor(@AttrRes attrRes: Int): Int {
    val typedValue = TypedValue()
    theme.resolveAttribute(attrRes, typedValue, true)
    return typedValue.data
}

inline fun <reified T: DTO> T.getJsonElement(): JsonElement {
    return GsonBuilder().create().toJsonTree(this, T::class.java)
}

val Any.nameClass: String
    get() = this.javaClass.name

val java.io.Serializable.mock: String
    get() = "mock"

fun Context.isAnyNetworkActive(): Boolean {
    val connectivityManager =
        this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
    val networkCapabilities = connectivityManager.activeNetwork ?: return false
    val actNw =
        connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
    val result = when {
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
        actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
        else -> false
    }
    return result
}

fun AppCompatActivity.setLightModeInStatusBar(light: Boolean) {
    val window = this.window
    //window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
    //window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS)
    //getWindow().decorView.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
    WindowInsetsControllerCompat(window, window.decorView).isAppearanceLightStatusBars = light
}

fun Throwable.classifyError(c: Context): ISealedError {
    return if (this is ISealedError) return this
    else if (!c.isAnyNetworkActive()) SealedAppError.ConnectivityError()
    else if (this is HttpException) {
        val stringDTO: String? = this.response()?.errorBody()?.string()
        try {
            val errorDto: ErrorDTO? = stringDTO?.let {
                Gson().fromJson(JsonParser.parseString(stringDTO), ErrorDTO::class.java)
            }

            val code = errorDto?.code
            when (errorDto?.type) {
                "SERVER_ERROR" -> SealedApiError.ServerError(this, code)
                "UNABLE_GET_TOKEN" -> SealedApiError.UnableToGetTokenError(this, code)
                "TOKEN_ERROR", "498" -> SealedApiError.TokenError(this, code)
                "NOT_AUTHORIZED" -> SealedApiError.NotAuthorizedError(this, code)
                "REFRESH_ERROR" -> SealedApiError.RefreshTokenError(this, code)
                "TIME_LIMIT_PASSED" -> SealedApiError.TimeLimitPassed(this, code)
                else -> {
                    when (this.code()) {
                        498 -> SealedApiError.TokenError(this, this.code())
                        else -> SealedApiError.HttpError(this, this.code())
                    }
                }
            }
        } catch (_: JsonParseException) {
            when (this.code()) {
                498 -> SealedApiError.TokenError(this, this.code())
                else -> SealedApiError.HttpError(this, this.code())
            }
        }
    } else {
        SealedAppError.DefaultError()
    }
}