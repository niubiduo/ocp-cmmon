package com.nbd.ocp.common.repository.multiTenancy.constant;

/**
 * Created by Administrator on 2019/2/15.
 */
public enum OcpMultiTenancyEnum {


    /**
     * 按字段做多租户分离
     */
    DISCRIMINATOR_MODE("discriminator"),
    /**
     * 按schema做多租户分离
     */
    SCHEMA_MODE("schema"),
    /**
     * 按database做多租户分离
     */
    DATABASE_MODE("database");

    private String mode;


    OcpMultiTenancyEnum(String mode){
        this.mode = mode;
    }


    public String getMode() {
        return mode;
    }

}
