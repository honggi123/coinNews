package com.hong7.coinnews.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hong7.coinnews.model.News
import com.hong7.coinnews.utils.GsonUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object HomeNav : Destination {
    override val route: String = NavigationRouteName.MAIN_HOME
    override val title: String = NavigationTitle.MAIN_HOME
}

object NewsNav : Destination {
    override val route: String = NavigationRouteName.NEWS
    override val title: String = NavigationTitle.NEWS
}

object NewsDetailNav : DestinationArg<News> {

    override val argName: String = "news"
    override val route: String = NavigationRouteName.ARTICLE_DETAIL
    override val title: String = NavigationTitle.ARTICLE_DETAIL

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    override fun navigateWithArg(item: News): String {
        val encodedArg = URLEncoder.encode(GsonUtils.toJson(item), StandardCharsets.UTF_8.toString())
        return "$route/$encodedArg"
    }
}

object CoinListNav : Destination {
    override val route: String = NavigationRouteName.ALL_COIN_LIST
    override val title: String = NavigationTitle.ALL_COIN_LIST
}

object ScrapNav : Destination {
    override val route: String = NavigationRouteName.SCRAP
    override val title: String = NavigationTitle.SCRAP
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
    const val MAIN_HOME = "main_home"
    const val ARTICLE_DETAIL = "news_detail"
    const val ALL_COIN_LIST = "all_coin_list"
    const val NEWS = "news"
    const val SCRAP = "scrap"
    const val SETTING = "setting"
}

object NavigationTitle {
    const val MAIN_HOME = "홈"
    const val ARTICLE_DETAIL = "뉴스 상세페이지"
    const val ALL_COIN_LIST = "코인 목록"
    const val NEWS = "뉴스"
    const val SCRAP = "스크랩"
    const val SETTING = "설정"
}