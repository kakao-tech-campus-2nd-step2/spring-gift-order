# spring-gift-order

## 1단계 구현 사항
- 로그인 앤드포인트를 구현하여 카카오 로그인을 하면 엑세스 토큰을 받기
- login.html 에서 로그인 인가 코드 받기
- home.html 리디렉션 페이지에 엑세스 토큰 보여주기
- 테스트 코드

## 2단계 구현 사항
- 주문하기 mvc 구현
- 메시지 전송 서비스 추가
- 테스트 코드


## 현재 코드 구조
```plaintext
└── src
    └── main
        ├── java
        │   └── gift
        │       ├── config
        │       │   ├── AppConfig.java
        │       │   └── WebConfig.java
        │       ├── controller
        │       │   ├── AdminController.java
        │       │   ├── CategoryController.java
        │       │   ├── HomeController.java
        │       │   ├── KakaoController.java
        │       │   ├── MemberController.java
        │       │   ├── OrderController.java
        │       │   ├── ProductConroller.java
        │       │   └── WishController.java
        │       ├── dto
        │       │   ├── MemberDto.java
        │       │   ├── OptionDto.java
        │       │   ├── OrderRequest.java
        │       │   ├── OrderResponse.java
        │       │   ├── ProductDto.java
        │       │   └── WishRequest.java
        │       ├── entity
        │       │   ├── Catogory.java
        │       │   ├── Member.java
        │       │   ├── Option.java
        │       │   ├── Order.java
        │       │   ├── Product.java
        │       │   └── Wish.java
        │       ├── exception
        │       │   ├── ApiRequestException.java
        │       │   ├── CategoryNotFoundException.java
        │       │   ├── GlobalExceptionHandler.java
        │       │   ├── InvalidProductNameException.java
        │       │   └── ProductNotFoundException.java
        │       ├── interceptor
        │       │   ├── JwtInterceptor.java
        │       │   └── TokenInterceptor.java
        │       ├── repository
        │       │   ├── CategoryRepository.java
        │       │   ├── MemberRepository.java
        │       │   ├── OptionRepository.java
        │       │   ├── OrderRepository.java
        │       │   ├── ProductRepository.java
        │       │   └── WishRepository.java
        │       ├── service
        │       │   ├── CategoryService.java
        │       │   ├── KakaoProperties.java
        │       │   ├── KakaoService.java
        │       │   ├── MemberService.java
        │       │   ├── OrderService.java
        │       │   ├── ProductService.java
        │       │   ├── TokenService.java
        │       │   └── WishService.java   
        │       ├── value
        │       │   ├── KakaoString.java
        │       │   ├── OptionName.java
        │       │   ├── OptionQuantity.java
        │       │   └── ProductName.java 
        │       └── Application.java
        └── resources
            ├── data.sql
            ├── schema.sql
            └── templates
                ├── add.html
                ├── edit.html
                ├── list.html
                ├── login.html
                └── view.html             
└── src
    └── test
        └── java
            └── gift 
                ├── controller
                │    ├── AdminControllerTest.java
                │    ├── CategoryControllerTest.java
                │    ├── KakaoControllerTest.java
                │    └── MemberControllerTest.java
                ├── entity
                │    └── ProductTest.java
                ├── Repository
                │    ├── CategoryrepositoryTest.java
                │    ├── MemberRepositoryTest.java
                │    ├── OptionRepositoryTest.java
                │    ├── ProductReposiroryTest.java
                │    └── WishRepositoryTest.java
                └── service
                     └── KakaoServiceTest.java
