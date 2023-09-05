package hello.DB.repository;

import hello.DB.connection.DBConnectionUtil;
import hello.DB.domain.Member;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.batch.BatchProperties;
import org.springframework.jdbc.support.JdbcUtils;

import javax.sql.DataSource;
import java.sql.*;
import java.util.NoSuchElementException;


@Slf4j
/*
 * jdbc datasource 사용*/
public class MemberRepositoryV1 {
    private final DataSource dataSource;

    public MemberRepositoryV1(DataSource dataSource){
        this.dataSource=dataSource;
    }
    public Member save(Member member) throws SQLException{
        String sql="insert into member(member_id,money) values(?,?)";

        Connection con = null;
        PreparedStatement pstmt=null;

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, member.getMemberId());
            pstmt.setInt(2, member.getMoney());
            pstmt.executeUpdate();
            return member;
        } catch (SQLException e) {
            log.error("db error", e);
            throw e;
        } finally {
            close(con, pstmt, null);
        }
    }



    public Member findById(String memberId) throws SQLException {
        String sql = "select * from member where member_id=?";
        Connection con = null;
        PreparedStatement pstmt = null;
        ResultSet rs = null;//결과를 반환

        try {
            con = getConnection();
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, memberId);

            rs = pstmt.executeQuery();
            if (rs.next()) {//한번호출해야 어떤걸 가리키키시작함
                Member member = new Member();
                member.setMemberId(rs.getString("member_id"));
                member.setMoney(rs.getInt("money"));
                return member;
            } else {
                throw new NoSuchElementException("member not found" + memberId);
            }
        } catch (SQLException e) {
            log.error("de error", e);
            throw e;
        }
        finally{
            close(con,pstmt,rs);
        }
    }



    public void update(String memberId,int money) throws SQLException{
        String sql="update member set money=? where member_id=?";
        Connection con=null;
        PreparedStatement pstmt=null;

        try{
            con = getConnection();
            pstmt=con.prepareStatement(sql);
            pstmt.setInt(1, money);
            pstmt.setString(2, memberId);
            int resultSize=pstmt.executeUpdate();
            log.info("resultsize={}",resultSize);
        }catch(SQLException e){
            log.error("update fail",e);
            throw e;
        }
        finally{
            close(con,pstmt,null);
        }
    }
    public void delete(String memberId) throws SQLException
    {
        String sql="delete from member where member_id=?";
        Connection con=null;
        PreparedStatement pstmt=null;
        try{
            con=getConnection();
            pstmt=con.prepareStatement(sql);
            pstmt.setString(1,memberId );

            pstmt.executeUpdate();
        }catch(SQLException e){
            log.error("db delete error",e);
            throw e;
        }finally{
            close(con,pstmt,null);
        }
    }

    private Connection getConnection() throws SQLException
    {
        Connection con = dataSource.getConnection();
        log.info("getConnection ={} class={}",con,con.getClass());
        return DBConnectionUtil.getConnection();
    }
    private void close(Connection con , Statement stmt, ResultSet rs){
       JdbcUtils.closeResultSet(rs);
       JdbcUtils.closeStatement(stmt);
        JdbcUtils.closeConnection(con);
    }

}
