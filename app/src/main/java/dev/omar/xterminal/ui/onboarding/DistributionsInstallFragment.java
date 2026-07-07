package dev.omar.xterminal.ui.onboarding;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.ListAdapter;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.NetworkUtils;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;

import dev.omar.xterminal.databinding.FragmentOnboardingDistributionsBinding;
import dev.omar.xterminal.databinding.ItemDistroBinding;
import dev.omar.xterminal.models.DistributionModel;
import dev.omar.xterminal.utils.IOUtils;

public class DistributionsInstallFragment extends OnBoardingFragment implements NetworkUtils.OnNetworkStatusChangedListener {
    private FragmentOnboardingDistributionsBinding binding;
    private OnboardingViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        viewModel = new ViewModelProvider(this).get(OnboardingViewModel.class);

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOnboardingDistributionsBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setupConnectionListener();

        Adapter adapter = new Adapter();
        new Thread(() -> {
            adapter.submitList(getDistributions());
        }).start();

        binding.recyclerView.setHasFixedSize(true);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.setAdapter(adapter);
        binding.includeLoading.getRoot().setVisibility(View.GONE);


    }

    class Adapter extends ListAdapter<DistributionModel, VH> {
        Adapter() {
            super(new DiffUtil.ItemCallback<DistributionModel>() {
                @Override
                public boolean areItemsTheSame(@NonNull DistributionModel oldItem, @NonNull DistributionModel newItem) {
                    return oldItem.equals(newItem);
                }

                @Override
                public boolean areContentsTheSame(@NonNull DistributionModel oldItem, @NonNull DistributionModel newItem) {
                    return oldItem.getUrl().equals(newItem.getUrl());
                }
            });
        }

        @NonNull
        @Override
        public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new VH(ItemDistroBinding.inflate(LayoutInflater.from(parent.getContext()), parent, false));
        }


        @Override
        public void onBindViewHolder(@NonNull VH holder, int position) {
            holder.bind(getItem(position));

        }
    }

    class VH extends RecyclerView.ViewHolder {
        ItemDistroBinding binding;
        String selecteddistro = null;

        VH(ItemDistroBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }

        @SuppressLint("NotifyDataSetChanged")
        public void bind(@NonNull DistributionModel model) {
            binding.btnInstall.setChecked(selecteddistro != null && selecteddistro == model.getUrl());
            binding.btnInstall.setOnCheckedChangeListener((cb,isChecked)->{
                if(isChecked){
                    selecteddistro = model.getUrl();
                }else {
                    selecteddistro = null;
                }
                if (getBindingAdapter() != null) {
                    getBindingAdapter().notifyDataSetChanged();
                }
            });
            binding.txtName.setText(model.getName());
            String iconPath = "icons/" + model.getIcon();
            try {
                InputStream inputStream = requireContext().getAssets().open(iconPath);
                Bitmap bitmap = BitmapFactory.decodeStream(inputStream);
                binding.icon.setImageBitmap(bitmap);
            } catch (IOException ignored) {

            }
        }

    }

    @NonNull
    private List<DistributionModel> getDistributions() {
        List<DistributionModel> list = new ArrayList<>();
        try {
            URL url = new URL("https://raw.githubusercontent.com/omar-haidar/XTerminal-Data/main/distributions.json");
            URLConnection connection = url.openConnection();
            connection.setConnectTimeout(7000);
            connection.setReadTimeout(7000);
            String content = IOUtils.read(connection.getInputStream());
            list.addAll(new Gson().fromJson(content, new TypeToken<List<DistributionModel>>() {
            }.getType()));
        } catch (Exception ignored) {
            throw new RuntimeException(ignored);
        }
        return list;
    }

    private void setupConnectionListener() {
        NetworkUtils.registerNetworkStatusChangedListener(this);
        viewModel.isConnected().observe(getViewLifecycleOwner(), isConnected -> {
            if (isConnected) {
                binding.cardNoConnection.setVisibility(View.GONE);
            } else {
                binding.cardNoConnection.setVisibility(View.VISIBLE);
            }
        });
        viewModel.checkInternetConnection();
    }

    @Override
    public void onDisconnected() {
        viewModel.checkInternetConnection();
    }

    @Override
    public void onConnected(NetworkUtils.NetworkType networkType) {
        viewModel.checkInternetConnection();
    }

    @Override
    public void onDestroy() {
        NetworkUtils.unregisterNetworkStatusChangedListener(this);
        super.onDestroy();
    }

    @Override
    public boolean isPolicyRespected() {
        return super.isPolicyRespected();
    }

    @Override
    public void onUserIllegallyRequestedNextPage() {
        super.onUserIllegallyRequestedNextPage();
    }
}
