# spring-gift-order

## **🚀 Step1- 카카오 로그인**

---

카카오 로그인은 아래 그림과 같이 진행된다.
![Untitled](https://developers.kakao.com/docs/latest/ko/assets/style/images/kakaologin/kakaologin_sequence.png)

### 💻 기능 요구 사항

---
카카오 로그인을 통해 인가 코드를 받고, 인가 코드를 사용해 토큰을 받은 후 향후 카카오 API 사용을 준비한다.

- [ ]  카카오계정 로그인을 통해 인증 코드를 받는다.
- [ ]  엑세스 토큰 추출
- [ ]  앱 키, 인가 코드 유출하지 않도록 구현
    - [ ]  따로 properties 만들어서 깃허브 등 외부에서 볼 수 없도록 한다.