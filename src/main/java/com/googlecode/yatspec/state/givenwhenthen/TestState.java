package com.googlecode.yatspec.state.givenwhenthen;

import com.googlecode.yatspec.plugin.sequencediagram.ByNamingConventionMessageProducer;
import com.googlecode.yatspec.plugin.sequencediagram.SequenceDiagramMessage;
import com.googlecode.yatspec.plugin.sequencediagram.SvgWrapper;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;

import java.util.Collection;
import java.util.Map;

public class TestState {

    private final InterestingGivens interestingGivens;
    private final CapturedInputAndOutputs capturedInputAndOutputs;
    private final ByNamingConventionMessageProducer byNamingConventionMessageProducer = new ByNamingConventionMessageProducer();

    private SvgWrapper diagram;

    public TestState() {
        interestingGivens = new InterestingGivens();
        capturedInputAndOutputs = new CapturedInputAndOutputs();
    }

    public TestState(TestState testState) {
        interestingGivens = new InterestingGivens().putAll(testState.interestingGivens.getTypes());
        capturedInputAndOutputs = new CapturedInputAndOutputs().putAll(testState.capturedInputAndOutputs.getTypes());
        diagram = (null == testState.diagram) ? null : new SvgWrapper(testState.getDiagram().toString());
    }

    public synchronized void log(String name, Object value) {
        int count = 1;
        String keyName = name;
        while (capturedInputAndOutputs.contains(keyName)) {
            keyName = name + count;
            count++;
        }
        capturedInputAndOutputs.add(keyName, value);
    }

    public InterestingGivens interestingGivens() {
        return interestingGivens;
    }

    public <R> R getType(String key, Class<R> aClass) {
        return capturedInputAndOutputs.getType(key, aClass);
    }

    public Collection<SequenceDiagramMessage> sequenceMessages() {
        return byNamingConventionMessageProducer.messages(capturedInputAndOutputs);
    }

    public Map<String, Object> getCapturedTypes() {
        return capturedInputAndOutputs.getTypes();
    }

    public Map<String, Object> getInterestingTypes() {
        return interestingGivens.getTypes();
    }

    @Override
    public boolean equals(Object o) {
        return EqualsBuilder.reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return HashCodeBuilder.reflectionHashCode(this);
    }

    public SvgWrapper getDiagram() {
        return diagram;
    }

    public void setDiagram(SvgWrapper diagram) {
        this.diagram = diagram;
    }

    public void reset() {
        capturedInputAndOutputs.clear();
    }
}