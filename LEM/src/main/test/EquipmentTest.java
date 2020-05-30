
import com.usts.lem.model.Equipment;
import com.usts.lem.service.IEquipmentService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

@RunWith(SpringJUnit4ClassRunner.class)// 添加spring测试方案
@ContextConfiguration("/spring-mybatis.xml")// 指定spring配置文件位置

public class EquipmentTest {
    @Autowired
    IEquipmentService equipmentService;

    @Test
    public void countSpec() {
        String spec = "显微镜";
        int count = equipmentService.countSpec(spec);
        System.out.println("The number of " + spec + ": " + count);
    }

    @Test
    public void count() {
        int count = equipmentService.count();
        System.out.println("The number of the equipment: " + count);
    }

    @Test
    public void findAllData() {
        List<Equipment> list =
                equipmentService.findAllData(0, 10, "name", "desc");
        for (Equipment equip : list)
            System.out.println(equip);
    }

    @Test
    public void findBySerialNumber() {
        Equipment equipment = equipmentService.findBySerialNumber("A0001");
        System.out.println(equipment);
    }

    @Test
    public void update() {
        Equipment equipment = equipmentService.findBySerialNumber("A0001");
        equipment.setAmount(6);
        equipmentService.update(equipment);

        equipment = equipmentService.findBySerialNumber("A0001");
        System.out.println(equipment);


    }

    @Test
    public void insertEquipment() {
        Equipment equipment = new Equipment();
        equipment.setId(0);
        equipment.setSerialNumber("T0001");
        equipment.setType("TEST");
        equipment.setName("Test Test");
        equipment.setAmount(3);
        equipment.setManager("Test Manager");
        equipment.setEState(1);
        equipment.setUnitPrice(1.1);
        equipment.setManufacture("Test Manufacture");
        equipment.setSpec("其他");
        equipment.setEState(0);
        equipmentService.insert(equipment);
        equipment = equipmentService.findBySerialNumber("T0001");
        System.out.println(equipment);


    }

    @Test
    public void fuzzSearch() {
        List<Equipment> list = equipmentService.fuzzSearch("显微镜");
        for (Equipment e : list)
            System.out.println(e);
    }

    @Test
    public void delete() {
        int rows = equipmentService.delete(3);
        if (rows > 0) {
            System.out.println("您成功删除了" + rows + "条数据！");
        } else {
            System.out.println("执行删除操作失败！！！");

        }
    }

    @Test
    public void findById() {
        Equipment equipment = equipmentService.findById(1);
        System.out.println(equipment);
    }

}
