package com.bytes.mangalho.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bytes.mangalho.databinding.FragmentServicesBinding;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.bytes.mangalho.Models.ServiceCategoryDTO;
import com.bytes.mangalho.Models.ServiceDTO;
import com.bytes.mangalho.R;
import com.bytes.mangalho.activity.dashboard.Dashboard;
import com.bytes.mangalho.adapter.ServicesAdapter;
import com.bytes.mangalho.https.HttpsRequest;
import com.bytes.mangalho.interfaces.Consts;
import com.bytes.mangalho.interfaces.Helper;
import com.bytes.mangalho.network.NetworkManager;
import com.bytes.mangalho.utils.ProjectUtils;

import org.json.JSONObject;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ServicesFrag extends Fragment {
    private FragmentServicesBinding binding;
    private static String TAG = ServicesFrag.class.getSimpleName();
    private ArrayList<ServiceCategoryDTO> categoryDTOSList;
    private ArrayList<ServiceDTO> serviceDTOSList;
    private Dashboard dashboard;
    private HashMap<String, String> params;
    private String cate_id = "";
    private ServicesAdapter servicesAdapter;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_services, container, false);
        dashboard.binding.headerNameTV.setText(getResources().getString(R.string.nav_services));
        setUiAction();
        return binding.getRoot();
    }

    private void setUiAction() {
        if (NetworkManager.isConnectToInternet(getActivity())) {
            getCategory();
        } else {
            ProjectUtils.showToast(getActivity(), getResources().getString(R.string.internet_concation));
        }

        binding.tabCat.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
               cate_id= categoryDTOSList.get(tab.getPosition()).getCategory_id();
               getServiceByCategory();
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        dashboard = (Dashboard) activity;

    }

    public void getCategory() {
        ProjectUtils.showProgressDialog(getActivity(), true, getResources().getString(R.string.please_wait));
        new HttpsRequest(Consts.GET_CATEGORY_API, getActivity()).stringGet(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                ProjectUtils.pauseProgressDialog();
                if (flag) {
                    try {
                        categoryDTOSList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ServiceCategoryDTO>>() {
                        }.getType();
                        categoryDTOSList = (ArrayList<ServiceCategoryDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showData();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                }
            }
        });
    }

    private void showData() {
        if (!categoryDTOSList.isEmpty()) {
            for (int i = 0; i < categoryDTOSList.size(); i++) {
                binding.tabCat.addTab(binding.tabCat.newTab().setText(categoryDTOSList.get(i).getCat_name()));
            }
            cate_id = categoryDTOSList.get(0).getCategory_id();
            getServiceByCategory();
        }

    }

    public void getServiceByCategory() {
        params = new HashMap<>();
        params.put(Consts.CATEGORY_ID, cate_id);

        new HttpsRequest(Consts.GET_SERVICE_BY_CATEGORY_API, params, getActivity()).stringPost(TAG, new Helper() {
            @Override
            public void backResponse(boolean flag, String msg, JSONObject response) {
                if (flag) {
                    try {
                        serviceDTOSList = new ArrayList<>();
                        Type getpetDTO = new TypeToken<List<ServiceDTO>>() {
                        }.getType();
                        serviceDTOSList = (ArrayList<ServiceDTO>) new Gson().fromJson(response.getJSONArray("data").toString(), getpetDTO);
                        showDataService();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } else {
                    binding.rvService.setVisibility(View.GONE);
                    binding.noProdut.setVisibility(View.VISIBLE);
                }
            }
        });
    }

    private void showDataService() {

        if (serviceDTOSList.size()>0){
            binding.rvService.setVisibility(View.VISIBLE);
            binding.noProdut.setVisibility(View.GONE);
            binding.rvService.setLayoutManager(new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL,false));
            servicesAdapter = new ServicesAdapter(getActivity(),serviceDTOSList);
            binding.rvService.setAdapter(servicesAdapter);
        }else {
            binding.rvService.setVisibility(View.GONE);
            binding.noProdut.setVisibility(View.VISIBLE);
        }
    }

}
