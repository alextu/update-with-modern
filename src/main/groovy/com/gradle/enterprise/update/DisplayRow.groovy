package com.gradle.enterprise.update

import groovy.transform.TupleConstructor

@TupleConstructor
class DisplayRow {
    int labelPad
    int valuePad
    StringBuilder builder

    void row(String label, Object value) {
        builder << label.padRight(labelPad)
        def valueStr = value != null ? value.toString() : 'N/A'
        builder << valueStr.padLeft(valuePad)
        builder << '\n'
    }
}
