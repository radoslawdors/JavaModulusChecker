package com.github.pauldambra.moduluschecker.chain.checks;

import com.github.pauldambra.moduluschecker.Account.BankAccount;
import com.github.pauldambra.moduluschecker.As;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Streams;

final class ModulusTotal {

    static int calculate(BankAccount account, ImmutableList<Integer> weights) {
        return Streams
                .zip(
                        account.allDigits(),
                        weights.stream(),
                        (l, r) -> l * r
                )
                .reduce(0, Integer::sum);
    }

    static int calculateDoubleAlternate(BankAccount account, ImmutableList<Integer> weights) {
        return Streams
                .zip(
                        account.allDigits(),
                        weights.stream(),
                        (l, r) -> l * r
                )
                .map(String::valueOf)
                .flatMap(As::integerStream)
                .reduce(0, Integer::sum);
    }
}
