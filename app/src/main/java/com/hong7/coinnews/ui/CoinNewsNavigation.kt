package com.hong7.coinnews.ui

import androidx.annotation.DrawableRes
import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hong7.coinnews.R
import com.hong7.coinnews.model.News
import com.hong7.coinnews.utils.GsonUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


sealed class MainNav(
    val title: String,
    @DrawableRes val icon: Int,
    val route: String
) {
    object Home : MainNav(NavigationTitle.MAIN_HOME, R.drawable.ic_home_24, NavigationRouteName.MAIN_HOME)
    object News : MainNav(NavigationTitle.MAIN_NEWS, R.drawable.ic_newspaper_24, NavigationRouteName.MAIN_NEWS)
    object Scrap : MainNav(NavigationTitle.MAIN_SCRAP, R.drawable.ic_bookmark, NavigationRouteName.MAIN_SCRAP)

}


object NewsDetailNav : DestinationArg<News> {

    override val argName: String = "news"
    override val route: String = NavigationRouteName.NEWS_DETAIL
    override val title: String = NavigationTitle.NEWS_DETAIL

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    override fun navigateWithArg(item: News): String {
        val encodedArg =
            URLEncoder.encode(GsonUtils.toJson(item.copy(title = "", description = "")), StandardCharsets.UTF_8.toString())

        return "$route/$encodedArg"
    }
}

object InfluencerDetailNav : DestinationArg<String> {

    override val argName: String = "influencerId"
    override val route: String = NavigationRouteName.INFLUENCER_DETAIL
    override val title: String = NavigationTitle.INFLUENCER_DETAIL

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    override fun navigateWithArg(item: String): String {
        return "$route/$item"
    }
}

object CoinListNav : Destination {
    override val route: String = NavigationRouteName.ALL_COIN_LIST
    override val title: String = NavigationTitle.ALL_COIN_LIST
}

object SettingNav : Destination {
    override val route: String = NavigationRouteName.SETTING
    override val title: String = NavigationTitle.SETTING
}

interface Destination {
    val route: String
    val title: String
}

interface DestinationArg<T> : Destination {
    val argName: String
    val arguments: List<NamedNavArgument>

    fun routeWithArgName() = "$route/{$argName}"
    fun navigateWithArg(item: T): String
}

object NavigationRouteName {
    const val MAIN_NEWS = "bews"
    const val MAIN_HOME = "home"
    const val MAIN_SCRAP = "scrap"

    const val SETTING = "setting"
    const val NEWS_DETAIL = "news_detail"
    const val INFLUENCER_DETAIL = "influencer_detail"
    const val ALL_COIN_LIST = "all_coin_list"
}

object NavigationTitle {
    const val MAIN_NEWS = "News"
    const val MAIN_HOME = "Home"
    const val MAIN_VIDEO = "Video"
    const val MAIN_SCRAP = "Scrap"

    const val SETTING = "Setting"
    const val NEWS_DETAIL = "News Detail"
    const val INFLUENCER_DETAIL = "influencer_detail"
    const val ALL_COIN_LIST = "Coin List"
}