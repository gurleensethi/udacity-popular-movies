package com.thetehnocafe.gurleensethi.popularmovies.home;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.thetehnocafe.gurleensethi.popularmovies.R;
import com.thetehnocafe.gurleensethi.popularmovies.common.SortOption;

import butterknife.BindView;
import butterknife.ButterKnife;

public class OptionsBottomSheetFragment extends BottomSheetDialogFragment {

    public interface ActionCallback {
        void onChangeSortOption(SortOption sortOption);
    }

    @BindView(R.id.popularButton)
    Button mPopularButton;
    @BindView(R.id.topRatedButton)
    Button mTopRatedButton;
    @BindView(R.id.favouriteButton)
    Button mFavouriteButton;

    private ActionCallback mActionCallback;

    public static OptionsBottomSheetFragment getInstance() {
        return new OptionsBottomSheetFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_options, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initViews();
    }

    private void initViews() {
        mPopularButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionAndDismiss(SortOption.POPULAR);
            }
        });

        mTopRatedButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionAndDismiss(SortOption.TOP_RATED);
            }
        });

        mFavouriteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectOptionAndDismiss(SortOption.FAVOURITE);
            }
        });
    }

    private void selectOptionAndDismiss(SortOption sortOption) {
        if (mActionCallback != null) {
            mActionCallback.onChangeSortOption(sortOption);
        }
        dismiss();
    }

    public void addActionCallback(ActionCallback actionCallback) {
        this.mActionCallback = actionCallback;
    }
}
