package com.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Data @NoArgsConstructor
@AllArgsConstructor
public class UserDto {
    private Long userId;
    private String name;

    public static final List<UserDto> userDtos;
    static {
        userDtos = new ArrayList<UserDto>(){{
            add(new UserDto(1000L,"jack"));
            add(new UserDto(1001L,"lucy"));
            add(new UserDto(1002L,"daniel"));
            add(new UserDto(1003L,"jins"));
        }};
    }
}
