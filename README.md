# spring-gift-order

## 🚀 1단계 - 카카오 로그인

### 애플리케이션 설정
- 카카오 API를 사용하기 위한 애플리케이션을 등록한다. 
- 내 애플리케이션 > 제품 설정 > 카카오 로그인 > 활성화 설정 ON (카카오 로그인 활성화 설정)
- 내 애플리케이션 > 제품 설정 > 카카오 로그인 > Redirect URI 등록 > http://localhost:8080
- 내 애플리케이션 > 제품 설정 > 카카오 로그인 > 동의항목 > 접근권한 > 카카오톡 메시지 전송 > 선택 동의 (접근권한 동의항목)

### 기능 요구 사항
- [x] 카카오 로그인을 통해 인가 코드 요청하기
- [x] redirect uri로 발급된 인가코드 추출하기
- [x] 인가 코드를 사용해 토큰 발급 받기
- [ ] 토큰 응답에서 액세스 토큰을 추출하기
- [ ] 인가 코드를 받기 위한 카카오 로그인 화면 구현
  (주의) 키, 인가 코드가 절대 유출되지 않도록 한다.