/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Data & Repository Layer
 * File: UpdateRepository.kt
 * Description: Data repository and storage component for UpdateRepository.kt.
 */

package com.sameerasw.essentials.data.repository

import android.content.Context
import com.google.gson.Gson
import com.sameerasw.essentials.domain.model.UpdateInfo
import com.sameerasw.essentials.utils.AppUtil
import com.sameerasw.essentials.utils.AutoUpdateManagerHelper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.net.HttpURLConnection
import java.net.URL

class UpdateRepository {
    companion object {
        const val DEFAULT_REPO_OWNER = "sameerasw"
        const val DEFAULT_REPO_NAME = "essentials"
        const val DEFAULT_REPO_FULL_NAME = "$DEFAULT_REPO_OWNER/$DEFAULT_REPO_NAME"
        const val ESSENTIALS_UPDATE_JSON_URL = "https://sameerasw.com/essentials-update.json"
        const val ESSENTIALS_UPDATE_HOST = "sameerasw.com"
        const val GITHUB_RELEASES_URL = "https://github.com/$DEFAULT_REPO_FULL_NAME/releases"
        const val GITHUB_API_RELEASES_URL = "https://api.github.com/repos/$DEFAULT_REPO_FULL_NAME/releases"
    }

    suspend fun checkForUpdates(
        context: Context,
        isPreReleaseCheckEnabled: Boolean,
        currentVersion: String,
    ): UpdateInfo? =
        withContext(Dispatchers.IO) {
            try {
                val autoUpdateHelper = AutoUpdateManagerHelper(context)
                val updateFeatures =
                    autoUpdateHelper.checkForUpdate(ESSENTIALS_UPDATE_JSON_URL)

                if (updateFeatures != null && updateFeatures.latestversion.isNotEmpty()) {
                    val latestVersion = updateFeatures.latestversion
                    val hasUpdate = isNewerVersion(currentVersion, latestVersion)
                    return@withContext UpdateInfo(
                        versionName = latestVersion,
                        releaseNotes = updateFeatures.changelog,
                        downloadUrl = updateFeatures.apk_url,
                        releaseUrl =
                            if (updateFeatures.changelog.startsWith(
                                    "http",
                                )
                            ) {
                                updateFeatures.changelog
                            } else {
                                GITHUB_RELEASES_URL
                            },
                        isUpdateAvailable = hasUpdate,
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }

            checkForUpdatesFromGitHub(isPreReleaseCheckEnabled, currentVersion)
        }

    private suspend fun checkForUpdatesFromGitHub(
        isPreReleaseCheckEnabled: Boolean,
        currentVersion: String,
    ): UpdateInfo? =
        withContext(Dispatchers.IO) {
            try {
                val urlString =
                    if (isPreReleaseCheckEnabled) {
                        GITHUB_API_RELEASES_URL
                    } else {
                        "$GITHUB_API_RELEASES_URL/latest"
                    }

                val url = URL(urlString)
                val connection = url.openConnection() as HttpURLConnection

                if (connection.responseCode != 200) {
                    return@withContext null
                }

                val releaseData = connection.inputStream.bufferedReader().readText()

                @Suppress("UNCHECKED_CAST")
                val release: Map<String, Any>? =
                    if (isPreReleaseCheckEnabled) {
                        val releases =
                            Gson()
                                .fromJson(releaseData, Array<Any>::class.java)
                                .filterIsInstance<Map<String, Any>>()

                        releases.maxWithOrNull { rel1, rel2 ->
                            val tag1 = (rel1["tag_name"] as? String)?.removePrefix("v") ?: "0.0.0"
                            val tag2 = (rel2["tag_name"] as? String)?.removePrefix("v") ?: "0.0.0"
                            AppUtil.compareSemanticVersions(tag1, tag2)
                        }
                    } else {
                        Gson().fromJson(releaseData, Map::class.java) as? Map<String, Any>
                    }

                if (release == null) return@withContext null

                val latestVersion = (release["tag_name"] as? String)?.removePrefix("v") ?: "0.0"
                val body = release["body"] as? String ?: ""
                val releaseUrl = release["html_url"] as? String ?: ""
                val assets = (release["assets"] as? List<*>)?.filterIsInstance<Map<String, Any>>()
                val downloadUrl =
                    assets
                        ?.firstOrNull { it["name"].toString() == "app-release.apk" }
                        ?.get("browser_download_url") as? String
                        ?: assets
                            ?.firstOrNull { it["name"].toString().endsWith(".apk") }
                            ?.get("browser_download_url") as? String
                        ?: ""

                val hasUpdate = isNewerVersion(currentVersion, latestVersion)

                UpdateInfo(
                    versionName = latestVersion,
                    releaseNotes = body,
                    downloadUrl = downloadUrl,
                    releaseUrl = releaseUrl,
                    isUpdateAvailable = hasUpdate,
                )
            } catch (e: Exception) {
                e.printStackTrace()
                null
            }
        }

    private fun isNewerVersion(
        current: String,
        latest: String,
    ): Boolean = AppUtil.compareSemanticVersions(latest, current) > 0
}
