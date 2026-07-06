package com.kraken.plugin.lang

import com.intellij.lang.Language

object KrakenLanguage : Language("Kraken") {
    private fun readResolve(): Any = KrakenLanguage
    override fun getDisplayName(): String = "Kraken Rules"
}
