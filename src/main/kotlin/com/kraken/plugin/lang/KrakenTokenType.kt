package com.kraken.plugin.lang

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class KrakenTokenType(@NonNls debugName: String) : IElementType(debugName, KrakenLanguage) {
    override fun toString(): String = "KrakenTokenType." + super.toString()
}
