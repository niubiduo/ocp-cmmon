package com.nbd.ocp.core.jpa.tree;

import com.alibaba.fastjson.JSON;
import com.nbd.ocp.core.context.threadlocal.InvocationInfoProxy;
import com.nbd.ocp.core.jpa.tree.entity.OcpMenuDo;
import com.nbd.ocp.core.jpa.tree.service.IOcpMenuService;
import com.nbd.ocp.common.repository.base.repository.OcpRepositoryImpl;
import com.nbd.ocp.common.repository.tree.request.OcpTreeOcpQueryBaseVo;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;



@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
@ComponentScan(basePackages="com.nbd.ocp.core.jpa")
@EnableJpaRepositories( basePackages = "com.nbd.ocp.core.jpa",repositoryBaseClass= OcpRepositoryImpl.class)
public class OcpTreeJpaApplicationTest {

	@Autowired
	IOcpMenuService menuService;

	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}

	@Test
	public void add(){
		InvocationInfoProxy.setTenantId("nbd1");
		OcpMenuDo ocpMenuDo = new OcpMenuDo();
		ocpMenuDo.setMenuCode("ddd");
		ocpMenuDo.setMenuName("dddd");
		ocpMenuDo.setPath("/asd/aasd/");
		ocpMenuDo.setPid("ff80808169eb68090169eb681a4f0000");
		OcpMenuDo ocpMenuDoDb = menuService.save(ocpMenuDo);
		System.out.println(JSON.toJSONString(ocpMenuDoDb));
		ocpMenuDoDb.setMenuCode("..");
		ocpMenuDoDb.setMenuName("..");
		ocpMenuDoDb.setPath("..");
		OcpMenuDo ocpMenuDoNew = menuService.updateSelective(ocpMenuDoDb);
		System.out.println(JSON.toJSONString(menuService.updateSelective(ocpMenuDoNew)));

	}

	@Test
	public void update() {
		InvocationInfoProxy.setTenantId("nbd1");
		OcpMenuDo ocpMenuDo = menuService.getById("ff80808169eb68090169eb681a4f0000");
		ocpMenuDo.setPid(null);
		System.out.println(JSON.toJSONString(menuService.updateSelective(ocpMenuDo)));
	}

	@Test
	public void testListTree() {
		InvocationInfoProxy.setTenantId("nbd1");
		OcpTreeOcpQueryBaseVo treeQueryBaseVo = new OcpTreeOcpQueryBaseVo();
		treeQueryBaseVo.setPid("root");
		System.out.println(JSON.toJSONString(menuService.listTree(treeQueryBaseVo)));
	}

	@Test
	public void testHasChildren() {
		InvocationInfoProxy.setTenantId("nbd1");
		System.out.println(JSON.toJSONString(menuService.hasChildren("00000C")));
	}
	@Test
	public void testGetById() {
		InvocationInfoProxy.setTenantId("nbd1");
		System.out.println(JSON.toJSONString(menuService.getById("ff80808169eb68090169eb681a4f0000")));
	}
	@Test
	public void testDeleteId() {
		InvocationInfoProxy.setTenantId("nbd1");
		menuService.deleteById("ff80808169eb66430169eb6651ab0000");
	}
}