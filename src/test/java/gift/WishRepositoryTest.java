package gift;


import gift.entity.Category;
import gift.entity.Member;
import gift.entity.Option;
import gift.entity.Product;
import gift.entity.Wish;
import gift.repository.CategoryRepository;
import gift.repository.MemberRepository;
import gift.repository.OptionRepository;
import gift.repository.ProductRepository;
import gift.repository.WishRepository;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.ArrayList;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
public class WishRepositoryTest {
    private WishRepository wishes;
    private ProductRepository products;
    private CategoryRepository categories;
    private OptionRepository options;
    private MemberRepository members;

    public WishRepositoryTest() {
    }

    public WishRepositoryTest(WishRepository wishes, ProductRepository products, CategoryRepository categories, OptionRepository options, MemberRepository members) {
        this.wishes = wishes;
        this.products = products;
        this.categories = categories;
        this.options = options;
        this.members = members;
    }


    @Test
    public void save() {
        Category category = categories.save(new Category("카테고리1", "빨강", "설명1", "카테고리 이미지url"));

        List<Option> optionList = new ArrayList<>();
        Option newOption = options.save(new Option("옵션이름1", 10L));
        optionList.add(newOption);

        Product product = products.save(new Product("상품1", 1000, "url", category, optionList));
        Member member = members.save(new Member("이메일1", "비밀번호"));

        Wish wish = wishes.save(new Wish(product, member));

        assertThat(wish).isNotNull();
        assertThat(wish.getProduct()).isNotNull();
        assertThat(wish.getMember()).isNotNull();
        assertThat(wish.getProductId()).isNotNull();
        assertThat(wish.getMemberId()).isNotNull();

    }

}

