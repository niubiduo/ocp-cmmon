package com.nbd.ocp.core.jpa.crud;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.nbd.ocp.core.context.threadlocal.InvocationInfoProxy;
import com.nbd.ocp.core.jpa.crud.entity.OcpUserDo;
import com.nbd.ocp.core.jpa.crud.service.IOcpUserService;
import com.nbd.ocp.common.repository.base.repository.OcpJpaRepositoryFactoryBean;
import com.nbd.ocp.core.request.OcpQueryPageBaseConstant;
import com.nbd.ocp.core.request.OcpQueryPageBaseVo;
import com.nbd.ocp.core.utils.uri.OcpUriParamUtil;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@RunWith(SpringRunner.class)
@SpringBootTest
@Rollback(false)
@ComponentScan(basePackages="com.nbd.ocp.core.jpa")
@EnableJpaRepositories( basePackages = "com.nbd.ocp.core.jpa",repositoryFactoryBeanClass= OcpJpaRepositoryFactoryBean.class)

public class OcpCrudJpaApplicationTest {

	@Autowired
	IOcpUserService userService;
	private MockMvc mockMvc;

	@Autowired
	private WebApplicationContext wac;

	@Before
	public void setup() {
		this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
	}


	public void save() {
		InvocationInfoProxy.setTenantId("nbd1");
		OcpUserDo userDO =new OcpUserDo();
		userDO.setUserName("aaa");
		userDO.setUserCode("bbb");
		userDO.setPassword("ccc");
		userDO.setEmail("d");
		userDO.setSalt("ddddd");
		userDO.setLocked(false);
		OcpUserDo userVO =userService.save(userDO);
		System.out.println(userVO);
		InvocationInfoProxy.setTenantId("nbd");
		OcpUserDo userDO1 =new OcpUserDo();
		userDO1.setEmail("d");
		userDO1.setPassword("ddd");
		userDO1.setSalt("ddddd");
		userDO1.setLocked(false);
		OcpUserDo userVO1 =userService.save(userDO1);
		System.out.println(userVO1);

	}
	@Test
	public void list() {
		InvocationInfoProxy.setTenantId("nbd1");
		System.out.println(JSON.toJSONString(userService.findAll().size()));
		InvocationInfoProxy.setTenantId("nbd");
		System.out.println(JSON.toJSONString(userService.findAll().size()));

	}
	@Test
	public void page() {
		OcpQueryPageBaseVo ocpQueryPageBaseVo=new OcpQueryPageBaseVo();
		ocpQueryPageBaseVo.setPageIndex(0);
		ocpQueryPageBaseVo.setPageSize(10);
		InvocationInfoProxy.setTenantId("nbd1");
		System.out.println(JSON.toJSONString(userService.page(ocpQueryPageBaseVo)));
	}

	@Test
	public  void listUsers(){
		System.out.println(JSON.toJSONString(userService.listUsers().size()));
	}
	@Test
	public void testAddController() throws Exception {
		OcpUserDo UserDO =new OcpUserDo();
		UserDO.setEmail("d");
		UserDO.setPassword("dddd");
		UserDO.setSalt("dddddd");
		UserDO.setLocked(false);

		MvcResult result = mockMvc.perform(post("/user/add").contentType(MediaType.APPLICATION_JSON_UTF8).content(JSONObject.toJSONString(UserDO)))
				.andExpect(status().isOk())
				.andReturn();
		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testPageController() throws Exception {
		OcpQueryPageBaseVo ocpQueryPageBaseVo =new OcpQueryPageBaseVo();
		ocpQueryPageBaseVo.setPageIndex(1);
		ocpQueryPageBaseVo.setPageSize(20);

		Map<String,Object> map=new HashMap<>();
		map.put(OcpQueryPageBaseConstant.VO_FIELD_FILTER_METHOD,"findByUserNameEqualsOrUserCodeLikeAndPasswordLikeOrIdIn");
		map.put("userName","aaa");
		map.put("userCode","bbb");
		map.put("password","ccc");
		List<String> ids =new ArrayList<>();
		ids.add("ff80808169d9b5220169d9b5317b0001");
		ids.add("eee");
		map.put("id",ids);
		ocpQueryPageBaseVo.setParameters(map);
		ocpQueryPageBaseVo.setIds(ids);

		MvcResult result = mockMvc.perform(get("/user/page?"+ OcpUriParamUtil.bean2UrlParamStr(ocpQueryPageBaseVo)))
				.andExpect(status().isOk())
				.andReturn();

		System.out.println(result.getResponse().getContentAsString());
	}

	@Test
	public void testListController() throws Exception {
		OcpQueryPageBaseVo ocpQueryPageBaseVo =new OcpQueryPageBaseVo();

		Map<String,Object> map=new HashMap<>();
		map.put(OcpQueryPageBaseConstant.VO_FIELD_FILTER_METHOD,"findByUserNameEqualsOrUserCodeLikeAndPasswordLikeOrIdIn");
		map.put("userName","aaa");
		map.put("userCode","bbb");
		map.put("password","ccc");
		List<String> ids =new ArrayList<>();
		ids.add("ddd");
		ids.add("eee");
		map.put("id",ids);
		ocpQueryPageBaseVo.setParameters(map);
		ocpQueryPageBaseVo.setIds(ids);
		MvcResult result = mockMvc.perform(get("/user/list?"+OcpUriParamUtil.bean2UrlParamStr(ocpQueryPageBaseVo)))
				.andExpect(status().isOk())
				.andReturn();

		System.out.println(result.getResponse().getContentAsString());
	}

}
