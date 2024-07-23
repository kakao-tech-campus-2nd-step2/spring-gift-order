# spring-gift-order

### 기능 요구 사항

* 카카오계정 로그인을 통해 인증 코드를 받는다.
* [토큰 받기](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token)를 읽고 액세스 토큰을 추출한다.
* 앱 키, 인가 코드가 절대 유출되지 않도록 한다.
  * 특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.
* (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.


### 구현한 기능

Api Util 클래스를 만들어 외부 api와 통신하여 accessToken을 받아왔습니다.

카카오톡 로그인 버튼을 가지고 있는 간단한 view를 만들었습니다.
