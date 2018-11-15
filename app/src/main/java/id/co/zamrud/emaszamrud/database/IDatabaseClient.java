package id.co.zamrud.emaszamrud.database;

import id.co.zamrud.emaszamrud.model.DataLiqo;
import id.co.zamrud.emaszamrud.model.DataLiqoClass;
import id.co.zamrud.emaszamrud.model.PostExecuteDatabase;
import id.co.zamrud.emaszamrud.model.DataPenjualanClass;
import id.co.zamrud.emaszamrud.model.LoginClass;
import id.co.zamrud.emaszamrud.model.keuangan.DataKeuanganClass;
import id.co.zamrud.emaszamrud.model.kota.DataKotaParentClass;
import id.co.zamrud.emaszamrud.model.quran.DataSuratClass;
import id.co.zamrud.emaszamrud.model.shalat.DataShalatParent;
import id.co.zamrud.emaszamrud.model.shalat.otherServer.ShalatOtherServer;
import id.co.zamrud.emaszamrud.util.Config;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface IDatabaseClient {
    @POST("Login/LoginApi")
    @FormUrlEncoded
    Call<LoginClass> LoginUser(@Field("data") String data);

    /*shop activity*/
    @GET("DataPenjualan/GetDataPenjualan")
    Call<DataPenjualanClass> getDataPenjualan(
            @Query("tgl_awal") String tgl_awal,
            @Query("tgl_akhir") String tgl_akhir
    );

    @POST("DataPenjualan/DeleteDataPenjualan")
    @FormUrlEncoded
    Call<PostExecuteDatabase> deleteDataPenjualan(@Field("jsondata") String jsondata);

    @POST("DataPenjualan/EditDataPenjualan")
    @FormUrlEncoded
    Call<PostExecuteDatabase> editDataPenjualan(@Field("jsondata") String jsondata);

    @POST("DataPenjualan/SaveDataPenjualan")
    @FormUrlEncoded
    Call<PostExecuteDatabase> insertDataPenjualan(@Field("jsondata") String jsondata);

    @POST("DataPenjualan/UpdateDataPenjualan")
    @FormUrlEncoded
    Call<PostExecuteDatabase> updateDataPenjualan(@Field("jsondata") String jsondata);

    /*liqo activity*/
    @GET("DataLiqo/GetDataLiqo")
    Call<DataLiqoClass> getDataLiqo(
            @Query("tanggal") String tanggal
    );

    @GET("surah")
    Call<DataSuratClass> getDataSurat();

    @POST("DataLiqo/SaveDataLiqo")
    @FormUrlEncoded
    Call<PostExecuteDatabase> insertDataLiqo(@Field("jsondata") String jsondata);

    @POST("DataLiqo/DeleteDataLiqo")
    @FormUrlEncoded
    Call<PostExecuteDatabase> deleteDataLiqo(@Field("jsondata") String jsondata);

    @POST("DataLiqo/UpdateDataLiqo")
    @FormUrlEncoded
    Call<PostExecuteDatabase> updateDataLiqo(@Field("jsondata") String jsondata);

    /*Shalat activity*/
    @Headers("key:e6ceaeaa85dda0ae5ba2157b989685f2")
    @GET("starter/city")
    Call<DataKotaParentClass> getKota();

    @GET("{city}.json")
    Call<DataShalatParent> getJadwalShalat(@Path("city") String city, @Query("key") String key);

    @GET("pray/{city}")
    Call<ShalatOtherServer> getJadwalShalatOtherServer(@Path("city") String city);

    /*Keuangan Pemasukan activity*/
    @GET("DataKeuangan/GetDataPemasukan")
    Call<DataKeuanganClass> getDataPemasukan(
            @Query("tanggal") String tanggal
    );

    @POST("DataKeuangan/SaveDataPemasukan")
    @FormUrlEncoded
    Call<PostExecuteDatabase> insertDataPemasukan(@Field("jsondata") String jsondata);

    @GET("DataKeuangan/DeleteDataPemasukan")
    Call<PostExecuteDatabase> deleteDataPemasukan(
            @Query("id_pemasukan") String id_pemasukan
    );

    @POST("DataKeuangan/UpdateDataPemasukan")
    @FormUrlEncoded
    Call<PostExecuteDatabase> updateDataPemasukan(@Field("jsondata") String jsondata);

    /*Keuangan Pengeluaran activity*/
    @GET("DataKeuangan/GetDataPengeluaran")
    Call<DataKeuanganClass> getDataPengeluaran(
            @Query("tanggal") String tanggal
    );

    @POST("DataKeuangan/SaveDataPengeluaran")
    @FormUrlEncoded
    Call<PostExecuteDatabase> insertDataPengeluaran(@Field("jsondata") String jsondata);

    @GET("DataKeuangan/DeleteDataPengeluaran")
    Call<PostExecuteDatabase> deleteDataPengeluaran(
            @Query("id_pengeluaran") String id_pengeluaran
    );

    @POST("DataKeuangan/UpdateDataPengeluaran")
    @FormUrlEncoded
    Call<PostExecuteDatabase> updateDataPengeluaran(@Field("jsondata") String jsondata);

    //UNTUK TEST BBM
    @POST("DataLiqo/DeleteDataLiqo")
    @FormUrlEncoded
    Call<PostExecuteDatabase> downloadData();
}
