package gift.controller;

import gift.domain.menu.Menu;
import gift.domain.menu.MenuRequest;
import gift.domain.other.MenuResponse;
import gift.service.MenuService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "Menu API 모아놨어요.", description = "메뉴 API 모아놨으니 보고 가세요.")
@Controller
@RequestMapping("api/menus/view")
public class MenuController {

    private final MenuService menuService;

    public MenuController(MenuService menuService) {
        this.menuService = menuService;
    }

    public String returnView(
            String errorMsg,
            Model model,
            Pageable pageable) {
        if (errorMsg != null) {
            model.addAttribute("errors", errorMsg);
            model.addAttribute("menus", menuService.findall(pageable));
            return "Menu";
        }
        model.addAttribute("menus", menuService.findall(pageable));
        return "redirect:/menu";
    }

    @PostMapping
    public void save(
            @ModelAttribute @Valid MenuRequest request,
            Model model,
            Pageable pageable
    ) {
        returnView(null, model, pageable);
    }

    @GetMapping
    public String read(Model model, Pageable pageable) {
        List<MenuResponse> menus = menuService.findall(pageable);
        model.addAttribute("menus", menus);
        return "Menu";
    }

    // 많이들 사용하고 있었다.

    @Operation(summary = "메뉴 수정", description = "메뉴를 수정합니다.")
    @ApiResponse(responseCode = "200", description = "메뉴 수정 성공")
    @GetMapping("/edit/{id}")
    public String edit(
            @Parameter(description = "수정할 메뉴의 id", example = "1")
            @PathVariable("id") Long id, Model model
    ) {
        Menu menu = menuService.findById(id);
        model.addAttribute("menu", menu);
        return "update_menu";
    }

}
