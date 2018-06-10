package com.thetehnocafe.gurleensethi.popularmovies.data;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MediatorLiveData;
import android.arch.lifecycle.Observer;
import android.support.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.ObservableEmitter;
import io.reactivex.ObservableOnSubscribe;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

@SuppressWarnings({"unchecked", "ConstantConditions"})
public abstract class NetworkBoundResource<ResultType, RequestType> {
    private static final String TAG = NetworkBoundResource.class.getSimpleName();
    private final MediatorLiveData<Resource<ResultType>> result = new MediatorLiveData<>();

    public NetworkBoundResource() {
        //Send Loading
        result.setValue(Resource.loading(null));

        final LiveData<ResultType> dbSource = loadFromDB();
        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType resultType) {
                result.removeSource(dbSource);
                if (shouldFetch(resultType)) {
                    fetchFromNetwork(dbSource);
                } else {
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType resultType) {
                            result.setValue(Resource.success(resultType));
                        }
                    });
                }
            }
        });
    }

    private void fetchFromNetwork(final LiveData<ResultType> dbSource) {
        final LiveData<ApiResponse<RequestType>> apiResponse = createCall();

        result.addSource(dbSource, new Observer<ResultType>() {
            @Override
            public void onChanged(@Nullable ResultType resultType) {
                result.setValue(Resource.loading(resultType));
            }
        });

        result.addSource(apiResponse, new Observer<ApiResponse<RequestType>>() {
            @Override
            public void onChanged(@Nullable final ApiResponse<RequestType> requestTypeApiResponse) {
                result.removeSource(apiResponse);
                result.removeSource(dbSource);

                if (requestTypeApiResponse.isSuccessful() && requestTypeApiResponse.getData() != null) {
                    saveAndReInit(requestTypeApiResponse);
                } else {
                    result.addSource(dbSource, new Observer<ResultType>() {
                        @Override
                        public void onChanged(@Nullable ResultType resultType) {
                            result.setValue(Resource.error(requestTypeApiResponse.getMessage(), resultType));
                        }
                    });
                }
            }
        });
    }

    private void saveAndReInit(final ApiResponse<RequestType> apiResponse) {
        Observable.create(new ObservableOnSubscribe<Void>() {
            @Override
            public void subscribe(ObservableEmitter<Void> emitter) throws Exception {
                saveCallResponse(apiResponse.getData());
                emitter.onComplete();
            }
        }).subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new io.reactivex.Observer<Void>() {
                    @Override
                    public void onSubscribe(Disposable d) {
                    }

                    @Override
                    public void onNext(Void aVoid) {
                    }

                    @Override
                    public void onError(Throwable e) {
                        e.printStackTrace();
                    }

                    @Override
                    public void onComplete() {
                        result.addSource(loadFromDB(), new Observer<ResultType>() {
                            @Override
                            public void onChanged(@Nullable ResultType resultType) {
                                result.setValue(Resource.success(resultType));
                            }
                        });
                    }
                });
    }

    protected abstract boolean shouldFetch(ResultType resultType);

    protected abstract LiveData<ResultType> loadFromDB();

    protected abstract void saveCallResponse(RequestType item);

    protected abstract LiveData<ApiResponse<RequestType>> createCall();

    public LiveData<Resource<ResultType>> getAsLiveData() {
        return result;
    }
}
