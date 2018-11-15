package id.co.zamrud.emaszamrud.database;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import id.co.zamrud.emaszamrud.util.Config;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class ApiGenerator {
    private static HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
    private static Interceptor logging = interceptor.setLevel(HttpLoggingInterceptor.Level.BASIC);

    private static OkHttpClient httpClient = new OkHttpClient.Builder()
            .addInterceptor(logging)
            .build();

    private static Retrofit.Builder builder =
            new Retrofit.Builder()
                    .baseUrl(Config.BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builderQuran =
            new Retrofit.Builder()
                    .baseUrl(Config.QURAN_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builderKota =
            new Retrofit.Builder()
                    .baseUrl(Config.KOTA_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builderShalat =
            new Retrofit.Builder()
                    .baseUrl(Config.SHALAT_URL)
                    .addConverterFactory(GsonConverterFactory.create());

    private static Retrofit.Builder builderShalatOtherServer =
            new Retrofit.Builder()
                    .baseUrl(Config.SHALAT_URL_OTHER_SERVER)
                    .addConverterFactory(GsonConverterFactory.create());

    public static <S> S createService(Class<S> serviceClass) {
        Retrofit retrofit = builder.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceQuran(Class<S> serviceClass) {
        Retrofit retrofit = builderQuran.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceKota(Class<S> serviceClass) {
        Retrofit retrofit = builderKota.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceShalat(Class<S> serviceClass) {
        Retrofit retrofit = builderShalat.client(httpClient).build();
        return retrofit.create(serviceClass);
    }

    public static <S> S createServiceShalatOtherServer(Class<S> serviceClass) {
        Retrofit retrofit = builderShalatOtherServer.client(httpClient).build();
        return retrofit.create(serviceClass);
    }
}
