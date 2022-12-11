package io.github.excu101.vortex.provider.storage

import android.database.Cursor
import android.database.MatrixCursor
import android.os.CancellationSignal
import android.os.ParcelFileDescriptor
import android.provider.DocumentsContract.Document
import android.provider.DocumentsContract.Root
import android.provider.DocumentsProvider

class StorageDocumentProvider : DocumentsProvider() {

    companion object {
        private val RootProjections = arrayOf(
            Root.COLUMN_ROOT_ID,
            Root.COLUMN_MIME_TYPES,
            Root.COLUMN_ICON,
            Root.COLUMN_TITLE,
            Root.COLUMN_FLAGS,
            Root.COLUMN_DOCUMENT_ID,
            Root.COLUMN_AVAILABLE_BYTES
        )

        private val DocumentProjections = arrayOf(
            Document.COLUMN_DOCUMENT_ID,
            Document.COLUMN_ICON,
            Document.COLUMN_MIME_TYPE,
            Document.COLUMN_DISPLAY_NAME,
            Document.COLUMN_FLAGS,
            Document.COLUMN_SIZE,
        )
    }

    override fun onCreate(): Boolean {
        return true
    }

    override fun queryRoots(projection: Array<out String>?): Cursor {
        val cursor = MatrixCursor(projection ?: RootProjections)
        val row = cursor.newRow()

        row.add(Root.COLUMN_ROOT_ID, "root")

        val flags = Root.FLAG_SUPPORTS_CREATE or
                Root.FLAG_SUPPORTS_RECENTS or
                Root.FLAG_SUPPORTS_SEARCH or
                Root.FLAG_SUPPORTS_IS_CHILD

        row.add(
            Root.COLUMN_FLAGS,
            flags
        )

        row.add(Root.COLUMN_TITLE, "Vortex Documents Provider")

        return cursor
    }

    override fun queryDocument(documentId: String?, projection: Array<out String>?): Cursor {
        val cursor = MatrixCursor(projection ?: DocumentProjections)

        return cursor
    }

    override fun queryChildDocuments(
        parentDocumentId: String?,
        projection: Array<out String>?,
        sortOrder: String?,
    ): Cursor {
        TODO("Not yet implemented")
    }

    override fun openDocument(
        documentId: String?,
        mode: String?,
        signal: CancellationSignal?,
    ): ParcelFileDescriptor {
        TODO("Not yet implemented")
    }
}