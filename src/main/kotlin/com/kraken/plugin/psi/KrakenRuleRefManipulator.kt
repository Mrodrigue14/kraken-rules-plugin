package com.kraken.plugin.psi

import com.intellij.openapi.util.TextRange
import com.intellij.psi.AbstractElementManipulator
import com.intellij.psi.impl.source.tree.LeafElement
import com.kraken.plugin.parser.KrakenTypes

class KrakenRuleRefManipulator : AbstractElementManipulator<KrakenRuleRef>() {

    override fun handleContentChange(element: KrakenRuleRef, range: TextRange, newContent: String): KrakenRuleRef {
        val leaf = element.node.findChildByType(KrakenTypes.STRING)
        if (leaf is LeafElement) {
            val quote = leaf.text.firstOrNull() ?: '"'
            leaf.replaceWithText("$quote$newContent$quote")
        }
        return element
    }

    override fun getRangeInElement(element: KrakenRuleRef): TextRange {
        val length = element.textLength
        return if (length >= 2) TextRange(1, length - 1) else TextRange(0, length)
    }
}
