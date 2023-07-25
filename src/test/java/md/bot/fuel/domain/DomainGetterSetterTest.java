package md.bot.fuel.domain;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;
import md.bot.fuel.common.GetterSetterTest;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import static org.apache.commons.lang3.StringUtils.EMPTY;
import static org.junit.jupiter.api.TestInstance.Lifecycle.PER_CLASS;

@TestInstance(PER_CLASS)
public class DomainGetterSetterTest extends GetterSetterTest {

    private static final double DEFAULT_DOUBLE = 0.0D;

    public static Stream<Arguments> getData() {
        return Stream.of(
                Arguments.of(new UserData()),
                Arguments.of(new FuelStation(EMPTY, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE, DEFAULT_DOUBLE,
                        DEFAULT_DOUBLE)));
    }

    @BeforeAll
    public void addMoreSuppliers() {
        final List<String> ignoreMethods = new ArrayList<>();

        ignoreMethods.add("setTimestamp");

        this.addIgnoreMethods(ignoreMethods);
    }

    @ParameterizedTest
    @MethodSource("getData")
    public void testDomain(Object domain) throws Exception {
        this.testGettersAndSetters(domain);
    }
}