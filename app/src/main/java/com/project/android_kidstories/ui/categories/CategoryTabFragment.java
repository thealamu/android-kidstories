package com.project.android_kidstories.ui.categories;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;
import com.project.android_kidstories.R;
import com.project.android_kidstories.data.model.Story;
import com.project.android_kidstories.data.source.remote.api.Api;
import com.project.android_kidstories.data.source.remote.api.RetrofitClient;
import com.project.android_kidstories.data.source.remote.response_models.BaseResponse2;
import com.project.android_kidstories.ui.base.BaseFragment;
import com.project.android_kidstories.ui.categories.adapters.CategoryTabAdapter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.List;

public class CategoryTabFragment extends BaseFragment {

    private static final String CATEGORY_ID_KEY = "CATEGORY_ID_KEY";
    private String category_id;

    private CategoryTabAdapter adapter;

    private View progressBar;
    private View errorView;
    private View emptyView;

    private Call<BaseResponse2> serviceCall;

    static CategoryTabFragment newInstance(String category_id) {
        Bundle args = new Bundle();
        args.putString(CATEGORY_ID_KEY, category_id);
        CategoryTabFragment ctf = new CategoryTabFragment();
        ctf.setArguments(args);
        return ctf;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.fragment_category_tab, container, false);

        Log.d("GLOBAL_SCOPE", "In TabFragment");

        // Get category name
        Bundle arguments = getArguments();
        if (arguments == null) {
            Log.e("GLOBAL_SCOPE", "No category id");
            return root;
        }

        category_id = getArguments().getString(CATEGORY_ID_KEY);

        RecyclerView recyclerView = root.findViewById(R.id.recyclerview_category_tab);
        progressBar = root.findViewById(R.id.tab_category_bar);
        errorView = root.findViewById(R.id.error_msg);
        emptyView = root.findViewById(R.id.empty_message);

        adapter = new CategoryTabAdapter(this);
        recyclerView.setAdapter(adapter);

        getCategoryStories();

        return root;
    }

    private void getCategoryStories() {
        Log.d("GLOBAL_SCOPE", "In TabFragment getting stories for this category");

        progressBar.setVisibility(View.VISIBLE);

        Api service = RetrofitClient.getInstance().create(Api.class);

        serviceCall = service.getStoriesByCategoryIdandUser2(category_id);
        serviceCall.enqueue(new Callback<BaseResponse2>() {
            @Override
            public void onResponse(Call<BaseResponse2> call, Response<BaseResponse2> response) {
                progressBar.setVisibility(View.GONE);

                Log.d("GLOBAL_SCOPE", String.valueOf(response.code()));

                if (response.isSuccessful()) {
                    BaseResponse2 br2 = response.body();
                    if (br2 != null && !br2.getStories().isEmpty()) {
                        emptyView.setVisibility(View.GONE);
                        List<Story> stories = br2.getStories();
                        Log.d("GLOBAL_SCOPE", String.valueOf(stories.size()));
                        adapter.submitList(stories);
                    } else {
                        emptyView.setVisibility(View.VISIBLE);
                    }

                } else {
                    errorView.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onFailure(Call<BaseResponse2> call, Throwable t) {
                progressBar.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onDetach() {
        super.onDetach();
        if (serviceCall != null) serviceCall.cancel();
    }
}