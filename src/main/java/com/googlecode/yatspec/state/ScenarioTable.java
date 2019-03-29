package com.googlecode.yatspec.state;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

@SuppressWarnings("unused")
public class ScenarioTable {
    private List<ScenarioTableHeader> headers = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();

    List<ScenarioTableHeader> getHeaders() {
        return headers;
    }

    public void setHeaders(List<String> headers) {
        if (headers != null)
            this.headers = sequence(headers)
                    .map(ScenarioTableHeader::new)
                    .toList();
    }

    public void addRow(List<String> values) {
        rows.add(values);
    }

    List<List<String>> getRows() {
        return rows;
    }

    public boolean isEmpty() {
        return headers.size() == 0 && rows.size() == 0;
    }
}
