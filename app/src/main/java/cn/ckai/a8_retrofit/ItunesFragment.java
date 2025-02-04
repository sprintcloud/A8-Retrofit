package cn.ckai.a8_retrofit;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.SearchView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;
import java.util.List;

import cn.ckai.a8_retrofit.databinding.FragmentItunesBinding;
import cn.ckai.a8_retrofit.databinding.ViewholderContenidoBinding;

public class ItunesFragment extends Fragment {
    private FragmentItunesBinding binding;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return (binding = FragmentItunesBinding.inflate(inflater, container, false)).getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        ItunesViewModel itunesViewModel = new ViewModelProvider(this).get(ItunesViewModel.class);

        binding.texto.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                itunesViewModel.getAlbums();
                return false;
            }
        });

        ContenidosAdapter contenidosAdapter = new ContenidosAdapter();
        binding.recyclerviewContenidos.setAdapter(contenidosAdapter);

        itunesViewModel.respuestaMutableLiveData.observe(getViewLifecycleOwner(), new Observer<Itunes.Respuesta>() {
            @Override
            public void onChanged(Itunes.Respuesta respuesta) {
                if (respuesta != null && respuesta.documents != null) {
                    for (Itunes.Document document : respuesta.documents) {
                        Itunes.Fields fields = document.fields;
                        if (fields != null) {
                            String artista = fields.artista != null ? fields.artista.stringValue : "";
                            String titulo = fields.titulo != null ? fields.titulo.stringValue : "";
                            String portada = fields.portada != null ? fields.portada.stringValue : "";
                            Log.e("ABCD", artista + ", " + titulo + ", " + portada);
                        }
                    }
                    // 更新适配器数据
                    List<Itunes.Contenido> contenidoList = new ArrayList<>();
                    for (Itunes.Document document : respuesta.documents) {
                        Itunes.Fields fields = document.fields;
                        if (fields != null) {
                            Itunes.Contenido contenido = new Itunes.Contenido();
                            contenido.artista = fields.artista != null ? fields.artista.stringValue : "";
                            contenido.titulo = fields.titulo != null ? fields.titulo.stringValue : "";
                            contenido.portada = fields.portada != null ? fields.portada.stringValue : "";
                            contenidoList.add(contenido);
                        }
                    }
                    contenidosAdapter.establecerListaContenido(contenidoList);
                } else {
                    Log.e("ItunesFragment", "respuesta or respuesta.documents is null");
                    contenidosAdapter.establecerListaContenido(null);
                }
            }
        });    }

    static class ContenidoViewHolder extends RecyclerView.ViewHolder {
        ViewholderContenidoBinding binding;

        public ContenidoViewHolder(@NonNull ViewholderContenidoBinding binding) {
            super(binding.getRoot());
            this.binding = binding;
        }
    }

    class ContenidosAdapter extends RecyclerView.Adapter<ContenidoViewHolder> {
        List<Itunes.Contenido> contenidoList;

        @NonNull
        @Override
        public ContenidoViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            return new ContenidoViewHolder(ViewholderContenidoBinding.inflate(getLayoutInflater(), parent, false));
        }

        @Override
        public void onBindViewHolder(@NonNull ContenidoViewHolder holder, int position) {
            Itunes.Contenido contenido = contenidoList.get(position);

            holder.binding.title.setText(contenido.titulo);
            holder.binding.artist.setText(contenido.artista);
            Glide.with(requireActivity()).load(contenido.portada).into(holder.binding.artwork);
        }

        @Override
        public int getItemCount() {
            return contenidoList == null ? 0 : contenidoList.size();
        }

        void establecerListaContenido(List<Itunes.Contenido> contenidoList) {
            this.contenidoList = contenidoList;
            notifyDataSetChanged();
        }
    }
}