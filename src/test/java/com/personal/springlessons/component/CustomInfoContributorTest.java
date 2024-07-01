package com.personal.springlessons.component;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import java.util.Map;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
@SuppressWarnings("unchecked")
class CustomInfoContributorTest {

    @Autowired
    private CustomInfoContributor customInfoContributor;

    @Test
    @DisplayName("Test author key is created")
    void testCustomInfoContributorDetails() {
        assertNotNull(this.customInfoContributor);

        Info.Builder builder = new Info.Builder();
        this.customInfoContributor.contribute(builder);
        Info info = builder.build();
        assertTrue(info.getDetails().containsKey("customInfo"));

        Object customInfo = info.getDetails().get("customInfo");
        assertTrue(customInfo instanceof java.util.Map);

        Map<String, Object> customInfoMap = (Map<String, Object>) customInfo;
        assertEquals(1, customInfoMap.size());
        assertTrue(customInfoMap.containsKey("author"));
        assertEquals("Denis Berretti", customInfoMap.get("author"));
    }
}
