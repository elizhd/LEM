import com.usts.lem.model.Buy;
import com.usts.lem.model.Scrap;
import com.usts.lem.model.User;
import com.usts.lem.service.IBuyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-mybatis.xml")
public class BuyTest {
    IBuyService buyService;

    public void countSpec() {
        String spec = "显微镜";
        int count = buyService.countSpec(spec);
        System.out.println("The number of" + spec + ":" + count);
    }
    @Test
    public void count() {
        int count = buyService.count();
        System.out.println("The number of the equipment:" + count);
    }

    @Test
    public void findAllData() {
        List<Buy> list =
                buyService.findAllData(0,10,"name","desc");
        for(Buy buy : list)
            System.out.println(buy);
    }

    @Test
    public void findBySerialNumber() {
        Buy buy = buyService.findBySerialNumber("A0001");
        System.out.println(buy);
    }


    @Test
    public void insertBuy() {
        Buy buy = new Buy();
        buy.setSpec("Test");
        buy.setName("Test");
        buy.setType("Test");
        buy.setSerialNumber("A0001");
        buy.setManufacture("Test");
        buy.setUnitPrice(123);
        buy.setApprover("Test Approver");
        buy.setResult("Test");
//        buyService.insert(buy);
//        buy = buyService.findBySerialNumber("A0001");
        System.out.println(buy);
    }
    @Test
    public void update() {
        Buy buy = buyService.findBySerialNumber("A0001");
        System.out.println(buy);
        buy.setApprover("Test");
        buyService.update(buy);
        buy = buyService.findBySerialNumber("A0001");
        System.out.println(buy);
    }
    @Test
    public void fuzzSearch() {
        List<Buy> list = buyService.fuzzSearch("显微镜");
        for(Buy b : list)
            System.out.println(b);
    }

    @Test
    public void findById() {
        Buy buy = buyService.findById(1);
        System.out.println(buy);
    }

}
