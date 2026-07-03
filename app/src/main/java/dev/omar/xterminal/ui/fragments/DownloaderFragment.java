package dev.omar.xterminal.ui.fragments;

import android.annotation.SuppressLint;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import dev.omar.xterminal.databinding.FragmentDownloaderBinding;
import dev.omar.xterminal.models.AbiUrl;
import dev.omar.xterminal.models.DownloadFile;
import dev.omar.xterminal.utils.AbiUrls;
import dev.omar.xterminal.utils.FileUtil;
import okhttp3.OkHttpClient;

public class DownloaderFragment extends BaseFragment {

    private FragmentDownloaderBinding binding;
    private final OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
        @Override
        public void handleOnBackPressed() {
            showConfirmCloseDialog();
        }
    };

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentDownloaderBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        new DownloaderTask().execute();
        requireActivity().getOnBackPressedDispatcher()
                .addCallback(
                        getViewLifecycleOwner(),
                        onBackPressedCallback);
    }

    private void showConfirmCloseDialog() {
        MaterialAlertDialogBuilder dialogBuilder = new MaterialAlertDialogBuilder(requireContext());
        dialogBuilder.setTitle("Note");
        dialogBuilder.setMessage("Are you sure to cancel download!");
        dialogBuilder.setPositiveButton("Yes",(d,i)->{
            getParentFragmentManager().popBackStack();
        });
        dialogBuilder.setNegativeButton("No",(d,i)->{});
        dialogBuilder.show();
    }

    private static class DownloadProgress {
        private String message;
        private int progress;
        private int max;

        public DownloadProgress(String message, int progress, int max) {
            this.message = message;
            this.progress = progress;
            this.max = max;
        }

        public String getMessage() {
            return message;
        }

        public int getProgress() {
            return progress;
        }

        public int getMax() {
            return max;
        }

        public int getPercentage() {
            return progress * 100 / max;
        }
    }

    @SuppressLint("StaticFieldLeak")
    @SuppressWarnings("deprecation")
    private class DownloaderTask extends AsyncTask<String, DownloadProgress, Boolean> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Boolean doInBackground(String... strings) {
            AbiUrl abiUrl = AbiUrls.getSupportedAbi();
            if (abiUrl == null) {
                return false;
            }
            List<DownloadFile> downloadFileList = new ArrayList<>();
            downloadFileList.add(new DownloadFile(abiUrl.getTalloc(), FileUtil.getFile("libtalloc.so.2")));
            downloadFileList.add(new DownloadFile(abiUrl.getProot(), FileUtil.getFile("proot")));
            downloadFileList.add(new DownloadFile(abiUrl.getAlpine(), FileUtil.getFile("alpine.tar.gz")));


            boolean needsDownload = false;
            for (DownloadFile downloadFile : downloadFileList) {
                if (!downloadFile.getOut().exists()) {
                    needsDownload = true;
                    break;
                }
            }
            if (!needsDownload) {
                return true;
            }

            int totalFiles = downloadFileList.size();
            int completedFiles = 0;

            for (DownloadFile downloadFile : downloadFileList) {
                if (!downloadFile.getOut().exists()) {
                    downloadFile(downloadFile.getUrl(), downloadFile.getOut());
                }
                completedFiles++;
                downloadFile.getOut().setExecutable(true, false);
            }
            return true;
        }

        private void downloadFile(String url, File out) {
            OkHttpClient client = new OkHttpClient();
            okhttp3.Request request = new okhttp3.Request.Builder()
                    .url(url)
                    .build();
            okhttp3.Response response = null;
            try {
                response = client.newCall(request).execute();
                if (!response.isSuccessful()) {
                    throw new IOException("Failrd to download file : " + response.code());
                }
                InputStream inputStream = response.body().byteStream();
                FileOutputStream outputStream = new FileOutputStream(out);
                byte[] buffer = new byte[8 * 1024];
                int bytesRead;
                long totalBytes = response.body().contentLength();
                long downloadedBytes = 0;
                while ((bytesRead = inputStream.read(buffer)) != -1) {
                    if (isCancelled()) {
                        break;
                    }
                    outputStream.write(buffer, 0, bytesRead);
                    downloadedBytes += bytesRead;
                    publishProgress(new DownloadProgress(Uri.parse(url).getLastPathSegment(), (int) downloadedBytes, (int) totalBytes));
                }
                outputStream.flush();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onProgressUpdate(DownloadProgress... values) {
            super.onProgressUpdate(values);
            binding.text.setText("Downloading " + values[0].getMessage() + "... " + values[0].getPercentage() + "%");
            binding.progressBar.setProgress(values[0].getPercentage());
        }

        @SuppressLint("SetTextI18n")
        @Override
        protected void onPostExecute(Boolean aBoolean) {
            super.onPostExecute(aBoolean);
            if (!aBoolean) {
                binding.progressBar.setVisibility(View.GONE);
                binding.text.setText("Download failed!");
                return;
            }
            binding.progressBar.setVisibility(View.GONE);
            binding.text.setText("Download was successfully!");
            new Handler().postDelayed(() -> {
                getMainActivity().loadFragment(new TerminalFragment());
            }, 2000);
        }
    }
}
