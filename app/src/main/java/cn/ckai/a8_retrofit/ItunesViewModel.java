package cn.ckai.a8_retrofit;

import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ItunesViewModel extends ViewModel {
    public MutableLiveData<Itunes.Respuesta> respuestaMutableLiveData = new MutableLiveData<>();

    public void getAlbums() {
        Call<Itunes.Respuesta> call = Itunes.api.getAlbums();
        call.enqueue(new Callback<Itunes.Respuesta>() {
            @Override
            public void onResponse(Call<Itunes.Respuesta> call, Response<Itunes.Respuesta> response) {
                if (response.isSuccessful()) {
                    Itunes.Respuesta respuesta = response.body();
                    if (respuesta != null) {
                        Log.d("ItunesViewModel", "Response received: " + respuesta.toString());
                        if (respuesta.documents != null) {
                            Log.d("ItunesViewModel", "Documents size: " + respuesta.documents.size());
                            List<Itunes.Contenido> contenidoList = new ArrayList<>();
                            for (Itunes.Document document : respuesta.documents) {
                                Itunes.Fields fields = document.fields;
                                if (fields != null) {
                                    Itunes.Contenido contenido = new Itunes.Contenido();
                                    contenido.artista = fields.artista != null ? fields.artista.stringValue : "";
                                    contenido.titulo = fields.titulo != null ? fields.titulo.stringValue : "";
                                    contenido.portada = fields.portada != null ? fields.portada.stringValue : "";
                                    contenidoList.add(contenido);

                                    Log.d("ItunesViewModel", "Contenido: artista=" + contenido.artista + ", titulo=" + contenido.titulo + ", portada=" + contenido.portada);
                                }
                            }
                        } else {
                            Log.e("ItunesViewModel", "Documents list is null");
                        }
                        respuestaMutableLiveData.setValue(respuesta);
                    } else {
                        Log.e("ItunesViewModel", "Response body is null");
                    }
                } else {
                    Log.e("ItunesViewModel", "Request failed with code: " + response.code());
                    respuestaMutableLiveData.setValue(null);
                }
            }

            @Override
            public void onFailure(Call<Itunes.Respuesta> call, Throwable t) {
                Log.e("ItunesViewModel", "Request failed: " + t.getMessage());
                respuestaMutableLiveData.setValue(null);
            }
        });
    }
}