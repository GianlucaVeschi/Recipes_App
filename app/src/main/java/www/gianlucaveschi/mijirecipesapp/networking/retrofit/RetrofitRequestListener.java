package www.gianlucaveschi.mijirecipesapp.networking.retrofit;


public interface RetrofitRequestListener<T> {
    //List<MealSimple> getResult(T object);
    void getResult(T object);
}

