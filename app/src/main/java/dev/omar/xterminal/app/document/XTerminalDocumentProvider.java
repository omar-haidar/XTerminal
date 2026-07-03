package dev.omar.xterminal.app.document;

import android.content.ComponentName;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.database.Cursor;
import android.database.MatrixCursor;
import android.graphics.Point;
import android.os.CancellationSignal;
import android.os.ParcelFileDescriptor;
import android.provider.DocumentsContract;
import android.provider.DocumentsProvider;
import android.util.Log;
import android.webkit.MimeTypeMap;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.Locale;

import dev.omar.xterminal.R;
import dev.omar.xterminal.utils.FileUtil;
import kotlin.jvm.Throws;


public class XTerminalDocumentProvider extends DocumentsProvider {

    private static final String TAG = "XTerminalDocumentProvider";

    @Override
    public Cursor queryRoots(String[] projection) throws FileNotFoundException {
        final MatrixCursor result = new MatrixCursor(
                projection != null ? projection : DEFAULT_ROOT_PROJECTION
        );
        final String applicationName = "XTerminal";

        MatrixCursor.RowBuilder row = result.newRow();
        row.add(DocumentsContract.Root.COLUMN_ROOT_ID, getDocIdForFile(BASE_DIR));
        row.add(DocumentsContract.Root.COLUMN_DOCUMENT_ID, getDocIdForFile(BASE_DIR));
        row.add(DocumentsContract.Root.COLUMN_SUMMARY, null);
        row.add(
                DocumentsContract.Root.COLUMN_FLAGS,
                DocumentsContract.Root.FLAG_SUPPORTS_CREATE | DocumentsContract.Root.FLAG_SUPPORTS_SEARCH | DocumentsContract.Root.FLAG_SUPPORTS_IS_CHILD
        );
        row.add(DocumentsContract.Root.COLUMN_TITLE, applicationName);
        row.add(DocumentsContract.Root.COLUMN_MIME_TYPES, ALL_MIME_TYPES);
        row.add(DocumentsContract.Root.COLUMN_AVAILABLE_BYTES, BASE_DIR.getFreeSpace());
        row.add(DocumentsContract.Root.COLUMN_ICON, R.mipmap.ic_launcher);
        return result;
    }

    @Throws(exceptionClasses = FileNotFoundException.class)
    @Override
    public Cursor queryDocument(String documentId, String[] projection) throws FileNotFoundException {
        final MatrixCursor result = new MatrixCursor(
                projection != null ? projection : DEFAULT_DOCUMENT_PROJECTION
        );
        includeFile(result, documentId, null);
        return result;
    }

    @Throws(exceptionClasses = FileNotFoundException.class)
    @Override
    public Cursor queryChildDocuments(
            String parentDocumentId,
            String[] projection,
            String sortOrder
    ) throws FileNotFoundException {
        final MatrixCursor result = new MatrixCursor(
                projection != null ? projection : DEFAULT_DOCUMENT_PROJECTION
        );
        final File parent = getFileForDocId(parentDocumentId);
        final File[] files = parent.listFiles();
        if (files != null) {
            for (File file : files) {
                includeFile(result, null, file);
            }
        } else {
            Log.e(TAG, "Unable to list files in " + parentDocumentId);
        }
        return result;
    }

    @Throws(exceptionClasses = FileNotFoundException.class)
    @Override
    public ParcelFileDescriptor openDocument(
            String documentId,
            String mode,
            CancellationSignal signal
    ) throws FileNotFoundException {
        final File file = getFileForDocId(documentId);
        final int accessMode = ParcelFileDescriptor.parseMode(mode);
        return ParcelFileDescriptor.open(file, accessMode);
    }

