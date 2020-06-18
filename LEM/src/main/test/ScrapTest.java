
import com.usts.lem.model.Scrap;
import com.usts.lem.service.IScrapService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)//添加spring测试方案
@ContextConfiguration("/spring-mybatis.xml")//指定spring配置文件位置

public class ScrapTest {
    @Autowired
    IScrapService scrapService;

    @Test
    public void countSpec() {
        String spec = "显微镜";
        int count = scrapService.countSpec(spec);
        System.out.println("The number of " + spec + ": "+count);
    }

    @Test
    public void count() {
        int count = scrapService.count();
        System.out.println("The number of the equipment: "+ count);
    }

    @Test
    public void findAllData() {
        List<Scrap> list = scrapService.findAllData(0,10,"name","desc");
        for(Scrap scrap:list)
            System.out.println(scrap);
    }

    @Test
    public void findBySerialNumber() {
        Scrap scrap = scrapService.findBySerialNumber("A0001");
        System.out.println(scrap);
    }


    @Test
    public void insertScrap() {
        Scrap scrap = new Scrap();
        scrap.setSerialNumber("T00001");
        scrap.setType("TEST");
        scrap.setName("Test Test");
        scrap.setApprover("Test Approver");
        scrap.setManufacture("Test Manufacture");
        scrap.setSpec("其他");
        scrapService.insert(scrap);
        scrap = scrapService.findBySerialNumber("T00001");
        System.out.println(scrap);
    }

    @Test
    public void fuzzSearch() {
        List<Scrap> list = scrapService.fuzzSearch("显微镜");
        for (Scrap s : list)
            System.out.println(s);
    }

    @Test
    public void findById() {
        Scrap scrap = scrapService.findById(1);
        System.out.println(scrap);
    }

}
