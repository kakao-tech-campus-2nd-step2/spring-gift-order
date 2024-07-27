package gift.controller;

import gift.DTO.Token;
import gift.DTO.User.UserRequest;
import gift.DTO.User.UserResponse;
import gift.security.AuthenticateMember;
import gift.security.JwtTokenProvider;
import gift.service.UserService;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class UserController {
    private final UserService userService;
    private final JwtTokenProvider jwtTokenProvider;

    public UserController(UserService userService, JwtTokenProvider jwtTokenProvider){
        this.userService = userService;
        this.jwtTokenProvider = jwtTokenProvider;
    }
    /*
     * 로그인 ( 유저 정보 인증 )
     */
    @PostMapping("/login")
    public ResponseEntity<Token> giveToken(@RequestBody UserRequest user){
        if(!userService.login(user)){
            return new ResponseEntity<>(HttpStatus.UNAUTHORIZED);
        }

        Token token = jwtTokenProvider.makeToken(user);
        return new ResponseEntity<>(token, HttpStatus.OK);
    }
    /*
     * 회원가입 ( 유저 추가 )
     */
    @PostMapping("/register")
    public ResponseEntity<Void> register(@RequestBody UserRequest user){
        if(userService.isDuplicate(user))
            return new ResponseEntity<>(HttpStatus.CONFLICT);

        userService.save(user);
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
    /*
     * 유저 조회
     */
    @GetMapping("/api/users")
    public ResponseEntity<Page<UserResponse>> readUsers(
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "5") int size,
            @RequestParam(value = "sort", defaultValue = "asc") String sort,
            @RequestParam(value = "field", defaultValue = "id") String field
    ) {
        if(sort.equals("asc")) {
            Page<UserResponse> users = userService.findAllASC(page, size,field);
            return new ResponseEntity<>(users, HttpStatus.OK);
        }

        Page<UserResponse> users = userService.findAllDESC(page, size,field);
        return new ResponseEntity<>(users, HttpStatus.OK);
    }
    /*
     * 유저 수정
     */
    @PutMapping("/api/users/{id}")
    public ResponseEntity<Void> updateUsers(
            @PathVariable("id") Long id,
            @RequestBody UserRequest user,
            @AuthenticateMember UserResponse userRes
    ){
        if(!id.equals(userService.findByUserId(user.getUserId()).getId())) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
        }

        if(!userService.isDuplicate(user))
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);

        userService.update(user);
        return new ResponseEntity<>(HttpStatus.OK);

    }
    /*
     * 유저 삭제
     */
    @DeleteMapping("/api/users/{id}")
    public ResponseEntity<Void> deleteUsers(
            @PathVariable("id") Long id,
            @AuthenticateMember UserResponse user
    ){
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}
