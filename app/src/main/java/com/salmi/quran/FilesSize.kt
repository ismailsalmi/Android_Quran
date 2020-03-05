package com.salmi.quran

import java.io.File

object FilesSize {
    private var fileSize: Double = 0.0
    private var resultSize: String = ""

    fun returnFileSize(directory : File): String {
        if (directory != null && directory.isDirectory) {
            for (file in directory.listFiles()) {
                fileSize += (file.length()).toDouble()
            }
            if (fileSize.equals(0.0)) {
                resultSize = "لم يتم تحميل أي سورة"
            } else if (fileSize < 1024) {
                resultSize = fileSize.toString() + "B"
            } else if (fileSize > 1024 && fileSize < 1024 * 1024) {
                resultSize = (Math.round(fileSize / 1024 * 100.0) / 100.0).toString() + "KB"
            } else {
                resultSize = (Math.round(fileSize / (1024 * 1204) * 100.0) / 100.0).toString() + "MB"
            }
        }
        return this.resultSize
    }
}
