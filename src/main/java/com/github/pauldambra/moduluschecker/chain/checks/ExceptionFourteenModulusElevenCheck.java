package com.github.pauldambra.moduluschecker.chain.checks;

import com.github.pauldambra.moduluschecker.Account.BankAccount;
import com.github.pauldambra.moduluschecker.ModulusCheckParams;

import java.util.Arrays;
import java.util.List;

/**
 * Second check:
 * • If the 8th digit of the account number (reading from left to right) is not 0, 1 or 9 then the account
 * number fails the second check and is not a valid Coutts account number
 * • If the 8th digit is 0, 1 or 9, then remove the digit from the account number and insert a 0 as the
 * 1st digit for check purposes only
 * • Perform the modulus 11 check on the modified account number using the same weightings as
 * specified in the table (that is, 0 0 0 0 0 0 8 7 6 5 4 3 2 1):
 * – If there is no remainder, then the account number should be considered valid
 * – If there is a remainder, then the account number fails the second check and is not a valid Coutts account number
 */
public final class ExceptionFourteenModulusElevenCheck {

    private final List<Integer> allowedValuesAtH = Arrays.asList(0, 1, 9);

    public Boolean check(ModulusCheckParams params) {

        int h = params.account.getNumberAt(BankAccount.H);
        if (!allowedValuesAtH.contains(h)) {
            return false;
        }

        final String newAccountNumber = "0" + params.account.accountNumber.substring(0, 7);
        final BankAccount correctedAccount = BankAccount.Of(params.account.sortCode, newAccountNumber);

        int total = ModulusTotal.calculate(
                correctedAccount,
                params.firstWeightRow.get().getWeights());
        int remainder = total % 11;

        return remainder == 0;
    }

}
