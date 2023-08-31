package hello.DB.connection;

public abstract class ConnectionConst {//객체생성을 막아둠.
    public static final String URL= "jdbc:h2:tcp://localhost/~/testDB";
    public static final String USERNAME="sa";
    public static final String PASSWORD="";
}
///DB커넥션에 대한 정보를 넣어줌//
///Users/kimyunsoo/Downloads/h2 3 경로