    @Override
    public AssetFileDescriptor openDocumentThumbnail(
            String documentId,
            Point sizeHint,
            CancellationSignal signal
    ) throws FileNotFoundException {
        final File file = getFileForDocId(documentId);
        final ParcelFileDescriptor pfd = ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY);
        return new AssetFileDescriptor(pfd, 0, file.length());
    }

    @Override
    public boolean onCreate() {
        return true;
    }

    @Override
    public String createDocument(String parentDocumentId,
                                 String mimeType,
                                 String displayName
    ) throws FileNotFoundException {
        final File parent = getFileForDocId(parentDocumentId);
        File newFile = new File(parent, displayName);
        int noConflictId = 2;
        while (newFile.exists()) {
            newFile = new File(parent, displayName + " (" + noConflictId++ + ")");
        }
        try {
            boolean succeeded;
            if (DocumentsContract.Document.MIME_TYPE_DIR.equals(mimeType)) {
                succeeded = newFile.mkdir();
            } else {
                succeeded = newFile.createNewFile();
            }
            if (!succeeded) {
                throw new FileNotFoundException("Failed to create document with id " + newFile.getAbsolutePath());
            }
        } catch (IOException e) {
            throw new FileNotFoundException("Failed to create document with id " + newFile.getAbsolutePath() + ": " + e.getMessage());
        }
        return getDocIdForFile(newFile);
    }

    @Override
    public void deleteDocument(String documentId) throws FileNotFoundException {
        final File file = getFileForDocId(documentId);
        if (!file.delete()) {
            throw new FileNotFoundException("Failed to delete document with id " + documentId);
        }
    }

    @Override
    public String getDocumentType(String documentId) throws FileNotFoundException {
        final File file = getFileForDocId(documentId);
        return getMimeType(file);
    }

    @Override
    public Cursor querySearchDocuments(
            String rootId,
            String query,
            String[] projection
    ) throws FileNotFoundException {
        final MatrixCursor result = new MatrixCursor(
                projection != null ? projection : DEFAULT_DOCUMENT_PROJECTION
        );
        final File parent = getFileForDocId(rootId);

        final LinkedList<File> pending = new LinkedList<>();
        pending.add(parent);

        final int MAX_SEARCH_RESULTS = 50;
        while (!pending.isEmpty() && result.getCount() < MAX_SEARCH_RESULTS) {
            final File file = pending.removeFirst();
            boolean isInsideHome;
            try {
                isInsideHome = file.getCanonicalPath().startsWith(BASE_DIR.getCanonicalPath());
            } catch (IOException e) {
                isInsideHome = true; // Fallback if canonical path cannot be resolved
            }
            if (isInsideHome) {
                if (file.isDirectory()) {
                    File[] children = file.listFiles();
                    if (children != null) {
                        Collections.addAll(pending, children);
                    }
                } else {
                    if (file.getName().toLowerCase(Locale.getDefault()).contains(query.toLowerCase(Locale.getDefault()))) {
                        includeFile(result, null, file);
                    }
                }
            }
        }

        return result;
    }

    @Override
    public boolean isChildDocument(String parentDocumentId, String documentId) {
        return documentId.startsWith(parentDocumentId);
    }

    @Throws(exceptionClasses = FileNotFoundException.class)
    private void includeFile(MatrixCursor result, String docId, File file) throws FileNotFoundException {
        if (docId == null) {
            docId = getDocIdForFile(file);
        } else {
            file = getFileForDocId(docId);
        }

        int flags = 0;
        if (file.isDirectory()) {
            if (file.canWrite()) flags |= DocumentsContract.Document.FLAG_DIR_SUPPORTS_CREATE;
        } else if (file.canWrite()) {
            flags |= DocumentsContract.Document.FLAG_SUPPORTS_WRITE;
        }
        if (file.getParentFile() != null && file.getParentFile().canWrite()) {
            flags |= DocumentsContract.Document.FLAG_SUPPORTS_DELETE;
        }

        final String displayName = file.getName();
        final String mimeType = getMimeType(file);
        if (mimeType.startsWith("image/")) flags |= DocumentsContract.Document.FLAG_SUPPORTS_THUMBNAIL;

        MatrixCursor.RowBuilder row = result.newRow();
        row.add(DocumentsContract.Document.COLUMN_DOCUMENT_ID, docId);
        row.add(DocumentsContract.Document.COLUMN_DISPLAY_NAME, displayName);
        row.add(DocumentsContract.Document.COLUMN_SIZE, file.length());
        row.add(DocumentsContract.Document.COLUMN_MIME_TYPE, mimeType);
        row.add(DocumentsContract.Document.COLUMN_LAST_MODIFIED, file.lastModified());
        row.add(DocumentsContract.Document.COLUMN_FLAGS, flags);
        row.add(DocumentsContract.Document.COLUMN_ICON, R.mipmap.ic_launcher);
    }

    public static boolean isDocumentProviderEnabled(Context context) {
        final ComponentName componentName = new ComponentName(context, XTerminalDocumentProvider.class);
        final int state = context.getPackageManager().getComponentEnabledSetting(componentName);
        return state == PackageManager.COMPONENT_ENABLED_STATE_ENABLED ||
                state == PackageManager.COMPONENT_ENABLED_STATE_DEFAULT;
    }

    public static void setDocumentProviderEnabled(Context context, boolean enabled) {
        if (isDocumentProviderEnabled(context) == enabled) {
            return;
        }
        final ComponentName componentName = new ComponentName(context, XTerminalDocumentProvider.class);
        final int newState = enabled ?
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED :
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED;

        context.getPackageManager().setComponentEnabledSetting(
                componentName,
                newState,
                PackageManager.DONT_KILL_APP
        );
    }

    private static final String ALL_MIME_TYPES = "*/*";

    private static final File BASE_DIR = FileUtil.getFilesDir();

    private static final String[] DEFAULT_ROOT_PROJECTION = new String[]{
            DocumentsContract.Root.COLUMN_ROOT_ID,
            DocumentsContract.Root.COLUMN_MIME_TYPES,
            DocumentsContract.Root.COLUMN_FLAGS,
            DocumentsContract.Root.COLUMN_ICON,
            DocumentsContract.Root.COLUMN_TITLE,
            DocumentsContract.Root.COLUMN_SUMMARY,
            DocumentsContract.Root.COLUMN_DOCUMENT_ID,
            DocumentsContract.Root.COLUMN_AVAILABLE_BYTES
    };

    private static final String[] DEFAULT_DOCUMENT_PROJECTION = new String[]{
            DocumentsContract.Document.COLUMN_DOCUMENT_ID,
            DocumentsContract.Document.COLUMN_MIME_TYPE,
            DocumentsContract.Document.COLUMN_DISPLAY_NAME,
            DocumentsContract.Document.COLUMN_LAST_MODIFIED,
            DocumentsContract.Document.COLUMN_FLAGS,
            DocumentsContract.Document.COLUMN_SIZE
    };

    private static String getDocIdForFile(File file) {
        return file.getAbsolutePath();
    }

    @Throws(exceptionClasses = FileNotFoundException.class)
    private static File getFileForDocId(String docId) throws FileNotFoundException {
        final File f = new File(docId);
        if (!f.exists()) throw new FileNotFoundException(f.getAbsolutePath() + " not found");
        return f;
    }

    private static String getMimeType(File file) {
        if (file.isDirectory()) {
            return DocumentsContract.Document.MIME_TYPE_DIR;
        } else {
            final String name = file.getName();
            final int lastDot = name.lastIndexOf('.');
            if (lastDot >= 0) {
                final String extension = name.substring(lastDot + 1).toLowerCase(Locale.getDefault());
                final String mime = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
                if (mime != null) return mime;
            }
            return "application/octet-stream";
        }
    }
}
