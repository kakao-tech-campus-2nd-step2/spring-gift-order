# spring-gift-order
> 카카오테크 캠퍼스 STEP2 - 5주차 클론 코딩
## 목차
[* 코드 소개](#코드-소개)<br>
[* 0 단계 - 기본 코드 준비](#-0-단계----기본-코드-준비)<br>
[* 1 단계 - 카카오 로그인](#-1-단계----카카오-로그인)<br>

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
