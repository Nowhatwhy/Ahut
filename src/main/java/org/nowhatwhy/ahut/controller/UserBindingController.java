package org.nowhatwhy.ahut.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.nowhatwhy.ahut.dto.BindingDormDTO;
import org.nowhatwhy.ahut.entity.Result;
import org.nowhatwhy.ahut.service.IUserBindingService;
import org.nowhatwhy.ahut.vo.UserBindingVO;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * 绑定关系
 * @author nowhatwhy
 */
@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/user-bindings")
public class UserBindingController {
    private final IUserBindingService userBindingService;
    @GetMapping
    public Result<List<UserBindingVO>> listBindings() {
        return Result.ok(userBindingService.listBindings());
    }

    @PostMapping
    public Result<Void> saveBinding(@RequestBody BindingDormDTO bindingDormDTO) {
        userBindingService.saveBinding(bindingDormDTO);
        return Result.ok();
    }
    @DeleteMapping
    public Result<Void> deleteBinding(@RequestBody List<Long> ids) {
        userBindingService.deleteBindingsByIds(ids);
        return Result.ok();
    }
}
