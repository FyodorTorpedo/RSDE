package io.github.chrislo27.rhre3.sfxdb.gui.util

import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.MultipleSelectionModel
import javafx.scene.control.Spinner
import javafx.scene.control.SpinnerValueFactory
import javafx.scene.input.KeyCombination
import javafx.scene.text.Font
import javafx.stage.Stage
import javafx.util.StringConverter


fun Scene.addDebugAccelerators() {
    this.accelerators[KeyCombination.keyCombination("Shortcut+Alt+'i'")] = Runnable(Localization::refreshLangs)
    this.accelerators[KeyCombination.keyCombination("F5")] = Runnable {
        val sheets = this.stylesheets.toList()
        this.stylesheets.clear()
        this.stylesheets.addAll(sheets)

        @Suppress("NAME_SHADOWING")
        fun Parent.reload() {
            val sheets = this.stylesheets.toList()
            this.stylesheets.clear()
            this.stylesheets.addAll(sheets)
            this.childrenUnmodifiable.forEach { if (it is Parent) it.reload() }
        }

        this.root.reload()
    }
}

fun Stage.setMinimumBoundsToSized() {
    this.sizeToScene()
    this.minWidth = this.width
    this.minHeight = this.height
}

val Double.em: Double get() = Font.getDefault().size * this

fun doubleSpinnerFactory(min: Double, max: Double, initial: Double, step: Double = 1.0): Spinner<Double> =
    Spinner<Double>().apply {
        valueFactory = SpinnerValueFactory.DoubleSpinnerValueFactory(min, max, initial, step).apply {
            this.converter = object : StringConverter<Double>() {
                override fun toString(`object`: Double): String {
                    return `object`.toString()
                }

                override fun fromString(string: String): Double {
                    return string.toDoubleOrNull() ?: 0.0
                }
            }
        }
        isEditable = true
    }

fun intSpinnerFactory(min: Int, max: Int, initial: Int, step: Int = 1): Spinner<Int> =
    Spinner<Int>().apply {
        valueFactory = SpinnerValueFactory.IntegerSpinnerValueFactory(min, max, initial, step).apply {
            this.converter = object : StringConverter<Int>() {
                override fun toString(`object`: Int): String {
                    return `object`.toString()
                }

                override fun fromString(string: String): Int {
                    return string.toIntOrNull() ?: 0
                }
            }
        }
        isEditable = true
    }

/**
 * @return true iff there is a selection and the selection is contiguous
 */
fun <T> MultipleSelectionModel<T>.isSelectionContiguous(): Boolean {
    if (this.selectedIndices.isEmpty()) return false
    if (this.selectedIndices.size == 1) return true
    val sorted = this.selectedIndices.toList().sorted()
    return sorted.first() + sorted.size - 1 == sorted.last()
}