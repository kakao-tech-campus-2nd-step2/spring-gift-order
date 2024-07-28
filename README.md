# spring-gift-order
> 카카오테크 캠퍼스 STEP2 - 5주차 클론 코딩

## 목차
[* 코드 소개](#코드-소개)<br>
[* 0 단계 - 기본 코드 준비](#-0-단계----기본-코드-준비)<br>
[* 1 단계 - 카카오 로그인](#-1-단계----카카오-로그인)<br>
[* 2 단계 - 주문하기](#-2-단계----주문하기)<br>
[* 3 단계 - API 문서 만들기]

## 코드 소개
카카오 선물하기의 상품을 관리하는 서비스를 구현해본다

## < 0 단계 > - 기본 코드 준비
### [ 기능 요구 사항 ]
- [spring-gift-enhancement](https://github.com/chris0825/spring-gift-enhancement.git)의 코드를 옮겨온다.

## < 1 단계 > - 카카오 로그인
### [ 기능 요구 사항 ]
> 카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.
- 카카오계정 로그인을 통해 인증 코드를 받는다.
- [토큰 받기](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token)를 읽고 액세스 토큰을 추출한다.
- 앱 키, 인가 코드가 절대 유출되지 않도록 한다.

## < 2 단계 > - 주문하기
### [ 과제 진행 요구 사항 ]
> 카카오 API를 사용하기 위한 [애플리케이션을 등록](https://developers.kakao.com/docs/latest/ko/getting-started/quick-start#create)한다.
- 내 애플리케이션 > 제품 설정 > 카카오 로그인 > 활성화 설정 ON ([카카오 로그인 활성화 설정](https://developers.kakao.com/docs/latest/ko/kakaologin/prerequisite#kakao-login-activate))
- 내 애플리케이션 > 제품 설정 > 카카오 로그인 > Redirect URI 등록 > http://localhost:8080 저장 ([Redirect URI 등록](https://developers.kakao.com/docs/latest/ko/kakaologin/prerequisite#kakao-login-redirect-uri))
- 내 애플리케이션 > 제품 설정 > 카카오 로그인 > 동의항목 > 접근권한 > 카카오톡 메시지 전송 > 선택 동의 ([접근권한 동의항목](https://developers.kakao.com/docs/latest/ko/kakaologin/prerequisite#permission))
- 내 애플리케이션 > 앱 설정 > Web 플랫폼 등록 > http://localhost:8080 저장 ([Web](https://developers.kakao.com/docs/latest/ko/getting-started/app#platform-web))

### [ 기능 요구 사항 ]
> 카카오톡 메세지 API를 사용하여 주문하기 기능을 구현한다.
- 주문할 때 수령인에게 보낼 메시지를 작성할 수 있다.
- 상품 옵션과 해당 수량을 선택하여 주문하면 해당 상품 옵션의 수량이 차감된다.
- 해당 상품이 위시 리스트에 있는 경우 위시 리스트에서 삭제한다.
- [나에게 보내기](https://developers.kakao.com/docs/latest/ko/message/rest-api#default-template-msg-me)를 읽고 주문 내역을 카카오톡 메시지로 전송한다.
  - 메시지는 [메시지 템플릿](https://developers.kakao.com/docs/latest/ko/message/message-template)의 기본 템플릿이나 사용자 정의 템플릿을 사용하여 자유롭게 작성한다.

## < 3 단계 > - API 문서 만들기
### [ 기능 요구 사항 ]
> API 사양에 관해 클라이언트와 어떻게 소통할 수 있을까? 어떻게 하면 편하게 소통할 수 있을지 고민해 보고 그 방법을 구현한다.
- [API 문서](http://localhost:8080/swagger-ui/index.html#/) (powerd by Swagger)