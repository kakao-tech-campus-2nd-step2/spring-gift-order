# spring-gift-order

## step0

### 기능 요구 사항

[상품 고도화](https://github.com/kakao-tech-campus-2nd-step2/spring-gift-enhancement) 코드를 옮겨 온다. 코드를 옮기는 방법에는 디렉터리의 모든 파일을 직접 복사하여 붙여 넣는 것부터 필요한 일부 파일만 이동하는 것, Git을 사용하는 것까지 여러 가지 방법이 있다. 코드 이동 시 반드시 **리소스 파일, 프로퍼티 파일, 테스트 코드** 등을 함께 이동한다.

## step1

카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

* [ ]  카카오계정 로그인을 통해 인증 코드를 받는다.
* [ ]  [토큰 받기](https://developers.kakao.com/docs/latest/ko/kakaologin/rest-api#request-token)를 읽고 액세스 토큰을 추출한다.
* [ ]  앱 키, 인가 코드가 절대 유출되지 않도록 한다.
  * [ ]  특히 시크릿 키는 GitHub나 클라이언트 코드 등 외부에서 볼 수 있는 곳에 추가하지 않는다.
* [ ]  (선택) 인가 코드를 받는 방법이 불편한 경우 카카오 로그인 화면을 구현한다.
