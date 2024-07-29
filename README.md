# spring-gift-order
## Step3
feat: 스웨거 api 문서화 [박한솔]

## Step2
feat: KakaoService - 주문 생성 기능 추가 및 Kakao 메시지 전송 구현 [박한솔]
- createOrder 메서드 구현: 주문을 생성하고, 카카오 메시지를 전송하여 주문 정보를 사용자에게 알림

feat: KakaoService - Kakao 메시지 전송 및 템플릿 객체 생성 메서드 구현 [박한솔]
- sendKakaoMessage 메서드 추가: Kakao API를 통해 사용자에게 메시지 전송
- createTemplateObject 메서드 추가: Kakao 메시지에 사용할 템플릿 객체를 생성

feat: WishRepository - Wish 조회 메서드 추가 [박한솔]
- findByUserIdAndProductIdAndIsDeletedFalse 메서드 추가: 삭제되지 않은 Wish를 사용자 ID와 상품 ID로 조회

feat: KakaoController - 주문 생성 API 추가 [박한솔]
- createOrder 메서드 추가: 로그인된 사용자에 대해 주문 생성 요청을 처리



### 로직 순서
### 1. 카카오 로그인
- 사용자가 카카오 로그인 버튼을 클릭하여 카카오 계정으로 로그인할 수 있는 기능.
- 로그인 후, 카카오 API를 통해 액세스 토큰을 받아 사용자 정보를 조회.
- JWT 토큰+카카오 엑세스 토큰을 생성하여 클라이언트에 반환.

### 2. 주문 생성
- JWT 토큰과 카카오 엑세스 토큰, OrderRequest의 정보를 담아 요청을 보낸다.
- 사용자가 상품 옵션과 수량을 선택하여 주문을 생성할 수 있는 기능.
- 주문 생성 시, 상품의 수량을 차감하고, 기존의 찜 목록에서 해당 상품을 삭제.
- 카카오 메시지를 통해 주문 확인 메시지를 사용자에게 전송.

### 3. 카카오 메시지 전송
- 주문 완료 후 카카오톡 메시지를 사용자에게 전송하는 기능.
- 메시지에는 주문 정보와 링크를 포함.

## Step1

Test
* RestClientTest: 카카오 로그인 테스트 [박한솔]
  
Feat
* KakaoProperties: 카카오 API와의 통신에 필요한 설정을 외부 설정 파일에서 로드할 수 있도록 KakaoProperties 클래스를 정의 [박한솔]
* KakaoController: 카카오 로그인 API와의 상호작용을 처리하고, 액세스 토큰과 사용자 정보를 반환하는 REST API를 구현 [박한솔]
* AdminKakaoController: 카카오 로그인 페이지를 제공하는 컨트롤러 및 HTML 템플릿(kakao.html)을 작성하여 사용자 로그인 인터페이스를 제공 [박한솔]


## 템플릿으로 카카오 서버에 code 요청 기능 클라이언트 화면 구현
1. **로그인 요청 처리 및 액세스 토큰 요청:**
    - 클라이언트에서 카카오 로그인 요청을 받으면, 카카오 로그인 API에 인가 코드(authorization code)를 발급하는 기능 구현
    - 인가 코드를 서버에 전송하면 (/login) WAS 서버에서 엑세스 토큰을 카카오 서버에 요청해서 받아옴
      
## code로 서버에 /login 요청
### 사용자 정보 조회 및 엑세스 토큰 발급
2. **액세스 토큰을 사용한 사용자 정보 조회:**
    - 받은 액세스 토큰을 이용해 카카오 사용자 정보를 조회합니다. 이를 위해 카카오의 사용자 정보 조회 API를 호출합니다.
    - 이 단계에서는 액세스 토큰을 HTTP 헤더에 포함시켜 API 요청을 보내고, 카카오 서버로부터 사용자의 정보(예: 닉네임, 이메일 등)를 반환받습니다.
    - 추후 카카오 정보를 받아와서 사용자 정보가 있다면 로그인 없다면 회원가입을 진행시킵니다.
3. **사용자 정보 및 액세스 토큰 반환:**
    - 최종적으로 클라이언트에게 액세스 토큰과 함께 사용자 정보를 반환하는 API 엔드포인트를 구현합니다.
    - 클라이언트는 이 정보를 이용해 사용자 세션을 생성하거나, 필요에 따라 사용자 정보를 저장하고 활용할 수 있습니다.
