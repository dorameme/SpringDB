package hello.DB.exceptinon.basic;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;

@Slf4j
public class UncheckedTest {

    @Test
    void unchecked_catch(){
        Service service=  new Service();
        service.callCatch();
    }

    @Test
    void unchecked_throw(){
        Service service = new Service();
        assertThatThrownBy(()->service.callThrow())
                .isInstanceOf(MyUncheckedException.class);
    }
    static class MyUncheckedException extends RuntimeException{
        public MyUncheckedException(String message){
            super(message);
        }
    }
    ///잡거나 던지거나 근데 안잡으면 자동으로 상위로 던저줌.
    static class Service{
        Repository repository=new Repository();
        public void callCatch(){
            try{
                repository.call();
            }catch(MyUncheckedException e){
                log.info("예외처리 message={}",e.getMessage(),e);
            }
        }

        public void callThrow(){
            repository.call();
        }
    }
    static class Repository{
        public void call(){
            throw new MyUncheckedException("ex");
        }

    }
}
