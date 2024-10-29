package com.hong7.coinnews.ui

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
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
    object News : MainNav(NavigationRouteName.MAIN_NEWS, R.drawable.ic_newspaper_24, NavigationRouteName.MAIN_NEWS)
    object Video : MainNav(NavigationRouteName.MAIN_VIDEO, R.drawable.ic_slow_motion_video_24, NavigationRouteName.MAIN_VIDEO)

    object Explore : MainNav(NavigationRouteName.MAIN_EXPLORE, R.drawable.ic_explore_24, NavigationRouteName.MAIN_EXPLORE)

    object Scrap : MainNav(NavigationRouteName.MAIN_SCRAP, R.drawable.ic_bookmark, NavigationRouteName.MAIN_SCRAP)

}


object NewsDetailNav : DestinationArg<News> {

    override val argName: String = "news"
    override val route: String = NavigationRouteName.ARTICLE_DETAIL
    override val title: String = NavigationTitle.ARTICLE_DETAIL

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    override fun navigateWithArg(item: News): String {
        val encodedArg =
            URLEncoder.encode(GsonUtils.toJson(item), StandardCharsets.UTF_8.toString())
        return "$route/$encodedArg"
    }
}

object CoinListNav : Destination {
    override val route: String = NavigationRouteName.ALL_COIN_LIST
    override val title: String = NavigationTitle.ALL_COIN_LIST
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
    const val MAIN_NEWS = "뉴스"
    const val MAIN_EXPLORE = "익스폴로러"
    const val MAIN_VIDEO = "비디오"
    const val MAIN_SCRAP = "스크랩"
    const val MAIN_SETTING = "설정"

    const val ARTICLE_DETAIL = "news_detail"
    const val ALL_COIN_LIST = "all_coin_list"
}

object NavigationTitle {
    const val MAIN_NEWS = "뉴스"
    const val MAIN_EXPLORE = "스크랩"
    const val MAIN_VIDEO = "비디오"
    const val MAIN_SCRAP = "스크랩"
    const val MAIN_SETTING = "설정"

    const val ARTICLE_DETAIL = "뉴스_상세"
    const val ALL_COIN_LIST = "코인_목록"
}