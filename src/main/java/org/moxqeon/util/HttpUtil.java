package org.moxqeon.util;

import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import manifold.ext.rt.api.Extension;
import manifold.ext.rt.api.This;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.moxqeon.MoxUtil;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Consumer;

/**
 * HTTP 请求工具类，封装 GET/POST 请求、代理设置、JSON 响应解析以及本地 HTTP 服务等功能。
 */
public final class HttpUtil {
    private HttpUtil() {
        throw new IllegalStateException();
    }

    public static Proxy proxy(@NotNull String proxyAddress) {
        String[] tmp = proxyAddress.split(":");
        if (tmp.length == 2)
            return new Proxy(Proxy.Type.HTTP, new InetSocketAddress(tmp[0], Integer.parseInt(tmp[1])));
        else
            throw new RuntimeException();
    }

    @NotNull
    public static HttpURLConnection getRequest(@NotNull String url) {
        return getRequest(url, null, null);
    }

    public static HttpURLConnection getRequest(@NotNull String url, @NotNull Proxy proxy) {
        return getRequest(url, proxy, null);
    }

    public static HttpURLConnection getRequest(@NotNull String url, @NotNull Map<String, String> header) {
        return getRequest(url, null, header);
    }

    @NotNull
    public static HttpURLConnection getRequest(@NotNull String url, @Nullable Proxy proxy, @Nullable Map<String, String> header) {
        try {
            URL realURL = new URL(url);
            HttpURLConnection connection = (HttpURLConnection) (proxy != null ? realURL.openConnection(proxy) : realURL.openConnection());
            connection.setRequestMethod("GET");
            if (header != null)
                header.forEach(connection::setRequestProperty);
            return connection;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Nullable
    public static JsonObject getJsonResponse(@NotNull String url, @Nullable Proxy proxy, @Nullable Map<String, String> header, @Nullable Charset charset, @Nullable Consumer<IOException> handle) {
        if (header != null)
            header.put("Accept", "application/json");
        else
            header = Map.of("Accept", "application/json");
        HttpURLConnection connection = getRequest(url, proxy, header);
        try (InputStream input = connection.getInputStream()) {
            String json = new String(input.readAllBytes(), charset != null ? charset : StandardCharsets.UTF_8);
            if (MoxUtil.debug)
                MoxUtil.info(json);
            return JsonUtil.parse(json).getAsJsonObject();
        } catch (IOException e) {
            if (handle != null)
                handle.accept(e);
            return null;
        }
    }

    @Nullable
    public static JsonObject getJsonResponse(@NotNull String url, @Nullable Proxy proxy, @Nullable Map<String, String> header, @Nullable Charset charset) {
        return getJsonResponse(url, proxy, header, charset, null);
    }

    @Nullable
    public static JsonObject getJsonResponse(@NotNull String url, @NotNull Proxy proxy) {
        return getJsonResponse(url, proxy, null, null);
    }

    @Nullable
    public static JsonObject getJsonResponse(@NotNull String url, @NotNull Map<String, String> header) {
        return getJsonResponse(url, null, header, null);
    }

    @Nullable
    public static JsonObject getJsonResponse(@NotNull String url) {
        return getJsonResponse(url, null, null, null);
    }

    @NotNull
    public static HttpURLConnection postRequest(@NotNull String url) throws IOException {
        return postRequest(url, null, null);
    }

    @NotNull
    public static HttpURLConnection postRequest(@NotNull String url, @NotNull Map<String, String> header) throws IOException {
        return postRequest(url, null, header);
    }

    @NotNull
    public static HttpURLConnection postRequest(@NotNull String url, @NotNull Proxy proxy) throws IOException {
        return postRequest(url, proxy, null);
    }

    @NotNull
    public static HttpURLConnection postRequest(@NotNull String url, @Nullable Proxy proxy, @Nullable Map<String, String> header) throws IOException {
        URL realURL = new URL(url);
        HttpURLConnection connection = (HttpURLConnection) (proxy != null ? realURL.openConnection(proxy) : realURL.openConnection());
        connection.setRequestMethod("POST");
        connection.setDoOutput(true);
        connection.setDoInput(true);
        if (header != null)
            header.forEach(connection::setRequestProperty);
        return connection;
    }

    @Nullable
    public static JsonObject postJsonResponse(@NotNull String url, @Nullable Proxy proxy, @Nullable Map<String, String> header, byte[] data, @Nullable Charset charset, @Nullable Consumer<IOException> handle) {
        if (header != null)
            header.put("Accept", "application/json");
        else
            header = Map.of("Accept", "application/json");
        try {
            HttpURLConnection connection = postRequest(url, proxy, header);
            try (OutputStream output = connection.getOutputStream()) {
                output.write(data);
                output.flush();
                try (InputStream input = connection.getInputStream()) {
                    String json = new String(input.readAllBytes(), charset != null ? charset : StandardCharsets.UTF_8);
                    if (MoxUtil.debug)
                        MoxUtil.info(json);
                    return JsonUtil.parse(json).getAsJsonObject();
                }
            }
        } catch (IOException e) {
            if (handle != null)
                handle.accept(e);
            return null;
        }
    }

    @Nullable
    public static JsonObject postJsonResponse(@NotNull String url, @Nullable Proxy proxy, @Nullable Map<String, String> header, byte[] data, @Nullable Charset charset) {
        return postJsonResponse(url, proxy, header, data, charset, null);
    }

    @Nullable
    public static JsonObject postJsonResponse(@NotNull String url, @NotNull Map<String, String> header, byte[] data) {
        return postJsonResponse(url, null, header, data, null);
    }

    @Nullable
    public static JsonObject postJsonResponse(@NotNull String url, @NotNull Proxy proxy, byte[] data) {
        return postJsonResponse(url, proxy, null, data, null);
    }

    @Nullable
    public static JsonObject postJsonResponse(@NotNull String url, byte[] data) {
        return postJsonResponse(url, null, null, data, null);
    }

    @NotNull
    public static HttpServer createHttpServer(int port) {
        try {
            return HttpServer.create(new InetSocketAddress(port), 0);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void response(@NotNull HttpExchange exchange, @NotNull String content) {
        try (OutputStream output = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, 0);
            output.write(content.getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static void response(@NotNull HttpExchange exchange, byte[] data) {
        try (OutputStream output = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(200, 0);
            output.write(data);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @NotNull
    public static String encodeFormData(@NotNull Map<String, String> formData) {
        if (formData.isEmpty())
            return "";
        StringJoiner joiner = new StringJoiner("&");
        for (Map.Entry<String, String> entry : formData.entrySet()) {
            String encodedKey = URLEncoder.encode(entry.getKey(), StandardCharsets.UTF_8);
            String encodedValue = URLEncoder.encode(entry.getValue(), StandardCharsets.UTF_8);
            joiner.add(encodedKey + "=" + encodedValue);
        }
        return joiner.toString();
    }
}