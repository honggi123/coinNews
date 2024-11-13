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
    object Info : MainNav(NavigationTitle.MAIN_INFO, R.drawable.ic_newspaper_24, NavigationRouteName.MAIN_INFO)

    object Market : MainNav(NavigationTitle.MAIN_MARKET, R.drawable.ic_align_vertical_bottom_24, NavigationRouteName.MAIN_MARKET)

    object Setting : MainNav(NavigationTitle.MAIN_SETTING, R.drawable.ic_setting, NavigationRouteName.MAIN_SETTING)

}

object NewsDetailNav : DestinationArg<News> {

    override val argName: String = "newsUrl"
    override val route: String = NavigationRouteName.NEWS_DETAIL
    override val title: String = NavigationTitle.NEWS_DETAIL

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    override fun navigateWithArg(item: News): String {
        val encodedArg =
            URLEncoder.encode((item.url), StandardCharsets.UTF_8.toString())

        return "$route/$encodedArg"
    }
}

object VideoListNav : DestinationArg<String> {

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

object NewsNav : Destination {
    override val route: String = NavigationRouteName.NEWS
    override val title: String = NavigationTitle.NEWS
}

object AddWatchListNav : Destination {
    override val route: String = NavigationRouteName.ADD_WATCH_LIST
    override val title: String = NavigationTitle.ADD_WATCH_LIST
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
    const val MAIN_INFO = "main_info"
    const val MAIN_MARKET = "main_market"
    const val MAIN_SETTING = "main_setting"

    const val NEWS = "news"
    const val NEWS_DETAIL = "news_detail"
    const val INFLUENCER_DETAIL = "influencer_detail"
    const val ADD_WATCH_LIST = "add_watch_list"
}

object NavigationTitle {
    const val MAIN_INFO = "코인 정보"
    const val MAIN_MARKET = "마켓"
    const val MAIN_SETTING = "알림 설정"

    const val NEWS = "news"
    const val NEWS_DETAIL = "News Detail"
    const val INFLUENCER_DETAIL = "influencer_detail"
    const val ADD_WATCH_LIST = "Add Watch List"
}