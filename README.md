# spring-gift-order

## 현재 코드 구조
```plaintext
└── src
    └── main
        ├── java
        │   └── gift
        │       ├── config
        │       │   ├── SecurityConfig.java
        │       │   └── WebConfig.java
        │       ├── controller
        │       │   ├── AdminController.java
        │       │   ├── CategoryController.java
        │       │   ├── HomeController.java
        │       │   ├── HomeController.java
        │       │   ├── MemberController.java
        │       │   ├── ProductConroller.java
        │       │   └── WishController.java
        │       ├── dto
        │       │   ├── MemberDto.java
        │       │   ├── OptionDto.java
        │       │   ├── ProductDto.java
        │       │   └── WishRequest.java
        │       ├── entity
        │       │   ├── Catogory.java
        │       │   ├── Member.java
        │       │   ├── Option.java
        │       │   ├── Product.java
        │       │   └── Wish.java
        │       ├── exception
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
        │       │   ├── ProductRepository.java
        │       │   └── WishRepository.java
        │       ├── service
        │       │   ├── CategoryService.java
        │       │   ├── MemberService.java
        │       │   ├── ProductService.java
        │       │   └── WishService.java   
        │       ├── util
        │       │   └── TokenUtil.java   
        │       ├── value
        │       │   ├── OptionName.java
        │       │   └── OptionQuantity.java   
        │       └── Application.java
        └── resources
            ├── data.sql
            ├── schema.sql
            └── templates
                ├── add.html
                ├── edit.html
                ├── list.html
                └── view.html             
└── src
    └── test
        └── java
            └── gift   
                ├── CategoryRepositoryTest.java
                ├── MemberRepositoryTest.java
                ├── OptionRepositoryTest.java
                ├── ProductRepositoryTest.java
                └── WishRepositoryTest.java