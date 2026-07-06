package com.kraken.plugin.inspection

import com.intellij.codeInspection.LocalInspectionTool
import com.intellij.codeInspection.ProblemHighlightType
import com.intellij.codeInspection.ProblemsHolder
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiElementVisitor
import com.kraken.plugin.psi.KrakenRuleDecl

/**
 * Signale les déclarations `Rule` sans nom : `Rule On Policy.state { ... }`.
 */
class KrakenRuleWithoutNameInspection : LocalInspectionTool() {

    override fun buildVisitor(holder: ProblemsHolder, isOnTheFly: Boolean): PsiElementVisitor =
        object : PsiElementVisitor() {
            override fun visitElement(element: PsiElement) {
                if (element is KrakenRuleDecl && element.nameIdentifier == null) {
                    val anchor = element.ruleKeyword() ?: element
                    holder.registerProblem(
                        anchor,
                        "Rule has no name",
                        ProblemHighlightType.GENERIC_ERROR
                    )
                }
            }
        }
}
