package com.demomvvm.school.fragment;

import android.app.Activity;
import android.content.Intent;
import androidx.databinding.DataBindingUtil;
import android.os.Bundle;
import android.os.SystemClock;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.demomvvm.school.SchoolApplication;
import com.demomvvm.school.R;
import com.demomvvm.school.adapter.StoreListAdapter;
import com.demomvvm.school.databinding.FragmentStoretlistBinding;
import com.demomvvm.school.model.storeListmodel.StoreResponse;
import com.demomvvm.school.model.storeListmodel.StoreResponseData;
import com.demomvvm.school.mvvm.storelist.StoreListNavigator;
import com.demomvvm.school.mvvm.storelist.StoreViewModel;
import com.demomvvm.school.util.Preferences;
import com.demomvvm.school.util.Utils;
import com.ethanhua.skeleton.Skeleton;
import com.ethanhua.skeleton.SkeletonScreen;
import com.miguelcatalan.materialsearchview.MaterialSearchView;

import java.util.ArrayList;


public class StoreListFragment extends BaseFragment implements StoreListAdapter.OnItemClickListener, StoreListNavigator {


    //Declaration

    private ArrayList<StoreResponse> modelArrayList = new ArrayList<>();
    private StoreListAdapter productListAdapter;

    private static int MAX_CLICK_INTERVAL = 1500;
    private long mLastClickTime = 0;
    private int lastVisibleItem;
    private int totalItemCount;
    private int visibleThreshold = 1;
    private int pageItemCount = 0;
    private int pageIndex = 0;
    private Activity activity;
    private MaterialSearchView searchView;

    private StoreListNavigator storeListNavigator;
    private StoreViewModel storeViewModel;
    private FragmentStoretlistBinding binding;

    private String lat;
    private String lng;



