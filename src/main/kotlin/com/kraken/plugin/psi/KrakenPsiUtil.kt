package com.kraken.plugin.psi

import com.intellij.openapi.project.Project
import com.intellij.psi.PsiElement
import com.intellij.psi.PsiManager
import com.intellij.psi.search.FileTypeIndex
import com.intellij.psi.search.GlobalSearchScope
import com.intellij.psi.util.PsiTreeUtil
import com.kraken.plugin.lang.KrakenFile
import com.kraken.plugin.lang.KrakenFileType
import com.kraken.plugin.parser.KrakenTypes

object KrakenPsiUtil {

    fun unquote(text: String): String {
        if (text.length >= 2) {
            val first = text.first()
            if ((first == '"' || first == '\'') && text.last() == first) {
                return text.substring(1, text.length - 1)
            }
        }
        return text
    }

    fun krakenFiles(project: Project): List<KrakenFile> =
        FileTypeIndex.getFiles(KrakenFileType, GlobalSearchScope.projectScope(project))
            .mapNotNull { PsiManager.getInstance(project).findFile(it) as? KrakenFile }

    fun findRules(project: Project): List<KrakenRuleDecl> =
        krakenFiles(project).flatMap { PsiTreeUtil.findChildrenOfType(it, KrakenRuleDecl::class.java) }

    fun findRule(project: Project, name: String): KrakenRuleDecl? =
        findRules(project).firstOrNull { it.name == name }

    fun findDimensionNames(project: Project): List<String> =
        krakenFiles(project)
            .flatMap { PsiTreeUtil.findChildrenOfType(it, KrakenDimensionDecl::class.java) }
            .mapNotNull { it.dimensionName }
            .distinct()

    fun findContextNames(project: Project): List<String> =
        krakenFiles(project)
            .flatMap { file ->
                PsiTreeUtil.collectElements(file) { it.node?.elementType == KrakenTypes.CONTEXT_DECL }
                    .mapNotNull { contextName(it) }
            }
            .distinct()

    private fun contextName(contextDecl: PsiElement): String? {
        val keyword = contextDecl.node.findChildByType(KrakenTypes.CONTEXT_KW) ?: return null
        var node = keyword.treeNext
        while (node != null && node.psi is com.intellij.psi.PsiWhiteSpace) {
            node = node.treeNext
        }
        return node?.text
    }
}
