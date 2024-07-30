package gift.controller;

import gift.service.BasicTokenService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
@Tag(name="basic-token",description = "Basic 토큰 API")
@RequestMapping("/token")
@RestController
public class TokenController {
    private final BasicTokenService basicTokenService;

    public TokenController(BasicTokenService basicTokenService) {
        this.basicTokenService = basicTokenService;
    }

    @GetMapping("/{memberId}")
    @Operation(summary = "멤버 id로 토큰 생성", description = "멤버 id로 토큰을 생성합니다.")
    public String makeTokenFrom(@RequestParam("memberId") Long memberId) {
        return basicTokenService.makeTokenFrom(memberId.toString());
    }

}
