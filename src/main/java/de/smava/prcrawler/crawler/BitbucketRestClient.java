package de.smava.prcrawler.crawler;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.ByteStreams;
import de.smava.prcrawler.resource.Mapper;
import de.smava.prcrawler.resource.Response;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

@Slf4j
public class BitbucketRestClient {
    private final HttpClient httpClient;
    private final Mapper mapper;
    private String token;

    public BitbucketRestClient(String token) {
        this.token = token;
        httpClient = HttpClient.newHttpClient();
        mapper = new Mapper(new ObjectMapper());
    }

    @SneakyThrows
    public <T> Response<T> getPage(String url, TypeReference typeReference) {
        var request = HttpRequest.newBuilder()
                .GET()
                .header("Authorization", String.format("Bearer %s", token))
                .uri(toUri(url))
                .build();

        log.debug("executing request to {}", request.uri());

        var response = perform(request);

        if (response.statusCode() != 200) {
            throw new RuntimeException(String.format("unexpected status code response %d (%s)", response.statusCode(), new String(ByteStreams.toByteArray(response.body()))));
        }

        return mapper.parse(response.body(), typeReference);
    }

    private HttpResponse<InputStream> perform(HttpRequest request) throws IOException, InterruptedException {
        return httpClient.send(request, HttpResponse.BodyHandlers.ofInputStream());
    }

    private URI toUri(String url) {
        try {
            return new URI(url);
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
