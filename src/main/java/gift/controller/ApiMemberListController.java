package gift.controller;

import gift.dto.MemberResponseDto;
import gift.service.MemberService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
@Tag(name="member-list",description = "멤버 리스트 API")
@RestController
public class ApiMemberListController {
    MemberService memberService;

    public ApiMemberListController(MemberService memberService) {
        this.memberService = memberService;
    }

    @GetMapping("/member/list")
    @Operation(summary = "멤버 목록 조회", description = "멤버 목록을 조회합니다.")
    public ResponseEntity<List<MemberResponseDto>> MemberList() {
        List<MemberResponseDto> memberResponseDto = memberService.getAllMemberResponseDto();
        return new ResponseEntity<>(memberResponseDto, HttpStatus.OK);
    }
}
