package com.nexusmaven;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    
    private static final Gson gson = new GsonBuilder().
    setPrettyPrinting().
    registerTypeAdapter(Usuario.class, new UsuarioAdapter()).
    create();

    // <T> significa que esse método aceita qualquer classe
    public static <T> List<T> ler(String nomeArquivo, Class<T> classe) {
        File file = new File(nomeArquivo);
        if (!file.exists()) return new ArrayList<>();

        try (Reader reader = new FileReader(file)) {
            // Cria o tipo genérico da lista baseado na classe passada
            Type tipoLista = TypeToken.getParameterized(List.class, classe).getType();
            List<T> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new RuntimeException("Erro ao ler o arquivo JSON: " + nomeArquivo, e);
        }
    }

    public static <T> void salvar(String nomeArquivo, List<T> lista) {
        try (Writer writer = new FileWriter(nomeArquivo)) {
            gson.toJson(lista, writer);
        } catch (IOException e) {
            throw new RuntimeException("Erro ao salvar o arquivo JSON: " + nomeArquivo, e);
        }
    }

    // =========================================================
    // O TRADUTOR (Lê a etiqueta "tipo" e cria a classe correta)
    // =========================================================
    private static class UsuarioAdapter implements JsonDeserializer<Usuario> {
        @Override
        public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            JsonObject jsonObject = json.getAsJsonObject();
            
            // Verifica se a etiqueta "tipo" existe no JSON
            if (jsonObject.has("tipo")) {
                String tipo = jsonObject.get("tipo").getAsString();
                if (tipo.equals("admin")) {
                    return context.deserialize(jsonObject, admin.class);
                } else if (tipo.equals("inquilino")) {
                    return context.deserialize(jsonObject, inquilino.class);
                }
            }
            
            // Se não tiver a etiqueta "tipo" (ou for um tipo desconhecido), 
            // tenta ler como um Usuario comum para não quebrar o sistema.
            // (Se o seu sistema só aceita admin/inquilino, você pode trocar por um throw)
            return context.deserialize(jsonObject, Usuario.class);
        }
    }
}
