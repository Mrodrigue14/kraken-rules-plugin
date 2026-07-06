package com.kraken.plugin.completion

import com.intellij.codeInsight.completion.CompletionContributor
import com.intellij.codeInsight.completion.CompletionParameters
import com.intellij.codeInsight.completion.CompletionProvider
import com.intellij.codeInsight.completion.CompletionResultSet
import com.intellij.codeInsight.completion.CompletionType
import com.intellij.codeInsight.lookup.LookupElementBuilder
import com.intellij.patterns.PlatformPatterns
import com.intellij.psi.PsiElement
import com.intellij.psi.tree.IElementType
import com.intellij.psi.util.PsiTreeUtil
import com.intellij.util.ProcessingContext
import com.kraken.plugin.lang.KrakenFile
import com.kraken.plugin.parser.KrakenTypes
import com.kraken.plugin.psi.KrakenPsiUtil

class KrakenCompletionContributor : CompletionContributor() {

    init {
        extend(CompletionType.BASIC, PlatformPatterns.psiElement(), KrakenCompletionProvider())
    }
}

private class KrakenCompletionProvider : CompletionProvider<CompletionParameters>() {

    override fun addCompletions(
        parameters: CompletionParameters,
        context: ProcessingContext,
        result: CompletionResultSet
    ) {
        // On privilégie la position dans le fichier original (sans l'identifiant
        // fictif inséré par la complétion), dont l'arbre PSI est intact.
        val position = parameters.originalPosition ?: parameters.position
        if (position.containingFile !is KrakenFile) return
        val project = position.project

        when {
            isInside(position, KrakenTypes.DIMENSION_ANNOTATION) -> {
                // Suggestion "intelligente" : dimensions déclarées dans le projet
                for (name in KrakenPsiUtil.findDimensionNames(project)) {
                    result.addElement(
                        LookupElementBuilder.create("\"$name\"")
                            .withPresentableText(name)
                            .withTypeText("dimension", true)
                    )
                }
            }
            isInside(position, KrakenTypes.ANNOTATION) -> {
                addKeywords(result, ANNOTATION_KEYWORDS)
            }
            isInside(position, KrakenTypes.RULE_TARGET) -> {
                for (name in KrakenPsiUtil.findContextNames(project)) {
                    result.addElement(
                        LookupElementBuilder.create(name).withTypeText("context", true)
                    )
                }
            }
            isInside(position, KrakenTypes.ENTRY_POINT_DECL) -> {
                for (rule in KrakenPsiUtil.findRules(project)) {
                    val name = rule.name ?: continue
                    result.addElement(
                        LookupElementBuilder.create("\"$name\"")
                            .withPresentableText(name)
                            .withTypeText("rule", true)
                    )
                }
            }
            isInside(position, KrakenTypes.RULE_BODY) -> {
                addKeywords(result, RULE_BODY_KEYWORDS)
            }
            else -> {
                addKeywords(result, TOP_LEVEL_KEYWORDS)
            }
        }
    }

    private fun addKeywords(result: CompletionResultSet, keywords: List<String>) {
        for (keyword in keywords) {
            result.addElement(LookupElementBuilder.create(keyword).bold())
        }
    }

    private fun isInside(position: PsiElement, elementType: IElementType): Boolean {
        var current: PsiElement? = position
        while (current != null && current !is KrakenFile) {
            if (current.node?.elementType == elementType) return true
            current = current.parent
        }
        return false
    }

    companion object {
        private val TOP_LEVEL_KEYWORDS = listOf(
            "Rule", "Rules", "EntryPoint", "EntryPoints",
            "Context", "Contexts", "Root Context", "System Context",
            "ExternalContext", "ExternalEntity",
            "Namespace", "Include", "Import Rule",
            "Dimension", "Function"
        )

        private val RULE_BODY_KEYWORDS = listOf(
            "Description", "Priority", "When",
            "Assert", "Assert Empty", "Assert Matches", "Assert Length",
            "Assert Size", "Assert Size Min", "Assert Number Min", "Assert In",
            "Set Mandatory", "Set Hidden", "Set Disabled",
            "Default To", "Reset To",
            "Error", "Warn", "Info", "Overridable"
        )

        private val ANNOTATION_KEYWORDS = listOf(
            "Dimension", "ServerSideOnly", "NotStrict", "ForbidTarget", "ForbidReference"
        )
    }
}
