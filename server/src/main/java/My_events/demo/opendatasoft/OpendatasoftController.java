package My_events.demo.opendatasoft;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import reactor.core.publisher.Mono;

@RestController
public class OpendatasoftController {
    private final OpendatasoftService service;

    public OpendatasoftController(OpendatasoftService service) {
        this.service = service;
    }

    @GetMapping("/api/datasets")
    public Mono<ResponseEntity<String>> getDatasets(
        @RequestParam(required = false) String select,
        @RequestParam(required = false) String where,
        @RequestParam(required = false, name = "order_by") String orderBy,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) String refine,
        @RequestParam(required = false) String exclude, 
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone,
        @RequestParam(required = false, name = "group_by") String groupBy,
        @RequestParam(required = false, name = "include_links") Boolean includeLinks,
        @RequestParam(required = false, name = "include_app_metas") Boolean includeAppMetas
    ) {
        return service.getDatasets(select, where, orderBy, limit, offset, refine, exclude, lang, timezone, groupBy, includeLinks, includeAppMetas)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }


    @GetMapping("/api/datasets/{dataset_id}")
    public Mono<ResponseEntity<String>> getDataset(
        @PathVariable String dataset_id,
        @RequestParam(required = false) String select,
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone,
        @RequestParam(required = false, name = "include_links") Boolean includeLinks,
        @RequestParam(required = false, name = "include_app_metas") Boolean includeAppMetas
    ) {
        return service.getDataset(dataset_id, select, lang, timezone, includeLinks, includeAppMetas)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }


    @GetMapping("/api/datasets/{dataset_id}/records")
    public Mono<ResponseEntity<String>> getDatasetRecords(
        @PathVariable String dataset_id,
        @RequestParam(required = false) String select,
        @RequestParam(required = false) String where,
        @RequestParam(required = false, name = "group_by") String groupBy,
        @RequestParam(required = false, name = "order_by") String orderBy,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) String refine,
        @RequestParam(required = false) String exclude,
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone,
        @RequestParam(required = false, name = "include_links") Boolean includeLinks,
        @RequestParam(required = false, name = "include_app_metas") Boolean includeAppMetas
    ) {
        return service.getDatasetRecords(dataset_id, select, where, groupBy, orderBy, limit, offset, refine, exclude, lang, timezone, includeLinks, includeAppMetas)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }


    @GetMapping("/api/exports")
    public Mono<ResponseEntity<String>> getExportFormats() {
        return service.getExportFormats()
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }


    @GetMapping("/api/exports/{format}")
    public Mono<ResponseEntity<String>> exportCatalog(
        @PathVariable String format,
        @RequestParam(required = false) String select,
        @RequestParam(required = false) String where,
        @RequestParam(required = false, name = "order_by") String orderBy,
        @RequestParam(required = false, name = "group_by") String groupBy,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) String refine,
        @RequestParam(required = false) String exclude,
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone
    ) {
        return service.exportCatalog(format, select, where, orderBy, groupBy, limit, offset, refine, exclude, lang, timezone)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/exports/csv")
    public Mono<ResponseEntity<String>> exportCSV(
        @RequestParam(required = false) String delimiter,
        @RequestParam(required = false, name = "list_separator") String listSeparator,
        @RequestParam(required = false, name = "quote_all") Boolean quoteAll,
        @RequestParam(required = false, name = "whith_bom") Boolean withBom
    ) {
        return service.exportCSV(delimiter, listSeparator, quoteAll, withBom)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/exports/dcat/{dcat_ap_format}")
    public Mono<ResponseEntity<String>> exportDCAT(
        @PathVariable String dcat_ap_format,
        @RequestParam(required = false, name = "include_exports") String includeExports,
        @RequestParam(required = false, name = "use_labels_in_exports") Boolean useLabelsInExports
    ) {
        return service.exportDCAT(dcat_ap_format, includeExports, useLabelsInExports)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/facets")
    public Mono<ResponseEntity<String>> facets(
        @RequestParam(required = false) String facet,
        @RequestParam(required = false) String refine,
        @RequestParam(required = false) String exclude,
        @RequestParam(required = false) String where,
        @RequestParam(required = false) String timezone
    ) {
        return service.facets(facet, refine, exclude, where, timezone)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }
}
