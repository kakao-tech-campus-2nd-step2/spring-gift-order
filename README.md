# spring-gift-order

## step1 요구사항

- [x] oauth를 통한 회원가입
- [x] oauth를 통한 로그인
- [x] jwt 토큰 발급
- [ ] 예외 처리
- [ ] 카카오톡 메세지 전송에 동의하지 않았을 경우 예외 처리
- [x] api 호출할 때 timeout 설정

## 생각해 볼점

### HTTP Client

- REST Clients
- 사용할 클라이언트를 선택할 때 어떤 기준을 사용해야 할까?
- 클라이언트 인스턴스를 효과적으로 생성/관리하는 방법은 무엇인가?
- 더 적절한 테스트 방법이 있을까?
- 요청을 보내고 응답을 파싱하는 부분만 테스트하려면 어떻게 해야 할까?
- 비즈니스 로직에 연결할 때 단위/통합 테스트는 어떻게 할까?
- API 호출에 문제가 발생하면 어떻게 할까?
- 응답 시간이 길면 어떻게 할까? 몇 초가 적당할까?
- 오류 코드는 어떻게 처리해야 할까?
- 응답 값을 파싱할 때 문제가 발생하면 어떻게 할까?
