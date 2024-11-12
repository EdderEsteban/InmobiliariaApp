package com.abbysoft.inmobiliariaapp.request;

import com.abbysoft.inmobiliariaapp.models.Contrato;
import com.abbysoft.inmobiliariaapp.models.Inmueble;
import com.abbysoft.inmobiliariaapp.models.Pago;
import com.abbysoft.inmobiliariaapp.models.Propietario;
import com.abbysoft.inmobiliariaapp.models.Tipo;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.List;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class ApiClient {
    // Siempre crear una constante para la url base de la API
    public static final String URLBASE = "https://cvhlm5sz-5058.brs.devtunnels.ms/api/";
    public static final String URLFOTOS = "https://cvhlm5sz-5058.brs.devtunnels.ms";

    //
    public static InmobiliariaService getApiInmobiliaria() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .setDateFormat("yyyy-MM-dd")
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(URLBASE)
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        return retrofit.create(InmobiliariaService.class);
    }



    // Creando una Interface
    public interface InmobiliariaService {

        // Metodo ApiLogin
        @FormUrlEncoded
        @POST("ApiPropietarios/ApiLogin")
        Call <String> apilogin(@Field("Email") String email, @Field("Password") String password);

        // Metodo UpdatePassword
        // Usamos FormUrlEncode cuando en endpoind pide [FromForm]
        @FormUrlEncoded
        @PUT("ApiPropietarios/UpdatePassword")
        Call <String> UpdatePassword(@Header("Authorization")String token, @Field("oldpassword") String oldPassword, @Field("newpassword") String newPassword);

        // Metodo RequestPasswordReset
        @FormUrlEncoded
        @POST("ApiPropietarios/RequestPasswordReset")
        Call <String> ResetPassword(@Field("Mail") String email);

        // Metodo MyPropietario
        @GET("ApiPropietarios/MyPropietario")
        Call <Propietario> MyPropietario(@Header("Authorization")String token);

        // Metodo UpdatePropietario
        // Sacamos FormUrlEncode cuando en endpoind pide [FromBody]
        @PUT("ApiPropietarios/UpdatePropietario")
        Call<Propietario> UpdatePropietario(@Header("Authorization") String token, @Body Propietario propietario);

        // Metodo ActualizarFoto
        @Multipart
        @PUT("ApiPropietarios/ActualizarFoto")
        Call <String> ActualizarFoto(@Header("Authorization") String token, @Part MultipartBody.Part avatarFile);

        // Metodo MisInmuebles
        @GET("ApiInmuebles/MisInmuebles")
        Call <List<Inmueble>> MisInmuebles(@Header("Authorization")String token);

        // Metodo ActualizarEstadoActivo
        @FormUrlEncoded
        @PATCH("ApiInmuebles/ActualizarEstado/{id}")
        Call<Void> actualizarEstadoActivo(@Header("Authorization") String token, @Path("id") int id, @Field("Activo") boolean activo);

        // Metodo SubirFotos a un inmueble
        @Multipart
        @POST("ApiInmuebles/SubirFotos")
        Call <String> SubirFotos(@Header("Authorization") String token, @Query("inmuebleId") int id, @Part List<MultipartBody.Part> fotos);

        // Metodo ListadodeTipos
        @GET("ApiInmuebles/ListadodeTipos")
        Call <List<Tipo>> ListadodeTipos(@Header("Authorization")String token);

        // Metodo AltaInmueble
        @POST("ApiInmuebles/AltaInmueble")
        Call <Inmueble> AltaInmueble(@Header("Authorization")String token, @Body Inmueble inmueble);
        
        // Metodo ListadodeContratos
        @GET("ApiInmuebles/ListadodeContratos/{id}")
        Call <List<Contrato>> ListadodeContratos(@Header("Authorization")String token, @Path("id") int id);

        // Metodo ListadodePagos/{id}
        @GET("ApiInmuebles/ListadodePagos/{id}")
        Call <List<Pago>> ListadodePagos(@Header("Authorization")String token, @Path("id") int id);
    
    }


}
