package com.personal.springlessons.component;

import java.util.Collections;
import org.springframework.boot.actuate.info.Info;
import org.springframework.boot.actuate.info.InfoContributor;
import org.springframework.stereotype.Component;

@Component
public class CustomInfoContributor implements InfoContributor {

    @Override
    public void contribute(final Info.Builder builder) {
        builder.withDetail("customInfo", Collections.singletonMap("author", "Denis Berretti"));
    }
}
