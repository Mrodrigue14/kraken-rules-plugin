package com.kraken.plugin.navigation

import com.intellij.codeInsight.navigation.actions.GotoDeclarationHandler
import com.intellij.openapi.editor.Editor
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.kraken.plugin.psi.KrakenRuleRef

/**
 * Navigation Ctrl+B / Ctrl+clic depuis une référence de règle dans un
 * `EntryPoint` vers la déclaration `Rule` correspondante.
 */
class KrakenGotoDeclarationHandler : GotoDeclarationHandler {

    override fun getGotoDeclarationTargets(
        sourceElement: PsiElement?,
        offset: Int,
        editor: Editor?
    ): Array<PsiElement>? {
        if (sourceElement == null) return null
        val ruleRef = PsiTreeUtil.getParentOfType(sourceElement, KrakenRuleRef::class.java, false)
            ?: return null
        val target = ruleRef.reference.resolve() ?: return null
        return arrayOf(target)
    }
}
