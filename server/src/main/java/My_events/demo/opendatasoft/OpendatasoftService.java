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
            .codecs(configurer -> configurer.defaultCodecs().maxInMemorySize(100 * 1024 * 1024))
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

    public Mono<String> getDatasetExportFormats(String dataset_id) {
        return webClient.get()
            .uri("/catalog/datasets/{dataset_id}/exports", dataset_id)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    public Mono<String> exportDataset(
        String dataset_id, String format, String select, String where, String orderBy, String groupBy,
        Integer limit, String refine, String exclude, String lang, String timezone,
        Boolean useLabels, Boolean compressed, Integer epsg
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}/exports/{format}");
            if (select != null) builder.queryParam("select", select);
            if (where != null) builder.queryParam("where", where);
            if (orderBy != null) builder.queryParam("order_by", orderBy);
            if (groupBy != null) builder.queryParam("group_by", groupBy);
            if (limit != null) builder.queryParam("limit", limit);
            if (refine != null) builder.queryParam("refine", refine);
            if (exclude != null) builder.queryParam("exclude", exclude);
            if (lang != null) builder.queryParam("lang", lang);
            if (timezone != null) builder.queryParam("timezone", timezone);
            if (useLabels != null) builder.queryParam("use_labels", useLabels);
            if (compressed != null) builder.queryParam("compressed", compressed);
            if (epsg != null) builder.queryParam("epsg", epsg);
            return builder.build(dataset_id, format);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    public Mono<String> exportDatasetCSV(
        String dataset_id, String delimiter, String listSeparator, Boolean quoteAll, Boolean withBom
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}/exports/csv");
            if (delimiter != null) builder.queryParam("delimiter", delimiter);
            if (listSeparator != null) builder.queryParam("list_separator", listSeparator);
            if (quoteAll != null) builder.queryParam("quote_all", quoteAll);
            if (withBom != null) builder.queryParam("with_bom", withBom);
            return builder.build(dataset_id);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    public Mono<String> exportDatasetParquet(
        String dataset_id, String parquetCompression
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}/exports/parquet");
            if (parquetCompression != null) builder.queryParam("parquet_compression", parquetCompression);
            return builder.build(dataset_id);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    public Mono<String> getDatasetFacets(
        String dataset_id, String where, String refine, String exclude, String facet, String lang, String timezone
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}/facets");
            if (where != null) builder.queryParam("where", where);
            if (refine != null) builder.queryParam("refine", refine);
            if (exclude != null) builder.queryParam("exclude", exclude);
            if (facet != null) builder.queryParam("facet", facet);
            if (lang != null) builder.queryParam("lang", lang);
            if (timezone != null) builder.queryParam("timezone", timezone);
            return builder.build(dataset_id);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    public Mono<String> exportDatasetGPX(
        String dataset_id, String nameField, String descriptionFieldList, Boolean useExtension
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}/exports/gpx");
            if (nameField != null) builder.queryParam("name_field", nameField);
            if (descriptionFieldList != null) builder.queryParam("description_field_list", descriptionFieldList);
            if (useExtension != null) builder.queryParam("use_extension", useExtension);
            return builder.build(dataset_id);
        };

        return webClient.get()
            .uri(uriFunction)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    public Mono<String> getDatasetAttachments(String dataset_id) {
        return webClient.get()
            .uri("/catalog/datasets/{dataset_id}/attachments", dataset_id)
            .retrieve()
            .bodyToMono(String.class)
            .map(JsonFormatter::formatJson);
    }

    public Mono<String> getDatasetRecord(
        String dataset_id, String record_id, String select, String lang, String timezone
    ) {
        Function<UriBuilder, java.net.URI> uriFunction = uriBuilder -> {
            UriBuilder builder = uriBuilder.path("/catalog/datasets/{dataset_id}/records/{record_id}");
            if (select != null) builder.queryParam("select", select);
            if (lang != null) builder.queryParam("lang", lang);
            if (timezone != null) builder.queryParam("timezone", timezone);
            return builder.build(dataset_id, record_id);
        };

        return webClient.get()
            .uri(uriFunction)
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