    private View rootView;
    private SkeletonScreen skeletonScreen;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_storetlist, container, false);
        rootView = binding.getRoot();
        activity = SchoolApplication.getmInstance().getActivity();
        initComponents(rootView);
        setHasOptionsMenu(true);
        return rootView;
    }


    @Override
    public void initComponents(View rootView) {

        storeListNavigator = this;
        storeViewModel = new StoreViewModel(activity, storeListNavigator);
        binding.setStoreViewModel(storeViewModel);
        binding.fragmentProductlistRvProductList.setHasFixedSize(true);


        lat = Preferences.readString(getActivity(), Preferences.SELECTED_CITY_LATITUDE, "");
        lng = Preferences.readString(getActivity(), Preferences.SELECTED_CITY_LONGITUDE, "");
        searchView = (MaterialSearchView) getActivity().findViewById(R.id.search_view);

        setUpAdapater(modelArrayList);
        setUpSkelotView();
        setUpSearch();

    }

    private void setUpSkelotView() {


        skeletonScreen = Skeleton.bind(binding.fragmentProductlistRvProductList)
                .adapter(productListAdapter)
                .load(R.layout.row_loading_skeleton)
                .shimmer(false)
                .show();

        getProductListData(false);


    }


    public void getProductListData(final boolean isLoadmore) {

        if (Utils.isOnline(activity, true)) {

            binding.fragmentProductlistLlLoadMoreProgress.setVisibility(isLoadmore ? View.VISIBLE : View.GONE);


            if (Preferences.readBoolean(getActivity(), Preferences.SELECTEDLOCATIONORCITY, false)) {

                lat = Preferences.readString(getActivity(), Preferences.SELECTED_CITY_LATITUDE, "");
                lng = Preferences.readString(getActivity(), Preferences.SELECTED_CITY_LONGITUDE, "");
                storeViewModel.getStoreListLatlng(lat, lng, "" + pageIndex);

            } else {

                storeViewModel.getStoreList(Preferences.readString(getActivity(), Preferences.SELECTED_LOCATION_ID, ""), "" + pageIndex);

            }


        } else {
            skeletonScreen = Skeleton.bind(binding.fragmentProductlistRvProductList)
                    .load(R.layout.nointernet_skeleton)
                    .adapter(productListAdapter)
                    .shimmer(false)
                    .show();

        }
    }

    @Override
    public void onItemClick(View view, StoreResponse viewModel) {

        if (SystemClock.elapsedRealtime() - mLastClickTime < MAX_CLICK_INTERVAL) {
            return;
        }
        mLastClickTime = SystemClock.elapsedRealtime();

        Utils.hideKeyboard(activity);



    }


    private void emptyView() {

        skeletonScreen = Skeleton.bind(binding.fragmentProductlistRvProductList)
                .adapter(productListAdapter)
                .load(R.layout.empty_skeleton)
                .shimmer(false)
                .show();

    }

    private void setUpAdapater(final ArrayList<StoreResponse> guidelineModelArrayList) {


        binding.fragmentProductlistRvProductList.removeAllViews();
        binding.fragmentProductlistRvProductList.setHasFixedSize(true);
        final LinearLayoutManager mLayoutManager = new LinearLayoutManager(activity);
        binding.fragmentProductlistRvProductList.setLayoutManager(mLayoutManager);

        productListAdapter = new StoreListAdapter(StoreListFragment.this, activity, guidelineModelArrayList);
        productListAdapter.setOnItemClickListener(this);
        binding.fragmentProductlistRvProductList.setAdapter(productListAdapter);

        binding.fragmentProductlistRvProductList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                totalItemCount = mLayoutManager.getItemCount();
                lastVisibleItem = mLayoutManager.findLastVisibleItemPosition();

                if (totalItemCount > 0) {

                    if (!productListAdapter.isLoading() && totalItemCount <= (lastVisibleItem + visibleThreshold)) {

                        if (pageItemCount > totalItemCount) {
                            productListAdapter.setLoading();
                            pageIndex++;
                            getProductListData(true);
                        }
                    }
                }
            }
        });


    }


    @Override
    public void onClick(View v) {


    }


    /**
     * Called when coming back from next screen
     */
    @Override
    public void onHiddenChanged(boolean hidden) {
        super.onHiddenChanged(hidden);
        if (!hidden) {
            setHasOptionsMenu(true);

        }
    }


    @Override
    public void handleError(Throwable throwable) {
        emptyView();
        Utils.snackbar(binding.fragmentProductlistLlContainer, "" + throwable.getMessage(), true, activity);
    }

    @Override
    public void storeResponce(StoreResponseData userResponse) {

        skeletonScreen.hide();


        if (userResponse.getResponsedata().getSuccess().equalsIgnoreCase("1")) {

            ArrayList<StoreResponse> productModelArrayList = (ArrayList<StoreResponse>) userResponse.getResponsedata().getData();
            pageItemCount = userResponse.getResponsedata().getCount();
            productListAdapter.setLoaded();
            binding.fragmentProductlistLlLoadMoreProgress.setVisibility(View.GONE);

            if (productModelArrayList != null && productModelArrayList.size() > 0) {
                modelArrayList.addAll(productModelArrayList);
                binding.fragmentProductlistRvProductList.scrollToPosition(totalItemCount);
                 //productListAdapter.addRecord(modelArrayList);
                //productListAdapter.notifyDataSetChanged();


            } else {

                if (modelArrayList.size() < 0) {
                    emptyView();
                }
            }
        } else {

            emptyView();
        }
    }

    private void setUpSearch() {

        searchView.setOnSearchViewListener(new MaterialSearchView.SearchViewListener() {
            @Override
            public void onSearchViewShown() {



            }

            @Override
            public void onSearchViewClosed() {
               // ((MainActivity) getActivity()).changeFragment(new StoreListFragment(), true);
                getFragmentManager().popBackStack();

            }
        });

    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.main, menu);
        MenuItem itemSearch = menu.findItem(R.id.action_search);
        searchView.setMenuItem(itemSearch);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);


//        if(requestCode==999)
//        {
//            setHasOptionsMenu(true);
//            ((MainActivity) getActivity()).changeTitle(true, Preferences.readString(activity, Preferences.SELECTED_CITY, ""));
//            StoreResponse storeResponse=data.getParcelableExtra(Utils.INTENT_STORE_DETAILS);
//            Intent intentStoreDetailsActivity = new Intent(getActivity(), StoreDetailsActivity.class);
//            intentStoreDetailsActivity.putExtra(Utils.INTENT_STORE_DETAILS, storeResponse);
//            startActivity(intentStoreDetailsActivity);
//        }
    }
}

