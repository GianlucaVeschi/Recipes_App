package www.gianlucaveschi.load_json_images_picasso.networking.retrofit;


public interface RetrofitRequestListener<T> {
    //List<MealSimple> getResult(T object);
    void getResult(T object);
}

