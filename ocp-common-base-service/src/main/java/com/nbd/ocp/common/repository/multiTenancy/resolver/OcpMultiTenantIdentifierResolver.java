package com.nbd.ocp.common.repository.multiTenancy.resolver;

import com.nbd.ocp.core.context.threadlocal.InvocationInfoProxy;
import org.hibernate.context.spi.CurrentTenantIdentifierResolver;
import org.springframework.util.StringUtils;

/**
 * 这个类是由Hibernate提供的用于识别tenantId的类，当每次执行sql语句被拦截就会调用这个类中的方法来获取tenantId
 * @author lanyuanxiaoyao
 * @version 1.0
 */
public class OcpMultiTenantIdentifierResolver implements CurrentTenantIdentifierResolver{

    // 获取tenantId的逻辑在这个方法里面写
    @Override
    public String resolveCurrentTenantIdentifier() {
        String tenantId=InvocationInfoProxy.getTenantId();
        return StringUtils.isEmpty(tenantId)?"default":tenantId;
    }

    @Override
    public boolean validateExistingCurrentSessions() {
        return true;
    }
}
