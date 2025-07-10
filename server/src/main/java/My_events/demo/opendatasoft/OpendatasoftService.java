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

    public OpendatasoftService(@Value("${opendatasoft.baseurl}") String baseUrl) {
        this.webClient = WebClient.builder()
            .baseUrl(baseUrl)
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(10 * 1024 * 1024))
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
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }


    public Mono<String> getDataset(
        String dataset_id, String select, String lang, String timezone, Boolean includeLinks, Boolean includeAppMetas
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}");
            if (select != null) builder.queryParam("select", select);
            if (lang != null) builder.queryParam("lang", lang);
            if (timezone != null) builder.queryParam("timezone", timezone);
            if (includeLinks != null) builder.queryParam("include_links", includeLinks);
            if (includeAppMetas != null) builder.queryParam("include_app_metas", includeAppMetas);
            return builder.build(dataset_id);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }


    public Mono<String> getDatasetRecords(
        String dataset_id, String select, String where, String groupBy, String orderBy, Integer limit, Integer offset, String refine, String exclude, String lang, String timezone, Boolean includeLinks, Boolean includeAppMetas
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}/records");
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
            if (includeLinks != null) builder.queryParam("include_links", includeLinks);
            if (includeAppMetas != null) builder.queryParam("include_app_metas", includeAppMetas);
            return builder.build(dataset_id);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }


    public Mono<String> getExportFormats() {
        return webClient.get()
            .uri("/catalog/exports")
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }


    public Mono<String> exportCatalog(
        String format, String select, String where, String orderBy, String groupBy,
        Integer limit, Integer offset, String refine, String exclude, String lang, String timezone
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/exports/{format}");
            if (select != null) builder.queryParam("select", select);
            if (where != null) builder.queryParam("where", where);
            if (orderBy != null) builder.queryParam("order_by", orderBy);
            if (groupBy != null) builder.queryParam("group_by", groupBy);
            if (limit != null) builder.queryParam("limit", limit);
            if (offset != null) builder.queryParam("offset", offset);
            if (refine != null) builder.queryParam("refine", refine);
            if (exclude != null) builder.queryParam("exclude", exclude);
            if (lang != null) builder.queryParam("lang", lang);
            if (timezone != null) builder.queryParam("timezone", timezone);
            return builder.build(format);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }


    public Mono<String> exportCSV(
        String delimiter, String listSeparator, Boolean quoteAll, Boolean withBom
        ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/exports/csv");
            if (delimiter != null) builder.queryParam("delimiter", delimiter);
            if (listSeparator != null) builder.queryParam("list_separator", listSeparator);
            if (quoteAll != null) builder.queryParam("quote_all", quoteAll);
            if (withBom != null) builder.queryParam("whith_bom", withBom);
            return builder.build();
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }


    public Mono<String> exportDCAT(
        String dcat_ap_format, String includeExports, Boolean useLabelsInExports
        ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/exports/dcat/{dcat_ap_format}");
            if (includeExports != null) builder.queryParam("include_exports", includeExports);
            if (useLabelsInExports != null) builder.queryParam("use_labels_in_exports", useLabelsInExports);
            return builder.build(dcat_ap_format);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    
    public Mono<String> facets(
        String facet, String refine, String exclude, String where, String timezone
        ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/facets");
            if (facet != null) builder.queryParam("facet", facet);
            if (refine != null) builder.queryParam("refine", refine);
            if (exclude != null) builder.queryParam("exclude", exclude);
            if (where != null) builder.queryParam("where", where);
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