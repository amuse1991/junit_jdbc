package dao;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import dto.Role;


// DB 관련 기능을 모아놓은 클래스
public class RoleDao {
    private static String dbUrl = "jdbc:mysql://localhost:3306/sampledb?useSSL=false";
    private static String dbUser = "jeffrey";
    private static String dbPasswd = "password";

    //select
    public Role getRole(Integer roleId){
        Role role = null;
        try{
            // 드라이버 로딩
            // Class.forName("") : 명시한 클래스를 메모리에 로드한다(new 랑 유사)
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        // sql문 작성
            /*
                WHERE role_id=? 이 부분에서 ? 부분의 역할
                --------------------------------------
                ps.setInt(몇번째?에,이걸매핑)
                ps.set...으로 ?에 데이터를 매핑할 수 있다.
                즉 매번 SELECT ... 를 만들지 않고 WHERE role_id=? 부분만 새로 바인딩하여 더 효율적으로 사용 가능하다.
             */
        String sql = "SELECT role_id, description FROM role WHERE role_id=?";

        // 드라이버에서 connection 객체를 얻어옴
        try(Connection conn = DriverManager.getConnection(dbUrl, dbUser, dbPasswd);
            PreparedStatement ps = conn.prepareStatement(sql);
            ){

                ps.setInt(1,roleId);
                ResultSet rs = ps.executeQuery();

                if(rs.next()){
                    int id = rs.getInt("role_id");
                    String description = rs.getString("description");
                    role = new Role(id,description);
            }
        }catch (Exception e) {
            e.printStackTrace();
        }
        return role;
    }

    public ArrayList<Role> getRoles(){
        ArrayList<Role> roles = new ArrayList<>();

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        String sql = "SELECT description, role_id FROM role order by role_id desc";
        try(Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
            PreparedStatement ps = conn.prepareStatement(sql);){

            try(ResultSet rs = ps.executeQuery()) {
                while (rs.next()){
                    String description = rs.getString("description");
                    int id = rs.getInt("role_id");
                    Role role = new Role(id, description);
                    roles.add(role);
                }
            }catch (Exception e){
                e.printStackTrace();
            }
        }catch (Exception e){
            e.printStackTrace();
        }
        return roles;
    }

    //insert
    public int addRole(Role role){
        int insertConut = 0;

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }
        String sql = "INSERT INTO role (role_id, description) VALUES(?,?)";

        try(Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
            PreparedStatement ps = conn.prepareStatement(sql);){

            ps.setInt(1, role.getRoleId());
            ps.setString(2, role.getDescription());

            insertConut = ps.executeUpdate();
        }catch (Exception ex){
            ex.printStackTrace();
        }
        return insertConut;
    }

    //delete
    public int deleteRole(Integer roleId){
        int deleteCount = 0;
        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        String sql =  "DELETE FROM role WHERE role_id = ?";

        try(Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
            PreparedStatement ps = conn.prepareStatement(sql)){

            ps.setInt(1,roleId);
            deleteCount = ps.executeUpdate();

        }catch (Exception e){
            e.printStackTrace();
        }
        return deleteCount;
    }


    //update
    public int updateRole(Role role) {
        int updateCount = 0;

        try{
            Class.forName("com.mysql.jdbc.Driver");
        }catch (ClassNotFoundException e){
            e.printStackTrace();
        }

        String sql = "update role set description = ? where role_id = ?";

        try(Connection conn = DriverManager.getConnection(dbUrl,dbUser,dbPasswd);
            PreparedStatement ps = conn.prepareStatement(sql);){
            ps.setString(1,role.getDescription());
            ps.setInt(2,role.getRoleId());
            updateCount = ps.executeUpdate();
        }catch (Exception e){
            e.printStackTrace();
        }
        return updateCount;
    }

}
