import dao.RoleDao;
import dto.Role;
//import org.junit.Test;
import org.junit.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.*;
import static org.hamcrest.CoreMatchers.*;
import static org.junit.matchers.JUnitMatchers.hasItem;

//tip : java test rollback - JUnit 테스트는 항상 트랜잭션을 롤백합니다.
public class JdbcExamTest {
    private static String dbUrl = "jdbc:mysql://localhost:3306/sampledb?useSSL=false";
    private static String dbUser = "jeffrey";
    private static String dbPasswd = "password";
    @After
    // db 테이블을 초기화
    public void refresh(){
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        String truncate = "truncate table `role`";
        String insert = "insert into `role` values (1,'executive'), (2,'manager'),(3,'staff');";
        try(Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
            PreparedStatement ps1 = conn.prepareStatement(truncate);
            PreparedStatement ps2 = conn.prepareStatement(insert);
            ){
            ps1.executeUpdate();
            ps2.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Test
    public void getRole(){
        RoleDao dao = new RoleDao();
        Role role = dao.getRole(2);
        assertTrue(role.getRoleId() == 2);
    }


    @Test
    public void getRoles(){
        RoleDao dao = new RoleDao();

        ArrayList<Role> res = dao.getRoles();
        assertTrue(res.size() == 3);
    }

    @Test
    public void addRole(){
        Role role = new Role(5,"test");
        RoleDao dao = new RoleDao();
        assertEquals(1,dao.addRole(role)); // 추가된 갯수 확인
        assertEquals("test",dao.getRole(5).getDescription()); //추가된 내용 확인
    }

    @Test
    public void deleteRole(){
        RoleDao dao = new RoleDao();
        assertEquals(1,dao.deleteRole(1));
    }

    @Test
    public void updateRole(){
        RoleDao dao = new RoleDao();
        Role role = new Role(1,"test");
        assertEquals(1,dao.updateRole(role)); //업데이트 개수 확인
        //업데이트된 내용 확인
        assertEquals("test",dao.getRole(1).getDescription());
    }

}
