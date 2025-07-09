package My_events.demo.opendatasoft;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

import java.util.function.Function;

@Service
public class OpendatasoftService {
    private final WebClient webClient;

    public OpendatasoftService(@Value("${opendatasoft.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .build();
    }

    public Mono<String> getDatasets(
        String select, String where, String orderBy, Integer limit, Integer offset,
        String refine, String exclude, String lang, String timezone, String groupBy,
        Boolean includeLinks, Boolean includeAppMetas
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets");
            if (select != null) builder.queryParam("select", select);
            if (where != null) builder.queryParam("where", where);
            if (orderBy != null) builder.queryParam("order_by", orderBy);
            if (limit != null) builder.queryParam("limit", limit);
            if (offset != null) builder.queryParam("offset", offset);
            if (refine != null) builder.queryParam("refine", refine);
            if (exclude != null) builder.queryParam("exclude", exclude);
            if (lang != null) builder.queryParam("lang", lang);
            if (timezone != null) builder.queryParam("timezone", timezone);
            if (groupBy != null) builder.queryParam("group_by", groupBy);
            if (includeLinks != null) builder.queryParam("include_links", includeLinks);
            if (includeAppMetas != null) builder.queryParam("include_app_metas", includeAppMetas);
            return builder.build();
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class);
    }
}