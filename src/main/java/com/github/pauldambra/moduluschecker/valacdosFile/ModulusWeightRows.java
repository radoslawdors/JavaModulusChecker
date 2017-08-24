package com.github.pauldambra.moduluschecker.valacdosFile;

import com.github.pauldambra.moduluschecker.Account.BankAccount;
import com.github.pauldambra.moduluschecker.ModulusCheckParams;
import com.google.common.base.Charsets;
import com.google.common.base.Splitter;
import com.google.common.collect.ImmutableList;
import com.google.common.io.Resources;

import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.Optional;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class ModulusWeightRows {

    private static final Splitter newlineSplitter = Splitter.on(Pattern.compile("\r?\n"));
    private final ImmutableList<ValacdosRow> valacdosRows;

    public ModulusWeightRows(List<ValacdosRow> valacdosRows) {
        this.valacdosRows = ImmutableList.copyOf(valacdosRows);
    }

    public ModulusCheckParams FindFor(BankAccount account) {

        WeightRow first = null;
        WeightRow second = null;

        for (ValacdosRow vr : this.valacdosRows) {
            boolean stillSeekingFirstMatch = first == null;

            if (!vr.sortCodeRange.contains(account.sortCode)) {
                if (stillSeekingFirstMatch) {
                    continue;
                } else {
                    break;
                }
            }

            if (stillSeekingFirstMatch) {
                first = vr.weightRow;
                continue;
            }

            second = vr.weightRow;
        }

        return new ModulusCheckParams(
                account,
                Optional.ofNullable(first),
                Optional.ofNullable(second),
                Optional.empty());
    }

    public static ModulusWeightRows fromFile(String filePath) throws IOException {

        URL url = Resources.getResource(filePath);
        String text = Resources.toString(url, Charsets.UTF_8);

        List<ValacdosRow> valacdosRows = newlineSplitter
                .splitToList(text)
                .stream()
                .filter(r -> !r.isEmpty())
                .map(r -> new Object() {
                    Optional<SortCodeRange> sortCodeRange = SortCodeRange.parse(r);
                    Optional<WeightRow> weightRow = WeightRow.parse(r);
                })
                .filter(r -> r.weightRow.isPresent())
                .filter(r -> r.sortCodeRange.isPresent())
                .map(r -> new ValacdosRow(r.sortCodeRange.get(), r.weightRow.get()))
                .collect(Collectors.toList());

        return new ModulusWeightRows(valacdosRows);
    }

}
