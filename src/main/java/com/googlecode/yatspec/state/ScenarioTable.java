package com.googlecode.yatspec.state;

import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.ArrayList;
import java.util.List;

import static com.googlecode.totallylazy.Sequences.sequence;

@SuppressWarnings("unused")
public class ScenarioTable {
    private List<ScenarioTableHeader> headers = new ArrayList<>();
    private List<List<String>> rows = new ArrayList<>();

    @SuppressWarnings("WeakerAccess")//Used by templates
    public List<ScenarioTableHeader> getHeaders() {
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

    @SuppressWarnings("WeakerAccess")//Used by templates
    public List<List<String>> getRows() {
        return rows;
    }

    public boolean isEmpty() {
        return headers.size() == 0 && rows.size() == 0;
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }
}
