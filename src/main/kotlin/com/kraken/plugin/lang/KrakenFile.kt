package com.kraken.plugin.lang

import com.intellij.extapi.psi.PsiFileBase
import com.intellij.openapi.fileTypes.FileType
import com.intellij.psi.FileViewProvider

class KrakenFile(viewProvider: FileViewProvider) : PsiFileBase(viewProvider, KrakenLanguage) {
    override fun getFileType(): FileType = KrakenFileType
    override fun toString(): String = "Kraken rules file"
}
