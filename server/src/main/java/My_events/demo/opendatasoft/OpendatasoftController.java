package My_events.demo.opendatasoft;

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

    @GetMapping("/api/datasets")
    public Mono<String> getDatasets(
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
        return service.getDatasets(select, where, orderBy, limit, offset, refine, exclude, lang, timezone, groupBy, includeLinks, includeAppMetas);
    }


    @GetMapping("/api/exports")
    public Mono<String> getExports() {
        return service.getExports();
    }
}
