package io.renren.modules.test.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.shiro.authz.annotation.RequiresPermissions;
import org.dom4j.DocumentException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import io.renren.modules.test.entity.TestStressThreadSetEntity;
import io.renren.modules.test.service.TestStressThreadSetService;
import io.renren.common.annotation.SysLog;
import io.renren.common.utils.R;


/**
 * 线程组管理
 * 
 * @author smooth
 * @email 
 * @date 2019-03-26 09:48:06
 */
@RestController
@RequestMapping("/test/teststressthreadset")
public class TestStressThreadSetController {
	@Autowired
	private TestStressThreadSetService testStressThreadSetService;
	
	/**
	 * 所有配置列表
	 */
	@RequestMapping("/list")
	@RequiresPermissions("test:stress:fileList")
	public List<TestStressThreadSetEntity> list(){
		List<TestStressThreadSetEntity> testStressThreadSetList = testStressThreadSetService.queryList(new HashMap<String, Object>());

		return testStressThreadSetList;
	}

	/**
	 * 指定脚本的配置列表
	 */
	@RequestMapping("/list/{fileIds}")
	@RequiresPermissions("test:stress:fileList")
	public List<TestStressThreadSetEntity> list(@PathVariable("fileIds") String[] fileIds){
		Map<String,Object> map = new HashMap<String, Object>();
		map.put("fileids", Arrays.asList(fileIds));
		List<TestStressThreadSetEntity> testStressThreadSetList = testStressThreadSetService.queryList(map);

		return testStressThreadSetList;
	}
	
	/**
	 * 选择配置项(添加、修改配置项)
	 */
	@RequestMapping("/select")
	@RequiresPermissions("test:stress")
	public R select(){
		//查询列表数据
		List<TestStressThreadSetEntity> testStressThreadSetList = testStressThreadSetService.queryNotSetList();
		
		//添加顶级菜单
		TestStressThreadSetEntity root = new TestStressThreadSetEntity();
		root.setSetId("0");
		root.setName("一级菜单");
		root.setParentId("-1");
		root.setOpen(true);
		testStressThreadSetList.add(root);
		
		return R.ok().put("testStressThreadSetList", testStressThreadSetList);
	}	
	
	/**
	 * 信息
	 */
	@RequestMapping("/info/{setId}")
	@RequiresPermissions("test:stress")
	public R info(@PathVariable("setId") String setId){
		TestStressThreadSetEntity testStressThreadSet = testStressThreadSetService.queryObject(setId);
		
		return R.ok().put("testStressThreadSet", testStressThreadSet);
	}
	
	/**
	 * 保存
	 */
	@RequestMapping("/save")
	@RequiresPermissions("test:stress:save")
	public R save(@RequestBody TestStressThreadSetEntity testStressThreadSet){
		testStressThreadSetService.save(testStressThreadSet);
		
		return R.ok();
	}
	
	/**
	 * 修改
	 */
	@RequestMapping("/update")
	@RequiresPermissions("test:stress:update")
	public R update(@RequestBody TestStressThreadSetEntity testStressThreadSet){
		testStressThreadSetService.update(testStressThreadSet);
		
		return R.ok();
	}
	
	/**
	 * 删除
	 */
	@RequestMapping("/delete")
	@RequiresPermissions("test:stress:delete")
	public R delete(@RequestBody String[] setIds){
		testStressThreadSetService.deleteBatch(setIds);
		
		return R.ok();
	}
	
	/**
     * 将线程组配置同步到对应脚本文件中。
	 * @throws DocumentException 
     */
    @SysLog("将线程组配置同步到对应脚本文件中")
    @RequestMapping("/synchronizeJmx")
    @RequiresPermissions("test:stress:synchronizeJmx")
    public R synchronizeJmx(@RequestBody Long fileId) throws DocumentException {
    	testStressThreadSetService.synchronizeJmx(fileId);
        return R.ok();
    }
}
