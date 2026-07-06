package com.kraken.plugin.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.kraken.plugin.parser.KrakenTypes

/**
 * Déclaration `Dimension "nom" : Type`.
 */
class KrakenDimensionDecl(node: ASTNode) : ASTWrapperPsiElement(node) {

    val dimensionName: String?
        get() = node.findChildByType(KrakenTypes.STRING)?.text?.let(KrakenPsiUtil::unquote)
}
