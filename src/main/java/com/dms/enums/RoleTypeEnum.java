package com.dms.enums;

public enum RoleTypeEnum {

    ROLE_ADMIN(1l, "ROLE_ADMIN"), ROLE_COMPANY(2l, "ROLE_COMPANY");

    private String name;
    private Long id;

    private RoleTypeEnum(Long id, String name) {
        this.name = name;
        this.id = id;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
