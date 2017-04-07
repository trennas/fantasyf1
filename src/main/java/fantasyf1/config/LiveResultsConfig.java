package fantasyf1.config;

import org.apache.http.HttpHost;
import org.apache.http.auth.AuthScope;
import org.apache.http.auth.UsernamePasswordCredentials;
import org.apache.http.client.CredentialsProvider;
import org.apache.http.client.HttpClient;
import org.apache.http.impl.client.BasicCredentialsProvider;
import org.apache.http.impl.client.HttpClientBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

import lombok.extern.log4j.Log4j;

@Configuration
@Log4j
public class LiveResultsConfig {
	@Value("${proxy-url:}")
	private String proxyUrl;
	@Value("${proxy-port:}")
	private Integer proxyPort;
	@Value("${proxy-user:}")
	private String proxyUser;
	@Value("${proxy-pass:}")
	private String proxyPass;

	@Bean
	public RestTemplate restTemplate() {
		final RestTemplate restTemplate = new RestTemplate();
		if(proxyUrl != null && !proxyUrl.isEmpty()) {
	        final CredentialsProvider credsProvider = new BasicCredentialsProvider();
	        credsProvider.setCredentials( 
	                new AuthScope(proxyUrl, proxyPort), 
	                new UsernamePasswordCredentials(proxyUser, proxyPass));
	
	        final HttpHost myProxy = new HttpHost(proxyUrl, proxyPort);
	        final HttpClientBuilder clientBuilder = HttpClientBuilder.create();	
	        clientBuilder.setProxy(myProxy).setDefaultCredentialsProvider(credsProvider).disableCookieManagement();	
	        final HttpClient httpClient = clientBuilder.build();
	        final HttpComponentsClientHttpRequestFactory factory = new HttpComponentsClientHttpRequestFactory();
	        factory.setHttpClient(httpClient);
	        restTemplate.setRequestFactory(factory);
	        log.info("Configured proxy " + proxyUrl);
		}
		return restTemplate;
    }
}
