package com.mty.jls.controller.rbac;


import com.mty.jls.contract.model.Response;
import com.mty.jls.rbac.api.ISysJobService;
import com.mty.jls.rbac.bean.ISysJob;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.dubbo.config.annotation.Reference;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


@Api(value = "岗位管理", tags = "岗位管理")
@RestController
@RequestMapping("/job")
public class SysJobController {

    @Reference(version = "1.0.0", group = "rbac")
    private ISysJobService jobService;

    @ApiOperation(value = "获取岗位列表")
    @GetMapping
     public Response getList(Integer page, Integer pageSize, @RequestParam(defaultValue = "") String jobName) {
        return Response.succeed(jobService.selectJobList(page, pageSize, jobName));
    }

    @PostMapping
    @ApiOperation(value = "保存岗位")
     public Response save(@RequestBody ISysJob sysJob) {
        return Response.succeed(jobService.save(sysJob));
    }

    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据id删除岗位")
     public Response delete(@PathVariable("id") Integer id) {
        return Response.succeed(jobService.removeById(id));
    }


    @PutMapping
    @ApiOperation(value = "更新岗位")
     public Response update(@RequestBody ISysJob sysJob) {
        return Response.succeed(jobService.updateById(sysJob));
    }


    @GetMapping("/{id}")
    @ApiOperation(value = "根据部门id查询岗位")
     public Response selectJobListByDeptId(@PathVariable("id") Integer deptId) {
        return Response.succeed(jobService.selectJobListByDeptId(deptId));
    }
}
