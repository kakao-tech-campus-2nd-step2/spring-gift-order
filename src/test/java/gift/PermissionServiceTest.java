package gift;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.times;

import gift.token.component.TokenComponent;
import gift.permission.dto.LoginRequestDto;
import gift.permission.dto.RegistrationRequestDto;
import gift.permission.entity.User;
import gift.permission.repository.PermissionRepository;
import gift.permission.service.PermissionService;
import java.util.NoSuchElementException;
import java.util.Optional;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;


@ExtendWith(MockitoExtension.class)
public class PermissionServiceTest {

    @InjectMocks
    private PermissionService permissionService;
    @Mock
    private PermissionRepository permissionRepository;
    @Mock
    private TokenComponent tokenComponent;

    /*
     * - [ ] 정상적인 경우
     * - [ ] 이메일이 중복되는 경우
     */
    @Test
    public void registerTest() {
        // given
        var email = "kangji0615@gmail.com";
        var dupEmail = "luckyrkd@naver.com";
        var password = "aaaaa11111";
        var isAdmin = true;

        var registrationRequestDto = new RegistrationRequestDto(email, password, isAdmin);
        var dupRegistrationRequestDto = new RegistrationRequestDto(dupEmail, password, isAdmin);

        given(permissionRepository.save(any(User.class))).willAnswer(
            invocation -> invocation.getArgument(0));
        given(permissionRepository.existsByEmail(email)).willReturn(false);
        given(permissionRepository.existsByEmail(dupEmail)).willReturn(true);

        // when, then
        // 정상
        Assertions.assertThatCode(() -> permissionService.register(registrationRequestDto))
            .doesNotThrowAnyException();
        // 중복 이메일
        Assertions.assertThatThrownBy(() -> permissionService.register(dupRegistrationRequestDto))
            .isInstanceOf(IllegalArgumentException.class);

        // 정상 호출
        then(permissionRepository).should().existsByEmail(email);
        // 중복 이메일
        then(permissionRepository).should().existsByEmail(dupEmail);
        // 정상 호출
        then(permissionRepository).should().save(any(User.class));
    }

    /*
     * - [ ] 정상적인 경우
     * - [ ] 이메일이 존재하지 않는 경우
     * - [ ] 비밀번호가 틀린 경우
     */
    @Test
    public void loginTest() {
        // given
        var email = "kangji0615@gmail.com";
        var invalidEmail = "luckyrkd@naver.com";
        var password = "aaaaa11111";
        var wrongPassword = "qwer1234";
        var isAdmin = true;

        var loginRequestDto = new LoginRequestDto(email, password);
        var dupLoginRequestDto = new LoginRequestDto(invalidEmail, password);
        var wrongLoginRequestDto = new LoginRequestDto(email, wrongPassword);

        given(permissionRepository.findByEmail(email)).willReturn(
            Optional.of(new User(1L, email, password, isAdmin)));
        given(permissionRepository.findByEmail(invalidEmail)).willReturn(Optional.empty());

        // when, then
        // 정상
        Assertions.assertThat(permissionService.login(loginRequestDto))
            .isEqualTo(tokenComponent.getToken(1L, email, isAdmin));

        // 가입되지 않은 이메일
        Assertions.assertThatThrownBy(() -> permissionService.login(dupLoginRequestDto))
            .isInstanceOf(NoSuchElementException.class);

        // 잘못된 비밀번호
        Assertions.assertThatThrownBy(() -> permissionService.login(wrongLoginRequestDto))
            .isInstanceOf(ResponseStatusException.class);

        // 가입되지 않은 이메일
        then(permissionRepository).should().findByEmail(invalidEmail);
        // 정상, 잘못된 비밀번호
        then(permissionRepository).should(times(2)).findByEmail(email);
    }
}
