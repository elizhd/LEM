import com.usts.lem.model.Repair;
import com.usts.lem.model.Scrap;
import com.usts.lem.service.IRepairService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)// 添加spring测试方案
@ContextConfiguration("/spring-mybatis.xml")// 指定spring配置文件位置
public class RepairTest {
    @Autowired
    IRepairService repairService;
    @Test
    public void countSpec() {
        String spec = "显微镜";
        int count = repairService.countSpec(spec);
        System.out.println("The number of " + spec + ": "+count);
    }

    @Test
    public void count() {
        int count = repairService.count();
        System.out.println("The number of the equipment: "+ count);
    }

    @Test
    public void findAllData() {
        List<Repair> list = repairService.findAllData(0,10,"name","desc");
        for(Repair repair:list)
            System.out.println(repair);
    }

    @Test
    public void findBySerialNumber() {
        Repair repair = repairService.findBySerialNumber("A0001");
        System.out.println(repair);
    }

    @Test
    public void fuzzSearch() {
        List<Repair> list = repairService.fuzzSearch("显微镜");
        for (Repair r : list)
            System.out.println(r);
    }

    @Test
    public void findById() {
        Repair repair = repairService.findById(1);
        System.out.println(repair);
    }

}
