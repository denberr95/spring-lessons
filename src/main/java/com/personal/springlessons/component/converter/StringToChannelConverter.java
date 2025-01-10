package com.personal.springlessons.component.converter;

import com.personal.springlessons.model.lov.Channel;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class StringToChannelConverter implements Converter<String, Channel> {

    @Override
    public Channel convert(String source) {
        return Channel.fromString(source);
    }
}
