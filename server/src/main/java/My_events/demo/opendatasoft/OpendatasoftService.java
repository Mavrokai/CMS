package My_events.demo.opendatasoft;

import java.util.function.Function;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriBuilder;
import reactor.core.publisher.Mono;

@Service
public class OpendatasoftService {
    private final WebClient webClient;
    private static final String DATASET_ID = "evenements-publics-openagenda";

    public OpendatasoftService(@Value("${opendatasoft.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
            .build();
    }

    public Mono<String> getEvenementsPublicsOpenagendaRecords(
        String select, String where, String groupBy, String orderBy, Integer limit, Integer offset, String refine, String exclude, String lang, String timezone
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/" + DATASET_ID + "/records");
            if (select != null) builder.queryParam("select", select);
            if (where != null) builder.queryParam("where", where);
            if (groupBy != null) builder.queryParam("group_by", groupBy);
            if (orderBy != null) builder.queryParam("order_by", orderBy);
            if (limit != null) builder.queryParam("limit", limit);
            if (offset != null) builder.queryParam("offset", offset);
            if (refine != null) builder.queryParam("refine", refine);
            if (exclude != null) builder.queryParam("exclude", exclude);
            if (lang != null) builder.queryParam("lang", lang);
            if (timezone != null) builder.queryParam("timezone", timezone);
            return builder.build();
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }
}