import org.junit.Test;

import static org.quicktheories.QuickTheory.qt;
import static org.quicktheories.generators.SourceDSL.integers;

public class PropertyBasedTest {
    @Test
    public void skal_qt() {
        qt()
                .forAll(integers().allPositive())
                .check(x -> x > 0);
    }
}
