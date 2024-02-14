package kr.co.company.repair.api;

import com.github.pagehelper.Page;
import java.util.HashMap;
import java.util.Map;
import kr.co.company.common.PageNavigation;
import kr.co.company.repair.application.RepairService;
import kr.co.company.repair.application.dto.AsRequest;
import kr.co.company.repair.domain.As;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/repairs")
public class RepairController {
    private final RepairService repairService;
    
    public RepairController(final RepairService repairService){
        this.repairService = repairService;
    }
    
    @GetMapping
    public Map<String, Object> repairs(AsRequest asRequest){
        return repairService.findAllByDate(asRequest);
        //return Arrays.asList(repairService.findAllByDate(asRequest), data);
    }

}
