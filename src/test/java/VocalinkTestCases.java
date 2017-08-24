import com.github.pauldambra.moduluschecker.ModulusChecker;
import org.junit.Test;

import java.io.IOException;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.Is.is;

public class VocalinkTestCases {
    private static ModulusChecker modulusChecker() throws IOException {
        return new ModulusChecker();
    }

    public static class ExceptionOne {
        @Test
        public void EnsuresThatTwentySevenHasBeenAddedToTheAccumulatedTotal() throws IOException {
            assertVocalinkTestCase("118765", "64371389", true);
        }

        @Test
        public void WhereItFailsTheDoubleAlternateCheck() throws IOException {
            assertVocalinkTestCase("118765", "64371388", false);
        }
    }

    public static class ExceptionTwoAndNine {
        @Test
        public void WhereFirstPassesAndSecondFails() throws IOException {

            assertVocalinkTestCase("309070", "02355688", true);
        }

        @Test
        public void WhereFirstFailsAndSecondPassesWithSubstitution() throws IOException {
            assertVocalinkTestCase("309070", "12345668", true);
        }

        @Test
        public void WhereSecondPassesWithNoMatchWeights() throws IOException {
            assertVocalinkTestCase("309070", "12345677", true);
        }

        @Test
        public void WhereSecondPassesUsingOneMatchWeight() throws IOException {
            assertVocalinkTestCase("309070", "99345694", true);
        }
    }

    public static class ExceptionThree {
        @Test
        public void CEqualsSixSoIgnoreSecondCheck() throws IOException {
            assertVocalinkTestCase("820000", "73688637", true);
        }

        @Test
        public void CEqualsNineSoIgnoreSecondCheck() throws IOException {
            assertVocalinkTestCase("827999", "73988638", true);
        }

        @Test
        public void CIsNeitherSixNorNineSoRunSecondCheck() throws IOException {
            assertVocalinkTestCase("827101", "28748352", true);
        }
    }

    public static class ExceptionFour {
        @Test
        public void WhereTheRemainderIsEqualToTheCheckDigit() throws IOException {
            assertVocalinkTestCase("134020", "63849203", true);
        }
    }

    public static class ExceptionFive {
        @Test
        public void WhereTheCheckPasses() throws IOException {
            assertVocalinkTestCase("938611", "07806039", true);
        }

        @Test
        public void WhereTheCheckPassesWithSubstitution() throws IOException {
            assertVocalinkTestCase("938600", "42368003", true);
        }

        @Test
        public void WhereBothChecksProduceARemainderOfZeroAndPass() throws IOException {
            assertVocalinkTestCase("938063", "55065200", true);
        }

        @Test
        public void WhereTheFirstCheckDigitIsCorrectAndTheSecondIsIncorrect() throws IOException {
            assertVocalinkTestCase("938063", "15764273", false);
        }

        @Test
        public void WhereTheFirstCheckDigitIsIncorrectAndTheSecondCorrect() throws IOException {
            assertVocalinkTestCase("938063", "15764264", false);
        }

        @Test
        public void WhereTheFirstCheckDigitIsIncorrectWithARemainderOfOne() throws IOException {
            assertVocalinkTestCase("938063", "15763217", false);
        }
    }

    public static class ExceptionSix {
        @Test
        public void WhereTheAccountFailsStandardCheckButIsAForeignCurrencyAccount() throws IOException {
            assertVocalinkTestCase("200915", "41011166", true);
        }
    }

    public static class ExceptionSeven {
        @Test
        public void WherePassesButWouldFailTheStandardCheck() throws IOException {
            assertVocalinkTestCase("772798", "99345694", true);
        }
    }

    public static class ExceptionEight {
        @Test
        public void WhereTheCheckPasses() throws IOException {
            assertVocalinkTestCase("086090", "06774744", true);
        }
    }

    public static class ExceptionTenAndEleven {
        @Test
        public void WhereAccountNumberABEqualsZeroNineAndGEquals9FirstCheckPassesAndSecondFails() throws IOException {
            assertVocalinkTestCase("871427", "09123496", true);
        }

        @Test
        public void WhereAccountNumberABEqualsNineNineAndGEquals9FirstCheckPassesAndSecondFailsE() throws IOException {
            assertVocalinkTestCase("871427", "99123496", true);
        }

        @Test
        public void WhereFirstCheckPassesAndSecondFails() throws IOException {
            assertVocalinkTestCase("871427", "46238510", true);
        }

        @Test
        public void WhereFirstCheckFailsAndSecondPasses() throws IOException {
            assertVocalinkTestCase("872427", "46238510", true);
        }
    }

    public static class ExceptionTwelveAndThirteen {
        @Test
        public void WherePassesModulusElevenButWouldFailModulusTen() throws IOException {
            assertVocalinkTestCase("074456", "12345112", true);
        }

        @Test
        public void WherePassesModulusElevenButWouldPassModulusTen() throws IOException {
            assertVocalinkTestCase("070116", "34012583", true);
        }

        @Test
        public void WhereFailsModulusElevenButPassesModulusTen() throws IOException {
            assertVocalinkTestCase("074456", "11104102", true);
        }
    }

    public static class ExceptionFourteen {
        /**
         * Listed in the pdf as example one on page 19 of v 4.30
         **/
        @Test
        public void WhereTheFirstCheckPasses() throws IOException {
            assertVocalinkTestCase("180002", "98093517", true);
        }

        @Test
        public void WhereTheFirstCheckFailsAndTheSecondPasses() throws IOException {
            assertVocalinkTestCase("180002", "00000190", true);
        }
    }

    @Test
    public void PASS_MODULUS_10_check() throws IOException {
        assertVocalinkTestCase("089999", "66374958", true);
    }

    @Test
    public void PASS_MODULUS_11_check() throws IOException {
        assertVocalinkTestCase("107999", "88837491", true);
    }

    @Test
    public void PASS_MODULUS_11_AND_double_alternate_checks() throws IOException {
        assertVocalinkTestCase("202959", "63748472", true);
    }

    @Test
    public void PASS_MODULUS_11_check_AND_fail_DOUBLE_alternate_check() throws IOException {
        assertVocalinkTestCase("203099", "66831036", false);
    }

    @Test
    public void FAIL_modulus_11_check_AND_pass_DOUBLE_alternate_check() throws IOException {
        assertVocalinkTestCase("203099", "58716970", false);
    }

    @Test
    public void FAIL_modulus_10_check() throws IOException {
        assertVocalinkTestCase("089999", "66374959", false);
    }

    @Test
    public void FAIL_modulus_11_check() throws IOException {
        assertVocalinkTestCase("107999", "88837493", false);
    }

    private static void assertVocalinkTestCase(String sortCode, String accountNumber, Boolean expectedValid) throws IOException {
        Boolean actual = modulusChecker().checkBankAccount(sortCode, accountNumber);
        assertThat(actual, is(equalTo(expectedValid)));
    }
}