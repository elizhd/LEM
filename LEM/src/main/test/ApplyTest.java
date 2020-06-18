import com.usts.lem.model.Apply;
import com.usts.lem.model.Equipment;
import com.usts.lem.service.IApplyService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration("/spring-mybatis.xml")
public class ApplyTest {
    @Autowired
    IApplyService applyService;

    @Test
    public void findAllData(){
        List<Apply> list =
                applyService.findAllData(0, 10, "name", "desc");
        for (Apply apply : list)
            System.out.println(apply);
    }

    @Test
    public void insert(){
        Apply apply = new Apply();
        apply.setName("Tes");
        apply.setIsvisible(false);
        applyService.insert(apply);
    }

    @Test
    public void findVisiableData(){
        List<Apply> list =
                applyService.findVisibleData(0, 10, "name", "desc");
        for (Apply apply : list)
            System.out.println(apply);
    }


    @Test
    public void findById(){
        Apply apply = applyService.findById(1);
        System.out.println(apply);
    }


}
