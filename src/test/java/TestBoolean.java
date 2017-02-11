import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import ru.pk.randgen.BooleanGen;

public class TestBoolean {
    Logger log = LoggerFactory.getLogger(TestBoolean.class);

    @Test
    public void test1() {
        BooleanGen b = new BooleanGen();
        for (int i = 0; i < 100; i++) {
            log.info(String.valueOf(b.gen()));
        }
    }

}
