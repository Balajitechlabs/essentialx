package com.sameerasw.essentials.utils

import android.graphics.Path
import androidx.compose.material3.MaterialShapes
import androidx.graphics.shapes.RoundedPolygon
import androidx.graphics.shapes.toPath
import java.util.Random

object AmbientMusicShapeHelper {

    val allShapesWithNames = listOf(
        "Circle" to MaterialShapes.Circle,
        "Square" to MaterialShapes.Square,
        "Slanted" to MaterialShapes.Slanted,
        "Arch" to MaterialShapes.Arch,
        "Oval" to MaterialShapes.Oval,
        "Pill" to MaterialShapes.Pill,
        "Triangle" to MaterialShapes.Triangle,
        "Arrow" to MaterialShapes.Arrow,
        "Diamond" to MaterialShapes.Diamond,
        "ClamShell" to MaterialShapes.ClamShell,
        "Pentagon" to MaterialShapes.Pentagon,
        "Gem" to MaterialShapes.Gem,
        "Sunny" to MaterialShapes.Sunny,
        "VerySunny" to MaterialShapes.VerySunny,
        "Cookie4Sided" to MaterialShapes.Cookie4Sided,
        "Cookie6Sided" to MaterialShapes.Cookie6Sided,
        "Cookie7Sided" to MaterialShapes.Cookie7Sided,
        "Cookie9Sided" to MaterialShapes.Cookie9Sided,
        "Cookie12Sided" to MaterialShapes.Cookie12Sided,
        "Clover4Leaf" to MaterialShapes.Clover4Leaf,
        "Clover8Leaf" to MaterialShapes.Clover8Leaf,
        "SoftBurst" to MaterialShapes.SoftBurst,
        "Flower" to MaterialShapes.Flower,
        "PuffyDiamond" to MaterialShapes.PuffyDiamond,
        "Ghostish" to MaterialShapes.Ghostish,
        "PixelCircle" to MaterialShapes.PixelCircle,
        "Bun" to MaterialShapes.Bun,
        "Heart" to MaterialShapes.Heart
    )

    private val allShapes = allShapesWithNames.map { it.second }

    fun getRandomPolygonFromSet(selectedNames: Set<String>): RoundedPolygon {
        val filtered = allShapesWithNames.filter { selectedNames.contains(it.first) }
        if (filtered.isEmpty()) return MaterialShapes.Cookie12Sided
        val random = Random()
        return filtered[random.nextInt(filtered.size)].second
    }

    fun getShapePath(seed: String?, size: Float, isRandomEnabled: Boolean = true): Path {
        return getPolygon(seed, isRandomEnabled).toAndroidPath(size)
    }

    fun getRandomShapePath(size: Float, isRandomEnabled: Boolean = true): Path {
        return getRandomPolygon(isRandomEnabled).toAndroidPath(size)
    }

    fun getPolygon(seed: String?, isRandomEnabled: Boolean = true): RoundedPolygon {
        if (!isRandomEnabled) return MaterialShapes.Cookie12Sided
        val hash = seed?.hashCode() ?: 0
        val random = Random(hash.toLong())
        return allShapes[random.nextInt(allShapes.size)]
    }

    fun getRandomPolygon(isRandomEnabled: Boolean = true): RoundedPolygon {
        if (!isRandomEnabled) return MaterialShapes.Cookie12Sided
        val random = Random()
        return allShapes[random.nextInt(allShapes.size)]
    }

    fun updatePathFromMorph(
        morph: androidx.graphics.shapes.Morph,
        progress: Float,
        size: Float,
        targetPath: Path,
        rotation: Float = 0f
    ) {
        val rawPath = morph.toPath(progress)
        val matrix = android.graphics.Matrix()
        matrix.postScale(size, size)
        if (rotation != 0f) {
            matrix.postRotate(rotation, size / 2f, size / 2f)
        }

        targetPath.reset()
        targetPath.set(rawPath)
        targetPath.transform(matrix)
    }

    private fun RoundedPolygon.toAndroidPath(size: Float): Path {
        val resultPath = this.toPath()
        val matrixObj = android.graphics.Matrix()
        matrixObj.postScale(size, size)
        resultPath.transform(matrixObj)
        return resultPath
    }
}
