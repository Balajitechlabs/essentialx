/*
 * Copyright (c) 2026 sameerasw.com
 * License: MIT License
 *
 * Feature Module: Background Services & Receivers
 * File: AppUpdateWorker.kt
 * Description: Background service component for AppUpdateWorker.kt.
 */

package com.sameerasw.essentials.services

import android.content.Context
import android.util.Log
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.sameerasw.essentials.data.repository.GitHubRepository
import com.sameerasw.essentials.data.repository.SettingsRepository
import com.sameerasw.essentials.data.repository.UpdateRepository
import com.sameerasw.essentials.utils.AppUtil
import com.sameerasw.essentials.utils.UpdateNotificationHelper
import com.sameerasw.essentials.viewmodels.MainViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

class AppUpdateWorker(
    appContext: Context,
    workerParams: WorkerParameters,
) : CoroutineWorker(appContext, workerParams) {
    override suspend fun doWork(): Result = coroutineScope {
        Log.d("AppUpdateWorker", "Executing periodic update check")
        val context = applicationContext
        val settingsRepository = SettingsRepository(context)
        val updateRepository = UpdateRepository()
        val gitHubRepository = GitHubRepository()

        try {
            val isAutoUpdateEnabled =
                settingsRepository.getBoolean(
                    SettingsRepository.KEY_AUTO_UPDATE_ENABLED,
                    true,
                )

            // 1. Essentials App Update Check
            if (isAutoUpdateEnabled) {
                val currentVersion =
                    try {
                        context.packageManager.getPackageInfo(context.packageName, 0).versionName
                    } catch (e: Exception) {
                        "0.0"
                    } ?: "0.0"

                val isPreReleaseEnabled =
                    settingsRepository.getBoolean(
                        SettingsRepository.KEY_CHECK_PRE_RELEASES_ENABLED,
                        false,
                    )

                val updateInfo =
                    updateRepository.checkForUpdates(context, isPreReleaseEnabled, currentVersion)

                if (updateInfo != null && updateInfo.isUpdateAvailable) {
                    MainViewModel.cachedIsUpdateAvailable = true
                    MainViewModel.cachedUpdateInfo = updateInfo

                    if (updateInfo.downloadUrl.isNotEmpty()) {
                        val isNotifEnabled =
                            settingsRepository.getBoolean(
                                SettingsRepository.KEY_UPDATE_NOTIFICATION_ENABLED,
                                true,
                            )
                        if (isNotifEnabled) {
                            UpdateNotificationHelper.showUpdateNotification(
                                context,
                                updateInfo.versionName,
                                updateInfo.downloadUrl,
                            )
                        }
                    }
                }
            }

            // 2. Tracked GitHub Repositories Update Check
            val trackedRepos = settingsRepository.getTrackedRepos()
            if (trackedRepos.isNotEmpty()) {
                val token = settingsRepository.getGitHubToken()
                val updatedRepos = trackedRepos.toMutableList()
                var changesMade = false

                for (i in updatedRepos.indices) {
                    val repo = updatedRepos[i]
                    try {
                        val fetchResult =
                            if (repo.allowPreReleases) {
                                gitHubRepository.getReleasesWithETag(repo.owner, repo.name, token, repo.lastETag)
                            } else {
                                gitHubRepository.getLatestReleaseWithETag(repo.owner, repo.name, token, repo.lastETag)
                            }

                    var isUpdateAvailable = repo.isUpdateAvailable
                    var release = fetchResult.release
                    var downloadUrl = repo.downloadUrl ?: ""
                    var releaseNotes = repo.latestReleaseBody

                    if (!fetchResult.isNotModified && release != null) {
                        if (repo.mappedPackageName != null) {
                            val installedVersion =
                                AppUtil.getAppVersion(context, repo.mappedPackageName)
                            if (installedVersion != null) {
                                isUpdateAvailable =
                                    AppUtil.compareSemanticVersions(
                                        release.tagName,
                                        installedVersion,
                                    ) > 0
                            }
                        }

                        val foundDownloadUrl =
                            release.assets.find { it.name == repo.selectedApkName }?.downloadUrl
                                ?: release.assets.firstOrNull { it.name.endsWith(".apk") }?.downloadUrl
                        if (foundDownloadUrl != null) {
                            downloadUrl = foundDownloadUrl
                        }
                        releaseNotes = release.body

                        val newRepo =
                            repo.copy(
                                latestTagName = release.tagName,
                                latestReleaseName = release.name,
                                latestReleaseBody = release.body,
                                latestReleaseUrl = release.htmlUrl,
                                downloadUrl = downloadUrl,
                                publishedAt = release.publishedAt,
                                isUpdateAvailable = isUpdateAvailable,
                                lastETag = fetchResult.etag ?: repo.lastETag,
                            )

                        if (newRepo != repo) {
                            updatedRepos[i] = newRepo
                            changesMade = true
                        }
                    }

                    if (isUpdateAvailable && repo.notificationsEnabled) {
                        val appName = repo.mappedAppName ?: repo.name
                        val version = release?.tagName ?: repo.latestTagName
                        UpdateNotificationHelper.showTrackedRepoUpdateNotification(
                            context = context,
                            repoName = appName,
                            repoFullName = repo.fullName,
                            version = version,
                            downloadUrl = downloadUrl,
                            releaseNotes = releaseNotes,
                            packageName = repo.mappedPackageName,
                        )
                    }
                    } catch (e: Exception) {
                        if (e.message == "RATE_LIMIT") {
                            Log.w("AppUpdateWorker", "GitHub API rate limit reached, aborting update checks")
                            break
                        } else {
                            Log.e("AppUpdateWorker", "Error checking update for repo ${repo.fullName}", e)
                        }
                    }
                }

                if (changesMade) {
                    settingsRepository.saveTrackedRepos(updatedRepos)
                }
            }

            settingsRepository.putLong(
                SettingsRepository.KEY_LAST_UPDATE_CHECK_TIME,
                System.currentTimeMillis(),
            )

            Result.success()
        } catch (e: Exception) {
            Log.e("AppUpdateWorker", "Error during periodic update check", e)
            Result.retry()
        }
    }
}
