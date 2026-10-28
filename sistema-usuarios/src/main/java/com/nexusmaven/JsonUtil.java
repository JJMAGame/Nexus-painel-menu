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
import com.google.gson.reflect.TypeToken;

public class JsonUtil {
    
    private static final Gson gson = new GsonBuilder().setPrettyPrinting().create();

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
}
