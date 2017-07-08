package com.dambra.paul.moduluschecker.chain;

import com.dambra.paul.moduluschecker.ModulusCheckParams;
import com.dambra.paul.moduluschecker.valacdosFile.WeightRow;

import java.util.Optional;

/**
 * Exception Fourteen
 * Perform the modulus 11 check as normal:
 * • If the check passes (that is, there is no remainder), then the account number should be
 * considered valid. Do not perform the second check
 */
public class ExceptionFourteenGate implements ModulusChainCheck {

    private final SecondModulusCheckRouter next;

    public ExceptionFourteenGate(SecondModulusCheckRouter next) {
        this.next = next;
    }

    @Override
    public ModulusResult check(ModulusCheckParams params) {

        final boolean firstCheckResult = firstCheckResult(params);
        return WeightRow.isExceptionFourteen(params.firstWeightRow) && firstCheckResult
            ? new ModulusResult(Optional.of(true), Optional.empty())
            : next.check(params);
    }

    private boolean firstCheckResult(ModulusCheckParams params) {
        return Optional.ofNullable(ModulusResult.copy(params.modulusResult.orElse(null))).isPresent() && Optional.ofNullable(ModulusResult.copy(params.modulusResult.orElse(null))).get().firstCheckResult.orElse(false);
    }
}