//"C:\Program Files\Java\jdk-17.0.2\bin\java.exe" -ea -Didea.test.cyclic.buffer.size=1048576 "-javaagent:C:\Program Files\JetBrains\IntelliJ IDEA 2023.3.2\lib\idea_rt.jar=2473:C:\Program Files\JetBrains\IntelliJ IDEA 2023.3.2\bin" -Dfile.encoding=UTF-8 -classpath "C:\Program Files\JetBrains\IntelliJ IDEA 2023.3.2\lib\idea_rt.jar;C:\Program Files\JetBrains\IntelliJ IDEA 2023.3.2\plugins\junit\lib\junit5-rt.jar;C:\Program Files\JetBrains\IntelliJ IDEA 2023.3.2\plugins\junit\lib\junit-rt.jar;C:\Users\a6438\Desktop\카테캠\step2과제\spring-gift-order\build\classes\java\test;C:\Users\a6438\Desktop\카테캠\step2과제\spring-gift-order\build\classes\java\main;C:\Users\a6438\Desktop\카테캠\step2과제\spring-gift-order\build\resources\main;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-data-jpa\3.3.1\f12725d58a944eaf4d44334a6a7b0fe14ed08577\spring-boot-starter-data-jpa-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-jdbc\3.3.1\73e79247180d277b68801ff5f4f9e9ab66210335\spring-boot-starter-jdbc-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-thymeleaf\3.3.1\bfaf6d8f8f8498d5c35758204af71aceb22c21eb\spring-boot-starter-thymeleaf-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-web\3.3.1\ec812e82a010d089438b6ac98ebe294f2e540f71\spring-boot-starter-web-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-test\3.3.1\4ca82495c11ccba4998fc4e6f195ff6d5673b96b\spring-boot-starter-test-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.h2database\h2\2.2.224\7bdade27d8cd197d9b5ce9dc251f41d2edc5f7ad\h2-2.2.224.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-aop\3.3.1\19da2c8abacda5bed6fadfe3e76e4b61e9cc0f09\spring-boot-starter-aop-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.data\spring-data-jpa\3.3.1\bf75f2140be9325d78de2c1c88eb9b7388a41bb1\spring-data-jpa-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.hibernate.orm\hibernate-core\6.5.2.Final\e9e0cc47f6cd2b2553968aee66bd9e55e7485221\hibernate-core-6.5.2.Final.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-aspects\6.1.10\8d869a8bcc99e71b98b9d5ca141539d6e6ba3061\spring-aspects-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter\3.3.1\30e0ac13cfa51c77db60909ea28572a1e973f186\spring-boot-starter-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.zaxxer\HikariCP\5.1.0\8c96e36c14461fc436bb02b264b96ef3ca5dca8c\HikariCP-5.1.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-jdbc\6.1.10\bba7d7de1d944443bd9e45d0ebcbbb6ec6864be0\spring-jdbc-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.thymeleaf\thymeleaf-spring6\3.1.2.RELEASE\6030c7b4e260c41336f378e53da6e8531263f24b\thymeleaf-spring6-3.1.2.RELEASE.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-json\3.3.1\b78d1ce67c3a44e8a2c2799b70e8c216166d0f5b\spring-boot-starter-json-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-tomcat\3.3.1\78bc3dedeb8abcea03f35d24f6779e0a3c6080d2\spring-boot-starter-tomcat-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-webmvc\6.1.10\476344c2f21ab070bc72108375d9355f2ec0ddd1\spring-webmvc-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-web\6.1.10\876a856af61ef5712fb9a3013b798aa2b4a9475e\spring-web-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-test-autoconfigure\3.3.1\be1e596cbe14f0102e1fd97bbc86b5910ed64396\spring-boot-test-autoconfigure-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-test\3.3.1\68f65454a9d7d84d077144bdd44bdda66cd3edb\spring-boot-test-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.jayway.jsonpath\json-path\2.9.0\37fe2217f577b0b68b18e62c4d17a8858ecf9b69\json-path-2.9.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\jakarta.xml.bind\jakarta.xml.bind-api\4.0.2\6cd5a999b834b63238005b7144136379dc36cad2\jakarta.xml.bind-api-4.0.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\net.minidev\json-smart\2.5.1\4c11d2808d009132dfbbf947ebf37de6bf266c8e\json-smart-2.5.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.assertj\assertj-core\3.25.3\792b270e73aa1cfc28fa135be0b95e69ea451432\assertj-core-3.25.3.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.awaitility\awaitility\4.2.1\e56b600e0b184182ba5b2baccd2bab593a98a624\awaitility-4.2.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.hamcrest\hamcrest\2.2\1820c0968dba3a11a1b30669bb1f01978a91dedc\hamcrest-2.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.junit.jupiter\junit-jupiter\5.10.2\831c0b86ddc2ce38391c5b81ea662b0cfdc02cce\junit-jupiter-5.10.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.mockito\mockito-junit-jupiter\5.11.0\8e658dd339f40305ed4293db25545b5df98b171b\mockito-junit-jupiter-5.11.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.mockito\mockito-core\5.11.0\e4069fa4f4ff2c94322cfec5f2e45341c6c70aff\mockito-core-5.11.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.skyscreamer\jsonassert\1.5.1\6d842d0faf4cf6725c509a5e5347d319ee0431c3\jsonassert-1.5.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-test\6.1.10\82d3c12836121e37843f21bf382a8c46c0448b59\spring-test-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-core\6.1.10\eaf5b1f3e3bb5aa8b45ab255cf3270c1c4578f1d\spring-core-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.xmlunit\xmlunit-core\2.9.1\e5833662d9a1279a37da3ef6f62a1da29fcd68c4\xmlunit-core-2.9.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-aop\6.1.10\aa6a147eb08820fb503a992a8fe2c9fee3439129\spring-aop-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.aspectj\aspectjweaver\1.9.22\10736ab74a53af5e2e1b07e76335a5391526b6f8\aspectjweaver-1.9.22.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-orm\6.1.10\b4cd93ddd043407b3ba3fdfca3b07db776581fec\spring-orm-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-tx\6.1.10\1a2361bc1881ed1a0dc2e09561e8f560e1949c8e\spring-tx-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-context\6.1.10\6f869ea35a26028f3bfbfb52c72ef2b077fbb6e5\spring-context-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.data\spring-data-commons\3.3.1\fe179b4f9f5d4fafdcf9e2a2030033d61423c3f6\spring-data-commons-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-beans\6.1.10\a49252929fb2918f73eb7659ef98dff7306a7c2c\spring-beans-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\jakarta.annotation\jakarta.annotation-api\2.1.1\48b9bda22b091b1f48b13af03fe36db3be6e1ae3\jakarta.annotation-api-2.1.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.slf4j\slf4j-api\2.0.13\80229737f704b121a318bba5d5deacbcf395bc77\slf4j-api-2.0.13.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.antlr\antlr4-runtime\4.13.0\5a02e48521624faaf5ff4d99afc88b01686af655\antlr4-runtime-4.13.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\jakarta.persistence\jakarta.persistence-api\3.1.0\66901fa1c373c6aff65c13791cc11da72060a8d6\jakarta.persistence-api-3.1.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\jakarta.transaction\jakarta.transaction-api\2.0.1\51a520e3fae406abb84e2e1148e6746ce3f80a1a\jakarta.transaction-api-2.0.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-autoconfigure\3.3.1\36a6489e3ba5e34163bddb5134021de9ce101abc\spring-boot-autoconfigure-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot\3.3.1\2c5cfe68bc12646ce44663f865d39c747a28f2c7\spring-boot-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework.boot\spring-boot-starter-logging\3.3.1\2097eebccdf7556cf42c8a74a5da64420ac143a1\spring-boot-starter-logging-3.3.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.yaml\snakeyaml\2.2\3af797a25458550a16bf89acc8e4ab2b7f2bfce0\snakeyaml-2.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.thymeleaf\thymeleaf\3.1.2.RELEASE\273997509a4c7aef86cee0521750140c587d9be2\thymeleaf-3.1.2.RELEASE.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.fasterxml.jackson.datatype\jackson-datatype-jsr310\2.17.1\969b0c3cb8c75d759e9a6c585c44c9b9f3a4f75\jackson-datatype-jsr310-2.17.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.fasterxml.jackson.module\jackson-module-parameter-names\2.17.1\74a998f6fbcedbddedf0a27e8ce72078b2e516a6\jackson-module-parameter-names-2.17.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.fasterxml.jackson.datatype\jackson-datatype-jdk8\2.17.1\76b495194c36058904c82e288d285a1bd13f0ffa\jackson-datatype-jdk8-2.17.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.fasterxml.jackson.core\jackson-databind\2.17.1\524dcbcccdde7d45a679dfc333e4763feb09079\jackson-databind-2.17.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.apache.tomcat.embed\tomcat-embed-websocket\10.1.25\efd7848a9677e9a7cf8367b43213805eca70805\tomcat-embed-websocket-10.1.25.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.apache.tomcat.embed\tomcat-embed-core\10.1.25\c1e5ee12f537e6b4fea2b98a7160dcf0db610561\tomcat-embed-core-10.1.25.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.apache.tomcat.embed\tomcat-embed-el\10.1.25\2f030e4971e29245c10e58723fb6b364ce23933b\tomcat-embed-el-10.1.25.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-expression\6.1.10\a17d61f1388d0e13d6e7a740304e9d648fffc7c\spring-expression-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\io.micrometer\micrometer-observation\1.13.1\f6f5fa79e482431531cc253a7204e5c085c7bb20\micrometer-observation-1.13.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\jakarta.activation\jakarta.activation-api\2.1.3\fa165bd70cda600368eee31555222776a46b881f\jakarta.activation-api-2.1.3.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\net.minidev\accessors-smart\2.5.1\19b820261eb2e7de7d5bde11d1c06e4501dd7e5f\accessors-smart-2.5.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\net.bytebuddy\byte-buddy\1.14.17\a8d08f3c1e75ecc7f38a8cfd7e9fa47919096373\byte-buddy-1.14.17.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.junit.jupiter\junit-jupiter-params\5.10.2\359132c82a9d3fa87a325db6edd33b5fdc67a3d8\junit-jupiter-params-5.10.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.junit.jupiter\junit-jupiter-api\5.10.2\fb55d6e2bce173f35fd28422e7975539621055ef\junit-jupiter-api-5.10.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\net.bytebuddy\byte-buddy-agent\1.14.17\e3c251a39dc90badaf71c83427ba46840f219d8d\byte-buddy-agent-1.14.17.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.vaadin.external.google\android-json\0.0.20131108.vaadin1\fa26d351fe62a6a17f5cda1287c1c6110dec413f\android-json-0.0.20131108.vaadin1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.springframework\spring-jcl\6.1.10\c22c18cf6ed7d768676816347f020a6868663224\spring-jcl-6.1.10.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\ch.qos.logback\logback-classic\1.5.6\afc75d260d838a3bddfb8f207c2805ed7d1b34f9\logback-classic-1.5.6.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.apache.logging.log4j\log4j-to-slf4j\2.23.1\425ad1eb8a39904d2830e907a324e956fb456520\log4j-to-slf4j-2.23.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.slf4j\jul-to-slf4j\2.0.13\a3bcd9d9dd50c71ce69f06b1fd05e40fdeff6ba5\jul-to-slf4j-2.0.13.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.attoparser\attoparser\2.0.7.RELEASE\e5d0e988d9124139d645bb5872b24dfa23e283cc\attoparser-2.0.7.RELEASE.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.unbescape\unbescape\1.1.6.RELEASE\7b90360afb2b860e09e8347112800d12c12b2a13\unbescape-1.1.6.RELEASE.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.fasterxml.jackson.core\jackson-annotations\2.17.1\fca7ef6192c9ad05d07bc50da991bf937a84af3a\jackson-annotations-2.17.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.fasterxml.jackson.core\jackson-core\2.17.1\5e52a11644cd59a28ef79f02bddc2cc3bab45edb\jackson-core-2.17.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\io.micrometer\micrometer-commons\1.13.1\5629ecbcc84a9f29e1cf976718de2497e50932bf\micrometer-commons-1.13.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.ow2.asm\asm\9.6\aa205cf0a06dbd8e04ece91c0b37c3f5d567546a\asm-9.6.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.apiguardian\apiguardian-api\1.1.2\a231e0d844d2721b0fa1b238006d15c6ded6842a\apiguardian-api-1.1.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.junit.platform\junit-platform-commons\1.10.2\3197154a1f0c88da46c47a9ca27611ac7ec5d797\junit-platform-commons-1.10.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.opentest4j\opentest4j\1.3.0\152ea56b3a72f655d4fd677fc0ef2596c3dd5e6e\opentest4j-1.3.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\ch.qos.logback\logback-core\1.5.6\41cbe874701200c5624c19e0ab50d1b88dfcc77d\logback-core-1.5.6.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.apache.logging.log4j\log4j-api\2.23.1\9c15c29c526d9c6783049c0a77722693c66706e1\log4j-api-2.23.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\io.jsonwebtoken\jjwt-impl\0.12.6\ac23673a84b6089e0369fb8ab2c69edd91cd6eb0\jjwt-impl-0.12.6.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\io.jsonwebtoken\jjwt-jackson\0.12.6\f141e0c1136ba17f2632858238a31ae05642dbf8\jjwt-jackson-0.12.6.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.junit.platform\junit-platform-launcher\1.10.2\8125dd29e847ca274dd1a7a9ca54859acc284cb3\junit-platform-launcher-1.10.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\io.jsonwebtoken\jjwt-api\0.12.6\478886a888f6add04937baf0361144504a024967\jjwt-api-0.12.6.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.junit.platform\junit-platform-engine\1.10.2\d53bb4e0ce7f211a498705783440614bfaf0df2e\junit-platform-engine-1.10.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.glassfish.jaxb\jaxb-runtime\4.0.5\ca84c2a7169b5293e232b9d00d1e4e36d4c3914a\jaxb-runtime-4.0.5.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.jboss.logging\jboss-logging\3.5.3.Final\c88fc1d8a96d4c3491f55d4317458ccad53ca663\jboss-logging-3.5.3.Final.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.hibernate.common\hibernate-commons-annotations\6.0.6.Final\77a5f94b56d49508e0ee334751db5b78e5ccd50c\hibernate-commons-annotations-6.0.6.Final.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\io.smallrye\jandex\3.1.2\a6c1c89925c7df06242b03dddb353116ceb9584c\jandex-3.1.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.fasterxml\classmate\1.7.0\e98374da1f2143ac8e6e0a95036994bb19137a3\classmate-1.7.0.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\jakarta.inject\jakarta.inject-api\2.0.1\4c28afe1991a941d7702fe1362c365f0a8641d1e\jakarta.inject-api-2.0.1.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.junit.jupiter\junit-jupiter-engine\5.10.2\f1f8fe97bd58e85569205f071274d459c2c4f8cd\junit-jupiter-engine-5.10.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.objenesis\objenesis\3.3\1049c09f1de4331e8193e579448d0916d75b7631\objenesis-3.3.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.glassfish.jaxb\jaxb-core\4.0.5\7b4b11ea5542eea4ad55e1080b23be436795b3\jaxb-core-4.0.5.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.eclipse.angus\angus-activation\2.0.2\41f1e0ddd157c856926ed149ab837d110955a9fc\angus-activation-2.0.2.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\org.glassfish.jaxb\txw2\4.0.5\f36a4ef12120a9bb06d766d6a0e54b144fd7ed98\txw2-4.0.5.jar;C:\Users\a6438\.gradle\caches\modules-2\files-2.1\com.sun.istack\istack-commons-runtime\4.1.2\18ec117c85f3ba0ac65409136afa8e42bc74e739\istack-commons-runtime-4.1.2.jar" com.intellij.rt.junit.JUnitStarter -ideVersion5 -junit5 gift.test2
//07:26:20.317 [main] INFO org.springframework.test.context.support.AnnotationConfigContextLoaderUtils -- Could not detect default configuration classes for test class [gift.test2]: test2 does not declare any static, non-private, non-final, nested classes annotated with @Configuration.
//07:26:21.163 [main] INFO org.springframework.boot.test.context.SpringBootTestContextBootstrapper -- Found @SpringBootConfiguration gift.Application for test class gift.test2
//
//  .   ____          _            __ _ _
// /\\ / ___'_ __ _ _(_)_ __  __ _ \ \ \ \
//( ( )\___ | '_ | '_| | '_ \/ _` | \ \ \ \
//        \\/  ___)| |_)| | | | | || (_| |  ) ) ) )
//        '  |____| .__|_| |_|_| |_\__, | / / / /
//        =========|_|==============|___/=/_/_/_/
//
//        :: Spring Boot ::                (v3.3.1)
//
//        2024-07-26T07:26:23.041+09:00  INFO 19568 --- [spring-gift] [           main] gift.test2                               : Starting test2 using Java 17.0.2 with PID 19568 (started by 신성희 in C:\Users\a6438\Desktop\카테캠\step2과제\spring-gift-order)
//        2024-07-26T07:26:23.046+09:00  INFO 19568 --- [spring-gift] [           main] gift.test2                               : No active profile set, falling back to 1 default profile: "default"
//        2024-07-26T07:26:24.238+09:00  INFO 19568 --- [spring-gift] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Bootstrapping Spring Data JPA repositories in DEFAULT mode.
//        2024-07-26T07:26:24.519+09:00  INFO 19568 --- [spring-gift] [           main] .s.d.r.c.RepositoryConfigurationDelegate : Finished Spring Data repository scanning in 253 ms. Found 5 JPA repository interfaces.
//2024-07-26T07:26:24.811+09:00  INFO 19568 --- [spring-gift] [           main] beddedDataSourceBeanFactoryPostProcessor : Replacing 'dataSource' DataSource bean with embedded version
//2024-07-26T07:26:25.579+09:00  INFO 19568 --- [spring-gift] [           main] o.s.j.d.e.EmbeddedDatabaseFactory        : Starting embedded database: url='jdbc:h2:mem:7a4b612e-3a57-4c3d-91d0-849fadd42134;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=false', username='sa'
//        2024-07-26T07:26:26.720+09:00  INFO 19568 --- [spring-gift] [           main] o.hibernate.jpa.internal.util.LogHelper  : HHH000204: Processing PersistenceUnitInfo [name: default]
//        2024-07-26T07:26:26.945+09:00  INFO 19568 --- [spring-gift] [           main] org.hibernate.Version                    : HHH000412: Hibernate ORM core version 6.5.2.Final
//2024-07-26T07:26:27.091+09:00  INFO 19568 --- [spring-gift] [           main] o.h.c.internal.RegionFactoryInitiator    : HHH000026: Second-level cache disabled
//2024-07-26T07:26:28.263+09:00  INFO 19568 --- [spring-gift] [           main] o.s.o.j.p.SpringPersistenceUnitInfo      : No LoadTimeWeaver setup: ignoring JPA class transformer
//2024-07-26T07:26:28.497+09:00  WARN 19568 --- [spring-gift] [           main] org.hibernate.orm.deprecation            : HHH90000025: MySQL8Dialect does not need to be specified explicitly using 'hibernate.dialect' (remove the property setting and it will be selected by default)
//        2024-07-26T07:26:28.504+09:00  WARN 19568 --- [spring-gift] [           main] org.hibernate.orm.deprecation            : HHH90000026: MySQL8Dialect has been deprecated; use org.hibernate.dialect.MySQLDialect instead
//2024-07-26T07:26:32.236+09:00  INFO 19568 --- [spring-gift] [           main] o.h.e.t.j.p.i.JtaPlatformInitiator       : HHH000489: No JTA platform available (set 'hibernate.transaction.jta.platform' to enable JTA platform integration)
//2024-07-26T07:26:32.350+09:00 DEBUG 19568 --- [spring-gift] [           main] org.hibernate.SQL                        :
//alter table option
//add constraint FK5t6etuqa4wl7lyn0ysxnts7q4
//foreign key (product_id)
//references product (id)
//Hibernate:
//alter table option
//add constraint FK5t6etuqa4wl7lyn0ysxnts7q4
//foreign key (product_id)
//references product (id)
//2024-07-26T07:26:32.354+09:00 DEBUG 19568 --- [spring-gift] [           main] org.hibernate.SQL                        :
//alter table product
//add constraint FK1mtsbur82frn64de7balymq9s
//foreign key (category_id)
//references category (id)
//Hibernate:
//alter table product
//add constraint FK1mtsbur82frn64de7balymq9s
//foreign key (category_id)
//references category (id)
//2024-07-26T07:26:32.358+09:00 DEBUG 19568 --- [spring-gift] [           main] org.hibernate.SQL                        :
//alter table wish
//add constraint FK70nrc4a6uvljrtemsn80eq1gd
//foreign key (member_id)
//references member (id)
//Hibernate:
//alter table wish
//add constraint FK70nrc4a6uvljrtemsn80eq1gd
//foreign key (member_id)
//references member (id)
//2024-07-26T07:26:32.360+09:00 DEBUG 19568 --- [spring-gift] [           main] org.hibernate.SQL                        :
//alter table wish
//add constraint FKh3bvkvkslnehbxqma1x2eynqb
//foreign key (product_id)
//references product (id)
//Hibernate:
//alter table wish
//add constraint FKh3bvkvkslnehbxqma1x2eynqb
//foreign key (product_id)
//references product (id)
//2024-07-26T07:26:32.372+09:00  INFO 19568 --- [spring-gift] [           main] j.LocalContainerEntityManagerFactoryBean : Initialized JPA EntityManagerFactory for persistence unit 'default'
//        2024-07-26T07:26:33.606+09:00  INFO 19568 --- [spring-gift] [           main] gift.test2                               : Started test2 in 12.146 seconds (process running for 16.516)
//Java HotSpot(TM) 64-Bit Server VM warning: Sharing is only supported for boot loader classes because bootstrap classpath has been appended
//2024-07-26T07:26:35.104+09:00 DEBUG 19568 --- [spring-gift] [           main] org.hibernate.SQL                        :
//insert
//        into
//category
//        (color, description, image_url, name)
//values
//        (?, ?, ?, ?)
//Hibernate:
//insert
//        into
//category
//        (color, description, image_url, name)
//values
//        (?, ?, ?, ?)
//2024-07-26T07:26:35.116+09:00 TRACE 19568 --- [spring-gift] [           main] org.hibernate.orm.jdbc.bind              : binding parameter (1:VARCHAR) <- [빨강]
//        2024-07-26T07:26:35.117+09:00 TRACE 19568 --- [spring-gift] [           main] org.hibernate.orm.jdbc.bind              : binding parameter (2:VARCHAR) <- [설명1]
//        2024-07-26T07:26:35.117+09:00 TRACE 19568 --- [spring-gift] [           main] org.hibernate.orm.jdbc.bind              : binding parameter (3:VARCHAR) <- [카테고리 이미지url]
//        2024-07-26T07:26:35.117+09:00 TRACE 19568 --- [spring-gift] [           main] org.hibernate.orm.jdbc.bind              : binding parameter (4:VARCHAR) <- [카테고리1]
//        2024-07-26T07:26:35.230+09:00 DEBUG 19568 --- [spring-gift] [           main] org.hibernate.SQL                        :
//insert
//        into
//option
//        (name, product_id, quantity)
//values
//        (?, ?, ?)
//Hibernate:
//insert
//        into
//option
//        (name, product_id, quantity)
//values
//        (?, ?, ?)
//2024-07-26T07:26:35.231+09:00 TRACE 19568 --- [spring-gift] [           main] org.hibernate.orm.jdbc.bind              : binding parameter (1:VARCHAR) <- [옵션이름1]
//        2024-07-26T07:26:35.231+09:00 TRACE 19568 --- [spring-gift] [           main] org.hibernate.orm.jdbc.bind              : binding parameter (2:BIGINT) <- [null]
//        2024-07-26T07:26:35.231+09:00 TRACE 19568 --- [spring-gift] [           main] org.hibernate.orm.jdbc.bind              : binding parameter (3:BIGINT) <- [10]
//        2024-07-26T07:26:35.236+09:00  WARN 19568 --- [spring-gift] [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : SQL Error: 23502, SQLState: 23502
//        2024-07-26T07:26:35.236+09:00 ERROR 19568 --- [spring-gift] [           main] o.h.engine.jdbc.spi.SqlExceptionHelper   : NULL not allowed for column "PRODUCT_ID"; SQL statement:
//insert into option (name,product_id,quantity) values (?,?,?) [23502-224]
//
//org.springframework.dao.DataIntegrityViolationException: could not execute statement [NULL not allowed for column "PRODUCT_ID"; SQL statement:
//insert into option (name,product_id,quantity) values (?,?,?) [23502-224]] [insert into option (name,product_id,quantity) values (?,?,?)]; SQL [insert into option (name,product_id,quantity) values (?,?,?)]; constraint [null]
//
//at org.springframework.orm.jpa.vendor.HibernateJpaDialect.convertHibernateAccessException(HibernateJpaDialect.java:290)
//at org.springframework.orm.jpa.vendor.HibernateJpaDialect.translateExceptionIfPossible(HibernateJpaDialect.java:241)
//at org.springframework.orm.jpa.AbstractEntityManagerFactoryBean.translateExceptionIfPossible(AbstractEntityManagerFactoryBean.java:550)
//at org.springframework.dao.support.ChainedPersistenceExceptionTranslator.translateExceptionIfPossible(ChainedPersistenceExceptionTranslator.java:61)
//at org.springframework.dao.support.DataAccessUtils.translateIfNecessary(DataAccessUtils.java:335)
//at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:160)
//at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
//at org.springframework.data.jpa.repository.support.CrudMethodMetadataPostProcessor$CrudMethodMetadataPopulatingMethodInterceptor.invoke(CrudMethodMetadataPostProcessor.java:165)
//at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
//at org.springframework.aop.interceptor.ExposeInvocationInterceptor.invoke(ExposeInvocationInterceptor.java:97)
//at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
//at org.springframework.aop.framework.JdkDynamicAopProxy.invoke(JdkDynamicAopProxy.java:223)
//at jdk.proxy2/jdk.proxy2.$Proxy124.save(Unknown Source)
//at gift.test2.save(test2.java:46)
//at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//at java.base/java.util.ArrayList.forEach(ArrayList.java:1511)
//Caused by: org.hibernate.exception.ConstraintViolationException: could not execute statement [NULL not allowed for column "PRODUCT_ID"; SQL statement:
//insert into option (name,product_id,quantity) values (?,?,?) [23502-224]] [insert into option (name,product_id,quantity) values (?,?,?)]
//at org.hibernate.exception.internal.SQLExceptionTypeDelegate.convert(SQLExceptionTypeDelegate.java:62)
//at org.hibernate.exception.internal.StandardSQLExceptionConverter.convert(StandardSQLExceptionConverter.java:58)
//at org.hibernate.engine.jdbc.spi.SqlExceptionHelper.convert(SqlExceptionHelper.java:108)
//at org.hibernate.engine.jdbc.internal.ResultSetReturnImpl.executeUpdate(ResultSetReturnImpl.java:197)
//at org.hibernate.id.insert.GetGeneratedKeysDelegate.performMutation(GetGeneratedKeysDelegate.java:116)
//at org.hibernate.engine.jdbc.mutation.internal.MutationExecutorSingleNonBatched.performNonBatchedOperations(MutationExecutorSingleNonBatched.java:47)
//at org.hibernate.engine.jdbc.mutation.internal.AbstractMutationExecutor.execute(AbstractMutationExecutor.java:55)
//at org.hibernate.persister.entity.mutation.InsertCoordinatorStandard.doStaticInserts(InsertCoordinatorStandard.java:194)
//at org.hibernate.persister.entity.mutation.InsertCoordinatorStandard.coordinateInsert(InsertCoordinatorStandard.java:132)
//at org.hibernate.persister.entity.mutation.InsertCoordinatorStandard.insert(InsertCoordinatorStandard.java:95)
//at org.hibernate.action.internal.EntityIdentityInsertAction.execute(EntityIdentityInsertAction.java:85)
//at org.hibernate.engine.spi.ActionQueue.execute(ActionQueue.java:670)
//at org.hibernate.engine.spi.ActionQueue.addResolvedEntityInsertAction(ActionQueue.java:291)
//at org.hibernate.engine.spi.ActionQueue.addInsertAction(ActionQueue.java:272)
//at org.hibernate.engine.spi.ActionQueue.addAction(ActionQueue.java:322)
//at org.hibernate.event.internal.AbstractSaveEventListener.addInsertAction(AbstractSaveEventListener.java:391)
//at org.hibernate.event.internal.AbstractSaveEventListener.performSaveOrReplicate(AbstractSaveEventListener.java:305)
//at org.hibernate.event.internal.AbstractSaveEventListener.performSave(AbstractSaveEventListener.java:224)
//at org.hibernate.event.internal.AbstractSaveEventListener.saveWithGeneratedId(AbstractSaveEventListener.java:137)
//at org.hibernate.event.internal.DefaultPersistEventListener.entityIsTransient(DefaultPersistEventListener.java:175)
//at org.hibernate.event.internal.DefaultPersistEventListener.persist(DefaultPersistEventListener.java:93)
//at org.hibernate.event.internal.DefaultPersistEventListener.onPersist(DefaultPersistEventListener.java:77)
//at org.hibernate.event.internal.DefaultPersistEventListener.onPersist(DefaultPersistEventListener.java:54)
//at org.hibernate.event.service.internal.EventListenerGroupImpl.fireEventOnEachListener(EventListenerGroupImpl.java:127)
//at org.hibernate.internal.SessionImpl.firePersist(SessionImpl.java:757)
//at org.hibernate.internal.SessionImpl.persist(SessionImpl.java:741)
//at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
//at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//at org.springframework.orm.jpa.SharedEntityManagerCreator$SharedEntityManagerInvocationHandler.invoke(SharedEntityManagerCreator.java:319)
//at jdk.proxy2/jdk.proxy2.$Proxy114.persist(Unknown Source)
//at org.springframework.data.jpa.repository.support.SimpleJpaRepository.save(SimpleJpaRepository.java:629)
//at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke0(Native Method)
//at java.base/jdk.internal.reflect.NativeMethodAccessorImpl.invoke(NativeMethodAccessorImpl.java:77)
//at java.base/jdk.internal.reflect.DelegatingMethodAccessorImpl.invoke(DelegatingMethodAccessorImpl.java:43)
//at java.base/java.lang.reflect.Method.invoke(Method.java:568)
//at org.springframework.aop.support.AopUtils.invokeJoinpointUsingReflection(AopUtils.java:354)
//at org.springframework.data.repository.core.support.RepositoryMethodInvoker$RepositoryFragmentMethodInvoker.lambda$new$0(RepositoryMethodInvoker.java:277)
//at org.springframework.data.repository.core.support.RepositoryMethodInvoker.doInvoke(RepositoryMethodInvoker.java:170)
//at org.springframework.data.repository.core.support.RepositoryMethodInvoker.invoke(RepositoryMethodInvoker.java:158)
//at org.springframework.data.repository.core.support.RepositoryComposition$RepositoryFragments.invoke(RepositoryComposition.java:516)
//at org.springframework.data.repository.core.support.RepositoryComposition.invoke(RepositoryComposition.java:285)
//at org.springframework.data.repository.core.support.RepositoryFactorySupport$ImplementationMethodExecutionInterceptor.invoke(RepositoryFactorySupport.java:628)
//at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
//at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.doInvoke(QueryExecutorMethodInterceptor.java:168)
//at org.springframework.data.repository.core.support.QueryExecutorMethodInterceptor.invoke(QueryExecutorMethodInterceptor.java:143)
//at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
//at org.springframework.data.projection.DefaultMethodInvokingMethodInterceptor.invoke(DefaultMethodInvokingMethodInterceptor.java:70)
//at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
//at org.springframework.transaction.interceptor.TransactionInterceptor$1.proceedWithInvocation(TransactionInterceptor.java:123)
//at org.springframework.transaction.interceptor.TransactionAspectSupport.invokeWithinTransaction(TransactionAspectSupport.java:392)
//at org.springframework.transaction.interceptor.TransactionInterceptor.invoke(TransactionInterceptor.java:119)
//at org.springframework.aop.framework.ReflectiveMethodInvocation.proceed(ReflectiveMethodInvocation.java:184)
//at org.springframework.dao.support.PersistenceExceptionTranslationInterceptor.invoke(PersistenceExceptionTranslationInterceptor.java:138)
//	... 11 more
//Caused by: org.h2.jdbc.JdbcSQLIntegrityConstraintViolationException: NULL not allowed for column "PRODUCT_ID"; SQL statement:
//insert into option (name,product_id,quantity) values (?,?,?) [23502-224]
//at org.h2.message.DbException.getJdbcSQLException(DbException.java:520)
//at org.h2.message.DbException.getJdbcSQLException(DbException.java:489)
//at org.h2.message.DbException.get(DbException.java:223)
//at org.h2.message.DbException.get(DbException.java:199)
//at org.h2.table.Column.validateConvertUpdateSequence(Column.java:365)
//at org.h2.table.Table.convertInsertRow(Table.java:936)
//at org.h2.command.dml.Insert.insertRows(Insert.java:167)
//at org.h2.command.dml.Insert.update(Insert.java:135)
//at org.h2.command.CommandContainer.executeUpdateWithGeneratedKeys(CommandContainer.java:242)
//at org.h2.command.CommandContainer.update(CommandContainer.java:163)
//at org.h2.command.Command.executeUpdate(Command.java:256)
//at org.h2.jdbc.JdbcPreparedStatement.executeUpdateInternal(JdbcPreparedStatement.java:216)
//at org.h2.jdbc.JdbcPreparedStatement.executeUpdate(JdbcPreparedStatement.java:174)
//at org.hibernate.engine.jdbc.internal.ResultSetReturnImpl.executeUpdate(ResultSetReturnImpl.java:194)
//	... 62 more
//
//2024-07-26T07:26:35.385+09:00  INFO 19568 --- [spring-gift] [ionShutdownHook] j.LocalContainerEntityManagerFactoryBean : Closing JPA EntityManagerFactory for persistence unit 'default'
//
//Process finished with exit code -1
