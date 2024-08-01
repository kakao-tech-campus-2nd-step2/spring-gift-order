package gift.global.utility;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import gift.global.model.MultiValueMapConvertibleDto;
import java.util.Map;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;

// dto를 MultiValueMap으로 변환해주는 클래스
public class MultiValueMapConverter {

    public static MultiValueMap<String, String> convert(Object dto) {
        // 변환 가능하도록 명시한 dto만 변환하기
        if (!(dto instanceof MultiValueMapConvertibleDto)) {
            throw new IllegalArgumentException("요청 변환 중 오류가 발생했습니다.");
        }

        try {
            var objectMapper = new ObjectMapper();
            objectMapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
            Map<String, String> dtoMap = objectMapper.convertValue(dto, new TypeReference<>() {
            });
            var dtoMultiValueMap = new LinkedMultiValueMap<String, String>();
            dtoMultiValueMap.setAll(dtoMap);

            return dtoMultiValueMap;
        } catch (Exception e) {
            throw new IllegalArgumentException("요청 변환 중 오류가 발생했습니다.");
        }
    }
}
