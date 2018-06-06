package com.thetehnocafe.gurleensethi.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public abstract class NetworkBoundResource<ResultType, RequestType> {

    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    public NetworkBoundResource() {
        //Send Loading
        result.setValue(Resource.loading(null));
        fetchFromNetwork();
    }

    private void fetchFromNetwork() {
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable ApiResponse<RequestType> requestTypeApiResponse) {
                result.removeSource(apiResponse);

                if (requestTypeApiResponse.isSuccessful() && requestTypeApiResponse.getData() != null) {
                    result.setValue(Resource.success(convert(requestTypeApiResponse.getData())));
                } else {
                    result.setValue(Resource.error(requestTypeApiResponse.getMessage(), null));
                }
            }
        });
    }

    protected abstract ResultType convert(RequestType requestType);

    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    public LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
