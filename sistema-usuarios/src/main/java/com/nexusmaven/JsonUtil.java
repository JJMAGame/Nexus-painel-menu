package com.nexusmaven;

import java.io.IOException;
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.lang.reflect.Type;
import java.nio.charset.CharacterCodingException;
import java.nio.charset.StandardCharsets;
import java.nio.file.AtomicMoveNotSupportedException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

/** Lê e grava os "bancos de dados" do sistema, que são arquivos JSON sempre em UTF-8. */
public class JsonUtil {

    public static final String ARQUIVO_USUARIOS = "usuarios.json";
    public static final String ARQUIVO_IMOVEIS = "imoveis.json";

    private static final Gson gson = new GsonBuilder()
            .setPrettyPrinting()
            .disableHtmlEscaping() // grava & ' < > como estão, em vez de \u0026 \u0027...
            .registerTypeAdapter(Usuario.class, new UsuarioAdapter())
            .create();

    // <T> significa que esse método aceita qualquer classe
    public static <T> List<T> ler(String nomeArquivo, Class<T> classe) {
        Path caminho = Paths.get(nomeArquivo);
        if (!Files.exists(caminho)) return new ArrayList<>();

        // Files.newBufferedReader é estrito: um arquivo que não seja UTF-8 falha
        // com erro claro em vez de trocar os acentos por "?" em silêncio.
        try (Reader reader = Files.newBufferedReader(caminho, StandardCharsets.UTF_8)) {
            Type tipoLista = TypeToken.getParameterized(List.class, classe).getType();
            List<T> lista = gson.fromJson(reader, tipoLista);
            return lista != null ? lista : new ArrayList<>();
        } catch (IOException e) {
            throw new UncheckedIOException("Erro ao ler o arquivo JSON (ele precisa estar em UTF-8): " + nomeArquivo, e);
        } catch (JsonParseException e) {
            // O Gson embrulha o erro de leitura; se a causa for charset, o problema é o arquivo não estar em UTF-8.
            CharacterCodingException codificacao = causaDeCodificacao(e);
            if (codificacao != null) {
                throw new UncheckedIOException(
                        "Erro ao ler o arquivo JSON (ele precisa estar em UTF-8): " + nomeArquivo, codificacao);
            }
            throw new IllegalStateException("Conteúdo inválido no arquivo JSON " + nomeArquivo + ": " + e.getMessage(), e);
        }
    }

    private static CharacterCodingException causaDeCodificacao(Throwable erro) {
        for (Throwable causa = erro; causa != null; causa = causa.getCause()) {
            if (causa instanceof CharacterCodingException) {
                return (CharacterCodingException) causa;
            }
        }
        return null;
    }

    /**
     * Grava num arquivo temporário e só depois troca pelo definitivo,
     * então uma falha no meio da gravação nunca deixa o banco pela metade.
     */
    public static <T> void salvar(String nomeArquivo, List<T> lista) {
        Path destino = Paths.get(nomeArquivo).toAbsolutePath();
        Path temporario = destino.resolveSibling(destino.getFileName() + ".tmp");

        try {
            try (Writer writer = Files.newBufferedWriter(temporario, StandardCharsets.UTF_8)) {
                gson.toJson(lista, writer);
            }
            try {
                Files.move(temporario, destino, StandardCopyOption.REPLACE_EXISTING, StandardCopyOption.ATOMIC_MOVE);
            } catch (AtomicMoveNotSupportedException e) {
                Files.move(temporario, destino, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException | JsonIOException e) {
            try {
                Files.deleteIfExists(temporario);
            } catch (IOException ignorada) {
                // não há mais nada a fazer com o temporário
            }
            throw new UncheckedIOException("Erro ao salvar o arquivo JSON: " + nomeArquivo,
                    e instanceof IOException ? (IOException) e : new IOException(e));
        }
    }

    // =========================================================
    // O TRADUTOR (Lê a etiqueta "tipo" e cria a classe correta)
    // =========================================================
    private static class UsuarioAdapter implements JsonDeserializer<Usuario> {
        @Override
        public Usuario deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            if (!json.isJsonObject()) {
                throw new JsonParseException("Cada usuário deve ser um objeto JSON.");
            }
            JsonObject jsonObject = json.getAsJsonObject();
            JsonElement tipoJson = jsonObject.get("tipo");

            // Registros antigos, sem "tipo", viram Inquilino (a conta padrão, sem privilégios).
            if (tipoJson == null || tipoJson.isJsonNull()) {
                return context.deserialize(jsonObject, inquilino.class);
            }
            if (!tipoJson.isJsonPrimitive()) {
                throw new JsonParseException("O campo \"tipo\" deve ser um texto.");
            }

            String tipo = tipoJson.getAsString();
            switch (tipo) {
                case "admin":
                    return context.deserialize(jsonObject, admin.class);
                case "inquilino":
                    return context.deserialize(jsonObject, inquilino.class);
                // Antes caía em context.deserialize(..., Usuario.class), que chamava este
                // mesmo adaptador de novo, sem parar (StackOverflowError).
                default:
                    throw new JsonParseException("Tipo de usuário desconhecido: " + tipo);
            }
        }
    }
}
