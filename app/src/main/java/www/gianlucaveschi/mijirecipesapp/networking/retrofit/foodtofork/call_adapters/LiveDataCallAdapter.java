package www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.call_adapters;

import java.lang.reflect.Type;

import androidx.lifecycle.LiveData;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Callback;
import retrofit2.Response;
import www.gianlucaveschi.mijirecipesapp.networking.retrofit.foodtofork.responses.ApiResponse;

/**
 * Converts the RETROFIT call to LIVE DATA*/
public class LiveDataCallAdapter<R> implements CallAdapter<R, LiveData<ApiResponse<R>>> {

    private Type responseType;

    public LiveDataCallAdapter(Type responseType) {
        this.responseType = responseType;
    }

    @Override
    public Type responseType() {
        return responseType;
    }

    @Override
    public LiveData<ApiResponse<R>> adapt(final Call<R> call) {
        //OnActive allows the access to the LIVE DATA methods postValue
        return new LiveData<ApiResponse<R>>() {
            @Override
            protected void onActive() {
                super.onActive();
                final ApiResponse apiResponse = new ApiResponse();
                call.enqueue(new Callback<R>() {
                    @Override
                    public void onResponse(Call<R> call, Response<R> response) {
                        postValue(apiResponse.create(response));
                    }

                    @Override
                    public void onFailure(Call<R> call, Throwable t) {
                        postValue(apiResponse.create(t));
                    }
                });
            }
        };
    }

}
