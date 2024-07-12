package com.hong7.coinnews.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.utils.GsonUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object HomeNav : Destination {
    override val route: String = NavigationRouteName.MAIN_HOME
    override val title: String = NavigationTitle.MAIN_HOME
}

object NewsDetailNav : DestinationArg<Article> {

    override val argName: String = "article"
    override val route: String = NavigationRouteName.ARTICLE_DETAIL
    override val title: String = NavigationTitle.ARTICLE_DETAIL

    override val arguments: List<NamedNavArgument> = listOf(
        navArgument(argName) { type = NavType.StringType }
    )

    override fun navigateWithArg(item: Article): String {
        val title = URLEncoder.encode(item.title, StandardCharsets.UTF_8.toString()).replace("+", "%20")
        val description = URLEncoder.encode(item.description, StandardCharsets.UTF_8.toString()).replace("+", "%20")
        val newUrl = URLEncoder.encode(item.url, StandardCharsets.UTF_8.toString())
        val arg = GsonUtils.toJson(item.copy(title = title, url = newUrl, description = description))
        return "$route/$arg"
    }

    override fun findArgument(navBackStackEntry: NavBackStackEntry): Article? {
        val articleString = navBackStackEntry.arguments?.getString(argName)
        return GsonUtils.fromJson<Article>(articleString)
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
    fun findArgument(navBackStackEntry: NavBackStackEntry): T?
}

object NavigationRouteName {
    const val MAIN_HOME = "main_home"
    const val ARTICLE_DETAIL = "article_detail"
    const val ALL_COIN_LIST = "all_coin_list"
    const val SCRAP = "scrap"
    const val SETTING = "setting"
}

object NavigationTitle {
    const val MAIN_HOME = "홈"
    const val ARTICLE_DETAIL = "뉴스 상세페이지"
    const val ALL_COIN_LIST = "코인 목록"
    const val SCRAP = "스크랩"
    const val SETTING = "설정"
}