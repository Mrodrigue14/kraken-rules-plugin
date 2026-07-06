package com.kraken.plugin.inspection

import com.intellij.codeInsight.intention.PsiElementBaseIntentionAction
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.util.PsiTreeUtil
import com.kraken.plugin.parser.KrakenTypes
import com.kraken.plugin.psi.KrakenRuleDecl

/**
 * Intention : ajoute une clause `On Context.field` manquante à une règle.
 */
class KrakenAddOnClauseIntention : PsiElementBaseIntentionAction() {

    override fun getFamilyName(): String = "Add missing 'On' clause"

    override fun getText(): String = "Add missing 'On' clause"

    override fun startInWriteAction(): Boolean = true

    override fun isAvailable(project: Project, editor: Editor?, element: PsiElement): Boolean {
        val rule = PsiTreeUtil.getParentOfType(element, KrakenRuleDecl::class.java, false)
        return rule != null && !rule.hasTarget()
    }

    override fun invoke(project: Project, editor: Editor?, element: PsiElement) {
        if (editor == null) return
        val rule = PsiTreeUtil.getParentOfType(element, KrakenRuleDecl::class.java, false) ?: return

        // Point d'insertion : après le nom de la règle s'il existe,
        // sinon après le mot-clé Rule.
        val anchor = rule.node.findChildByType(KrakenTypes.RULE_NAME)
            ?: rule.node.findChildByType(KrakenTypes.RULE_KW)
            ?: return
        val offset = anchor.textRange.endOffset
        val placeholder = " On ContextName.field"

        editor.document.insertString(offset, placeholder)

        // Sélectionne le placeholder pour que l'utilisateur puisse le remplacer
        val start = offset + " On ".length
        editor.caretModel.moveToOffset(start)
        editor.selectionModel.setSelection(start, offset + placeholder.length)
    }
}
