package gift.domain.controller;

import gift.domain.controller.apiResponse.OauthTokenApiResponse;
import gift.domain.service.OauthService;
import gift.global.apiResponse.SuccessApiResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class OauthController {

    private final OauthService oauthService;

    public OauthController(OauthService oauthService) {
        this.oauthService = oauthService;
    }

    @GetMapping("/oauth/result")
    public ResponseEntity<OauthTokenApiResponse> getToken(@RequestParam("code") String code) {
        return SuccessApiResponse.ok(new OauthTokenApiResponse(HttpStatus.OK, oauthService.getOauthToken(code)));
    }
}
