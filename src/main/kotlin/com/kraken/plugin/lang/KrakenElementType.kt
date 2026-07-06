package com.kraken.plugin.lang

import com.intellij.psi.tree.IElementType
import org.jetbrains.annotations.NonNls

class KrakenElementType(@NonNls debugName: String) : IElementType(debugName, KrakenLanguage)
