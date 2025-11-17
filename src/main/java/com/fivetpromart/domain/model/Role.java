package com.fivetpromart.domain.model;


import lombok.*;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED, force = true)
@AllArgsConstructor
@Builder
public class Role {

    private final String id;
    private final String name;

}
