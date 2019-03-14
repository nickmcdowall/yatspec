package com.googlecode.yatspec.rendering.html.tagindex;

import com.googlecode.yatspec.state.TestMethod;

import java.util.Collection;

public interface TagFinder {
    Collection<String> tags(TestMethod testMethod);
}
