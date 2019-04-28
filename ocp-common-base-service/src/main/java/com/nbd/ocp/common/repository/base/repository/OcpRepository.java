package com.nbd.ocp.common.repository.base.repository;

import com.nbd.ocp.core.request.OcpQueryPageBaseVo;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.repository.NoRepositoryBean;

import java.io.Serializable;
import java.util.List;

@NoRepositoryBean
public interface OcpRepository<T, ID extends Serializable>
  extends JpaRepository<T, ID>, JpaSpecificationExecutor<T> {

      Page<T> page(final OcpQueryPageBaseVo ocpQueryPageBaseVo);

      List<T> list(OcpQueryPageBaseVo queryBaseVo);

}