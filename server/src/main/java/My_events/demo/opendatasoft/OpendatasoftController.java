package My_events.demo.opendatasoft;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class OpendatasoftController {
    private final OpendatasoftService service;

    public OpendatasoftController(OpendatasoftService service) {
        this.service = service;
    }

    @GetMapping("/api/explore/v2.1/catalog/datasets/evenements-publics-openagenda/records")
    public Mono<ResponseEntity<String>> queryDatasetRecords(
        @RequestParam(required = false) String select,
        @RequestParam(required = false) String where,
        @RequestParam(required = false, name = "group_by") String groupBy,
        @RequestParam(required = false, name = "order_by") String orderBy,
        @RequestParam(required = false) Integer limit,
        @RequestParam(required = false) Integer offset,
        @RequestParam(required = false) String refine,
        @RequestParam(required = false) String exclude,
        @RequestParam(required = false) String lang,
        @RequestParam(required = false) String timezone
    ) {
        return service.getEvenementsPublicsOpenagendaRecords(select, where, groupBy, orderBy, limit, offset, refine, exclude, lang, timezone)
            .map(json -> ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                .header(HttpHeaders.CACHE_CONTROL, "no-cache")
                .body(json));
    }
}
