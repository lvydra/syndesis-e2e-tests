package io.syndesis.qe.utils;

import org.apache.http.client.HttpClient;
import org.apache.http.conn.ssl.SSLConnectionSocketFactory;
import org.apache.http.conn.ssl.TrustStrategy;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.ssl.SSLContextBuilder;

import org.jboss.resteasy.client.jaxrs.ResteasyClientBuilder;
import org.jboss.resteasy.client.jaxrs.engines.ApacheHttpClient4Engine;
import org.jboss.resteasy.plugins.providers.jackson.ResteasyJackson2Provider;
import org.jboss.resteasy.spi.ResteasyProviderFactory;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jdk8.Jdk8Module;

import javax.ws.rs.client.Client;

import java.security.KeyManagementException;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.cert.X509Certificate;

import io.fabric8.kubernetes.client.LocalPortForward;
import io.syndesis.qe.exceptions.RestClientException;
import lombok.extern.slf4j.Slf4j;

/**
 * Utility class for Rest client (RestEasy).
 *
 * @author jknetl
 */
@Slf4j
public final class RestUtils {

    private static LocalPortForward localPortForward = null;
    private static String restUrl = null;

    private RestUtils() {
    }

    public static Client getClient() throws RestClientException {
        final ResteasyJackson2Provider jackson2Provider = RestUtils.createJacksonProvider();
        final ApacheHttpClient4Engine engine = new ApacheHttpClient4Engine(RestUtils.createAllTrustingClient());

        final Client client = new ResteasyClientBuilder()
                .providerFactory(new ResteasyProviderFactory()) // this is needed otherwise default jackson2provider is used, which causes problems with JDK8 Optional
                .register(jackson2Provider)
                .httpEngine(engine)
                .build();

        return client;
    }

    private static ResteasyJackson2Provider createJacksonProvider() {
        final ResteasyJackson2Provider jackson2Provider = new ResteasyJackson2Provider();
        final ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.registerModule(new Jdk8Module());
        jackson2Provider.setMapper(objectMapper);
        return jackson2Provider;
    }

    //Required in order to skip certificate validation
    private static HttpClient createAllTrustingClient() throws RestClientException {
        HttpClient httpclient = null;
        try {
            final SSLContextBuilder builder = new SSLContextBuilder();
            builder.loadTrustMaterial((TrustStrategy) (X509Certificate[] chain, String authType) -> true);
            final SSLConnectionSocketFactory sslsf = new SSLConnectionSocketFactory(
                    builder.build());
            httpclient = HttpClients
                    .custom()
                    .setSSLSocketFactory(sslsf)
                    .setMaxConnTotal(1000)
                    .setMaxConnPerRoute(1000)
                    .build();
        } catch (NoSuchAlgorithmException | KeyStoreException | KeyManagementException e) {
            throw new RestClientException("Cannot create all SSL certificates trusting client", e);
        }
        return httpclient;
    }

    public static String getRestUrl() {
        if (localPortForward == null || !localPortForward.isAlive()) {
            log.debug("creating local port forward for pod syndesis-rest");
            localPortForward = TestUtils.createLocalPortForward("syndesis-rest", 8080, 8080);
            try {
                restUrl = String.format("http://%s:%s", localPortForward.getLocalAddress().getLoopbackAddress().getHostName(), localPortForward.getLocalPort());
            } catch (IllegalStateException ex) {
                restUrl = String.format("http://%s:%s", "127.0.0.1", 8080);
            }
            log.debug("rest endpoint URL: " + restUrl);
        }
        return restUrl;
    }
}
