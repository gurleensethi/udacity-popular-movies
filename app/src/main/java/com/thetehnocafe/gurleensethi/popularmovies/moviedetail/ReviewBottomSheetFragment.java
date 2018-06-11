package com.thetehnocafe.gurleensethi.popularmovies.moviedetail;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.thetehnocafe.gurleensethi.popularmovies.R;
import com.thetehnocafe.gurleensethi.popularmovies.data.models.MovieReview;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewBottomSheetFragment extends BottomSheetDialogFragment {

    private static final String ARGS_AUTHOR_NAME = "author_name";
    private static final String ARGS_CONTENT = "content";

    public static class Builder {
        private String authorName;
        private String content;

        public Builder setAuthorName(String authorName) {
            this.authorName = authorName;
            return this;
        }

        public Builder setContent(String content) {
            this.content = content;
            return this;
        }

        public ReviewBottomSheetFragment build() {
            return new ReviewBottomSheetFragment(this);
        }
    }

    public ReviewBottomSheetFragment() {
    }

    @SuppressLint("ValidFragment")
    public ReviewBottomSheetFragment(Builder builder) {
        Bundle args = new Bundle();
        args.putString(ARGS_AUTHOR_NAME, builder.authorName);
        args.putString(ARGS_CONTENT, builder.content);
        setArguments(args);
    }

    @BindView(R.id.authorNameTextView)
    TextView mAuthorNameTextView;
    @BindView(R.id.contentTextView)
    TextView mContentTextView;
    @BindView(R.id.closeImageButton)
    ImageButton mCloseImageButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.bottom_sheet_review, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);

        initViews();
    }

    private void initViews() {
        if (getArguments() == null) {
            dismiss();
            return;
        }

        mAuthorNameTextView.setText(getArguments().getString(ARGS_AUTHOR_NAME));
        mContentTextView.setText(getArguments().getString(ARGS_CONTENT));
        mCloseImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
