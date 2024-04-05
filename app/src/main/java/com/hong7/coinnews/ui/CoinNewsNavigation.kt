package com.hong7.coinnews.ui

import androidx.navigation.NamedNavArgument
import androidx.navigation.NavBackStackEntry
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.hong7.coinnews.model.Article
import com.hong7.coinnews.ui.utils.GsonUtils
import java.net.URLEncoder
import java.nio.charset.StandardCharsets


object HomeNav : Destination {
    override val route: String = NavigationRouteName.MAIN_HOME
    override val title: String = NavigationTitle.MAIN_HOME
}

object ArticleDetailNav : DestinationArg<Article> {

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
}

object NavigationTitle {
    const val MAIN_HOME = "홈"
    const val ARTICLE_DETAIL = "뉴스 상세페이지"
}