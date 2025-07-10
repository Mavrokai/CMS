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

    @GetMapping("/api/datasets/{dataset_id}/exports")
    public Mono<ResponseEntity<String>> getDatasetExportFormats(
        @PathVariable String dataset_id
    ) {
        return service.getDatasetExportFormats(dataset_id)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/datasets/{dataset_id}/exports/{format}")
    public Mono<ResponseEntity<String>> exportDataset(
        @PathVariable String dataset_id,
        @PathVariable String format,
        @RequestParam(required = false) String select,
        @RequestParam(required = false) String where,
        @RequestParam(required = false, name = "order_by") String orderBy,
        @RequestParam(required = false, name = "group_by") String groupBy,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) String refine,
        @RequestParam(required = false) String exclude,
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone,
        @RequestParam(required = false, name = "use_labels") Boolean useLabels,
        @RequestParam(required = false) Boolean compressed,
        @RequestParam(required = false) Integer epsg
    ) {
        return service.exportDataset(dataset_id, format, select, where, orderBy, groupBy, limit, refine, exclude, lang, timezone, useLabels, compressed, epsg)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/datasets/{dataset_id}/exports/csv")
    public Mono<ResponseEntity<String>> exportDatasetCSV(
        @PathVariable String dataset_id,
        @RequestParam(required = false) String delimiter,
        @RequestParam(required = false, name = "list_separator") String listSeparator,
        @RequestParam(required = false, name = "quote_all") Boolean quoteAll,
        @RequestParam(required = false, name = "with_bom") Boolean withBom
    ) {
        return service.exportDatasetCSV(dataset_id, delimiter, listSeparator, quoteAll, withBom)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/datasets/{dataset_id}/exports/parquet")
    public Mono<ResponseEntity<String>> exportDatasetParquet(
        @PathVariable String dataset_id,
        @RequestParam(required = false, name = "parquet_compression") String parquetCompression
    ) {
        return service.exportDatasetParquet(dataset_id, parquetCompression)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/datasets/{dataset_id}/facets")
    public Mono<ResponseEntity<String>> getDatasetFacets(
        @PathVariable String dataset_id,
        @RequestParam(required = false) String where,
        @RequestParam(required = false) String refine,
        @RequestParam(required = false) String exclude,
        @RequestParam(required = false) String facet,
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone
    ) {
        return service.getDatasetFacets(dataset_id, where, refine, exclude, facet, lang, timezone)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/datasets/{dataset_id}/exports/gpx")
    public Mono<ResponseEntity<String>> exportDatasetGPX(
        @PathVariable String dataset_id,
        @RequestParam(required = false, name = "name_field") String nameField,
        @RequestParam(required = false, name = "description_field_list") String descriptionFieldList,
        @RequestParam(required = false, name = "use_extension") Boolean useExtension
    ) {
        return service.exportDatasetGPX(dataset_id, nameField, descriptionFieldList, useExtension)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/datasets/{dataset_id}/attachments")
    public Mono<ResponseEntity<String>> getDatasetAttachments(
        @PathVariable String dataset_id
    ) {
        return service.getDatasetAttachments(dataset_id)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }

    @GetMapping("/api/datasets/{dataset_id}/records/{record_id}")
    public Mono<ResponseEntity<String>> getDatasetRecord(
        @PathVariable String dataset_id,
        @PathVariable String record_id,
        @RequestParam(required = false) String select,
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone
    ) {
        return service.getDatasetRecord(dataset_id, record_id, select, lang, timezone)
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
