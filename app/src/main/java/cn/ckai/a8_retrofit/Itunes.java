package cn.ckai.a8_retrofit;

import java.util.List;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public class Itunes {

    public static class Respuesta {
        public List<Document> documents;
    }

    public static class Document {
        public Fields fields;
    }

    public static class Fields {
        public StringValue artista;
        public StringValue titulo;
        public StringValue portada;
    }

    // 对应 JSON 中的 stringValue 字段
    public static class StringValue {
        public String stringValue;
    }

    public static class Contenido {
        public String artista;
        public String titulo;
        public String portada;
    }
    public static Api api = new Retrofit.Builder()
            .baseUrl("https://firestore.googleapis.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(Api.class);

    interface Api {
        @GET("v1/projects/retrofit-683b2/databases/(default)/documents/albums")
        Call<Respuesta> getAlbums();
    }
}
