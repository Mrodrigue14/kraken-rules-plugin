package com.kraken.plugin.psi

import com.intellij.extapi.psi.ASTWrapperPsiElement
import com.intellij.lang.ASTNode
import com.intellij.psi.PsiReference

/**
 * Référence à une règle dans un `EntryPoint { "nomDeRegle", ... }`.
 */
class KrakenRuleRef(node: ASTNode) : ASTWrapperPsiElement(node) {

    val ruleName: String
        get() = KrakenPsiUtil.unquote(text)

    override fun getReference(): PsiReference = KrakenRuleReference(this)
}
