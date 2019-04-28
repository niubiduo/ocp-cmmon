package com.nbd.ocp.common.repository.multiTenancy.constant;

/**
 * Created by Administrator on 2019/2/15.
 */
public enum OcpDbTypeEnum {


    /**
     * oracle
     */
    DISCRIMINATOR_MODE("oracle"),
    /**
     * mysql
     */
    SCHEMA_MODE("mysql");

    private String type;


    OcpDbTypeEnum(String type){
        this.type = type;
    }


    public String getType() {
        return type;
    }

}
